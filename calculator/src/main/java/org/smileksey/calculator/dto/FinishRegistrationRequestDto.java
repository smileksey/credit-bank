package org.smileksey.calculator.dto;

import java.time.LocalDate;

public class FinishRegistrationRequestDto {
    private Gender gender;
    private MaritalStatus maritalStatus;
    private int dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDto employment;
    private String accountNumber;
}
