package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.smileksey.calculator.dto.enums.EmploymentStatus;
import org.smileksey.calculator.dto.enums.Position;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class EmploymentDto {
    @NotNull(message = "Не указано поле employmentStatus")
    private EmploymentStatus employmentStatus;

    @NotNull(message = "Не указано поле employerINN")
    @Pattern(regexp = "^(?=[0-9]*$)(?:.{10}|.{12})$", message = "ИНН должен состоять из 10 или 12 цифр")
    private String employerINN;

    @NotNull(message = "Не указано поле salary")
    private BigDecimal salary;

    @NotNull(message = "Не указано поле position")
    private Position position;

    @NotNull(message = "Не указано поле workExperienceTotal")
    @PositiveOrZero
    private Integer workExperienceTotal;

    @NotNull(message = "Не указано поле workExperienceCurrent")
    @PositiveOrZero
    private Integer workExperienceCurrent;
}
