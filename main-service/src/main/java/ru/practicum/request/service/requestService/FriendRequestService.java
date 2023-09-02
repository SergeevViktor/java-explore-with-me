package ru.practicum.request.service.requestService;

import ru.practicum.request.dto.friendRequestDto.FriendRequestDto;
import ru.practicum.request.dto.friendRequestDto.UpdateFriendRequestDto;

import java.util.List;

public interface FriendRequestService {

    FriendRequestDto createFriendRequest(Long requesterId, Long friendId);

    List<FriendRequestDto> getAllOutgoingFriendRequests(Long userId);

    List<FriendRequestDto> getAllIncomingFriendRequests(Long userId);

    List<FriendRequestDto> updateIncomingFriendRequestsStatus(Long userId, UpdateFriendRequestDto requestDto);

    List<FriendRequestDto> updateOutgoingFriendRequestsStatus(Long userId, UpdateFriendRequestDto requestDto);
}
