package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAllEventsByUserId(@PathVariable Long userId,
                                                   @RequestParam(required = false, defaultValue = "0")
                                                   @PositiveOrZero Integer from,
                                                   @RequestParam(required = false, defaultValue = "10")
                                                   @PositiveOrZero Integer size) {

        return ResponseEntity.ok().body(eventService.getAllEventsByUserId(userId, from, size));
    }

    @PostMapping
    public ResponseEntity<EventFullDto> createEvents(@PathVariable Long userId,
                                                     @RequestBody @Valid NewEventDto newEventDto) {
        URI uri = URI.create("http://localhost:8080/users/" + userId + "/events");
        return ResponseEntity.created(uri).body(eventService.createEvents(userId, newEventDto));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventsByUserId(@PathVariable Long userId,
                                          @PathVariable Long eventId) {
        return ResponseEntity.ok().body(eventService.getEventsByUserId(userId, eventId));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventsByUser(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody @Valid UpdateEventRequestDto requestDto) {
        return ResponseEntity.ok().body(eventService.updateEventsByUser(userId, eventId, requestDto));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestUserEvents(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return ResponseEntity.ok().body(eventService.getRequestUserEvents(userId, eventId));
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateStatusRequestByUserIdForEvents(@PathVariable Long userId,
                                                                               @PathVariable Long eventId,
                                                                               @RequestBody @Valid EventRequestStatusUpdateRequest requestDto) {
        return ResponseEntity.ok().body(
                eventService.updateStatusRequestByUserIdForEvents(userId, eventId, requestDto)
        );
    }
}
