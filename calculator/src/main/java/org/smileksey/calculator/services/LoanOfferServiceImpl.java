package org.smileksey.calculator.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.calculator.calculators.CreditParamsCalculator;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanOfferServiceImpl implements LoanOfferService {


    /** Loan base rate */
    @Value("${base.rate}")
    private String baseRate;

    private final CreditParamsCalculator creditParamsCalculator;


    /**
     * Method creates a list of 4 preliminary loan offers
     * @param loanStatementRequestDto - input data from client
     * @return list of 4 preliminary loan offers
     */
    @Override
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto loanStatementRequestDto) {

        List<LoanOfferDto> loanOffers = new ArrayList<>(List.of(
                createLoanOffer(loanStatementRequestDto, false, false),
                createLoanOffer(loanStatementRequestDto, false, true),
                createLoanOffer(loanStatementRequestDto, true, false),
                createLoanOffer(loanStatementRequestDto, true, true)
        ));

        loanOffers.sort((o1, o2) -> o2.getRate().compareTo(o1.getRate()));

        log.info("**** Final LoanOfferDto list: ****");

        for (LoanOfferDto loanOfferDto : loanOffers) {
            log.info(String.valueOf(loanOfferDto));
        }

        return loanOffers;
    }


    /**
     * Method performs prescoring and calculation of a preliminary loan offer depending on whether the client has insurance and whether he is a salary client
     * @param loanStatementRequestDto - input data from client
     * @param isInsuranceEnabled - having insurance
     * @param isSalaryClient - is a salary client
     * @return preliminary loan offer
     */
    private LoanOfferDto createLoanOffer(LoanStatementRequestDto loanStatementRequestDto ,Boolean isInsuranceEnabled, Boolean isSalaryClient) {

        log.info("====================================================");
        log.info("Loan offer considering: insuranceEnabled - {}, isSalaryClient - {}", isInsuranceEnabled, isSalaryClient);

        BigDecimal initialRate = new BigDecimal(baseRate);
        BigDecimal initialAmount = loanStatementRequestDto.getAmount();
        Integer term = loanStatementRequestDto.getTerm();

        log.info("Initial data: loan amount = {}, term = {}, rate = {}", initialAmount, term, initialRate);

        BigDecimal amount = creditParamsCalculator.calculateAmount(loanStatementRequestDto.getAmount(), isInsuranceEnabled);
        BigDecimal rate = creditParamsCalculator.calculateRate(initialRate, isInsuranceEnabled, isSalaryClient);
        BigDecimal insurancePrice = creditParamsCalculator.calculateInsurancePrice(amount, isInsuranceEnabled, isSalaryClient);

        log.info("Data after calculation: loan amount = {}, term = {}, rate = {}, insurance price = {}", amount, term, rate, insurancePrice);

        BigDecimal monthlyPayment = creditParamsCalculator.calculateMonthlyPayment(amount, rate, term, insurancePrice);
        BigDecimal totalAmount = creditParamsCalculator.calculateTotalAmount(monthlyPayment, term);

        log.info("====================================================");

        return LoanOfferDto.builder()
                        .statementId(UUID.randomUUID())
                        .requestedAmount(amount)
                        .totalAmount(totalAmount)
                        .term(term)
                        .monthlyPayment(monthlyPayment)
                        .rate(rate)
                        .isInsuranceEnabled(isInsuranceEnabled)
                        .isSalaryClient(isSalaryClient)
                        .build();
    }

}
