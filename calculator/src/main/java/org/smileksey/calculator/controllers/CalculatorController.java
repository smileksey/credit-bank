package org.smileksey.calculator.controllers;

import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.dto.ScoringDataDto;
import org.smileksey.calculator.exeptions.PrescoringException;
import org.smileksey.calculator.utils.ErrorResponse;
import org.smileksey.calculator.utils.LoanStatementRequestValidator;
import org.smileksey.calculator.utils.PrescoringErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    private final LoanStatementRequestValidator loanStatementRequestValidator;

    @Autowired
    public CalculatorController(LoanStatementRequestValidator loanStatementRequestValidator) {
        this.loanStatementRequestValidator = loanStatementRequestValidator;
    }

    @PostMapping("/offers")
    public List<LoanOfferDto> getOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto,
                                        BindingResult bindingResult) {

        //TODO добавить валидацию возраста и формата даты
        loanStatementRequestValidator.validate(loanStatementRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = PrescoringErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new PrescoringException(errorMessage);
        }
        return List.of(new LoanOfferDto());
    }

    @PostMapping("/calc")
    public CreditDto getCreditDetails(@RequestBody ScoringDataDto scoringDataDto) {
        return null;
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(PrescoringException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
