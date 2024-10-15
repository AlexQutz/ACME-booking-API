package com.acme.mvp.controllers;

import com.acme.mvp.models.Booking;
import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.services.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Contains all available endpoints for the {@link Booking} model.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/bookings")
public class BookingController {

    /**
     * Initialize {@link BookingService}
     */
    private final BookingService bookingService;


    /**
     * Gets all the bookings of a meeting room on a specific date
     * @param roomName
     * @param date
     * @return list of bookings.
     */
    @GetMapping
    public ResponseEntity<List<GetBookingsResponseDTO>> getBookings(
            @RequestParam String roomName,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<GetBookingsResponseDTO> bookings = bookingService.findBookingsByRoomAndDate(roomName, date);

        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(bookings);
    }
}
