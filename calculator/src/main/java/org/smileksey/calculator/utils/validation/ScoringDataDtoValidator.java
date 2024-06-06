package org.smileksey.calculator.utils.validation;

import org.smileksey.calculator.dto.ScoringDataDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.Period;

/** Validator for ScoringDataDto */
@Component
public class ScoringDataDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ScoringDataDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ScoringDataDto scoringDataDto = (ScoringDataDto) target;

        LocalDate birthdate = scoringDataDto.getBirthdate();

        if (birthdate == null) {
            return;
        }

        int age = Period.between(birthdate, LocalDate.now()).getYears();

        if (age < 18) {
            errors.rejectValue("birthdate", "", "Minimum age is 18");
        }

    }
}
