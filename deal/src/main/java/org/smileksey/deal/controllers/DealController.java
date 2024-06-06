package org.smileksey.deal.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.dto.FinishRegistrationRequestDto;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
public class DealController {

    @PostMapping("/statement")
    public List<LoanOfferDto> calculateOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto,
                                              BindingResult bindingResult) {

        log.info("calculate offers");

        return null;
    }

    @PostMapping("/offer/select")
    public void selectOffer(@RequestBody @Valid LoanOfferDto loanOfferDto, BindingResult bindingResult) {

        log.info("select offer");
    }

    @PostMapping("/calculate/{statementId}")
    public void calculateCreditDetails(@PathVariable int statementId, @RequestBody @Valid FinishRegistrationRequestDto finishRegistrationRequestDto,
                                       BindingResult bindingResult) {

        log.info("calculate credit details");
    }
}
