package ru.practicum.main_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.main_service.dto.compilation.CompilationDto;
import ru.practicum.main_service.dto.compilation.NewCompilationDto;
import ru.practicum.main_service.model.Compilation;

@Mapper
public interface CompilationDtoMapper {

    CompilationDtoMapper INSTANCE = Mappers.getMapper(CompilationDtoMapper.class);

    @Mapping(target = "events", ignore = true)
    Compilation dtoToCompilation(NewCompilationDto newCompilationDto);

    CompilationDto compilationToDto(Compilation compilation);

}
