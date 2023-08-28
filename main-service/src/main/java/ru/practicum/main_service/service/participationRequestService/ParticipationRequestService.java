package ru.practicum.main_service.service.participationRequestService;

import ru.practicum.main_service.dto.participationRequest.RequestDto;
import ru.practicum.main_service.dto.participationRequest.UpdateRequestEventInitiatorRequestDto;
import ru.practicum.main_service.dto.participationRequest.UpdateRequestResponse;

import java.util.List;

public interface ParticipationRequestService {

    RequestDto createRequest(Long userId, Long eventId);

    List<RequestDto> getAllRequestsByRequester(Long userId);

    RequestDto updateRequestStatusByRequester(Long userId, Long requestId);

    List<RequestDto> getAllRequestsByEventInitiator(Long userId, Long eventId);

    UpdateRequestResponse updateRequestStatusByEventInitiator(Long userId,
                                                              Long eventId,
                                                              UpdateRequestEventInitiatorRequestDto requestDto);

}
