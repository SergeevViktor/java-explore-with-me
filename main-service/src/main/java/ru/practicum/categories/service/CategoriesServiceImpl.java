package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.categories.dto.CategoriesMapper;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.repository.CategoriesRepository;
import ru.practicum.categories.dto.NewCategoryDto;
import ru.practicum.categories.model.Category;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ObjectNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoriesServiceImpl implements CategoriesService {
    private final CategoriesRepository categoriesRepository;
    private final EventRepository eventRepository;


    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        List<Category> categoryList = categoriesRepository.findAll(page).getContent();
        log.info("Запрос GET на получение списка категорий");
        return categoryList.stream().map(CategoriesMapper::toCategoryDto).collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoriesId(Long catId) {
        Category category = getCategoriesIfExist(catId);
        return CategoriesMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto createCategories(NewCategoryDto newCategoryDto) {
        if (categoriesRepository.existsCategoriesByName(newCategoryDto.getName())) {
            throw new ConflictException("Такая категория уже есть");
        }
        Category category = categoriesRepository.save(CategoriesMapper.toCategory(newCategoryDto));
        log.info("Запрос POST на сохранение категории: {}", newCategoryDto.getName());
        return CategoriesMapper.toCategoryDto(category);
    }

    @Override
    public void deleteCategories(Long catId) {


        var category = categoriesRepository.findById(catId);

        if (category == null) {
            throw new ObjectNotFoundException("Не найдена выбранная категория");
        }

        if (eventRepository.existsEventsByCategory_Id(catId)) {
            throw new ConflictException("Такой пользователь уже есть");
        }
        categoriesRepository.deleteById(catId);
        log.info("Запрос DELETE на удаление категории: c id: {}", catId);
    }

    @Override
    public CategoryDto updateCategories(CategoryDto categoryDto) {


        Category category = getCategoriesIfExist(categoryDto.getId());

        if (categoriesRepository.existsCategoriesByNameAndIdNot(categoryDto.getName(), categoryDto.getId())) {
            throw new ConflictException("Такая категория уже есть");
        }

        category.setName(categoryDto.getName());
        log.info("Запрос PATH на изменение категории: c id: {}", categoryDto.getId());
        return CategoriesMapper.toCategoryDto(categoriesRepository.save(category));
    }

    private Category getCategoriesIfExist(Long catId) {
        return categoriesRepository.findById(catId).orElseThrow(
                () -> new ObjectNotFoundException("Не найдена выбранная категория"));
    }

}
