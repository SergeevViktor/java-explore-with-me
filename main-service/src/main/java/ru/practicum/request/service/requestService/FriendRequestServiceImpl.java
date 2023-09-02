package ru.practicum.request.service.requestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.exception.ValidationException;
import ru.practicum.request.dto.friendRequestDto.FriendRequestDto;
import ru.practicum.request.dto.friendRequestDto.UpdateFriendRequestDto;
import ru.practicum.request.dto.friendRequestMapper.FriendRequestDtoMapper;
import ru.practicum.request.model.FriendRequest;
import ru.practicum.request.model.RequestStatus;
import ru.practicum.request.repository.FriendRequestRepository;
import ru.practicum.users.model.User;
import ru.practicum.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FriendRequestServiceImpl implements FriendRequestService {
    private final FriendRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final FriendRequestDtoMapper requestDtoMapper = Mappers.getMapper(FriendRequestDtoMapper.class);

    @Transactional
    @Override
    public FriendRequestDto createFriendRequest(Long requesterId, Long friendId) {

        User requester = userRepository.findById(requesterId).orElseThrow(() -> {
            log.warn("Пользователь с id {} не найден", requesterId);
            throw new ObjectNotFoundException(String.format("User with id=%d was not found", requesterId));
        });

        if (requesterId.equals(friendId)) {
            log.warn("Пользователь не может добавить заявку в друзья самому себе");
            throw new ConflictException("The user cannot add a friend request to himself");
        }

        User friend = userRepository.findById(friendId).orElseThrow(() -> {
            log.warn("Пользователь с id {} не найден", friendId);
            throw new ObjectNotFoundException(String.format("User with id=%d was not found", friendId));
        });

        Optional<FriendRequest> requestByFriendOptional = requestRepository.findByRequesterIdAndFriendId(friendId,
                requesterId);
        if (requestByFriendOptional.isPresent()) {
            FriendRequest requestByFriend = requestByFriendOptional.get();
            if (requestByFriend.getStatus().equals(RequestStatus.CANCELED)) {
                requestByFriend.setRequester(requester);
                requestByFriend.setFriend(friend);
                requestByFriend.setStatus(RequestStatus.PENDING);
                requestByFriend.setCreated(LocalDateTime.now());
                return requestDtoMapper.friendRequestToDto(requestByFriend);
            } else {
                log.warn("Обработайте входящую заявку в друзья от пользователя с id {} к пользователю с id {}",
                        friendId, requesterId);
                throw new ValidationException(String.format("Process incoming friend request from user" +
                        " with id %d to user with id %d", friendId, requesterId));
            }
        }

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRequester(requester);
        friendRequest.setFriend(friend);
        friendRequest.setStatus(RequestStatus.PENDING);
        friendRequest.setCreated(LocalDateTime.now());

        FriendRequest newFriendRequest = requestRepository.save(friendRequest);
        log.info("Добавлена заявка в друзья от пользователя с id {} к пользователю с id {}", requesterId, friendId);
        return requestDtoMapper.friendRequestToDto(newFriendRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public List<FriendRequestDto> getAllOutgoingFriendRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException(String.format("User with id=%d was not found", userId));
        }

        List<FriendRequest> requests = requestRepository.findAllByRequesterIdAndStatusNot(userId, RequestStatus.CONFIRMED);
        return requests
                .stream()
                .map(requestDtoMapper::friendRequestToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<FriendRequestDto> getAllIncomingFriendRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException(String.format("User with id=%d was not found", userId));
        }

        List<FriendRequest> requests = requestRepository.findAllByFriendIdAndStatusIn(userId,
                List.of(RequestStatus.PENDING, RequestStatus.REJECTED));
        return requests
                .stream()
                .map(requestDtoMapper::friendRequestToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<FriendRequestDto> updateIncomingFriendRequestsStatus(Long userId, UpdateFriendRequestDto requestDto) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException(String.format("User with id=%d was not found", userId));
        }

        List<FriendRequest> requests = requestRepository.findAllByFriendIdAndIdIn(userId, requestDto.getRequestIds());

        if (requestDto.getStatus().equals(RequestStatus.CONFIRMED)) {
            for (FriendRequest request : requests) {
                if (!(request.getStatus().equals(RequestStatus.PENDING) || request.getStatus().equals(RequestStatus.REJECTED))) {
                    log.warn("Невозможно подтвердить заявку, которая находится в неподходящем статусе: {}", request.getStatus());
                    throw new ConflictException("Request cannot be confirmed because it's not in the right " +
                            "status: " + request.getStatus());
                }
                request.setStatus(RequestStatus.CONFIRMED);
            }
        } else if (requestDto.getStatus().equals(RequestStatus.REJECTED)) {
            for (FriendRequest request : requests) {
                if (!request.getStatus().equals(RequestStatus.PENDING)) {
                    log.warn("Невозможно отклонить заявку, которая находится в неподходящем статусе: {}", request.getStatus());
                    throw new ConflictException("Request cannot be rejected because it's not in the right " +
                            "status: " + request.getStatus());
                }
                request.setStatus(RequestStatus.REJECTED);
            }
        } else {
            log.warn("Новый статус для заявок в друзья должен быть CONFIRMED or REJECTED");
            throw new ConflictException("New status of friend requests must be CONFIRMED or REJECTED");
        }

        log.info("Пользователем с id {} обновлен статус входящих заявок в друзья: {}", userId, requests);
        return requests
                .stream()
                .map(requestDtoMapper::friendRequestToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<FriendRequestDto> updateOutgoingFriendRequestsStatus(Long userId, UpdateFriendRequestDto requestDto) {
        if (!userRepository.existsById(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new ObjectNotFoundException(String.format("User with id=%d was not found", userId));
        }

        List<FriendRequest> requests = requestRepository.findAllByRequesterIdAndIdIn(userId, requestDto.getRequestIds());

        if (requestDto.getStatus().equals(RequestStatus.PENDING)) {
            for (FriendRequest request : requests) {
                if (!request.getStatus().equals(RequestStatus.CANCELED)) {
                    log.warn("Невозможно перевести в рассмотрение заявку, которая находится в неподходящем статусе: {}", request.getStatus());
                    throw new ConflictException("Request cannot be pending because it's not in the right " +
                            "status: " + request.getStatus());
                }
                request.setStatus(RequestStatus.PENDING);
            }
        } else if (requestDto.getStatus().equals(RequestStatus.CANCELED)) {
            for (FriendRequest request : requests) {
                if (!(request.getStatus().equals(RequestStatus.PENDING) || request.getStatus().equals(RequestStatus.REJECTED))) {
                    log.warn("Невозможно отменить заявку, которая находится в неподходящем статусе: {}", request.getStatus());
                    throw new ConflictException("Request cannot be cancelled because it's not in the right " +
                            "status: " + request.getStatus());
                }
                request.setStatus(RequestStatus.CANCELED);
            }
        } else {
            log.warn("Новый статус для заявок в друзья должен быть PENDING or CANCELED");
            throw new ConflictException("New status of friend requests must be CONFIRMED or REJECTED");
        }

        log.info("Пользователем с id {} обновлен статус исходящих заявок в друзья: {}", userId, requests);
        return requests
                .stream()
                .map(requestDtoMapper::friendRequestToDto)
                .collect(Collectors.toList());
    }
}
