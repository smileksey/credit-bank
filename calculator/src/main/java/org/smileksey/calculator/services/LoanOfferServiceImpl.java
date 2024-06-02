package org.smileksey.calculator.services;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smileksey.calculator.calculators.CreditParamsCalculator;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.utils.LoanOfferDtoComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanOfferServiceImpl implements LoanOfferService {

    private final static Logger logger = LogManager.getLogger(LoanOfferServiceImpl.class);

    /** Базовая кредитная ставка */
    @Value("${base.rate}")
    private String stringBaseRate;

    private final CreditParamsCalculator creditParamsCalculator;


    /**
     * Метод формирует список из 4 предварительных кредитных предложений
     * @param loanStatementRequestDto - входящие данные от клиента
     * @return возвращает список из 4 кредитных предложений LoanOfferDto
     */
    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {

        List<LoanOfferDto> loanOffers = new ArrayList<>(List.of(
                createLoanOffer(loanStatementRequestDto, false, false),
                createLoanOffer(loanStatementRequestDto, false, true),
                createLoanOffer(loanStatementRequestDto, true, false),
                createLoanOffer(loanStatementRequestDto, true, true)
        ));

        loanOffers.sort(new LoanOfferDtoComparator());

        logger.info("**** Итоговые LoanOfferDto: ****");

        for (LoanOfferDto loanOfferDto : loanOffers) {
            logger.info(loanOfferDto);
        }

        return loanOffers;
    }


    /**
     * Метод выполняет прескоринг и расчет предварительного кредитного предложения в зависимости от того, есть ли у клиента страхова и является ли он зарплатным клиентом
     * @param loanStatementRequestDto - входящие данные от клиента
     * @param isInsuranceEnabled - наличие страховки
     * @param isSalaryClient - является ли зарплатным клиентом
     * @return возвращает кредитное предложение LoanOfferDto
     */
    private LoanOfferDto createLoanOffer(LoanStatementRequestDto loanStatementRequestDto ,Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        logger.info("====================================================");
        logger.info("Loan offer для: страховка - {}, зарплатный клиент - {}", isInsuranceEnabled, isSalaryClient);

        BigDecimal initialRate = new BigDecimal(stringBaseRate);
        BigDecimal initialAmount = loanStatementRequestDto.getAmount();
        Integer term = loanStatementRequestDto.getTerm();

        logger.info("Исходные данные: сумма кредита = {}, срок = {}, ставка = {}", initialAmount, term, initialRate);

        BigDecimal amount = creditParamsCalculator.calculateAmount(loanStatementRequestDto.getAmount(), isInsuranceEnabled);
        BigDecimal rate = creditParamsCalculator.calculateRate(initialRate, isInsuranceEnabled, isSalaryClient);
        BigDecimal insurancePrice = creditParamsCalculator.calculateInsurancePrice(amount, isInsuranceEnabled, isSalaryClient);

        logger.info("Данные после пересчета: сумма кредита = {}, срок = {}, ставка = {}, стоимость страховки = {}", amount, term, rate, insurancePrice);

        BigDecimal monthlyPayment = creditParamsCalculator.calculateMonthlyPayment(amount, rate, term, insurancePrice);

        BigDecimal totalAmount = creditParamsCalculator.calculateTotalAmount(monthlyPayment, term);

        logger.info("====================================================");

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
