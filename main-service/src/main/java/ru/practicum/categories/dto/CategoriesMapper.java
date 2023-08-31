package ru.practicum.categories.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.categories.model.Categories;

@UtilityClass
public class CategoriesMapper {
    public static CategoryDto toCategoryDto(Categories categories) {
        return CategoryDto.builder()
                .id(categories.getId())
                .name(categories.getName())
                .build();
    }

    public static Categories toCategories(NewCategoryDto newCategoryDto) {
        return Categories.builder()
                .name(newCategoryDto.getName())
                .build();
    }

}
