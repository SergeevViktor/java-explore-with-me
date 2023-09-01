package ru.practicum.users.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.users.dto.NewUserRequestDto;
import ru.practicum.users.dto.UserDto;
import ru.practicum.users.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class AdminUserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(required = false) List<Long> ids,
                                  @RequestParam(required = false, defaultValue = "0")
                                  @PositiveOrZero Integer from,
                                  @RequestParam(required = false, defaultValue = "10")
                                  @PositiveOrZero Integer size) {
        return ResponseEntity.ok().body(userService.getUsers(ids, from, size));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid NewUserRequestDto userRequestDto) {
        URI uri = URI.create("http://localhost:8080/admin/users");
        return ResponseEntity.created(uri).body(userService.createUser(userRequestDto));
    }

    @DeleteMapping
    @RequestMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

}
