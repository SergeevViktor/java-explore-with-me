package ru.practicum.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.categories.service.CategoriesService;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.dto.NewCategoryDto;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class AdminCategoriesController {
    private final CategoriesService categoriesService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategories(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return ResponseEntity.created(URI.create("http://localhost:8080/admin/categories"))
                .body(categoriesService.createCategories(newCategoryDto));
    }

    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> deleteCategories(@PathVariable Long catId) {
        categoriesService.deleteCategories(catId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> updateCategories(@PathVariable Long catId,
                                        @RequestBody @Valid CategoryDto categoryDto) {
        categoryDto.setId(catId);
        return ResponseEntity.ok().body(categoriesService.updateCategories(categoryDto));
    }

}
