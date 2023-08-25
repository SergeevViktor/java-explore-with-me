package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewStats {
    private String app;
    private String uri;
    //hits - Количество просмотров
    private long count;

    public ViewStats(String app, String uri, long hits) {
        this.app = app;
        this.uri = uri;
        this.count = hits;
    }
}
