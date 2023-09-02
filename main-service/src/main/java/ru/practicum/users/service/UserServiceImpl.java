package ru.practicum.users.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.request.model.FriendRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.FriendRequestRepository;
import ru.practicum.users.dto.NewUserRequestDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.dto.UserMapper;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendRequestRepository requestRepository;

    @Override
    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        List<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(page).getContent();
        } else {
            users = userRepository.findByIdIn(ids, page);
        }
        log.info("Запрос GET на поиск пользователей, с ids: {}", ids);

        return users.stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @Override
    public List<UserShortDto> getUserFriends(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException(String.format("User with id=%d was not found", userId));
        }

        List<User> users = userRepository.findUserFriends(userId);
        return users
                .stream()
                .map(UserMapper::toUserShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto createUser(NewUserRequestDto userRequestDto) {
        if (userRepository.existsUserByName(userRequestDto.getName())) {
            throw new ConflictException("Такой пользователь уже есть");
        }
        User user = UserMapper.toUser(userRequestDto);
        log.info("Запрос POST на сохранение пользователя: {}", user.getName());
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!isUserExists(userId)) {
            throw new ObjectNotFoundException("Пользователя не существует!");
        }
        log.info("Запрос DELETE на удаление пользователя: c id: {}", userId);
        userRepository.deleteById(userId);
    }

    @Override
    public List<UserShortDto> removeFriendFromUserFriends(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException(String.format("User with id=%d was not found", userId));
        });

        User friend = userRepository.findById(friendId).orElseThrow(() -> {
            log.warn("Пользователь с id {} не найден", friendId);
            throw new ObjectNotFoundException(String.format("User with id=%d was not found", friendId));
        });

        Optional<FriendRequest> requestOptional = requestRepository.findConfirmedFriendRequestBetweenUserAndFriend(userId, friendId);
        if (requestOptional.isPresent()) {
            FriendRequest request = requestOptional.get();
            User requester = request.getRequester();
            if (userId.equals(requester.getId())) {
                request.setRequester(friend);
                request.setFriend(user);
            }
            request.setStatus(RequestStatus.REJECTED);
        } else {
            log.warn("Заявки с id {} и {} не являются подтвержденными заявками в друзья", userId, friendId);
            throw new ConflictException(String.format("Users with id=%d and id=%d are not confirmed friend" +
                    " requests", userId, friendId));
        }

        List<User> users = userRepository.findUserFriends(userId);
        return users
                .stream()
                .map(UserMapper::toUserShortDto)
                .collect(Collectors.toList());
    }

    public boolean isUserExists(Long userId) {
        var userOptional = userRepository.findById(userId);
        return !userOptional.isEmpty();
    }

}
