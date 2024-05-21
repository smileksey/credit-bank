package org.smileksey.calculator.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
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
