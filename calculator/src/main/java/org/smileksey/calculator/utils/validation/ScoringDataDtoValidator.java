package org.smileksey.calculator.utils.validation;

import org.smileksey.calculator.dto.ScoringDataDto;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.time.Period;

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

        //FIXME
//        //Проверяем, что поле gender указано корректно (значение принадлежит соотв. Enum)
//        Gender gender = scoringDataDto.getGender();
//
//        if (!EnumUtils.isValidEnum(Gender.class, gender.name())) {
//            errors.rejectValue("gender", "", "Поле gender имеет некорректное значение");
//        }
//
//        //Проверяем, что поле maritalStatus указано корректно (значение принадлежит соотв. Enum)
//        MaritalStatus maritalStatus = scoringDataDto.getMaritalStatus();
//
//        if (!EnumUtils.isValidEnum(MaritalStatus.class, maritalStatus.name())) {
//            errors.rejectValue("maritalStatus", "", "Поле maritalStatus имеет некорректное значение");
//        }

    }
}