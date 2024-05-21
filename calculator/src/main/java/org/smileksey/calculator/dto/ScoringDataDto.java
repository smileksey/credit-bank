package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;
import org.smileksey.calculator.dto.enums.Gender;
import org.smileksey.calculator.dto.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class ScoringDataDto {
    private BigDecimal amount;
    private int term;
    private String firstName;
    private String lastName;
    private String middleName;
    private Gender gender;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private int dependentAmount;
    private EmploymentDto employment;
    private String accountNumber;
    private boolean isInsuranceEnabled;
    private boolean isSalaryClient;
}
