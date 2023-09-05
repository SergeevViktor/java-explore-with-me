package ru.practicum.request.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.users.model.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "friend_requests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    @NotNull
    private User requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id")
    @NotNull
    private User friend;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NotNull
    private RequestStatus status;

    @Column(name = "created")
    @NotNull
    private LocalDateTime created;
}
