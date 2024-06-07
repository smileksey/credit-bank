package org.smileksey.calculator.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.calculator.calculators.CreditParamsCalculator;
import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.PaymentScheduleElementDto;
import org.smileksey.calculator.dto.ScoringDataDto;
import org.smileksey.calculator.dto.enums.EmploymentStatus;
import org.smileksey.calculator.dto.enums.Gender;
import org.smileksey.calculator.dto.enums.MaritalStatus;
import org.smileksey.calculator.dto.enums.Position;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditServiceImpl implements CreditService{


    /** Loan base rate */
    @Value("${base.rate}")
    private String baseRate;

    /** Minimum acceptable loan rate */
    private final BigDecimal MIN_RATE = new BigDecimal("9.00");

    /** Minimum acceptable age */
    private final int MIN_AGE = 20;

    /** Maximum acceptable age */
    private final int MAX_AGE = 65;

    /** Minimum acceptable total experience */
    private final int MIN_EXP_TOTAL = 18;

    /** Minimum acceptable current experience */
    private final int MIN_EXP_CURRENT = 3;

    /** Minimum eligible female age */
    private final int FEMALE_MIN_ELIGIBLE_AGE = 32;

    /** Maximum eligible female age */
    private final int FEMALE_MAX_ELIGIBLE_AGE = 60;

    /** Minimum eligible male age */
    private final int MALE_MIN_ELIGIBLE_AGE = 30;

    /** Maximum eligible male age */
    private final int MALE_MAX_ELIGIBLE_AGE = 55;

    private final CreditParamsCalculator creditParamsCalculator;


    /**
     * Method performs final calculation of the loan parameters or generates refusal
     * @param scoringDataDto - input data from client
     * @return Optional with nested CreditDto in case of approval, or Optional.empty() in case of refusal
     */
    @Override
    public Optional<CreditDto> getCreditDto(ScoringDataDto scoringDataDto) {

        BigDecimal rate = executeScoringAndGetRate(scoringDataDto);

        //Если executeScoringAndGetRate вернул rate == 0, значит в кредите отказано, возвращаем в контроллер пустой Optional
        if (rate.compareTo(BigDecimal.ZERO) == 0) {
            return Optional.empty();
        }

        BigDecimal insurancePrice = creditParamsCalculator.calculateInsurancePrice(scoringDataDto.getAmount(), scoringDataDto.getIsInsuranceEnabled(), scoringDataDto.getIsSalaryClient());
        BigDecimal monthlyPayment = creditParamsCalculator.calculateMonthlyPayment(scoringDataDto.getAmount(), rate, scoringDataDto.getTerm(), insurancePrice);
        BigDecimal totalAmount = creditParamsCalculator.calculateTotalAmount(monthlyPayment, scoringDataDto.getTerm());
        BigDecimal psk = creditParamsCalculator.calculatePSK(totalAmount, scoringDataDto.getAmount(), scoringDataDto.getTerm());
        List<PaymentScheduleElementDto> paymentSchedule = creditParamsCalculator.getPaymentSchedule(monthlyPayment, scoringDataDto.getAmount(), rate, scoringDataDto.getTerm());

        CreditDto creditDto = CreditDto.builder()
                .amount(scoringDataDto.getAmount())
                .term(scoringDataDto.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringDataDto.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDto.getIsSalaryClient())
                .paymentSchedule(paymentSchedule)
                .build();

        return Optional.of(creditDto);
    }


    /**
     * Method performs scoring and final loan rate calculation
     * @param scoringDataDto - input data from client
     * @return final loan rate, %
     */
    private BigDecimal executeScoringAndGetRate(ScoringDataDto scoringDataDto) {

        BigDecimal initialRate = new BigDecimal(baseRate);
        BigDecimal amount = scoringDataDto.getAmount();
        EmploymentStatus employmentStatus = scoringDataDto.getEmployment().getEmploymentStatus();
        Position position = scoringDataDto.getEmployment().getPosition();
        BigDecimal salary = scoringDataDto.getEmployment().getSalary();
        MaritalStatus maritalStatus = scoringDataDto.getMaritalStatus();
        int age = Period.between(scoringDataDto.getBirthdate(), LocalDate.now()).getYears();
        Gender gender = scoringDataDto.getGender();
        Integer workExperienceTotal = scoringDataDto.getEmployment().getWorkExperienceTotal();
        Integer workExperienceCurrent = scoringDataDto.getEmployment().getWorkExperienceCurrent();

        BigDecimal rate = creditParamsCalculator.calculateRate(initialRate, scoringDataDto.getIsInsuranceEnabled(), scoringDataDto.getIsSalaryClient());

        if (age < MIN_AGE || age > MAX_AGE) {
            log.info("age = {} does not meet requirements - loan refused", age);
            return BigDecimal.ZERO;
        }

        if (workExperienceTotal < MIN_EXP_TOTAL) {
            log.info("workExperienceTotal = {} does not meet requirements - loan refused", workExperienceTotal);
            return BigDecimal.ZERO;
        }

        if (workExperienceCurrent < MIN_EXP_CURRENT) {
            log.info("workExperienceCurrent = {} does not meet requirements - loan refused", workExperienceCurrent);
            return BigDecimal.ZERO;
        }

        BigDecimal salary25x = salary.multiply(BigDecimal.valueOf(25));

        if (amount.compareTo(salary25x) > 0) {
            log.info("loan amount = {} exceeds salary x 25 = {} - loan refused", amount, salary25x);
            return BigDecimal.ZERO;
        }

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

        log.info("employmentStatus = {}, new rate = {} %", employmentStatus, rate);

        switch (position) {
            case MID_MANAGER:
                rate = rate.subtract(new BigDecimal("2.00"));
                break;
            case TOP_MANAGER:
                rate = rate.subtract(new BigDecimal("3.00"));
                break;
        }

        log.info("position = {}, new rate = {} %", position, rate);

        switch (maritalStatus) {
            case MARRIED:
                rate = rate.subtract(new BigDecimal("3.00"));
                break;
            case SINGLE:
                rate = rate.add(new BigDecimal("1.00"));
                break;
        }

        log.info("maritalStatus = {}, new rate = {} %", maritalStatus, rate);

        switch (gender) {
            case FEMALE:
                if (age >= FEMALE_MIN_ELIGIBLE_AGE && age < FEMALE_MAX_ELIGIBLE_AGE) {
                    rate = rate.subtract(new BigDecimal("3.00"));
                }
                break;
            case MALE:
                if (age >= MALE_MIN_ELIGIBLE_AGE && age < MALE_MAX_ELIGIBLE_AGE) {
                    rate = rate.subtract(new BigDecimal("3.00"));
                }
                break;
            case NON_BINARY:
                rate = rate.add(new BigDecimal("7.00"));
                break;
        }

        log.info("gender = {}, age = {}, new rate = {} %", gender, age, rate);

        if (rate.compareTo(MIN_RATE) < 0) {

            log.info("rate = {} % is lower than minimum = {} %", rate, MIN_RATE);

            rate = MIN_RATE;

            log.info("Minimum acceptable loan rate = {} % is set", rate);
        }

        log.info("Final loan rate = {} %", rate);

        return rate;
    }

}
