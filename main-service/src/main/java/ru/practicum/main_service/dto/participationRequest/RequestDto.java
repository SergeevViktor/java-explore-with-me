package ru.practicum.main_service.dto.participationRequest;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.practicum.main_service.model.enums.ParticipationRequestStatus;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class RequestDto {

    LocalDateTime created;
    Long event;
    Long id;
    Long requester;
    ParticipationRequestStatus status;

}
