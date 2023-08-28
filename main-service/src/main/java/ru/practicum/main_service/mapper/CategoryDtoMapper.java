package ru.practicum.main_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.main_service.dto.CategoryDto;
import ru.practicum.main_service.model.Category;

@Mapper
public interface CategoryDtoMapper {

    CategoryDtoMapper INSTANCE = Mappers.getMapper(CategoryDtoMapper.class);

    Category dtoToCategory(CategoryDto categoryDto);

    CategoryDto categoryToDto(Category category);
}
