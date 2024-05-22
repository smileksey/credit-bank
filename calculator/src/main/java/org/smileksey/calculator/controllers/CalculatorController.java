package org.smileksey.calculator.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.dto.ScoringDataDto;
import org.smileksey.calculator.exeptions.PrescoringException;
import org.smileksey.calculator.services.LoanOfferServiceImpl;
import org.smileksey.calculator.utils.ErrorResponse;
import org.smileksey.calculator.utils.LoanStatementRequestValidator;
import org.smileksey.calculator.utils.PrescoringErrorMessage;
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
    private final LoanOfferServiceImpl loanOfferServiceImpl;

    @Autowired
    public CalculatorController(LoanStatementRequestValidator loanStatementRequestValidator, LoanOfferServiceImpl loanOfferServiceImpl) {
        this.loanStatementRequestValidator = loanStatementRequestValidator;
        this.loanOfferServiceImpl = loanOfferServiceImpl;
    }



    @PostMapping("/offers")
    public List<LoanOfferDto> getOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto,
                                        BindingResult bindingResult) {

        logger.info("Входящие данные: {}", loanStatementRequestDto );

        loanStatementRequestValidator.validate(loanStatementRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = PrescoringErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new PrescoringException(errorMessage);
        }

        return loanOfferServiceImpl.getLoanOffers(loanStatementRequestDto);
    }



    @PostMapping("/calc")
    public CreditDto getCreditDetails(@RequestBody ScoringDataDto scoringDataDto) {
        return null;
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlePrescoringException(PrescoringException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        logger.error("Ошибка прескоринга: {}", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeParseException e) {

        String message = "Дата рождения должна быть в формате гггг-мм-дд";
        ErrorResponse response = new ErrorResponse(message);

        logger.error("Ошибка прескоринга: {}", message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
