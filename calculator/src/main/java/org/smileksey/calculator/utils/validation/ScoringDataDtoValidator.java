package org.smileksey.calculator.utils.validation;

import org.smileksey.calculator.dto.ScoringDataDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.Period;

/** Валидатор для входящих объектов ScoringDataDto */
@Component
public class ScoringDataDtoValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return ScoringDataDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ScoringDataDto scoringDataDto = (ScoringDataDto) target;

        //Проверяем, что возраст клиента не менее 18 лет
        LocalDate birthdate = scoringDataDto.getBirthdate();

        if (birthdate == null) {
            return;
        }

        int age = Period.between(birthdate, LocalDate.now()).getYears();

        if (age < 18) {
            errors.rejectValue("birthdate", "", "Возраст должен быть не менее 18 лет");
        }

    }
}
