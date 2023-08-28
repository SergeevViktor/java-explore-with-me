package ru.practicum.main_service.dto.event.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class EventDateValidator implements ConstraintValidator<StartTwoHoursAfterNow, LocalDateTime> {

    @Override
    public void initialize(StartTwoHoursAfterNow constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalDateTime eventDate, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime now = LocalDateTime.now();
        if (eventDate != null) {
            return eventDate.isAfter(now.plusHours(2L));
        } else return true;
    }
}
