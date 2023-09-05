package ru.practicum.request.dto.friendRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.request.model.RequestStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendRequestDto {

    private Long id;
    private Long requester;
    private Long friend;
    private RequestStatus status;
    private LocalDateTime created;
}
