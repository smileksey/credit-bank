package org.smileksey.calculator.calculators;

import lombok.extern.slf4j.Slf4j;
import org.smileksey.calculator.dto.PaymentScheduleElementDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/** Class for credit parameters calculation */
@Component
@Slf4j
public class CreditParamsCalculatorImpl implements CreditParamsCalculator {


    /**
     * Method for monthly payment calculation
     * @param amount - required loan amount
     * @param rate - loan rate, %
     * @param term - loan term, months
     * @param insurancePrice - insurance price
     * @return amount of the monthly payment
     */
    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term, BigDecimal insurancePrice) {

        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP);

        log.info("Monthly rate = {}", monthlyRate);

        monthlyRate = monthlyRate.divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);

        //Вычисляем коэффициент аннуитета по формуле (monthlyRate x (1 + monthlyRate)^term) / ((1 + monthlyRate)^term - 1)
        //Выделил в отдельную переменную повторяющийся фрагмент (1 + monthlyRate)^term
        BigDecimal pow = (monthlyRate.add(new BigDecimal("1.00"))).pow(term);

        BigDecimal annuityCoefficient = (monthlyRate.multiply(pow))
                .divide(pow.subtract(new BigDecimal("1.00")), 8, RoundingMode.HALF_UP);

        log.info("Annuity coefficient = {}", annuityCoefficient);

        BigDecimal monthlyPayment = annuityCoefficient
                .multiply(amount)
                .add(insurancePrice.divide(BigDecimal.valueOf(term), 4, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

        log.info("Monthly payment = {}", monthlyPayment);

        return monthlyPayment;
    }


    /**
     * Method for total amount of all loan payments calculation
     * @param monthlyPayment - monthly payment amount
     * @param term - loan term, months
     * @return total amount of all loan payments
     */
    @Override
    public BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, Integer term) {

        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));

        log.info("Total amount = {}", totalAmount);

        return totalAmount;

    }


    /**
     * Method for loan rate calculation
     * @param initialRate - initial (base) rate
     * @param isInsuranceEnabled - having insurance
     * @param isSalaryClient - is a salary client
     * @return loan rate, %
     */
    @Override
    public BigDecimal calculateRate(BigDecimal initialRate, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        log.info("Initial (base) loan rate = {} %", initialRate);

        BigDecimal rate = initialRate;

        if(isInsuranceEnabled) {
            rate = rate.subtract(new BigDecimal("3.00"));
        } else {
            rate = rate.add(new BigDecimal("1.00"));
        }

        log.info("isInsuranceEnabled = {}, new rate = {} %", isInsuranceEnabled, rate);

        if(isSalaryClient) {
            rate = rate.subtract(new BigDecimal("1.00"));
        }

        log.info("isSalaryClient = {}, new rate = {} %", isSalaryClient, rate);

        log.info("Preliminary rate = {} %", rate);

        return rate;
    }


    /**
     * Method for offered loan amount calculation depending on the insurance enabled
     * @param initialAmount - required loan amount
     * @param isInsuranceEnabled - having insurance
     * @return offered loan amount
     */
    @Override
    public BigDecimal calculateAmount(BigDecimal initialAmount, Boolean isInsuranceEnabled) {

        BigDecimal amount = initialAmount;

        if(isInsuranceEnabled) {
            amount = amount.add(amount.multiply(new BigDecimal("0.10")));
        }

        amount = amount.setScale(2, RoundingMode.HALF_UP);

        log.info("isInsuranceEnabled = {}, new loan amount = {} %", isInsuranceEnabled, amount);

        return amount;
    }


    /**
     * Method for insurance price calculation
     * @param amount - loan amount
     * @param isInsuranceEnabled - having insurance
     * @param isSalaryClient - is a salary client
     * @return insurance price
     */
    @Override
    public BigDecimal calculateInsurancePrice(BigDecimal amount, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        BigDecimal insurancePrice = new BigDecimal("0.00");

        if (isInsuranceEnabled && !isSalaryClient) {
            insurancePrice = amount.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        }

        log.info("Insurance price = {}", insurancePrice);

        return insurancePrice;
    }


    /**
     * Method for total loan cost calculation
     * @param totalAmount - total amount of all loan payments
     * @param amount - loan amount
     * @param term - loan term, months
     * @return total loan cost, %
     */
    @Override
    public BigDecimal calculatePSK(BigDecimal totalAmount, BigDecimal amount, Integer term) {

        BigDecimal termInYears = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);

        //Рассчет ПСК выполняется по формуле ПСК = (S/S0-1)/n * 100
        BigDecimal pskPerYear = (totalAmount.divide(amount, 4, RoundingMode.HALF_UP).subtract(BigDecimal.valueOf(1)))
                                            .divide(termInYears, 4, RoundingMode.HALF_UP)
                                            .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);

        log.info("Total loan cost (PSK) = {} % per year", pskPerYear);

        return pskPerYear;
    }


    /**
     * Method for payment schedule calculation
     * @param monthlyPayment - monthly payment amount
     * @param amount - loan amount
     * @param rate - loan rate, %
     * @param term - loan term, months
     * @return list of all payments in order of payment
     */
    @Override
    public List<PaymentScheduleElementDto> getPaymentSchedule(BigDecimal monthlyPayment, BigDecimal amount, BigDecimal rate, Integer term) {

        log.info("====== Payment schedule calculation ======");

        List<PaymentScheduleElementDto> paymentScheduleElementDtos = new ArrayList<>();

        //Исходные данные перед первой итерацией
        Integer number = 0;
        LocalDate date = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), 1);
        BigDecimal remainingDebt = amount;
        BigDecimal totalPayment = monthlyPayment;

        while (number < term) {

            number = number + 1;

            log.info("*** Payment #{} ***", number);

            date = date.plusMonths(1);

            log.info("Payment date: {}", date);

            BigDecimal interestPayment = remainingDebt.multiply(rate).multiply(new BigDecimal("0.01")).multiply(BigDecimal.valueOf(date.lengthOfMonth()))
                    .divide(BigDecimal.valueOf(date.lengthOfYear()), 2, RoundingMode.HALF_UP);

            log.info("Interest part of payment: {}", interestPayment);

            BigDecimal debtPayment = totalPayment.subtract(interestPayment);
            remainingDebt = remainingDebt.subtract(debtPayment);

            //Корректировка суммы для уплаты основного долга для последнего платежа (добавляем переплату или недоплату из remainingDebt в debtPayment)
            if (number.equals(term) && remainingDebt.compareTo(BigDecimal.ZERO) != 0) {
                debtPayment = debtPayment.add(remainingDebt);
                remainingDebt = BigDecimal.ZERO;
            }

            log.info("Debt part of payment: {}", debtPayment);
            log.info("Amount of remaining debt: {}", remainingDebt);

            totalPayment = debtPayment.add(interestPayment);

            log.info("Total amount of payment: {}", totalPayment);

            PaymentScheduleElementDto paymentScheduleElementDto = PaymentScheduleElementDto.builder()
                    .number(number)
                    .date(date)
                    .totalPayment(totalPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(remainingDebt)
                    .build();

            paymentScheduleElementDtos.add(paymentScheduleElementDto);
        }

        return paymentScheduleElementDtos;
    }

}
