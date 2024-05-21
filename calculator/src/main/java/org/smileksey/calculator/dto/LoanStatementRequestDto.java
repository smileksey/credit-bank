package org.smileksey.calculator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LoanStatementRequestDto {
    private BigDecimal amount;
    private int term;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private LocalDate birthdate;
    private String passportSeries;
    private String passportNumber;
}
