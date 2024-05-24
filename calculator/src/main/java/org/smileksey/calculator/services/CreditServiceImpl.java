package org.smileksey.calculator.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smileksey.calculator.calculators.CreditParamsCalculator;
import org.smileksey.calculator.calculators.CreditParamsCalculatorImpl;
import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.PaymentScheduleElementDto;
import org.smileksey.calculator.dto.ScoringDataDto;
import org.smileksey.calculator.dto.enums.EmploymentStatus;
import org.smileksey.calculator.dto.enums.Gender;
import org.smileksey.calculator.dto.enums.MaritalStatus;
import org.smileksey.calculator.dto.enums.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class CreditServiceImpl implements CreditService{

    private final static Logger logger = LogManager.getLogger(CreditServiceImpl.class);

    //Базовая кредитная ставка
    @Value("${base.rate}")
    private String stringBaseRate;

    private final CreditParamsCalculator creditParamsCalculator;

    @Autowired
    public CreditServiceImpl(CreditParamsCalculator creditParamsCalculator) {
        this.creditParamsCalculator = creditParamsCalculator;
    }

    //TODO
    @Override
    public Optional<CreditDto> getCreditDto(ScoringDataDto scoringDataDto) {

        BigDecimal rate = executeScoringAndGetRate(scoringDataDto);

        //Если executeScoringAndGetRate вернул rate == 0, значит в кредите отказано, возвращаем в контроллер пустой Optional
        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            return Optional.empty();
        }

        //Рассчет стоимости страховки, если есть
        BigDecimal insurancePrice = creditParamsCalculator.calculateInsurancePrice(scoringDataDto.getAmount(), scoringDataDto.getIsInsuranceEnabled(), scoringDataDto.getIsSalaryClient());

        //Рассчет суммы ежемесячного платежа
        BigDecimal monthlyPayment = creditParamsCalculator.calculateMonthlyPayment(scoringDataDto.getAmount(), rate, scoringDataDto.getTerm(), insurancePrice);

        //Рассчет общей суммы выплат
        BigDecimal totalAmount = creditParamsCalculator.calculateTotalAmount(monthlyPayment, scoringDataDto.getTerm());

        //Рассчет полной стоимости кредита (ПСК)
        BigDecimal psk = creditParamsCalculator.calculatePSK(totalAmount, scoringDataDto.getAmount(), scoringDataDto.getTerm());

        //Рассчет графика платежей
        List<PaymentScheduleElementDto> paymentSchedule = creditParamsCalculator.getPaymentSchedule(monthlyPayment, scoringDataDto.getAmount(), rate, scoringDataDto.getTerm());

        //Создаем объект CreditDto и заполняем его поля вычисленными значениями
        CreditDto creditDto = new CreditDto();

        creditDto.setAmount(scoringDataDto.getAmount());
        creditDto.setTerm(scoringDataDto.getTerm());
        creditDto.setMonthlyPayment(monthlyPayment);
        creditDto.setRate(rate);
        creditDto.setPsk(psk);
        creditDto.setIsInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled());
        creditDto.setIsSalaryClient(scoringDataDto.getIsSalaryClient());
        creditDto.setPaymentSchedule(paymentSchedule);

        return Optional.of(creditDto);
    }


    private BigDecimal executeScoringAndGetRate(ScoringDataDto scoringDataDto) {

        BigDecimal initialRate = new BigDecimal(stringBaseRate);
        BigDecimal amount = scoringDataDto.getAmount();
        EmploymentStatus employmentStatus = scoringDataDto.getEmployment().getEmploymentStatus();
        Position position = scoringDataDto.getEmployment().getPosition();
        BigDecimal salary = scoringDataDto.getEmployment().getSalary();
        MaritalStatus maritalStatus = scoringDataDto.getMaritalStatus();
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        Gender gender = scoringDataDto.getGender();
        Integer workExperienceTotal = scoringDataDto.getEmployment().getWorkExperienceTotal();
        Integer workExperienceCurrent = scoringDataDto.getEmployment().getWorkExperienceCurrent();

        //Минимально допустимое значение ставки
        BigDecimal minRate = new BigDecimal("9.00");

        //Рассчет предварительной ставки исходя из наличия страховки и зарплатной карты
        BigDecimal rate = creditParamsCalculator.calculateRate(initialRate, scoringDataDto.getIsInsuranceEnabled(), scoringDataDto.getIsSalaryClient());


        if (age < 20 || age > 65) {
            logger.info("Поле age = {} не соответствует критериям - отказ", age);
            return BigDecimal.ZERO;
        }

        if (workExperienceTotal < 18) {
            logger.info("Поле workExperienceTotal = {} не соответствует критериям - отказ", workExperienceTotal);
            return BigDecimal.ZERO;
        }

        if (workExperienceCurrent < 3) {
            logger.info("Поле workExperienceCurrent = {} не соответствует критериям - отказ", workExperienceCurrent);
            return BigDecimal.ZERO;
        }

        //Если сумма кредита больше 25 зарплат - отказ
        BigDecimal salary25x = salary.multiply(BigDecimal.valueOf(25));

        if (amount.compareTo(salary25x) > 0) {
            logger.info("Поле amount = {} превышает salary x 25 = {} - отказ", amount, salary25x);
            return BigDecimal.ZERO;
        }

        //Проверяем employmentStatus
        switch (employmentStatus) {
            case UNEMPLOYED:
                return BigDecimal.ZERO;
            case SELF_EMPLOYED:
                rate = rate.add(new BigDecimal("1.00"));
                break;
            case BUSINESS_OWNER:
                rate = rate.add(new BigDecimal("2.00"));
                break;
        }

        logger.info("Поле employmentStatus = {}, новая cтавка (rate) = {} %", employmentStatus, rate);

        //Проверяем position
        switch (position) {
            case MIDDLE_MANAGER:
                rate = rate.subtract(new BigDecimal("2.00"));
                break;
            case TOP_MANAGER:
                rate = rate.subtract(new BigDecimal("3.00"));
                break;
        }

        logger.info("Поле position = {}, новая cтавка (rate) = {} %", position, rate);

        //Проверяем maritalStatus
        switch (maritalStatus) {
            case MARRIED:
                rate = rate.subtract(new BigDecimal("3.00"));
                break;
            case NOT_MARRIED:
                rate = rate.add(new BigDecimal("1.00"));
                break;
        }

        logger.info("maritalStatus = {}, новая cтавка (rate) = {} %", maritalStatus, rate);

        //Проверяем gender
        switch (gender) {
            case FEMALE:
                if (age >= 32 && age < 60) {
                    rate = rate.subtract(new BigDecimal("3.00"));
                }
                break;
            case MALE:
                if (age >= 30 && age < 55) {
                    rate = rate.subtract(new BigDecimal("3.00"));
                }
                break;
            case NON_BINARY:
                rate = rate.add(new BigDecimal("7.00"));
                break;
        }

        logger.info("Поле gender = {}, поле age = {}, новая cтавка (rate) = {} %", gender, age, rate);

        //Если в результате рассчетов ставка опустилась ниже минимального значения, усталиваем ей это значение
        if (rate.compareTo(minRate) < 0) {

            logger.info("Размер ставки (rate) = {} % опустился ниже минимального (minRate) = {} %", rate, minRate);

            rate = minRate;

            logger.info("Установлен минимально возможный размер ставки (rate) = {} %", rate);
        }

        logger.info("Итоговая ставка (rate) = {} %", rate);

        return rate;
    }

}
