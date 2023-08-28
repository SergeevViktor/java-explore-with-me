package ru.practicum.main_service.service.categoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.CategoryDto;
import ru.practicum.main_service.exception.ConditionsNotMetException;
import ru.practicum.main_service.exception.EntityNotFoundException;
import ru.practicum.main_service.mapper.CategoryDtoMapper;
import ru.practicum.main_service.model.Category;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.pagination.CustomPageRequest;
import ru.practicum.main_service.repository.CategoryRepository;
import ru.practicum.main_service.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = CategoryDtoMapper.INSTANCE.dtoToCategory(categoryDto);
        Category newCategory = categoryRepository.save(category);
        log.info("Добавлена категория: {}", newCategory);
        return CategoryDtoMapper.INSTANCE.categoryToDto(newCategory);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Pageable page = CustomPageRequest.of(from, size);
        List<Category> categories = categoryRepository.findAll(page).getContent();
        return categories
                .stream()
                .map(CategoryDtoMapper.INSTANCE::categoryToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("Категория с id {} не найдена", id);
            throw new EntityNotFoundException(String.format("Category with id=%d was not found", id));
        });
        return CategoryDtoMapper.INSTANCE.categoryToDto(category);
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> {
            log.warn("Категория с id {} не найдена", id);
            throw new EntityNotFoundException(String.format("Category with id=%d was not found", id));
        });
        category.setName(categoryDto.getName());
        log.info("Обновлена категория c id {} на {}", id, category);
        return CategoryDtoMapper.INSTANCE.categoryToDto(category);
    }

    @Transactional
    @Override
    public void deleteCategory(Long id) {
        if (!categoryRepository.existsById(id)) {
            log.warn("Категория с id {} не найдена", id);
            throw new EntityNotFoundException(String.format("Category with id=%d was not found", id));
        }

        List<Event> categoryEvents = eventRepository.findAllByCategoryId(id);
        if (!categoryEvents.isEmpty()) {
            log.warn("Существуют события, связанные с категорией");
            throw new ConditionsNotMetException("The category is not empty");
        }

        categoryRepository.deleteById(id);
        log.info("Удалена категория с id {}", id);
    }

}
