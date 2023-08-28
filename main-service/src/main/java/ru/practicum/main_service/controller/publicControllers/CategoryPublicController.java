package ru.practicum.main_service.controller.publicControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.CategoryDto;
import ru.practicum.main_service.service.categoryService.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.info("Поступил публичный запрос на получение всех категорий. Параметры: from={}, size={}", from, size);
        return ResponseEntity.ok().body(categoryService.getAllCategories(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long catId) {
        log.info("Поступил публичный запрос на получение категории с id={}", catId);
        return ResponseEntity.ok().body(categoryService.getCategoryById(catId));
    }

}