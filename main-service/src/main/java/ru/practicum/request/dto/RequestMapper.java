package ru.practicum.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.event.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.model.Request;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class RequestMapper {
    public static RequestDto toRequestDto(Request request) {
        return RequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .status(request.getStatus())
                .requester(request.getRequester().getId())
                .event(request.getEvent().getId())
                .build();
    }

    public static EventRequestStatusUpdateResult toUpdateResultDto(
            List<Request> confirmedRequests,
            List<Request> rejectedRequests
    ) {
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmedRequests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList()))
                .rejectedRequests(rejectedRequests.stream().map(RequestMapper::toRequestDto).collect(Collectors.toList()))
                .build();
    }
}
