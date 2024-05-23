package org.smileksey.calculator.utils.validation;

import org.smileksey.calculator.dto.enums.Gender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

//FIXME
public class GenderSubSetValidator implements ConstraintValidator<GenderSubset, Gender> {

    private Gender[] subset;

    @Override
    public void initialize(GenderSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(Gender value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}
