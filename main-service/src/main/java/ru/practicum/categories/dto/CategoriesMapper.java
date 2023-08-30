package ru.practicum.categories.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.categories.model.Categories;

@Mapper
public interface CategoriesMapper {

    CategoriesMapper INSTANCE = Mappers.getMapper(CategoriesMapper.class);

    CategoryDto toCategoryDto(Categories categories);

    Categories toCategories(NewCategoryDto newCategoryDto);

}
