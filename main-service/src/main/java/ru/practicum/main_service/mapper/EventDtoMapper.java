package ru.practicum.main_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.main_service.dto.event.EventFullDto;
import ru.practicum.main_service.dto.event.EventShortDto;
import ru.practicum.main_service.dto.event.NewEventDto;
import ru.practicum.main_service.model.Category;
import ru.practicum.main_service.model.Event;

@Mapper(componentModel = "spring")
public interface EventDtoMapper {

    EventDtoMapper INSTANCE = Mappers.getMapper(EventDtoMapper.class);

    @Mapping(target = "category", ignore = true)
    Event dtoToEvent(NewEventDto newEventDto);

    EventFullDto eventToDto(Event event);

    EventShortDto eventToShortDto(Event event);

    default Long mapCategoryToCategoryId(Category category) {
        return category.getId();
    }

}
