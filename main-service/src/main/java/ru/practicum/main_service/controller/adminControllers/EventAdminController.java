package ru.practicum.main_service.controller.adminControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.event.EventFullDto;
import ru.practicum.main_service.dto.event.UpdateEventAdminRequestDto;
import ru.practicum.main_service.model.enums.EventState;
import ru.practicum.main_service.service.eventService.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventAdminController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAllEventsByAdmin(@RequestParam(required = false) List<Long> users,
                                                  @RequestParam(required = false) List<EventState> states,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                      LocalDateTime rangeStart,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                      LocalDateTime rangeEnd,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Поступил запрос от администратора на получение всех событий. Параметры: users={}, states={}, " +
                        "categories={}, rangeStart={}, rangeEnd={}, from={}, size={}", users, states, categories,
                rangeStart, rangeEnd, from, size);
        return ResponseEntity.ok().body(
                eventService.getAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(@PathVariable Long eventId,
                                                           @Valid @RequestBody UpdateEventAdminRequestDto eventDto) {
        log.info("Поступил запрос от администратора на обновление события с id={} на {}", eventId, eventDto);
        return ResponseEntity.ok().body(eventService.updateEventByAdmin(eventId, eventDto));
    }

}