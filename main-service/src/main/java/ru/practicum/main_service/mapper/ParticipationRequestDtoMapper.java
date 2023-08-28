package ru.practicum.main_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.main_service.dto.participationRequest.RequestDto;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.ParticipationRequest;
import ru.practicum.main_service.model.User;

@Mapper(componentModel = "spring")
public interface ParticipationRequestDtoMapper {

    ParticipationRequestDtoMapper INSTANCE = Mappers.getMapper(ParticipationRequestDtoMapper.class);

    RequestDto participationRequestToDto(ParticipationRequest request);

    default Long mapEventToLong(Event event) {
        return event.getId();
    }

    default Long mapUserToLong(User user) {
        return user.getId();
    }

}
