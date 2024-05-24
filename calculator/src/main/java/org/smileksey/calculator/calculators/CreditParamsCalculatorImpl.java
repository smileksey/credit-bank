package org.smileksey.calculator.calculators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smileksey.calculator.services.LoanOfferServiceImpl;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class CreditParamsCalculatorImpl implements CreditParamsCalculator {

    private final static Logger logger = LogManager.getLogger(LoanOfferServiceImpl.class);


    @Override
    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term, BigDecimal insurancePrice) {

        //Вычисляем месячную ставку
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 8, RoundingMode.HALF_UP);

        logger.info("Месячная ставка = {}", monthlyRate);

        //Для вычислений переводим процентную ставку в десятичнуб дробь
        monthlyRate = monthlyRate.divide(BigDecimal.valueOf(100), 8, RoundingMode.HALF_UP);

        //Выделил в переменную повторяющийся фрагмент из последующей формулы коэффициента аннуитета
        BigDecimal pow = (monthlyRate.add(new BigDecimal("1.00"))).pow(term);

        //Вычисляем коэффициент аннуитета
        BigDecimal annuityCoefficient = (monthlyRate.multiply(pow))
                .divide(pow.subtract(new BigDecimal("1.00")), 8, RoundingMode.HALF_UP);

        logger.info("Коэффициент аннуитета = {}", annuityCoefficient);

        //Вычисляем ежемесячный платеж с учетом стоимости страховки (включаем ее в сумму платежа равными долями)
        BigDecimal monthlyPayment = annuityCoefficient
                .multiply(amount)
                .add(insurancePrice.divide(BigDecimal.valueOf(term), 4, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

        logger.info("Ежемесячный платеж = {}", monthlyPayment);

        return monthlyPayment;
    }


    @Override
    public BigDecimal calculateTotalAmount(BigDecimal monthlyPayment, Integer term) {

        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));

        logger.info("Сумма всех выплат по кредиту = {}", totalAmount);

        return totalAmount;

    }


    @Override
    public BigDecimal calculateRate(BigDecimal initialRate, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        logger.info("Исходная базовая ставка (initialRate) = {} %", initialRate);

        BigDecimal rate = initialRate;

        //Если есть страховка, уменьшаем ставку на 3%
        //Если нет, увеличиваем ставку на 1%
        if(isInsuranceEnabled) {
            rate = rate.subtract(new BigDecimal("3.00"));
        } else {
            rate = rate.add(new BigDecimal("1.00"));
        }

        logger.info("Поле isInsuranceEnabled = {}, новая cтавка (rate) = {} %", isInsuranceEnabled, rate);

        //Если получатель - зарплатный клиент, уменьшаем ставку на 1%
        if(isSalaryClient) {
            rate = rate.subtract(new BigDecimal("1.00"));
        }

        logger.info("Поле isSalaryClient = {}, новая cтавка (rate) = {} %", isSalaryClient, rate);

        logger.info("Предварительная ставка (rate) = {} %", rate);

        return rate;
    }


    @Override
    public BigDecimal calculateAmount(BigDecimal initialAmount, Boolean isInsuranceEnabled) {

        BigDecimal amount = initialAmount;

        //Если есть страховка, увеличиваем сумму кредита на 10%
        if(isInsuranceEnabled) {
            amount = amount.add(amount.multiply(new BigDecimal("0.10"))).setScale(2, RoundingMode.HALF_UP);
        }

        logger.info("Поле isInsuranceEnabled = {}, новая сумма кредита (amount) = {} %", isInsuranceEnabled, amount);

        return amount;
    }


    @Override
    public BigDecimal calculateInsurancePrice(BigDecimal amount, Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        BigDecimal insurancePrice = new BigDecimal("0.00");

        //Стоимость страховки включаем в стоимость кредита только в том случае, если получатель не является зарплатным клиентом. Если является, страховка - бесплатная.
        if (isInsuranceEnabled && !isSalaryClient) {
            insurancePrice = amount.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
        }

        logger.info("Стоимость страховки = {}", insurancePrice);

        return insurancePrice;
    }

    @Override
    public BigDecimal calculatePSK(BigDecimal totalAmount, BigDecimal amount, Integer term) {

        //Рассчет ПСК выполняется по формуле ПСК = (S/S0-1)/n * 100

        BigDecimal termInYears = BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);

        BigDecimal pskPerYear = (totalAmount.divide(amount, 4, RoundingMode.HALF_UP).subtract(BigDecimal.valueOf(1)))
                                            .divide(termInYears, 4, RoundingMode.HALF_UP)
                                            .multiply(BigDecimal.valueOf(100));

        BigDecimal totalPsk = pskPerYear.multiply(termInYears).setScale(2, RoundingMode.HALF_UP);

        logger.info("Размер ПСК = {} %", totalPsk);

        return totalPsk;
    }
}
