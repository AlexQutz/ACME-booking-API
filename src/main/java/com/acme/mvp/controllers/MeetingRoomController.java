package com.acme.mvp.controllers;

import com.acme.mvp.models.MeetingRoom;
import com.acme.mvp.services.MeetingRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * Room Controller.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/rooms")
public class MeetingRoomController {

    private final MeetingRoomService roomService;

    /**
     * Gets all the rooms.
     * @return list of all the rooms.
     */
    @GetMapping
    public List<MeetingRoom> getAllRooms() {
        return roomService.getAllRooms();
    }
}