package com.acme.mvp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * DTO for getting all the bookings.
 * @param id
 * @param meetingRoomName
 * @param employeeEmail
 * @param date
 * @param timeFrom
 * @param timeTo
 */
@JsonIgnoreProperties({"id","meetingRoomName"})
public record GetBookingsResponseDTO(
        Long id,
        String meetingRoomName,
        String employeeEmail,
        LocalDate date,
        LocalTime timeFrom,
        LocalTime timeTo
) {}
