package org.smileksey.calculator.calculators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smileksey.calculator.dto.PaymentScheduleElementDto;
import org.smileksey.calculator.services.LoanOfferServiceImpl;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Класс с методами для рассчета отдельных параметров кредита */
@Component
public class CreditParamsCalculatorImpl implements CreditParamsCalculator {

    private final static Logger logger = LogManager.getLogger(LoanOfferServiceImpl.class);

    /**
     * Метод для расчета ежемесячного платежа
     * @param amount - запрашиваемая сумма кредита
     * @param rate - ставка, %
     * @param term - срок кредита, мес
     * @param insurancePrice - стоимость страховки
     * @return возвращает сумму ежемесячного платежа
     */
    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term, BigDecimal insurancePrice) {

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP);

        logger.info("Месячная ставка = {}", monthlyRate);

        monthlyRate = monthlyRate.divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);

        //Вычисляем коэффициент аннуитета по формуле (monthlyRate x (1 + monthlyRate)^term) / ((1 + monthlyRate)^term - 1)
        //Выделил в отдельную переменную повторяющийся фрагмент (1 + monthlyRate)^term
        BigDecimal pow = (monthlyRate.add(new BigDecimal("1.00"))).pow(term);

        BigDecimal annuityCoefficient = (monthlyRate.multiply(pow))
                .divide(pow.subtract(new BigDecimal("1.00")), 8, RoundingMode.HALF_UP);

        logger.info("Коэффициент аннуитета = {}", annuityCoefficient);

        BigDecimal monthlyPayment = annuityCoefficient
                .multiply(amount)
                .add(insurancePrice.divide(BigDecimal.valueOf(term), 4, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

        logger.info("Ежемесячный платеж = {}", monthlyPayment);

        return monthlyPayment;
    }


    /**
     * Метод для расчета полной суммы кредита
     * @param monthlyPayment - сумма ежемесячного платежа
     * @param term - срок кредита, мес
     * @return возвращает сумму всех платажей по кредиту
     */
    @Override
    public BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, Integer term) {

        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));

        logger.info("Сумма всех выплат по кредиту = {}", totalAmount);

        return totalAmount;

    }


    /**
     * Метод для расчета кредитной ставки
     * @param initialRate - исходная ставка (базовая)
     * @param isInsuranceEnabled - наличие страховки
     * @param isSalaryClient - является ли зарплатным клиентом
     * @return возвращает кредитную ставку в процентах
     */
    @Override
    public BigDecimal calculateRate(BigDecimal initialRate, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        logger.info("Исходная базовая ставка (initialRate) = {} %", initialRate);

        BigDecimal rate = initialRate;

        if(isInsuranceEnabled) {
            rate = rate.subtract(new BigDecimal("3.00"));
        } else {
            rate = rate.add(new BigDecimal("1.00"));
        }

        logger.info("Поле isInsuranceEnabled = {}, новая cтавка (rate) = {} %", isInsuranceEnabled, rate);

        if(isSalaryClient) {
            rate = rate.subtract(new BigDecimal("1.00"));
        }

        logger.info("Поле isSalaryClient = {}, новая cтавка (rate) = {} %", isSalaryClient, rate);

        logger.info("Предварительная ставка (rate) = {} %", rate);

        return rate;
    }


    /**
     * Метод для перерасчета предлагаемой суммы кредита в зависимости от наличия страховки
     * @param initialAmount - запрашиваемая (изначальная) сумма кредита
     * @param isInsuranceEnabled - наличие страховки
     * @return возвращает новую предлагаему сумму кредита
     */
    @Override
    public BigDecimal calculateAmount(BigDecimal initialAmount, Boolean isInsuranceEnabled) {

        BigDecimal amount = initialAmount;

        if(isInsuranceEnabled) {
            amount = amount.add(amount.multiply(new BigDecimal("0.10")));
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        logger.info("Поле isInsuranceEnabled = {}, новая сумма кредита (amount) = {} %", isInsuranceEnabled, amount);

        return amount;
    }


    /**
     * Метод для расчета стоимости страховки
     * @param amount - сумма кредита
     * @param isInsuranceEnabled - наличие страховки
     * @param isSalaryClient - является ли зарплатным клиентом
     * @return возвращает стоимость страховки
     */
    @Override
    public BigDecimal calculateInsurancePrice(BigDecimal amount, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        BigDecimal insurancePrice = new BigDecimal("0.00");

        if (isInsuranceEnabled && !isSalaryClient) {
            insurancePrice = amount.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        }

        logger.info("Стоимость страховки = {}", insurancePrice);

        return insurancePrice;
    }


    /**
     * Метод для расчета полной стоимости кредита (ПСК)
     * @param totalAmount - сумма всех платежей по кредиту
     * @param amount - сумма кредита
     * @param term - срок кредита, мес
     * @return возвращает полную стоимость кредита (ПСК) в процентах
     */
    @Override
    public BigDecimal calculatePSK(BigDecimal totalAmount, BigDecimal amount, Integer term) {

        BigDecimal termInYears = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);

        //Рассчет ПСК выполняется по формуле ПСК = (S/S0-1)/n * 100
        BigDecimal pskPerYear = (totalAmount.divide(amount, 4, RoundingMode.HALF_UP).subtract(BigDecimal.valueOf(1)))
                                            .divide(termInYears, 4, RoundingMode.HALF_UP)
                                            .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);

        logger.info("Размер ПСК = {} % в год", pskPerYear);

        return pskPerYear;
    }


    /**
     * Метод для расчета графика платежей
     * @param monthlyPayment - сумма ежемесячного платежа
     * @param amount - сумма кредита
     * @param rate - ставка, %
     * @param term - срок кредита, мес
     * @return возвращает список всех платежей по кредиту в порядке выплаты
     */
    @Override
    public List<PaymentScheduleElementDto> getPaymentSchedule(BigDecimal monthlyPayment, BigDecimal amount, BigDecimal rate, Integer term) {

        logger.info("====== Рассчет графика платежей ======");

        List<PaymentScheduleElementDto> paymentScheduleElementDtos = new ArrayList<>();

        //Исходные данные перед первой итерацией
        Integer number = 0;
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        BigDecimal remainingDebt = amount;
        BigDecimal totalPayment = monthlyPayment;

        while (number < term) {

            number = number + 1;

            logger.info("*** Платеж № {} ***", number);

            date = date.plusMonths(1);

            logger.info("Дата: {}", date);

            BigDecimal interestPayment = remainingDebt.multiply(rate).multiply(new BigDecimal("0.01")).multiply(BigDecimal.valueOf(date.lengthOfMonth()))
                    .divide(BigDecimal.valueOf(date.lengthOfYear()), 2, RoundingMode.HALF_UP);

            logger.info("Сумма для уплаты процентов: {}", interestPayment);

            BigDecimal debtPayment = totalPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            //Корректировка суммы для уплаты основного долга для последнего платежа (добавляем переплату или недоплату из remainingDebt в debtPayment)
            if (number.equals(term) && remainingDebt.compareTo(BigDecimal.ZERO) != 0) {
                debtPayment = debtPayment.add(remainingDebt);
                remainingDebt = BigDecimal.ZERO;
            }

            logger.info("Сумма для уплаты основного долга: {}", debtPayment);
            logger.info("Сумма оставшегося основного долга: {}", remainingDebt);

            totalPayment = debtPayment.add(interestPayment);

            logger.info("Общая сумма платежа: {}", totalPayment);

            PaymentScheduleElementDto paymentScheduleElementDto = new PaymentScheduleElementDto();

            paymentScheduleElementDto.setNumber(number);
            paymentScheduleElementDto.setDate(date);
            paymentScheduleElementDto.setTotalPayment(totalPayment);
            paymentScheduleElementDto.setInterestPayment(interestPayment);
            paymentScheduleElementDto.setDebtPayment(debtPayment);
            paymentScheduleElementDto.setRemainingDebt(remainingDebt);

            paymentScheduleElementDtos.add(paymentScheduleElementDto);
        }

        return paymentScheduleElementDtos;
    }

}
