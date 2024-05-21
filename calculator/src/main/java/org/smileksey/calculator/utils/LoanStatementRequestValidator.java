package org.smileksey.calculator.utils;

import org.smileksey.calculator.dto.LoanStatementRequestDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoanStatementRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return LoanStatementRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        LoanStatementRequestDto loanStatementRequestDto = (LoanStatementRequestDto) target;

//        if () {
//            errors.rejectValue("sensor", "", "This sensor has NOT been registered yet");
//        }

    }
}
