package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.request.model.ParticipationRequestStatus;
import ru.practicum.request.model.Request;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {
    List<Request> findByRequesterId(Long id);

    List<Request> findByEventId(Long eventId);

    Long countAllByEventIdAndStatus(Long eventId, ParticipationRequestStatus state);

    List<Request> findAllByIdIn(List<Long> ids);

    boolean existsRequestByRequester_IdAndEvent_Id(Long requesterId, Long eventId);

}
