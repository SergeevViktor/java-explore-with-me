package ru.practicum.request.dto.friendRequestDto;

import lombok.Data;
import ru.practicum.request.model.RequestStatus;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class UpdateFriendRequestDto {

    @NotNull(message = "must not be null")
    private List<Long> requestIds;

    @NotNull(message = "must not be null")
    private RequestStatus status;
}
