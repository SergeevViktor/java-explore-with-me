package ru.practicum.request.dto.friendRequestMapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.request.dto.friendRequestDto.FriendRequestDto;
import ru.practicum.request.model.FriendRequest;
import ru.practicum.users.model.User;

@Mapper(componentModel = "spring")
public interface FriendRequestDtoMapper {

    FriendRequestDtoMapper INSTANCE = Mappers.getMapper(FriendRequestDtoMapper.class);

    FriendRequestDto friendRequestToDto(FriendRequest request);

    default Long mapUserToLong(User user) {
        return user.getId();
    }
}
