package org.smileksey.deal.models;

import lombok.*;
import org.smileksey.deal.models.enums.EmploymentPosition;
import org.smileksey.deal.models.enums.EmploymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employment {
    private UUID employmentId;
    private EmploymentStatus status;
    private String employerINN;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;

}
