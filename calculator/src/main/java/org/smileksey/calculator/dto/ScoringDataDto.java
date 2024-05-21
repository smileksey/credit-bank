package org.smileksey.calculator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

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
