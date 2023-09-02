package ru.practicum.request.service.friendRequestService;

import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.request.dto.RequestDto;

import java.util.List;

public interface RequestService {
    List<RequestDto> getRequest(@RequestParam Long userId);

    RequestDto createRequest(Long userId, Long eventId);

    RequestDto cancelRequest(Long userId, Long requestId);
}
