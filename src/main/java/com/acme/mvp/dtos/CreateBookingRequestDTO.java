package com.acme.mvp.dtos;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Create Booking Request DTO.
 * @param meetingRoomName
 * @param employeeEmail
 * @param date
 * @param timeFrom
 * @param timeTo
 */
public record CreateBookingRequestDTO(
        String meetingRoomName,
        String employeeEmail,
        LocalDate date, // YYYY-MM-DD format
        LocalTime timeFrom, // Start time
        LocalTime timeTo // End time
) {}

