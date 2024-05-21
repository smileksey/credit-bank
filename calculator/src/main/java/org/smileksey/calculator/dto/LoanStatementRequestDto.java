package org.smileksey.calculator.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@ToString
public class LoanStatementRequestDto {
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

    @NotNull(message = "Не указано поле email")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "Email введен некорректно")
    private String email;

    @NotNull(message = "Не указано поле birthdate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthdate;

    @NotNull(message = "Не указано поле passportSeries")
    @Pattern(regexp = "^\\d{4}$", message = "Серия паспорта должна состоять из 4 цифр")
    private String passportSeries;

    @NotNull(message = "Не указано поле passportNumber")
    @Pattern(regexp = "^\\d{6}$", message = "Номер паспорта должен состоять из 6 цифр")
    private String passportNumber;
}
