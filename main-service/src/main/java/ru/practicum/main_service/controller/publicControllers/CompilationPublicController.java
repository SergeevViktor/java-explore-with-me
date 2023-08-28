package ru.practicum.main_service.controller.publicControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.compilation.CompilationDto;
import ru.practicum.main_service.service.compilationService.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationPublicController {

    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                                   @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                                   @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Поступил публичный запрос на получение всех подборок событий. Параметры: pinned={}, from={}, size={}",
                pinned, from, size);
        return ResponseEntity.ok().body(compilationService.getAllCompilations(pinned, from, size));
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable Long compId) {
        log.info("Поступил публичный запрос на получение подборки событий с id={}", compId);
        return ResponseEntity.ok().body(compilationService.getCompilationById(compId));
    }

}