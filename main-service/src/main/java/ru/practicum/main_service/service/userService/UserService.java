package ru.practicum.main_service.service.userService;

import ru.practicum.main_service.dto.user.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);

    void deleteUser(Long id);

    List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size);

}
