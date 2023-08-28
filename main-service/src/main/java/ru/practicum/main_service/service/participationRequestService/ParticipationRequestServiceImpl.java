package ru.practicum.main_service.service.participationRequestService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.participationRequest.RequestDto;
import ru.practicum.main_service.dto.participationRequest.UpdateRequestEventInitiatorRequestDto;
import ru.practicum.main_service.dto.participationRequest.UpdateRequestResponse;
import ru.practicum.main_service.exception.ConditionsNotMetException;
import ru.practicum.main_service.exception.EntityNotFoundException;
import ru.practicum.main_service.mapper.ParticipationRequestDtoMapper;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.ParticipationRequest;
import ru.practicum.main_service.model.User;
import ru.practicum.main_service.model.enums.EventState;
import ru.practicum.main_service.model.enums.ParticipationRequestStatus;
import ru.practicum.main_service.repository.EventRepository;
import ru.practicum.main_service.repository.ParticipationRequestRepository;
import ru.practicum.main_service.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public RequestDto createRequest(Long userId, Long eventId) {

        ParticipationRequest request = new ParticipationRequest();

        User requester = userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь с id {} не найден", userId);
            throw new EntityNotFoundException(String.format("User with id=%d was not found", userId));
        });
        request.setRequester(requester);

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.warn("Событие с id {} не найдено", eventId);
            throw new EntityNotFoundException(String.format("Event with id=%d was not found", eventId));
        });
        request.setEvent(event);

        if (event.getInitiator().getId().equals(userId)) {
            log.warn("Инициатор события не может добавить запрос на участие в своём событии");
            throw new ConditionsNotMetException("The initiator of the event cannot add a request to participate in " +
                    "his event");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            log.warn("Нельзя участвовать в неопубликованном событии");
            throw new ConditionsNotMetException("It is not possible to participate in an unpublished event");
        }

        Integer confirmedRequests = requestRepository.countAllByEventIdAndStatus(eventId,
                ParticipationRequestStatus.CONFIRMED);
        if (event.getParticipantLimit() <= confirmedRequests && event.getParticipantLimit() != 0) {
            log.warn("У события достигнут лимит запросов на участие.");
            throw new ConditionsNotMetException(String.format("The event has reached participant limit %d",
                    event.getParticipantLimit()));
        }

        if (event.getRequestModeration().equals(Boolean.FALSE) || event.getParticipantLimit() == 0) {
            request.setStatus(ParticipationRequestStatus.CONFIRMED);
        } else {
            request.setStatus(ParticipationRequestStatus.PENDING);
        }

        request.setCreated(LocalDateTime.now());

        ParticipationRequest newRequest = requestRepository.save(request);
        log.info("Добавлен запрос на участие в событии: {}", newRequest);
        return ParticipationRequestDtoMapper.INSTANCE.participationRequestToDto(newRequest);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequestsByRequester(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь с id {} не найден", userId);
            throw new EntityNotFoundException(String.format("User with id=%d was not found", userId));
        });

        List<ParticipationRequest> requests = requestRepository.findAllByRequesterId(userId);
        return requests
                .stream()
                .map(ParticipationRequestDtoMapper.INSTANCE::participationRequestToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequestsByEventInitiator(Long userId, Long eventId) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь с id {} не найден", userId);
            throw new EntityNotFoundException(String.format("User with id=%d was not found", userId));
        });

        eventRepository.findById(eventId).orElseThrow(() -> {
            log.warn("Событие с id {} не найдено", eventId);
            throw new EntityNotFoundException(String.format("Event with id=%d was not found", eventId));
        });

        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId);
        return requests
                .stream()
                .map(ParticipationRequestDtoMapper.INSTANCE::participationRequestToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public RequestDto updateRequestStatusByRequester(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findAllByIdAndRequesterId(requestId, userId).orElseThrow(() -> {
            log.warn("Запрос на участие в событии с id {} не найден у пользователя с id {}", requestId, userId);
            throw new EntityNotFoundException(String.format("Request with id=%d was not found", requestId));
        });

        request.setStatus(ParticipationRequestStatus.CANCELED);
        log.info("Пользователем обновлен статус заявки на участие в событии c id {} на {}", requestId,
                ParticipationRequestStatus.CANCELED);
        return ParticipationRequestDtoMapper.INSTANCE.participationRequestToDto(request);
    }

    @Transactional
    @Override
    public UpdateRequestResponse updateRequestStatusByEventInitiator(Long userId,
                                                                     Long eventId,
                                                                     UpdateRequestEventInitiatorRequestDto requestDto) {
        userRepository.findById(userId).orElseThrow(() -> {
            log.warn("Пользователь с id {} не найден", userId);
            throw new EntityNotFoundException(String.format("User with id=%d was not found", userId));
        });

        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            log.warn("Событие с id {} не найдено", eventId);
            throw new EntityNotFoundException(String.format("Event with id=%d was not found", eventId));
        });

        Integer confirmedRequests = requestRepository.countAllByEventIdAndStatus(eventId,
                ParticipationRequestStatus.CONFIRMED);
        int limitForConfirmation = event.getParticipantLimit() - confirmedRequests;
        if (requestDto.getStatus().equals(ParticipationRequestStatus.CONFIRMED) && limitForConfirmation <= 0) {
            log.warn("У события достигнут лимит запросов на участие.");
            throw new ConditionsNotMetException(String.format("The event has reached participant limit %d",
                    event.getParticipantLimit()));
        }

        List<ParticipationRequest> requests = requestRepository.findAllByEventIdAndEventInitiatorIdAndIdIn(eventId,
                userId, requestDto.getRequestIds());

        if (requestDto.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
            for (ParticipationRequest request : requests) {
                if (!request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                    log.warn("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
                    throw new ConditionsNotMetException("Request cannot be confirmed because it's not in the right " +
                            "status: " + request.getStatus());
                }
                if (limitForConfirmation > 0) {
                    request.setStatus(ParticipationRequestStatus.CONFIRMED);
                    limitForConfirmation--;
                } else {
                    request.setStatus(ParticipationRequestStatus.REJECTED);
                }
            }
        } else if (requestDto.getStatus().equals(ParticipationRequestStatus.REJECTED)) {
            for (ParticipationRequest request : requests) {
                if (!request.getStatus().equals(ParticipationRequestStatus.PENDING)) {
                    log.warn("Статус можно изменить только у заявок, находящихся в состоянии ожидания");
                    throw new ConditionsNotMetException("Request cannot be confirmed because it's not in the right " +
                            "status: " + request.getStatus());
                }
                request.setStatus(ParticipationRequestStatus.REJECTED);
            }
        } else {
            log.warn("Новый статус для заявок на участие в событии текущего пользователя должен быть CONFIRMED" +
                    " or REJECTED");
            throw new ConditionsNotMetException("New status of participation requests must be CONFIRMED or REJECTED");
        }

        log.info("Инициатором с id {} обновлен статус заявок на участие в событии c id {} на {}", userId, eventId,
                requests);
        List<RequestDto> requestsDto = requests
                .stream()
                .map(ParticipationRequestDtoMapper.INSTANCE::participationRequestToDto)
                .collect(Collectors.toList());
        List<RequestDto> confirmedRequestsDto = new ArrayList<>();
        List<RequestDto> rejectedRequestsDto = new ArrayList<>();
        for (RequestDto dto : requestsDto) {
            if (dto.getStatus().equals(ParticipationRequestStatus.CONFIRMED)) {
                confirmedRequestsDto.add(dto);
            } else {
                rejectedRequestsDto.add(dto);
            }
        }
        UpdateRequestResponse response = new UpdateRequestResponse();
        response.setConfirmedRequests(confirmedRequestsDto);
        response.setRejectedRequests(rejectedRequestsDto);
        return response;
    }

}