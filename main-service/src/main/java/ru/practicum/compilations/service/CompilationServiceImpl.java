package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.categories.dto.CategoriesMapper;
import ru.practicum.compilations.dto.UpdateCompilationRequest;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.compilations.repository.CompilationRepository;
import ru.practicum.event.dto.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ObjectNotFoundException;
import ru.practicum.users.dto.UserMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationsRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        int offset = from > 0 ? from / size : 0;
        PageRequest page = PageRequest.of(offset, size);
        List<Compilation> compilations;
        //искать только закрепленные/не закрепленные подборки
        //В случае, если по заданным фильтрам не найдено ни одной подборки, возвращает пустой список
        if (pinned == null) {
            compilations = compilationsRepository.findAll(page).getContent();
        } else {
            compilations = compilationsRepository.findAllByPinned(pinned, page);
        }
        if (compilations.isEmpty()) {
            return Collections.emptyList();
        }
        List<CompilationDto> collect = compilations.stream().map(compilation ->
                CompilationMapper.INSTANCE.toCompilationDto(compilation,
                        maptoDto(compilation.getEvents()))).collect(Collectors.toList());
        log.info("Запрос GET на получение подборок событий");

        return collect;
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (newCompilationDto.getEvents() != null) {
            events = eventRepository.findAllById(newCompilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.INSTANCE.toCompilation(newCompilationDto, events);
        Compilation result = compilationsRepository.save(compilation);
        log.info("Запрос POST на добавление новой подборки c id: {}", compilation.getId());

        return CompilationMapper.INSTANCE.toCompilationDto(result, maptoDto(events));
    }

    @Override
    public void deleteCompilation(Long compId) {
        getCompilation(compId);
        log.info("Запрос DELETE по удалению подборки c id: {}", compId);
        compilationsRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest request) {
        //Если поле в запросе не указано (равно null) - значит изменение этих данных не треубется
        Compilation compilation = getCompilation(compId);
        if (request.getEvents() != null) {
            compilation.setEvents(getFromId(request.getEvents()));
        }
        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }
        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        Compilation result = compilationsRepository.save(compilation);

        List<Event> events = new ArrayList<>();
        if (request.getEvents() != null) {
            events = eventRepository.findAllById(request.getEvents());
        }
        log.info("Запрос PATH на изменение подборки событий по id: {}", compId);

        return CompilationMapper.INSTANCE.toCompilationDto(result, maptoDto(events));
    }

    @Override
    public CompilationDto getCompilationsById(Long compId) {
        Compilation compilation = getCompilation(compId);
        log.info("Запрос GET на получение подборки событий по id: {}", compId);
        return CompilationMapper.INSTANCE.toCompilationDto(compilation, maptoDto(compilation.getEvents()));
    }

    private List<Event> getFromId(List<Long> evenIdList) {
        List<Event> events = eventRepository.findAllByIdIn(evenIdList);
        //Если размер списка  из репозитория != evenIdList, то -> не все события с id  были найдены
        if (events.size() != evenIdList.size()) {
            List<Long> list = new ArrayList<>();
            for (Event event : events) {
                Long id = event.getId();
                list.add(id);
            }
            //удаляем из списка evenIdList id событий, которые были в базе данных
            //-> evenIdList останется только id событий, которые не были найдены
            evenIdList.removeAll(list);
        }
        return events;
    }

    private List<EventShortDto> maptoDto(List<Event> events) {
        List<EventShortDto> eventShortDto = events.stream().map(event ->
                EventMapper.toEventShortDto(
                        event,
                        CategoriesMapper.INSTANCE.toCategoryDto(event.getCategory()),
                        UserMapper.INSTANCE.toUserDto(event.getInitiator())
                )).collect(Collectors.toList());
        return eventShortDto;
    }

    private Compilation getCompilation(Long compId) {
        return compilationsRepository.findById(compId)
                .orElseThrow(() -> new ObjectNotFoundException("Такой подборки не существует!"));
    }

}
