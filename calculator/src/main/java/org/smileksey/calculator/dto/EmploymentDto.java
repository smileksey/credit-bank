package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;
import org.smileksey.calculator.dto.enums.EmploymentStatus;
import org.smileksey.calculator.dto.enums.Position;

import java.math.BigDecimal;

@Getter
@Setter
public class EmploymentDto {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
