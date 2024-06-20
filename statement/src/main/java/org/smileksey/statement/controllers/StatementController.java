package org.smileksey.statement.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.statement.dto.LoanOfferDto;
import org.smileksey.statement.dto.LoanStatementRequestDto;
import org.smileksey.statement.exceptions.ValidationException;
import org.smileksey.statement.services.LoanOfferService;
import org.smileksey.statement.utils.ValidationErrorMessage;
import org.smileksey.statement.utils.validation.LoanStatementRequestValidator;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
@Slf4j
public class StatementController {

    private final LoanStatementRequestValidator loanStatementRequestValidator;
    private final LoanOfferService loanOfferService;


    @PostMapping
    public List<LoanOfferDto> calculateOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto,
                                              BindingResult bindingResult) {

        log.info("Input data to /statement: {}", loanStatementRequestDto );

        loanStatementRequestValidator.validate(loanStatementRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new ValidationException(errorMessage);
        }

        return loanOfferService.getLoanOffers(loanStatementRequestDto);
    }


    @PostMapping("/offer")
    public void selectOffer(@RequestBody @Valid LoanOfferDto loanOfferDto, BindingResult bindingResult) {

    }


}
