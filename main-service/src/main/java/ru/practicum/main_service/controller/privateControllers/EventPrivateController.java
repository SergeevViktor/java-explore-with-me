package ru.practicum.main_service.controller.privateControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.event.EventFullDto;
import ru.practicum.main_service.dto.event.NewEventDto;
import ru.practicum.main_service.dto.event.UpdateEventInitiatorRequestDto;
import ru.practicum.main_service.service.eventService.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventPrivateController {

    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<EventFullDto> createEvent(@PathVariable Long userId,
                                                    @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Поступил запрос от пользователя с id {} на создание события {} ", userId, newEventDto);
        return ResponseEntity.created(URI.create("http://localhost:8080/users/{userId}/events"))
                .body(eventService.createEvent(userId, newEventDto));
    }

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<List<EventFullDto>> getAllEventsByInitiator(@PathVariable Long userId,
                                                                      @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                                      @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Поступил запрос на получение всех событий, добавленных пользователем с id={}. " +
                "Параметры: from={}, size={}", userId, from, size);
        return ResponseEntity.ok().body(eventService.getAllEventsByInitiator(userId, from, size));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventByIdByInitiator(@PathVariable Long userId,
                                                                @PathVariable Long eventId) {
        log.info("Поступил запрос от инициатора с id={} на получение события с id={} ", userId, eventId);
        return ResponseEntity.ok().body(eventService.getEventByIdByInitiator(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByInitiator(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @Valid @RequestBody UpdateEventInitiatorRequestDto eventDto) {
        log.info("Поступил запрос на обновление события с id={} от инициатора с id={} на {}",
                eventId, userId, eventDto);
        return ResponseEntity.ok().body(eventService.updateEventByInitiator(userId, eventId, eventDto));
    }

}