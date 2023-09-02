package ru.practicum.request.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.FriendRequest;
import ru.practicum.request.model.RequestStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {

    Optional<FriendRequest> findByRequesterIdAndFriendId(Long requesterId, Long friendId);

    List<FriendRequest> findAllByRequesterIdAndStatusNot(Long userId, RequestStatus status);

    List<FriendRequest> findAllByFriendIdAndStatusIn(Long userId, List<RequestStatus> status);

    List<FriendRequest> findAllByFriendIdAndIdIn(Long userId, List<Long> requestsId);

    List<FriendRequest> findAllByRequesterIdAndIdIn(Long userId, List<Long> requestsId);

    @Query("SELECT fr FROM FriendRequest fr " +
            "WHERE (fr.requester.id = :userId AND fr.friend.id = :friendId AND fr.status = 'CONFIRMED') OR " +
            "(fr.friend.id = :userId AND fr.requester.id = :friendId AND fr.status = 'CONFIRMED') ")
    Optional<FriendRequest> findConfirmedFriendRequestBetweenUserAndFriend(@Param("userId") Long userId,
                                                                           @Param("friendId") Long friendId);

}