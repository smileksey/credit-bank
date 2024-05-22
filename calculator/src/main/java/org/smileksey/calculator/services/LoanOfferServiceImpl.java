package org.smileksey.calculator.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.utils.LoanOfferDtoComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoanOfferServiceImpl implements LoanOfferService {

    private final static Logger logger = LogManager.getLogger(LoanOfferServiceImpl.class);

    //Базовая кредитная ставка
    @Value("${base.rate}")
    private String stringBaseRate;


    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {

        List<LoanOfferDto> loanOffers = new ArrayList<>();

        //Создаем 4 кредитных предложения в зависимости от того, является ли получатель зарплатным клиентом и оформляет ли страховку

        LoanOfferDto loanOfferNoInsuranceNoClient = createLoanOffer(loanStatementRequestDto, false, false);
        loanOffers.add(loanOfferNoInsuranceNoClient);

        LoanOfferDto loanOfferNoInsuranceClient = createLoanOffer(loanStatementRequestDto, false, true);
        loanOffers.add(loanOfferNoInsuranceClient);

        LoanOfferDto loanOfferInsuranceNoClient = createLoanOffer(loanStatementRequestDto, true, false);
        loanOffers.add(loanOfferInsuranceNoClient);

        LoanOfferDto loanOfferInsuranceClient = createLoanOffer(loanStatementRequestDto, true, true);
        loanOffers.add(loanOfferInsuranceClient);

        //сортируем список в порядке от набольшей ставки к наименьшей
        loanOffers.sort(new LoanOfferDtoComparator());

        logger.info("**** Итоговые LoanOfferDto: ****");

        for (LoanOfferDto loanOfferDto : loanOffers) {
            logger.info(loanOfferDto);
        }

        return loanOffers;
    }



    private LoanOfferDto createLoanOffer(LoanStatementRequestDto loanStatementRequestDto ,Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        logger.info("====================================================");
        logger.info("Loan offer для: страховка - {}, зарплатный клиент - {}", isInsuranceEnabled, isSalaryClient);

        BigDecimal rate = new BigDecimal(stringBaseRate);
        BigDecimal amount = loanStatementRequestDto.getAmount();
        BigDecimal insurancePrice = new BigDecimal("0.00");
        Integer term = loanStatementRequestDto.getTerm();

        logger.info("Исходные данные: сумма кредита = {}, срок = {}, ставка = {}", amount, term, rate);

        //Если есть страховка, уменьшаем ставку на 3%, увеличиваем сумму кредита на 10%
        //Если нет, увеличиваем ставку на 1%

        if(isInsuranceEnabled) {
            rate = rate.subtract(new BigDecimal("3.00"));
            amount = amount.add(amount.multiply(new BigDecimal("0.10"))).setScale(2, RoundingMode.HALF_UP);

            //Стоимость страховки включаем в стоимость кредита только в том случае, если получатель не является зарплатным клиентом. Если является, страховка - бесплатная.
            if (!isSalaryClient) {
                insurancePrice = amount.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP);
            }

        } else {
            rate = rate.add(new BigDecimal("1.00"));
        }

        //Если получатель - зарплатный клиент, уменьшаем ставку на 1%
        if(isSalaryClient) {
            rate = rate.subtract(new BigDecimal("1.00"));
        }

        logger.info("Данные после пересчета: сумма кредита = {}, срок = {}, ставка = {}, стоимость страховки = {}", amount, term, rate, insurancePrice);


        //==============================================================================================
        //Вычисляем месячную ставку
        BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);

        logger.info("Месячная ствка = {}", monthlyRate);

        //Для вычислений переводим процентную ставку в десятичнуб дробь
        monthlyRate = monthlyRate.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);

        //Выделил в переменную повторяющийся фрагмент из последующей формулы коэффициента аннуитета
        BigDecimal pow = (monthlyRate.add(new BigDecimal("1.00"))).pow(term);

        //Вычисляем коэффициент аннуитета
        BigDecimal annuityCoefficient = (monthlyRate.multiply(pow))
                                        .divide(pow.subtract(new BigDecimal("1.00")), 4, RoundingMode.HALF_UP);

        logger.info("Коэффициент аннуитета = {}", annuityCoefficient);

        //Вычисляем ежемесячный платеж с учетом стоимости страховки (включаем ее в сумму платежа равными долями)
        BigDecimal monthlyPayment = annuityCoefficient
                .multiply(amount)
                .add(insurancePrice.divide(BigDecimal.valueOf(term), 2, RoundingMode.HALF_UP))
                .setScale(2, RoundingMode.HALF_UP);

        logger.info("Ежемесячный платеж = {}", monthlyPayment);

        //Вычисляем полную стоимость кредита
        BigDecimal totalAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));

        logger.info("Полная стоимость кредита = {}", totalAmount);
        logger.info("====================================================");

        //Создаем объект LoanOfferDto и заполняем его поля вычисленными значениями
        LoanOfferDto loanOfferDto = new LoanOfferDto();

        loanOfferDto.setStatementId(UUID.randomUUID());
        loanOfferDto.setRequestedAmount(amount);
        loanOfferDto.setTotalAmount(totalAmount);
        loanOfferDto.setTerm(term);
        loanOfferDto.setMonthlyPayment(monthlyPayment);
        loanOfferDto.setRate(rate);
        loanOfferDto.setIsInsuranceEnabled(isInsuranceEnabled);
        loanOfferDto.setIsSalaryClient(isSalaryClient);

        return loanOfferDto;
    }
}
