package org.smileksey.calculator.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.smileksey.calculator.dto.CreditDto;
import org.smileksey.calculator.dto.LoanOfferDto;
import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.smileksey.calculator.dto.ScoringDataDto;
import org.smileksey.calculator.exceptions.LoanRefusedException;
import org.smileksey.calculator.exceptions.PrescoringException;
import org.smileksey.calculator.services.CreditService;
import org.smileksey.calculator.services.LoanOfferService;
import org.smileksey.calculator.utils.ErrorResponse;
import org.smileksey.calculator.utils.validation.LoanStatementRequestValidator;
import org.smileksey.calculator.utils.PrescoringErrorMessage;
import org.smileksey.calculator.utils.validation.ScoringDataDtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController {

    private final static Logger logger = LogManager.getLogger(CalculatorController.class);

    private final LoanStatementRequestValidator loanStatementRequestValidator;
    private final ScoringDataDtoValidator scoringDataDtoValidator;
    private final LoanOfferService loanOfferServiceImpl;
    private final CreditService creditServiceImpl;


    @Operation(summary = "Get 4 credit options")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "4 credit options generated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid field values",
                    content = @Content) })
    @PostMapping("/offers")
    public List<LoanOfferDto> getOffers(@RequestBody @Valid LoanStatementRequestDto loanStatementRequestDto,
                                        BindingResult bindingResult) {

        logger.info("Входящие данные по /calculator/offers: {}", loanStatementRequestDto );

        loanStatementRequestValidator.validate(loanStatementRequestDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = PrescoringErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new PrescoringException(errorMessage);
        }

        return loanOfferServiceImpl.getLoanOffers(loanStatementRequestDto);
    }


    @Operation(summary = "Get personal credit details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Personal credit details generated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CreditDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid field values",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Loan refused",
                    content = @Content) })
    @PostMapping("/calc")
    public CreditDto getCreditDetails(@RequestBody @Valid ScoringDataDto scoringDataDto, BindingResult bindingResult) {

        logger.info("Входящие данные по /calculator/calc: {}", scoringDataDto);

        scoringDataDtoValidator.validate(scoringDataDto, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = PrescoringErrorMessage.createMessage(bindingResult.getFieldErrors());
            throw new PrescoringException(errorMessage);
        }

        return creditServiceImpl.getCreditDto(scoringDataDto).orElseThrow(LoanRefusedException::new);
    }


    /** Метод перехватывает исключение PrescoringException и возвращает клиенту ответ с ошибкой */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handlePrescoringException(PrescoringException e) {

        ErrorResponse response = new ErrorResponse(e.getMessage());

        logger.error("Ошибка валидации: {}", e.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /** Метод перехватывает исключение DateTimeParseException в случае некорректного ввода даты и возвращает клиенту ответ с ошибкой */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleDateTimeException(DateTimeParseException e) {

        String message = "Дата рождения должна быть в формате гггг-мм-дд";
        ErrorResponse response = new ErrorResponse(message);

        logger.error("Ошибка прескоринга: {}", message);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    /** Метод перехватывает исключение LoanRefusedException в случае отказа в кредите и возвращает клиенту ответ с ошибкой */
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleLoanRefusedException(LoanRefusedException e) {

        String message = "В кредите отказано";
        ErrorResponse response = new ErrorResponse(message);

        logger.error(message);

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


}
