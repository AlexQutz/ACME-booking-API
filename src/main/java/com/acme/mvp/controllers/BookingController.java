package com.acme.mvp.controllers;

import com.acme.mvp.dtos.CreateBookingRequestDTO;
import com.acme.mvp.models.Booking;
import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.services.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Operation(summary = "Get bookings for a specific room and date",
            description = "Retrieve all bookings for a specific room and date")
    @ApiResponse(responseCode = "200", description = "List of bookings")
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

    /**
     * Creates a {@link Booking} or throws an exception if something goes wrong.
     * @param bookingCreateDTO
     * @return
     */
    @Operation(summary = "Create a new booking",
            description = "Book a meeting room with a specific time slot")
    @ApiResponse(responseCode = "201", description = "Booking created successfully")
    @PostMapping
    public ResponseEntity<GetBookingsResponseDTO> createBooking(@RequestBody CreateBookingRequestDTO bookingCreateDTO) {
        GetBookingsResponseDTO createdBooking = bookingService.createBooking(bookingCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBooking);
    }


    /**
     * Endpoint that deletes the {@link Booking} that has the id provided.
     * @param id
     * @return
     */
    @Operation(summary = "Cancel a booking",
            description = "Cancel a specific booking by its ID")
    @ApiResponse(responseCode = "200", description = "Booking canceled successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        String response = bookingService.cancelBooking(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Exception handler that throws the messages from the {@link BookingService} as a json message.
     * @param ex
     * @return
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Exception handler for an other exception other than the ones specified.
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
