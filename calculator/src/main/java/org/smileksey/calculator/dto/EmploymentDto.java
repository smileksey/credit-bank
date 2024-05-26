package org.smileksey.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(
            description = "Тип занятости",
            example = "EMPLOYED")
    private EmploymentStatus employmentStatus;

    @NotNull(message = "Не указано поле employerINN")
    @Pattern(regexp = "^(?=[0-9]*$)(?:.{10}|.{12})$", message = "ИНН должен состоять из 10 или 12 цифр")
    @Schema(
            description = "ИНН работодателя",
            example = "1234567890")
    private String employerINN;

    @NotNull(message = "Не указано поле salary")
    @PositiveOrZero(message = "Зарплата не может быть меньше 0")
    @Schema(
            description = "Ежемесячная зарплата",
            example = "50000")
    private BigDecimal salary;

    @NotNull(message = "Не указано поле position")
    @Schema(
            description = "Должность",
            example = "MIDDLE_MANAGER")
    private Position position;

    @NotNull(message = "Не указано поле workExperienceTotal")
    @PositiveOrZero
    @Schema(
            description = "Общий трудовой стаж, мес.",
            example = "60")
    private Integer workExperienceTotal;

    @NotNull(message = "Не указано поле workExperienceCurrent")
    @PositiveOrZero
    @Schema(
            description = "Стаж на текущем месте работы, мес.",
            example = "5")
    private Integer workExperienceCurrent;
}
