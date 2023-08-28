package ru.practicum.main_service.service.compilationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_service.dto.compilation.CompilationDto;
import ru.practicum.main_service.dto.compilation.NewCompilationDto;
import ru.practicum.main_service.dto.event.EventShortDto;
import ru.practicum.main_service.exception.EntityNotFoundException;
import ru.practicum.main_service.mapper.CompilationDtoMapper;
import ru.practicum.main_service.model.Compilation;
import ru.practicum.main_service.model.Event;
import ru.practicum.main_service.model.enums.ParticipationRequestStatus;
import ru.practicum.main_service.pagination.CustomPageRequest;
import ru.practicum.main_service.repository.CompilationRepository;
import ru.practicum.main_service.repository.EventRepository;
import ru.practicum.main_service.repository.ParticipationRequestRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        Compilation compilation = CompilationDtoMapper.INSTANCE.dtoToCompilation(compilationDto);

        Set<Long> eventsId = compilationDto.getEvents();
        if (eventsId != null) {
            Set<Event> events = eventRepository.findAllByIdIn(eventsId);
            compilation.setEvents(events);
        }

        Compilation newCompilation = compilationRepository.save(compilation);
        log.info("Добавлена подборка событий: {}", newCompilation);
        CompilationDto dto = CompilationDtoMapper.INSTANCE.compilationToDto(newCompilation);
        List<EventShortDto> compilationEvents = dto.getEvents();
        for (EventShortDto event : compilationEvents) {
            Integer confirmedRequests = requestRepository.countAllByEventIdAndStatus(event.getId(),
                    ParticipationRequestStatus.CONFIRMED);
            event.setConfirmedRequests(confirmedRequests);
        }
        return dto;
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable page = CustomPageRequest.of(from, size);

        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, page);
        } else {
            compilations = compilationRepository.findAll(page).getContent();
        }
        List<CompilationDto> compilationsDto = compilations
                .stream()
                .map(CompilationDtoMapper.INSTANCE::compilationToDto)
                .collect(Collectors.toList());
        for (CompilationDto dto : compilationsDto) {
            List<EventShortDto> compilationEvents = dto.getEvents();
            if (compilationEvents != null) {
                for (EventShortDto event : compilationEvents) {
                    Integer confirmedRequests = requestRepository.countAllByEventIdAndStatus(event.getId(),
                            ParticipationRequestStatus.CONFIRMED);
                    event.setConfirmedRequests(confirmedRequests);
                }
            }
        }
        return compilationsDto;
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationById(Long id) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> {
            log.warn("Подборка событий с id {} не найдена", id);
            throw new EntityNotFoundException(String.format("Compilation with id=%d was not found", id));
        });
        CompilationDto dto = CompilationDtoMapper.INSTANCE.compilationToDto(compilation);
        List<EventShortDto> compilationEvents = dto.getEvents();
        if (compilationEvents != null) {
            for (EventShortDto event : compilationEvents) {
                Integer confirmedRequests = requestRepository.countAllByEventIdAndStatus(event.getId(),
                        ParticipationRequestStatus.CONFIRMED);
                event.setConfirmedRequests(confirmedRequests);
            }
        }
        return dto;
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long id, NewCompilationDto compilationDto) {
        Compilation compilation = compilationRepository.findById(id).orElseThrow(() -> {
            log.warn("Подборка событий с id {} не найдена", id);
            throw new EntityNotFoundException(String.format("Compilation with id=%d was not found", id));
        });

        if (compilationDto.getTitle() != null && !compilationDto.getTitle().isBlank()) {
            compilation.setTitle(compilationDto.getTitle());
        }
        if (compilationDto.getPinned() != null) {
            compilation.setPinned(compilationDto.getPinned());
        }
        if (compilationDto.getEvents() != null) {
            Set<Long> eventsId = compilationDto.getEvents();
            Set<Event> events = eventRepository.findAllByIdIn(eventsId);
            compilation.setEvents(events);
        }

        log.info("Обновлена подборка событий с id {} на {}", id, compilation);
        CompilationDto dto = CompilationDtoMapper.INSTANCE.compilationToDto(compilation);
        List<EventShortDto> compilationEvents = dto.getEvents();
        if (compilationEvents != null) {
            for (EventShortDto event : compilationEvents) {
                Integer confirmedRequests = requestRepository.countAllByEventIdAndStatus(event.getId(),
                        ParticipationRequestStatus.CONFIRMED);
                event.setConfirmedRequests(confirmedRequests);
            }
        }
        return dto;
    }

    @Transactional
    @Override
    public void deleteCompilation(Long id) {
        if (!compilationRepository.existsById(id)) {
            log.warn("Подборка событий с id {} не найдена", id);
            throw new EntityNotFoundException(String.format("Compilation with id=%d was not found", id));
        }

        compilationRepository.deleteById(id);
        log.info("Удалена подборка событий с id {}", id);
    }

}