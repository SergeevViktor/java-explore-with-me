package ru.practicum.main_service.controller.adminControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.user.UserDto;
import ru.practicum.main_service.service.userService.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        log.info("Поступил запрос на создание пользователя {}", userDto);
        return ResponseEntity.created(URI.create("http://localhost:8080/admin/users")).body(userService.createUser(userDto));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers(@RequestParam(required = false) List<Long> ids,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Поступил запрос на получение всех пользователей. Параметры: ids={}, from={}, size={}",
                ids, from, size);
        return ResponseEntity.ok().body(userService.getAllUsers(ids, from, size));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        log.info("Поступил запрос на удаление пользователя с id={}", userId);
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}