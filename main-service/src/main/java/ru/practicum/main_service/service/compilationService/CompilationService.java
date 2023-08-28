package ru.practicum.main_service.service.compilationService;

import ru.practicum.main_service.dto.compilation.CompilationDto;
import ru.practicum.main_service.dto.compilation.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    CompilationDto createCompilation(NewCompilationDto compilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, NewCompilationDto compilationDto);

    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);

}
