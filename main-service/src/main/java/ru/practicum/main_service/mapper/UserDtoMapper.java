package ru.practicum.main_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.main_service.dto.user.UserDto;
import ru.practicum.main_service.model.User;

@Mapper
public interface UserDtoMapper {

    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);
}
