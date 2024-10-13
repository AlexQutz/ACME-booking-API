package com.acme.mvp.repositories;

import com.acme.mvp.models.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for the {@link MeetingRoom} model.
 */
@Repository
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Long> {

}
