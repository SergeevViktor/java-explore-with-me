package ru.practicum.categories.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.model.Category;

@UtilityClass
public class CategoriesMapper {
    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }

}
