package ru.practicum.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.event.model.State;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByIdAndAndState(Long eventId, State state);

    List<Event> findByInitiatorId(Long initiatorId, Pageable pageable);

    Optional<Event> findByInitiatorIdAndId(Long initiatorId, Long eventId);

    List<Event> findAllByIdIn(List<Long> ids);

    boolean existsEventsByCategory_Id(Long catId);

    @Query("SELECT DISTINCT pr.event From Request pr " +
            "WHERE pr.status = 'CONFIRMED' " +
            "AND pr.requester.id IN " +
            "(SELECT fr.friend.id FROM FriendRequest fr " +
            "WHERE fr.requester.id = :userId AND fr.status = 'CONFIRMED') " +
            "OR pr.requester.id IN " +
            "(SELECT fr.requester.id FROM FriendRequest fr " +
            "WHERE fr.friend.id = :userId AND fr.status = 'CONFIRMED')")
    List<Event> findAllWithUserFriendsInParticipants(@Param("userId") Long userId,
                                                     Pageable pageable);

}
