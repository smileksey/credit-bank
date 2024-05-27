package org.smileksey.calculator.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smileksey.calculator.calculators.CreditParamsCalculator;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.utils.LoanOfferDtoComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class LoanOfferServiceImpl implements LoanOfferService {

    private final static Logger logger = LogManager.getLogger(LoanOfferServiceImpl.class);

    /** Базовая кредитная ставка */
    @Value("${base.rate}")
    private String stringBaseRate;

    private final CreditParamsCalculator creditParamsCalculator;

    @Autowired
    public LoanOfferServiceImpl(CreditParamsCalculator creditParamsCalculator) {
        this.creditParamsCalculator = creditParamsCalculator;
    }


    /**
     * Метод формирует список из 4 предварительных кредитных предложений
     * @param loanStatementRequestDto - входящие данные от клиента
     * @return возвращает список из 4 кредитных предложений LoanOfferDto
     */
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

        //Пересчитываем сумму кредита, ставку и стоимость страховки
        BigDecimal amount = creditParamsCalculator.calculateAmount(initialAmount, isInsuranceEnabled);
        BigDecimal rate = creditParamsCalculator.calculateRate(initialRate, isInsuranceEnabled, isSalaryClient);
        BigDecimal insurancePrice = creditParamsCalculator.calculateInsurancePrice(amount, isInsuranceEnabled, isSalaryClient);

        logger.info("Данные после пересчета: сумма кредита = {}, срок = {}, ставка = {}, стоимость страховки = {}", amount, term, rate, insurancePrice);

        //Рассчитываем ежемесячныйы платеж
        BigDecimal monthlyPayment = creditParamsCalculator.calculateMonthlyPayment(amount, rate, term, insurancePrice);

        //Вычисляем полную стоимость кредита в денежном выражении
        BigDecimal totalAmount = creditParamsCalculator.calculateTotalAmount(monthlyPayment, term);

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
