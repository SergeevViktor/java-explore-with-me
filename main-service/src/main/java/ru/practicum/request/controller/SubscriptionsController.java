package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.friendRequestDto.FriendRequestDto;
import ru.practicum.request.dto.friendRequestDto.UpdateFriendRequestDto;
import ru.practicum.request.service.friendRequestService.FriendRequestService;
import ru.practicum.users.dto.UserShortDto;
import ru.practicum.users.service.UserService;

import javax.servlet.http.HttpServletRequest;
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
public class SubscriptionsController {
    private final FriendRequestService friendRequestService;
    private final EventService eventService;
    private final UserService userService;

    @PostMapping("/subscriptions/{userId}/{friendId}")
    public ResponseEntity<FriendRequestDto> createFriendRequest(@PathVariable Long userId,
                                                                @PathVariable Long friendId) {
        URI uri = URI.create("http://localhost:8080/subscriptions/" + userId + "/" + friendId);
        log.info("Поступила заявка от пользователя с id {} на добавление в друзья пользователя с id {} ",
                userId, friendId);
        return ResponseEntity.created(uri).body(friendRequestService.createFriendRequest(userId, friendId));
    }

    @GetMapping("/subscribers/{userId}")
    public ResponseEntity<List<FriendRequestDto>> getAllIncomingFriendRequests(@PathVariable Long userId) {
        log.info("Поступил запрос от пользователя с id {} на получение его входящих заявок в друзья", userId);
        return ResponseEntity.ok().body(friendRequestService.getAllIncomingFriendRequests(userId));
    }

    @GetMapping("/subscriptions/{userId}")
    public ResponseEntity<List<FriendRequestDto>> getAllOutgoingFriendRequests(@PathVariable Long userId) {
        log.info("Поступил запрос от пользователя с id {} на получение его исходящих заявок в друзья", userId);
        return ResponseEntity.ok().body(friendRequestService.getAllOutgoingFriendRequests(userId));
    }

    @GetMapping("/subscriptions/my/{userId}")
    public ResponseEntity<List<UserShortDto>> getUserFriends(@PathVariable Long userId) {
        log.info("Поступил запрос от пользователя с id {} на получение списка его друзей", userId);
        return ResponseEntity.ok().body(userService.getUserFriends(userId));
    }

    @PatchMapping("/subscribers/{userId}")
    public ResponseEntity<List<FriendRequestDto>> updateIncomingFriendRequestsStatus(@PathVariable Long userId,
                                                                     @Valid @RequestBody UpdateFriendRequestDto requestDto) {
        log.info("Поступил запрос на обновление статуса входящих заявок в друзья от пользователя с id={}", userId);
        return ResponseEntity.ok().body(friendRequestService.updateIncomingFriendRequestsStatus(userId, requestDto));
    }

    @PatchMapping("/subscriptions/{userId}")
    public ResponseEntity<List<FriendRequestDto>> updateOutgoingFriendRequestsStatus(@PathVariable Long userId,
                                                                     @Valid @RequestBody UpdateFriendRequestDto requestDto) {
        log.info("Поступил запрос на обновление статуса исходящих заявок в друзья от пользователя с id={}", userId);
        return ResponseEntity.ok().body(friendRequestService.updateOutgoingFriendRequestsStatus(userId, requestDto));
    }

    @DeleteMapping("/subscriptions/{userId}/{friendId}")
    public ResponseEntity<List<UserShortDto>> removeFriendFromUserFriends(@PathVariable Long userId,
                                                          @PathVariable Long friendId) {
        log.info("Поступил запрос на отклонение подтвержденной заявки в друзья между пользователями с id={} и id={}",
                userId, friendId);
        return ResponseEntity.ok().body(userService.removeFriendFromUserFriends(userId, friendId));
    }

    @GetMapping("/subscriptions/events/{userId}")
    public ResponseEntity<List<EventShortDto>> getEventsWithUserFriendsInParticipants(@PathVariable Long userId,
                                                                      HttpServletRequest request,
                                                                      @RequestParam(defaultValue = "0")
                                                                          @PositiveOrZero Integer from,
                                                                      @RequestParam(defaultValue = "10")
                                                                          @Positive Integer size) {
        log.info("Поступил запрос от пользователя с id {} на получение списка актуальных событий, в которых его" +
                " друзья принимают участие. Параметры: from={}, size={}", userId, from, size);
        return ResponseEntity.ok()
                .body(eventService.getEventsWithUserFriendsInParticipants(userId, request, from, size));
    }
}
