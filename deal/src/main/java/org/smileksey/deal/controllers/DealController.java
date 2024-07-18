package org.smileksey.deal.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.deal.dto.FinishRegistrationRequestDto;
import org.smileksey.deal.dto.LoanOfferDto;
import org.smileksey.deal.dto.LoanStatementRequestDto;
import org.smileksey.deal.dto.SESCodeDto;
import org.smileksey.deal.exceptions.ValidationException;
import org.smileksey.deal.models.Statement;
import org.smileksey.deal.services.CreditService;
import org.smileksey.deal.services.DocumentsService;
import org.smileksey.deal.services.LoanOfferService;
import org.smileksey.deal.services.StatementService;
import org.smileksey.deal.utils.ValidationErrorMessage;
import org.smileksey.deal.utils.validation.LoanStatementRequestValidator;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
public class DealController {

    private final LoanStatementRequestValidator loanStatementRequestValidator;
    private final LoanOfferService loanOfferService;
    private final StatementService statementService;
    private final CreditService creditService;
    private final DocumentsService documentsService;

    @Operation(summary = "Calculate 4 credit options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "4 credit options have been generated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid field values",
                    content = @Content)})
    @PostMapping("/statement")
    public List<LoanOfferDto> calculateOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto,
                                              BindingResult bindingResult) {

        log.info("Input data to /deal/statement: {}", loanStatementRequestDto );

        loanStatementRequestValidator.validate(loanStatementRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new ValidationException(errorMessage);
        }

        return loanOfferService.getLoanOffers(loanStatementRequestDto);
    }


    @Operation(summary = "Select one of the 4 offered credit options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Option has been selected"),
            @ApiResponse(responseCode = "400", description = "Invalid field values"),
            @ApiResponse(responseCode = "404", description = "Statement is not found")
            })
    @PostMapping("/offer/select")
    public void selectOffer(@RequestBody @Valid LoanOfferDto loanOfferDto, BindingResult bindingResult) {

        log.info("Input data to /deal/offer/select: {}", loanOfferDto);

        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new ValidationException(errorMessage);
        }

        statementService.updateStatementWithSelectedOffer(loanOfferDto);
    }


    @Operation(summary = "Calculate credit details and finish registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Credit details have been calculated, registration is finished"),
            @ApiResponse(responseCode = "400", description = "Invalid field values")
            })
    @PostMapping("/calculate/{statementId}")
    public void calculateCreditDetails(@PathVariable UUID statementId, @RequestBody @Valid FinishRegistrationRequestDto finishRegistrationRequestDto,
                                       BindingResult bindingResult) {

        log.info("Input data to /deal/calculate/{}: {}", statementId, finishRegistrationRequestDto);

        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new ValidationException(errorMessage);
        }

        creditService.calculateCreditAndFinishRegistration(statementId, finishRegistrationRequestDto);
    }


    @Operation(summary = "Request documents to be sent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents have been sent"),
            @ApiResponse(responseCode = "400", description = "Statement has inappropriate status for this action"),
            @ApiResponse(responseCode = "404", description = "Statement is not found")
    })
    @PostMapping("/document/{statementId}/send")
    public void sendDocuments(@PathVariable UUID statementId) {
        log.info("Getting request to /deal/document/{}/send", statementId);
        documentsService.handleSendDocuments(statementId);
    }


    @Operation(summary = "Request SES code to sign documents")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "SES code has been sent"),
            @ApiResponse(responseCode = "400", description = "Statement has inappropriate status for this action"),
            @ApiResponse(responseCode = "404", description = "Statement is not found")
    })
    @PostMapping("/document/{statementId}/sign")
    public void signDocuments(@PathVariable UUID statementId) {
        log.info("Getting request to /deal/document/{}/sign", statementId);
        documentsService.handleSignDocuments(statementId);
    }


    @Operation(summary = "Sign documents with SES code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Documents have been signed. Credit has been issued"),
            @ApiResponse(responseCode = "400", description = "Statement has inappropriate status for this action OR SES code is invalid"),
            @ApiResponse(responseCode = "404", description = "Statement is not found")
    })
    @PostMapping("/document/{statementId}/code")
    public void verifySESCode(@PathVariable UUID statementId, @RequestBody @Valid SESCodeDto sesCodeDto,
                              BindingResult bindingResult) {
        log.info("Getting request to /deal/document/{}/code", statementId);

        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new ValidationException(errorMessage);
        }

        documentsService.handleVerifySESCode(statementId, sesCodeDto);
    }


    @Operation(summary = "Update Statement status when documents sent")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statement status has been updated"),
            @ApiResponse(responseCode = "400", description = "Statement has inappropriate status for this action"),
            @ApiResponse(responseCode = "404", description = "Statement is not found")
    })
    @PutMapping("/admin/statement/{statementId}/status")
    public void updateStatementStatus(@PathVariable UUID statementId) {
        log.info("Getting request to /deal/admin/statement/{}/status", statementId);
        statementService.updateToDocumentCreated(statementId);
    }


    @Operation(summary = "Get Statement by it's ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "Statement is not found")
    })
    @GetMapping("/admin/statement/{statementId}")
    public Statement getStatementById(@PathVariable UUID statementId) {
        log.info("Getting request to /deal/admin/statement/{}", statementId);
        return statementService.getStatementById(statementId);
    }


    @Operation(summary = "Get all Statements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK")
    })
    @GetMapping("/admin/statement")
    public List<Statement> getStatements() {
        log.info("Getting request to /deal/admin/statement");
        return statementService.getAllStatements();
    }


}
