package com.acme.mvp.services;

import com.acme.mvp.models.MeetingRoom;
import com.acme.mvp.repositories.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Meeting room
 */
@Service
@RequiredArgsConstructor
public class MeetingRoomService {

    /**
     * {@link MeetingRoomRepository} initialization.
     */
    private final MeetingRoomRepository roomRepository;

    /**
     * Gets a list structure with all the {@link MeetingRoom} that exist in the database.
     */

    public List<MeetingRoom> getAllRooms() {
        return roomRepository.findAll();
    }
}
