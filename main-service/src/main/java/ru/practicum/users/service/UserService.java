package ru.practicum.users.service;

import ru.practicum.users.dto.NewUserRequestDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserShortDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(List<Long> ids, Integer from, Integer size);

    UserDto createUser(NewUserRequestDto userRequestDto);

    void deleteUser(Long userId);

    List<UserShortDto> getUserFriends(Long userId);

    List<UserShortDto> removeFriendFromUserFriends(Long userId, Long friendId);
}
