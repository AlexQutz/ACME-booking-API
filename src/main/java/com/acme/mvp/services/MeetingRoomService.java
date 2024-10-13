package com.acme.mvp.services;

import com.acme.mvp.models.MeetingRoom;
import com.acme.mvp.repositories.MeetingRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MeetingRoomService {

    private final MeetingRoomRepository roomRepository;

    // Get all rooms
    public List<MeetingRoom> getAllRooms() {
        return roomRepository.findAll();
    }
}
