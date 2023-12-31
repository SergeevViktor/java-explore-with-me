package ru.practicum.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.categories.model.Category;

@Repository
public interface CategoriesRepository extends JpaRepository<Category, Long> {

    boolean existsCategoriesByName(String name);

    boolean existsCategoriesByNameAndIdNot(String name, Long id);


}
