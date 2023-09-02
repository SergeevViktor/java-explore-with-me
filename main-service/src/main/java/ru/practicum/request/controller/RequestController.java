package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class RequestController {
    private final RequestService requestsService;

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getRequest(@PathVariable Long userId) {
        return ResponseEntity.ok().body(requestsService.getRequest(userId));
    }

    @PostMapping
    public ResponseEntity<ParticipationRequestDto> createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        URI uri = URI.create("http://localhost:8080/users/" + userId + "/requests");
        return ResponseEntity.created(uri).body(requestsService.createRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return ResponseEntity.ok().body(requestsService.cancelRequest(userId, requestId));
    }

}
