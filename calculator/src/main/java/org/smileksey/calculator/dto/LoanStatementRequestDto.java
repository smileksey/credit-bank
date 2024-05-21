package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LoanStatementRequestDto {
    @NotNull
    @Min(value = 30000, message = "Минимальная сумма кредита - 30000")
    private BigDecimal amount;

    @NotNull
    @Min(value = 6, message = "Минимальный срок кредита - 6 месяцев")
    private int term;

    @NotNull
    @Size(min = 2, max = 30, message = "Имя должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Имя должно состоять только из латинских букв")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 30, message = "Фамилия должна содержать от 2 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Фамилия должна состоять только из латинских букв")
    private String lastName;

    @Size(min = 2, max = 30, message = "Отчество должно содержать от 2 до 30 символов")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Отчество должно состоять только из латинских букв")
    private String middleName;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email введен некорректно")
    private String email;

    @NotNull
    //@Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "Дата рождения должна быть в формате гггг-мм-дд")
    private LocalDate birthdate;

    @NotNull
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта должна состоять из 4 цифр")
    private String passportSeries;

    @NotNull
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта должен состоять из 6 цифр")
    private String passportNumber;
}
