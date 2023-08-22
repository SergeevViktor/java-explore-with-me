package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.model.StatsModel;
import ru.practicum.model.ViewStats;

@Mapper
public interface StatsMapper {

    StatsMapper INSTANCE = Mappers.getMapper(StatsMapper.class);

    StatsModel toStats(HitRequestDto endpointHitRequestDto);

    @Mapping(target = "hits", source = "viewStats.count")
    StatsResponseDto toStatsResponseDto(ViewStats viewStats);
}
