package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.UpdateEventRequestDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.State;
import ru.practicum.event.service.EventService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping()
    public ResponseEntity<List<EventFullDto>> adminGetEvents(@RequestParam(required = false) List<Long> users,
                                                             @RequestParam(required = false) List<State> states,
                                                             @RequestParam(required = false) List<Long> categories,
                                                             @RequestParam(required = false) String rangeStart,
                                                             @RequestParam(required = false) String rangeEnd,
                                                             @RequestParam(defaultValue = "0") Integer from,
                                                             @RequestParam(defaultValue = "10") Integer size) {

        return ResponseEntity.ok().body(
                eventService.adminGetEvents(users, states, categories, rangeStart, rangeEnd, from, size)
        );
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> patchAdminEvent(@PathVariable @Min(1) Long eventId,
                                        @RequestBody @Validated UpdateEventRequestDto requestDto) {
        return ResponseEntity.ok().body(eventService.adminUpdateEvent(eventId, requestDto));
    }
}
