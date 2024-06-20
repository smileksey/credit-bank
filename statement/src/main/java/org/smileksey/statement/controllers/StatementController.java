package org.smileksey.statement.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "Calculate 4 credit options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "4 credit options generated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid field values",
                    content = @Content)})
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


    @Operation(summary = "Select one of the 4 offered credit options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Option selected"),
            @ApiResponse(responseCode = "400", description = "Invalid field values")
    })
    @PostMapping("/offer")
    public void selectOffer(@RequestBody @Valid LoanOfferDto loanOfferDto, BindingResult bindingResult) {

        log.info("Input data to /statement/offer: {}", loanOfferDto);

        if (bindingResult.hasErrors()) {
            String errorMessage = ValidationErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new ValidationException(errorMessage);
        }

        loanOfferService.selectOffer(loanOfferDto);
    }


}
