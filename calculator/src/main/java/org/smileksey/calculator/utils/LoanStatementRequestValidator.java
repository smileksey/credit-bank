package org.smileksey.calculator.utils;

import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.Period;

@Component
public class LoanStatementRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LoanStatementRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        LoanStatementRequestDto loanStatementRequestDto = (LoanStatementRequestDto) target;

        //Проверяем, что возраст клиента не менее 18 лет
        LocalDate birthdate = loanStatementRequestDto.getBirthdate();

        if (birthdate == null) {
            return;
        }

        int age = Period.between(birthdate, LocalDate.now()).getYears();

        if (age < 18) {
            errors.rejectValue("birthdate", "", "Возраст должен быть не менее 18 лет");
        }

    }
}
