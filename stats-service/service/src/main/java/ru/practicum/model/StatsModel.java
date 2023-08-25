package ru.practicum.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "stats")
public class StatsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    //Идентификатор сервиса для которого записывается информация
    @Column
    private String app;
    //URI для которого был осуществлен запрос
    @Column
    private String uri;
    //IP-адрес пользователя, осуществившего запрос
    @Column
    private String ip;
    //Дата и время, когда был совершен запрос к эндпоинту
    @Column
    private LocalDateTime timestamp;
}
