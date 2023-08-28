package ru.practicum.main_service.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import ru.practicum.main_service.model.enums.ParticipationRequestStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
@Entity
@Table(name = "participation_requests")
public class ParticipationRequest {

    @Column(name = "created")
    @NotNull
    LocalDateTime created;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    @NotNull
    Event event;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    @NotNull
    User requester;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    @NotNull
    ParticipationRequestStatus status;

}
