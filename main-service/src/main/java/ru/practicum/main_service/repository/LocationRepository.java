package ru.practicum.main_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.main_service.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
