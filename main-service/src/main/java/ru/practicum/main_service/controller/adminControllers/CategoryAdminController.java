package ru.practicum.main_service.controller.adminControllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main_service.dto.CategoryDto;
import ru.practicum.main_service.service.categoryService.CategoryService;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
@Validated
@Slf4j
public class CategoryAdminController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Поступил запрос на создание категории {}", categoryDto);
        return ResponseEntity.created(URI.create("http://localhost:8080/admin/categories"))
                .body(categoryService.createCategory(categoryDto));
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long catId,
                                                      @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Поступил запрос на обновление категории с id={} на {}", catId, categoryDto);
        return ResponseEntity.ok().body(categoryService.updateCategory(catId, categoryDto));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        log.info("Поступил запрос на удаление категории с id={}", catId);
        categoryService.deleteCategory(catId);
        return ResponseEntity.noContent().build();
    }

}