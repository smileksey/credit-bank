package org.smileksey.gateway.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.gateway.dto.FinishRegistrationRequestDto;
import org.smileksey.gateway.dto.LoanOfferDto;
import org.smileksey.gateway.dto.LoanStatementRequestDto;
import org.smileksey.gateway.dto.SESCodeDto;
import org.smileksey.gateway.services.GatewayServiceImpl;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GatewayController {

    private final GatewayServiceImpl gatewayService;

    @PostMapping("/statement")
    public List<LoanOfferDto> calculateOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {

        log.info("Input data to /statement: {}", loanStatementRequestDto);

        return gatewayService.getLoanOffers(loanStatementRequestDto);
    }


    @PostMapping("/statement/select")
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {

        log.info("Input data to /statement/offer: {}", loanOfferDto);

        gatewayService.selectOffer(loanOfferDto);
    }


    @PostMapping("/statement/registration/{statementId}")
    public void finishRegistration(@PathVariable UUID statementId, @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {

        log.info("Input data to /statement/registration/{}: {}", statementId, finishRegistrationRequestDto);

        gatewayService.calculateCredit(statementId, finishRegistrationRequestDto);
    }


    @PostMapping("/document/{statementId}")
    public void sendDocuments(@PathVariable UUID statementId) {

        log.info("Getting request to /document/{}", statementId);

        gatewayService.sendDocuments(statementId);
    }


    @PostMapping("/document/{statementId}/sign")
    public void signDocuments(@PathVariable UUID statementId) {

        log.info("Getting request to /document/{}/sign", statementId);

        gatewayService.signDocuments(statementId);
    }


    @PostMapping("/document/{statementId}/sign/code")
    public void verifySESCode(@PathVariable UUID statementId, @RequestBody SESCodeDto sesCodeDto) {

        log.info("Getting request to /document/{}/sign/code", statementId);

        gatewayService.verifySesCode(statementId, sesCodeDto);
    }

}
