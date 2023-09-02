package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.RequestDto;
import ru.practicum.request.service.friendRequestService.RequestService;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class RequestController {
    private final RequestService requestsService;

    @GetMapping
    public ResponseEntity<List<RequestDto>> getRequest(@PathVariable Long userId) {
        return ResponseEntity.ok().body(requestsService.getRequest(userId));
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        URI uri = URI.create("http://localhost:8080/users/" + userId + "/requests");
        return ResponseEntity.created(uri).body(requestsService.createRequest(userId, eventId));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return ResponseEntity.ok().body(requestsService.cancelRequest(userId, requestId));
    }

}
