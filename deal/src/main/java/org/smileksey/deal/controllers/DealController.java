package org.smileksey.deal.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.services.ClientServiceImpl;
import org.smileksey.deal.services.LoanOfferServiceImpl;
import org.smileksey.deal.utils.validation.LoanStatementRequestValidator;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.dto.FinishRegistrationRequestDto;
import org.smileksey.deal.exceptions.ValidationException;
import org.smileksey.deal.utils.ValidationErrorMessage;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
public class DealController {

    private final LoanStatementRequestValidator loanStatementRequestValidator;
    private final LoanOfferServiceImpl loanOfferServiceImpl;


    @PostMapping("/statement")
    public List<LoanOfferDto> calculateOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto,
                                              BindingResult bindingResult) {

        log.info("Input data to /deal/statement: {}", loanStatementRequestDto );

        loanStatementRequestValidator.validate(loanStatementRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new ValidationException(errorMessage);
        }

        return loanOfferServiceImpl.getLoanOffers(loanStatementRequestDto);
    }


    //TODO
    @PostMapping("/offer/select")
    public void selectOffer(@RequestBody @Valid LoanOfferDto loanOfferDto, BindingResult bindingResult) {

        log.info("select offer");
    }


    //TODO
    @PostMapping("/calculate/{statementId}")
    public void calculateCreditDetails(@PathVariable int statementId, @RequestBody @Valid FinishRegistrationRequestDto finishRegistrationRequestDto,
                                       BindingResult bindingResult) {

        log.info("calculate credit details");
    }
}
