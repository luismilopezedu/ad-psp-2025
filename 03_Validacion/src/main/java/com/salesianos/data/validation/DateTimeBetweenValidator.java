package com.salesianos.data.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class DateTimeBetweenValidator
    implements ConstraintValidator<DateTimeBetween, LocalDateTime> {


    private String strMinDate, strMaxDate;

    @Override
    public void initialize(DateTimeBetween constraintAnnotation) {
        strMinDate = constraintAnnotation.min();
        strMaxDate = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext constraintValidatorContext) {
        // Transformar los strings a LocalDateTime
        LocalDateTime minDate = LocalDateTime.parse(strMinDate);
        LocalDateTime maxDate = LocalDateTime.parse(strMaxDate);

        return value != null && value.isAfter(minDate) && value.isBefore(maxDate);
    }
}
