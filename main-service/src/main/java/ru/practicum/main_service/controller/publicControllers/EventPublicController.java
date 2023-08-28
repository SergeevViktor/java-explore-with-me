package ru.practicum.main_service.controller.publicControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.controller.enums.EventSort;
import ru.practicum.main_service.dto.event.EventFullDto;
import ru.practicum.main_service.dto.event.EventShortDto;
import ru.practicum.main_service.service.eventService.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
@Validated
@Slf4j
public class EventPublicController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getAllEventsByUser(@RequestParam(required = false) String text,
                                                                  @RequestParam(required = false) List<Long> categories,
                                                                  @RequestParam(required = false) Boolean paid,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                      LocalDateTime rangeStart,
                                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                                      LocalDateTime rangeEnd,
                                                                  @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                                  @RequestParam(required = false) EventSort sort,
                                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                                  @RequestParam(defaultValue = "10") @Positive Integer size,
                                                                  HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        log.info("Поступил публичный запрос на получение всех событий. Параметры: text={}, categories={}, paid={}, " +
                        "rangeStart={}, rangeEnd={}, onlyAvailable={}, sort={}, from={}, size={}", text, categories,
                paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return ResponseEntity.ok().body(eventService.getAllEventsByUser(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                uri, ip));
    }

    @GetMapping("{eventId}")
    public ResponseEntity<EventFullDto> getEventByIdByUser(@PathVariable Long eventId, HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ip = request.getRemoteAddr();
        log.info("Поступил публичный запрос на получение события с id={} ", eventId);
        return ResponseEntity.ok().body(eventService.getEventByIdByUser(eventId, uri, ip));
    }

}