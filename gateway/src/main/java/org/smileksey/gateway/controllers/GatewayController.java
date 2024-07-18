package org.smileksey.gateway.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.gateway.dto.FinishRegistrationRequestDto;
import org.smileksey.gateway.dto.LoanOfferDto;
import org.smileksey.gateway.dto.LoanStatementRequestDto;
import org.smileksey.gateway.dto.SESCodeDto;
import org.smileksey.gateway.services.GatewayServiceImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GatewayController {

    private final GatewayServiceImpl gatewayService;


    @Operation(summary = "Calculate 4 credit options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "4 credit options generated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid field values",
                    content = @Content)})
    @PostMapping("/statement")
    public List<LoanOfferDto> calculateOffers(@RequestBody LoanStatementRequestDto loanStatementRequestDto) {

        log.info("Input data to /statement: {}", loanStatementRequestDto);

        return gatewayService.getLoanOffers(loanStatementRequestDto);
    }


    @Operation(summary = "Select one of the 4 offered credit options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Option selected"),
            @ApiResponse(responseCode = "400", description = "Invalid field values")
    })
    @PostMapping("/statement/select")
    public void selectOffer(@RequestBody LoanOfferDto loanOfferDto) {

        log.info("Input data to /statement/offer: {}", loanOfferDto);

        gatewayService.selectOffer(loanOfferDto);
    }


    @Operation(summary = "Calculate credit details and finish registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit details have been calculated, registration is finished"),
            @ApiResponse(responseCode = "400", description = "Invalid field values"),
            @ApiResponse(responseCode = "404", description = "Loan was refused")
    })
    @PostMapping("/statement/registration/{statementId}")
    public void finishRegistration(@PathVariable UUID statementId, @RequestBody FinishRegistrationRequestDto finishRegistrationRequestDto) {

        log.info("Input data to /statement/registration/{}: {}", statementId, finishRegistrationRequestDto);

        gatewayService.calculateCredit(statementId, finishRegistrationRequestDto);
    }


    @Operation(summary = "Request documents to be sent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents have been sent"),
            @ApiResponse(responseCode = "400", description = "Statement has inappropriate status for this action"),
            @ApiResponse(responseCode = "404", description = "Statement is not found")
    })
    @PostMapping("/document/{statementId}")
    public void sendDocuments(@PathVariable UUID statementId) {

        log.info("Getting request to /document/{}", statementId);

        gatewayService.sendDocuments(statementId);
    }


    @Operation(summary = "Request SES code to sign documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SES code has been sent"),
            @ApiResponse(responseCode = "400", description = "Statement has inappropriate status for this action"),
            @ApiResponse(responseCode = "404", description = "Statement is not found")
    })
    @PostMapping("/document/{statementId}/sign")
    public void signDocuments(@PathVariable UUID statementId) {

        log.info("Getting request to /document/{}/sign", statementId);

        gatewayService.signDocuments(statementId);
    }


    @Operation(summary = "Sign documents with SES code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents have been signed. Credit has been issued"),
            @ApiResponse(responseCode = "400", description = "Statement has inappropriate status for this action OR SES code is invalid"),
            @ApiResponse(responseCode = "404", description = "Statement is not found")
    })
    @PostMapping("/document/{statementId}/sign/code")
    public void verifySESCode(@PathVariable UUID statementId, @RequestBody SESCodeDto sesCodeDto) {

        log.info("Getting request to /document/{}/sign/code", statementId);

        gatewayService.verifySesCode(statementId, sesCodeDto);
    }

}
