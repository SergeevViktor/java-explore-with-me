package ru.practicum.main_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.main_service.dto.LocationDto;
import ru.practicum.main_service.model.Location;

@Mapper
public interface LocationDtoMapper {

    LocationDtoMapper INSTANCE = Mappers.getMapper(LocationDtoMapper.class);

    Location dtoToLocation(LocationDto locationDto);

    LocationDto locationToDto(Location location);
}
