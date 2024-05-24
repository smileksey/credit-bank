package org.smileksey.calculator.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.dto.ScoringDataDto;
import org.smileksey.calculator.exceptions.LoanRefusedException;
import org.smileksey.calculator.exceptions.PrescoringException;
import org.smileksey.calculator.services.CreditService;
import org.smileksey.calculator.services.CreditServiceImpl;
import org.smileksey.calculator.services.LoanOfferService;
import org.smileksey.calculator.services.LoanOfferServiceImpl;
import org.smileksey.calculator.utils.ErrorResponse;
import org.smileksey.calculator.utils.validation.LoanStatementRequestValidator;
import org.smileksey.calculator.utils.PrescoringErrorMessage;
import org.smileksey.calculator.utils.validation.ScoringDataDtoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    private final static Logger logger = LogManager.getLogger(CalculatorController.class);

    private final LoanStatementRequestValidator loanStatementRequestValidator;
    private final ScoringDataDtoValidator scoringDataDtoValidator;
    private final LoanOfferService loanOfferServiceImpl;
    private final CreditService creditServiceImpl;


    @Autowired
    public CalculatorController(LoanStatementRequestValidator loanStatementRequestValidator, ScoringDataDtoValidator scoringDataDtoValidator, LoanOfferService loanOfferServiceImpl, CreditService creditServiceImpl) {
        this.loanStatementRequestValidator = loanStatementRequestValidator;
        this.scoringDataDtoValidator = scoringDataDtoValidator;
        this.loanOfferServiceImpl = loanOfferServiceImpl;
        this.creditServiceImpl = creditServiceImpl;
    }



    @PostMapping("/offers")
    public List<LoanOfferDto> getOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto,
                                        BindingResult bindingResult) {

        logger.info("Входящие данные по /calculator/offers: {}", loanStatementRequestDto );

        loanStatementRequestValidator.validate(loanStatementRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = PrescoringErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new PrescoringException(errorMessage);
        }

        return loanOfferServiceImpl.getLoanOffers(loanStatementRequestDto);
    }



    @PostMapping("/calc")
    public CreditDto getCreditDetails(@RequestBody @Valid ScoringDataDto scoringDataDto, BindingResult bindingResult) {

        logger.info("Входящие данные по /calculator/calc: {}", scoringDataDto);

        scoringDataDtoValidator.validate(scoringDataDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = PrescoringErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new PrescoringException(errorMessage);
        }

        return creditServiceImpl.getCreditDto(scoringDataDto).orElseThrow(LoanRefusedException::new);
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlePrescoringException(PrescoringException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        logger.error("Ошибка валидации: {}", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeParseException e) {

        String message = "Дата рождения должна быть в формате гггг-мм-дд";
        ErrorResponse response = new ErrorResponse(message);

        logger.error("Ошибка прескоринга: {}", message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleLoanRefusedException(LoanRefusedException e) {

        String message = "В кредите отказано";
        ErrorResponse response = new ErrorResponse(message);

        logger.error(message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
