package ru.practicum.main_service.controller.adminControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.compilation.CompilationDto;
import ru.practicum.main_service.dto.compilation.Create;
import ru.practicum.main_service.dto.compilation.NewCompilationDto;
import ru.practicum.main_service.dto.compilation.Update;
import ru.practicum.main_service.service.compilationService.CompilationService;

import java.net.URI;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@Validated(Create.class) @RequestBody NewCompilationDto compilationDto) {
        log.info("Поступил запрос на создание подборки событий {}", compilationDto);
        return ResponseEntity.created(URI.create("http://localhost:8080/admin/compilations"))
                .body(compilationService.createCompilation(compilationDto));
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@PathVariable Long compId,
                                                            @Validated(Update.class) @RequestBody NewCompilationDto compilationDto) {
        log.info("Поступил запрос на обновление подборки событий с id={} на {}", compId, compilationDto);
        return ResponseEntity.ok().body(compilationService.updateCompilation(compId, compilationDto));
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) {
        log.info("Поступил запрос на удаление подборки событий с id={}", compId);
        compilationService.deleteCompilation(compId);
        return ResponseEntity.noContent().build();
    }

}