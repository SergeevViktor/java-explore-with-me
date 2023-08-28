package ru.practicum.main_service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.practicum.HitRequestDto;
import ru.practicum.StatsResponseDto;
import ru.practicum.client.StatsClient;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@RequiredArgsConstructor
public class StatisticClient {
    private static final String APP = "main-service";
    private static final int YEARS_OFFSET = 100;
    private final StatsClient statsClient;

    public ResponseEntity<Object> saveHit(String uri, String ip) {
        HitRequestDto hitRequestDto = HitRequestDto.builder()
                .app(APP)
                .uri(uri)
                .ip(ip)
                .timestamp(LocalDateTime.now())
                .build();
        return statsClient.postEndpointHit(hitRequestDto);
    }

    public Map<Long, Long> setViewsNumber(List<Long> eventsId) {
        List<String> uris = new ArrayList<>();
        for (Long eventId : eventsId) {
            uris.add("/events/" + eventId);
        }

        List<StatsResponseDto> hits = statsClient.getStatistic(LocalDateTime.now().minusYears(YEARS_OFFSET),
                LocalDateTime.now(), uris, true);

        return mapHits(hits);
    }

    private Map<Long, Long> mapHits(List<StatsResponseDto> hits) {
        Map<Long, Long> hitMap = new HashMap<>();
        for (var hit : hits) {
            String hitUri = hit.getUri();
            Long id = Long.valueOf(hitUri.substring(8));
            hitMap.put(id, hit.getHits());
        }
        return hitMap;
    }
}
