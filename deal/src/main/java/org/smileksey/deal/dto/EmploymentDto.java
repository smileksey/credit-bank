package org.smileksey.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.smileksey.deal.dto.enums.EmploymentStatus;
import org.smileksey.deal.dto.enums.EmploymentPosition;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Builder
public class EmploymentDto {
    @NotNull(message = "'employmentStatus' is not specified")
    @Schema(
            description = "Employment status",
            example = "EMPLOYED")
    private EmploymentStatus employmentStatus;

    @NotBlank(message = "'employerINN' is not specified")
    @Pattern(regexp = "^(?=[0-9]*$)(?:.{10}|.{12})$", message = "INN must consist of 10 or 12 digits")
    @Schema(
            description = "Employer's taxpayer identification number (INN)",
            example = "1234567890")
    private String employerINN;

    @NotNull(message = "'salary' is not specified")
    @PositiveOrZero(message = "Salary amount must be positive or zero")
    @Schema(
            description = "Monthly salary amount",
            example = "50000")
    private BigDecimal salary;

    @NotNull(message = "'position' is not specified")
    @Schema(
            description = "Job position",
            example = "MIDDLE_MANAGER")
    private EmploymentPosition position;

    @NotNull(message = "'workExperienceTotal' is not specified")
    @PositiveOrZero
    @Schema(
            description = "Total work experience, months",
            example = "60")
    private Integer workExperienceTotal;

    @NotNull(message = "'workExperienceCurrent' is not specified")
    @PositiveOrZero
    @Schema(
            description = "Work experience at the current job, months",
            example = "5")
    private Integer workExperienceCurrent;
}
