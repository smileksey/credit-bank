package org.smileksey.calculator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(
            description = "Сумма кредита",
            example = "500000")
    private BigDecimal amount;

    @NotNull(message = "Не указано поле term")
    @Min(value = 6, message = "Минимальный срок кредита - 6 месяцев")
    @Schema(
            description = "Срок кредита, мес.",
            example = "12")
    private Integer term;

    @NotNull(message = "Не указано поле firstName")
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно состоять только из латинских букв")
    @Schema(
            description = "Имя",
            example = "Ivan")
    private String firstName;

    @NotNull(message = "Не указано поле lastName")
    @Size(min = 2, max = 30, message = "Фамилия должна содержать от 2 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Фамилия должна состоять только из латинских букв")
    @Schema(
            description = "Фамилия",
            example = "Ivanov")
    private String lastName;

    @Size(min = 2, max = 30, message = "Отчество должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Отчество должно состоять только из латинских букв")
    @Schema(
            description = "Отчество (при наличии)",
            example = "Ivanovich")
    private String middleName;

    @NotNull(message = "Не указано поле gender")
//    @GenderSubset(anyOf = {Gender.MALE, Gender.FEMALE, Gender.NON_BINARY})
    @Schema(
            description = "Пол",
            example = "MALE")
    private Gender gender;

    @NotNull(message = "Не указано поле birthdate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Дата рождения",
            example = "1990-01-10")
    private LocalDate birthdate;

    @NotNull(message = "Не указано поле passportSeries")
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта должна состоять из 4 цифр")
    @Schema(
            description = "Серия паспорта",
            example = "1122")
    private String passportSeries;

    @NotNull(message = "Не указано поле passportNumber")
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта должен состоять из 6 цифр")
    @Schema(
            description = "Номер паспорта",
            example = "123456")
    private String passportNumber;

    @NotNull(message = "Не указано поле passportIssueDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(
            description = "Дата выдачи паспорта",
            example = "2020-01-10")
    private LocalDate passportIssueDate;

    @NotNull(message = "Не указано поле passportIssueBranch")
    @Size(min = 10, max = 100, message = "Поле passportIssueBranch должно содержать от 10 до 100 символов")
    @Schema(
            description = "Кем выдан паспорт",
            example = "УВД Советского р-на гор. Нижнего Новгорода")
    private String passportIssueBranch;

    @NotNull(message = "Не указано поле maritalStatus")
    @Schema(
            description = "Семейное положение",
            example = "MARRIED")
    private MaritalStatus maritalStatus;

    @NotNull(message = "Не указано поле dependentAmount")
    @Schema(
            description = "Количество иждивенцев",
            example = "0")
    private Integer dependentAmount;

    @NotNull(message = "Не указано поле employment")
    @Valid
    private EmploymentDto employment;

    @NotNull(message = "Не указано поле accountNumber")
    @Schema(
            description = "Номер счета",
            example = "123456789")
    private String accountNumber;

    @NotNull(message = "Не указано поле isInsuranceEnabled")
    @Schema(
            description = "Наличие страховки",
            example = "true")
    private Boolean isInsuranceEnabled;

    @NotNull(message = "Не указано поле isSalaryClient")
    @Schema(
            description = "Является ли зарплатным клиентом банка",
            example = "false")
    private Boolean isSalaryClient;
}
