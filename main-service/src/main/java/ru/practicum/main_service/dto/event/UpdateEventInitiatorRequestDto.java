package ru.practicum.main_service.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main_service.dto.LocationDto;
import ru.practicum.main_service.dto.event.enums.InitiatorStateAction;
import ru.practicum.main_service.dto.event.valid.StartTwoHoursAfterNow;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class UpdateEventInitiatorRequestDto {

    @Size(min = 20, max = 2000)
    String annotation;

    Long category;

    @Size(min = 20, max = 7000)
    String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @StartTwoHoursAfterNow
    LocalDateTime eventDate;

    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    InitiatorStateAction stateAction;

    @Size(min = 3, max = 120)
    String title;
}
