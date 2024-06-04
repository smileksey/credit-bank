package org.smileksey.calculator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.dto.ScoringDataDto;
import org.smileksey.calculator.exceptions.LoanRefusedException;
import org.smileksey.calculator.exceptions.PrescoringException;
import org.smileksey.calculator.services.CreditService;
import org.smileksey.calculator.services.LoanOfferService;
import org.smileksey.calculator.utils.validation.LoanStatementRequestValidator;
import org.smileksey.calculator.utils.PrescoringErrorMessage;
import org.smileksey.calculator.utils.validation.ScoringDataDtoValidator;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
@Slf4j
public class CalculatorController {

    private final LoanStatementRequestValidator loanStatementRequestValidator;
    private final ScoringDataDtoValidator scoringDataDtoValidator;
    private final LoanOfferService loanOfferServiceImpl;
    private final CreditService creditServiceImpl;


    @Operation(summary = "Calculate 4 credit options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "4 credit options generated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid field values",
                    content = @Content) })
    @PostMapping("/offers")
    public List<LoanOfferDto> calculateOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto,
                                              BindingResult bindingResult) {

        log.info("Input data to /calculator/offers: {}", loanStatementRequestDto );

        loanStatementRequestValidator.validate(loanStatementRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = PrescoringErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new PrescoringException(errorMessage);
        }

        return loanOfferServiceImpl.getLoanOffers(loanStatementRequestDto);
    }


    @Operation(summary = "Calculate personal credit details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personal credit details generated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreditDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid field values",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Loan refused",
                    content = @Content) })
    @PostMapping("/calc")
    public CreditDto calculateCreditDetails(@RequestBody @Valid ScoringDataDto scoringDataDto, BindingResult bindingResult) {

        log.info("Input data to /calculator/calc: {}", scoringDataDto);

        scoringDataDtoValidator.validate(scoringDataDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = PrescoringErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new PrescoringException(errorMessage);
        }

        return creditServiceImpl.getCreditDto(scoringDataDto).orElseThrow(LoanRefusedException::new);
    }


}
