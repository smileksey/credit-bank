package org.smileksey.calculator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.smileksey.calculator.dto.enums.Gender;
import org.smileksey.calculator.dto.enums.MaritalStatus;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ScoringDataDto {

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

    @NotNull(message = "'gender' is not specified")
    @Schema(
            description = "Gender",
            example = "MALE")
    private Gender gender;

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

    @NotNull(message = "'passportIssueDate' is not specified")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Passport issue date",
            example = "2020-01-10")
    private LocalDate passportIssueDate;

    @NotBlank(message = "'passportIssueBranch' is not specified")
    @Size(min = 10, max = 100, message = "'passportIssueBranch' must contain 10 to 100 symbols")
    @Schema(
            description = "Branch issued the passport",
            example = "УВД Советского р-на гор. Нижнего Новгорода")
    private String passportIssueBranch;

    @NotNull(message = "'maritalStatus' is not specified")
    @Schema(
            description = "Martial status",
            example = "MARRIED")
    private MaritalStatus maritalStatus;

    @NotNull(message = "'dependentAmount' is not specified")
    @Schema(
            description = "Amount of dependents",
            example = "0")
    private Integer dependentAmount;

    @NotNull(message = "'employment' is not specified")
    @Valid
    private EmploymentDto employment;

    @NotBlank(message = "'accountNumber' is not specified")
    @Schema(
            description = "Account number",
            example = "123456789")
    private String accountNumber;

    @NotNull(message = "'isInsuranceEnabled' is not specified")
    @Schema(
            description = "Having insurance",
            example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull(message = "'isSalaryClient' is not specified")
    @Schema(
            description = "Is a salary client",
            example = "false")
    private Boolean isSalaryClient;
}
