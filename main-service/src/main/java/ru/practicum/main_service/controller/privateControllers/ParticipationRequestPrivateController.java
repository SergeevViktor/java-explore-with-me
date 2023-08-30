package ru.practicum.main_service.controller.privateControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.participationRequest.RequestDto;
import ru.practicum.main_service.dto.participationRequest.UpdateRequestEventInitiatorRequestDto;
import ru.practicum.main_service.dto.participationRequest.UpdateRequestResponse;
import ru.practicum.main_service.service.participationRequestService.ParticipationRequestService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class ParticipationRequestPrivateController {

    private final ParticipationRequestService requestService;

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<RequestDto> createRequest(@PathVariable Long userId,
                                                    @RequestParam Long eventId) {
        log.info("Поступил запрос от пользователя с id {} на создание запроса на участие в событии с id {} ",
                userId, eventId);
        URI uri = URI.create("http://localhost:8080/users/" + userId);
        return ResponseEntity.created(uri).body(requestService.createRequest(userId, eventId));
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getAllRequestsByEventInitiator(@PathVariable Long userId,
                                                                           @PathVariable Long eventId) {
        log.info("Поступил запрос от инициатора с id {} на получение всех запросов на участие в его событии с id {}",
                userId, eventId);
        return ResponseEntity.ok().body(requestService.getAllRequestsByEventInitiator(userId, eventId));
    }

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<List<RequestDto>> getAllRequestsByRequester(@PathVariable Long userId) {
        log.info("Поступил запрос от пользователя с id {} на получение всех его запросов на участие в событиях",
                userId);
        return ResponseEntity.ok().body(requestService.getAllRequestsByRequester(userId));
    }

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<RequestDto> updateRequestStatusByRequester(@PathVariable Long userId,
                                                                     @PathVariable Long requestId) {
        log.info("Поступил запрос на обновление статуса запроса на участие в событии с id={} от пользователя с id={}",
                requestId, userId);
        return ResponseEntity.ok().body(requestService.updateRequestStatusByRequester(userId, requestId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<UpdateRequestResponse> updateRequestsStatusByEventInitiator(@PathVariable Long userId,
                                                                                      @PathVariable Long eventId,
                                                                                      @Valid @RequestBody
                                                                                          UpdateRequestEventInitiatorRequestDto requestDto) {
        log.info("Поступил запрос на обновление статуса запросов на участие в событии с id={} от инициатора данного " +
                "события с id={}", eventId, userId);
        return ResponseEntity.ok().body(requestService.updateRequestStatusByEventInitiator(userId, eventId, requestDto));
    }

}
