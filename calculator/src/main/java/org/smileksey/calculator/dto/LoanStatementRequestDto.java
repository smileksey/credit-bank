package org.smileksey.calculator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class LoanStatementRequestDto {
    @NotNull(message = "'amount' is not specified")
    @Min(value = 30000, message = "Minimum loan amount is 30000")
    @Schema(
            description = "Loan amount",
            example = "500000")
    private BigDecimal amount;

    @NotNull(message = "'term' is not specified")
    @Min(value = 6, message = "Minimum loan term is 6 months")
    @Schema(
            description = "Loan term, months",
            example = "12")
    private Integer term;

    @NotBlank(message = "'firstName' is not specified")
    @Size(min = 2, max = 30, message = "First name must contain 2 to 30 symbols")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "First name must contain only latin letters")
    @Schema(
            description = "First name",
            example = "Ivan")
    private String firstName;

    @NotBlank(message = "'lastName' is not specified")
    @Size(min = 2, max = 30, message = "Last name must contain 2 to 30 symbols")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Last name must contain only latin letters")
    @Schema(
            description = "Last name",
            example = "Ivanov")
    private String lastName;

    @Size(min = 2, max = 30, message = "Middle name must contain 2 to 30 symbols")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Middle name must contain only latin letters")
    @Schema(
            description = "Middle name (if exists)",
            example = "Ivanovich")
    private String middleName;

    @NotBlank(message = "'email' is not specified")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email address is invalid")
    @Schema(
            description = "Email address",
            example = "ivanov@mail.ru")
    private String email;

    @NotNull(message = "'birthdate' is not specified")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Date of birth",
            example = "1990-01-10")
    private LocalDate birthdate;

    @NotBlank(message = "'passportSeries' is not specified")
    @Pattern(regexp = "^\\d{4}$", message = "Passport series must consist of 4 digits")
    @Schema(
            description = "Passport series",
            example = "1122")
    private String passportSeries;

    @NotBlank(message = "'passportNumber' is not specified")
    @Pattern(regexp = "^\\d{6}$", message = "Passport number must consist of 6 digits")
    @Schema(
            description = "Passport number",
            example = "123456")
    private String passportNumber;
}
