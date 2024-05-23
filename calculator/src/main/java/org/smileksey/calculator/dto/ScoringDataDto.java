package org.smileksey.calculator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.smileksey.calculator.dto.enums.Gender;
import org.smileksey.calculator.dto.enums.MaritalStatus;
import org.smileksey.calculator.utils.validation.GenderSubset;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class ScoringDataDto {

    @NotNull(message = "Не указано поле amount")
    @Min(value = 30000, message = "Минимальная сумма кредита - 30000")
    private BigDecimal amount;

    @NotNull(message = "Не указано поле term")
    @Min(value = 6, message = "Минимальный срок кредита - 6 месяцев")
    private Integer term;

    @NotNull(message = "Не указано поле firstName")
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно состоять только из латинских букв")
    private String firstName;

    @NotNull(message = "Не указано поле lastName")
    @Size(min = 2, max = 30, message = "Фамилия должна содержать от 2 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Фамилия должна состоять только из латинских букв")
    private String lastName;

    @Size(min = 2, max = 30, message = "Отчество должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Отчество должно состоять только из латинских букв")
    private String middleName;

    @NotNull(message = "Не указано поле gender")
//    @GenderSubset(anyOf = {Gender.MALE, Gender.FEMALE, Gender.NON_BINARY})
    private Gender gender;

    @NotNull(message = "Не указано поле birthdate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @NotNull(message = "Не указано поле passportSeries")
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта должна состоять из 4 цифр")
    private String passportSeries;

    @NotNull(message = "Не указано поле passportNumber")
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта должен состоять из 6 цифр")
    private String passportNumber;

    @NotNull(message = "Не указано поле passportIssueDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate passportIssueDate;

    @NotNull(message = "Не указано поле passportIssueBranch")
    @Size(min = 10, max = 100, message = "Поле passportIssueBranch должно содержать от 10 до 100 символов")
    private String passportIssueBranch;

    @NotNull(message = "Не указано поле maritalStatus")
    private MaritalStatus maritalStatus;

    @NotNull(message = "Не указано поле dependentAmount")
    private Integer dependentAmount;

    @NotNull(message = "Не указано поле employment")
    @Valid
    private EmploymentDto employment;

    @NotNull(message = "Не указано поле accountNumber")
    private String accountNumber;

    @NotNull(message = "Не указано поле isInsuranceEnabled")
    private Boolean isInsuranceEnabled;

    @NotNull(message = "Не указано поле isSalaryClient")
    private Boolean isSalaryClient;
}
