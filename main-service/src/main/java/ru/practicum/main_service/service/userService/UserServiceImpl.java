package ru.practicum.main_service.service.userService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.user.UserDto;
import ru.practicum.main_service.exception.EntityNotFoundException;
import ru.practicum.main_service.mapper.UserDtoMapper;
import ru.practicum.main_service.model.User;
import ru.practicum.main_service.pagination.CustomPageRequest;
import ru.practicum.main_service.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserDtoMapper.INSTANCE.dtoToUser(userDto);
        User newUser = userRepository.save(user);
        log.info("Добавлен пользователь: {}", newUser);
        return UserDtoMapper.INSTANCE.userToDto(newUser);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllUsers(List<Long> ids, Integer from, Integer size) {
        Pageable page = CustomPageRequest.of(from, size);
        List<User> users;
        if (ids != null) {
            users = userRepository.findAllByIdIn(ids);
        } else {
            users = userRepository.findAll(page).getContent();
        }
        return users
                .stream()
                .map(UserDtoMapper.INSTANCE::userToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            log.warn("Пользователь с id {} не найден", id);
            throw new EntityNotFoundException(String.format("User with id=%d was not found", id));
        }
        userRepository.deleteById(id);
        log.info("Удален пользователь с id {}", id);
    }

}