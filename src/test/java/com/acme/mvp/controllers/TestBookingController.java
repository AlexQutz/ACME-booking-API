package com.acme.mvp.controllers;

import com.acme.mvp.dtos.CreateBookingRequestDTO;
import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.services.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TestBookingController {

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test for the GET endpoint that retrieves bookings based on room and date.
     * Case: Non-empty list of bookings
     */
    @Test
    void testGetBookings_ReturnsBookings() {
        // Given
        String roomName = "Room A";
        LocalDate date = LocalDate.of(2024, 10, 20);
        List<GetBookingsResponseDTO> bookings = List.of(
                new GetBookingsResponseDTO(1L, "Room A", "employee@example.com", date, LocalTime.of(9, 0), LocalTime.of(10, 0))
        );
        when(bookingService.findBookingsByRoomAndDate(roomName, date)).thenReturn(bookings);

        // When
        ResponseEntity<List<GetBookingsResponseDTO>> response = bookingController.getBookings(roomName, date);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(bookingService, times(1)).findBookingsByRoomAndDate(roomName, date);
    }

    /**
     * Test for the GET endpoint that retrieves bookings based on room and date.
     * Case: No bookings found (empty list)
     */
    @Test
    void testGetBookings_NoContent() {
        // Given
        String roomName = "Room A";
        LocalDate date = LocalDate.of(2024, 10, 20);
        when(bookingService.findBookingsByRoomAndDate(roomName, date)).thenReturn(List.of());

        // When
        ResponseEntity<List<GetBookingsResponseDTO>> response = bookingController.getBookings(roomName, date);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookingService, times(1)).findBookingsByRoomAndDate(roomName, date);
    }

    /**
     * Test for the POST endpoint that creates a new booking.
     */
    @Test
    void testCreateBooking_Success() {
        // Given
        CreateBookingRequestDTO createBookingDTO = new CreateBookingRequestDTO("Room A", "employee@example.com",
                LocalDate.of(2024, 10, 20), LocalTime.of(9, 0), LocalTime.of(10, 0));
        GetBookingsResponseDTO createdBooking = new GetBookingsResponseDTO(1L, "Room A", "employee@example.com",
                LocalDate.of(2024, 10, 20), LocalTime.of(9, 0), LocalTime.of(10, 0));

        when(bookingService.createBooking(any(CreateBookingRequestDTO.class))).thenReturn(createdBooking);

        // When
        ResponseEntity<GetBookingsResponseDTO> response = bookingController.createBooking(createBookingDTO);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdBooking, response.getBody());
        verify(bookingService, times(1)).createBooking(createBookingDTO);
    }

    /**
     * Test for the DELETE endpoint that cancels a booking.
     * Case: Booking successfully canceled.
     */
    @Test
    void testCancelBooking_Success() {
        // Given
        Long bookingId = 1L;
        String successMessage = "Booking was canceled successfully!";
        when(bookingService.cancelBooking(bookingId)).thenReturn(successMessage);

        // When
        ResponseEntity<String> response = bookingController.cancelBooking(bookingId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(successMessage, response.getBody());
        verify(bookingService, times(1)).cancelBooking(bookingId);
    }

    /**
     * Test for IllegalArgumentException using the exception handler.
     */
    @Test
    void testHandleIllegalArgumentException() {
        // Given
        IllegalArgumentException ex = new IllegalArgumentException("Test error message");

        // When
        ResponseEntity<Map<String, String>> response = bookingController.handleIllegalArgumentException(ex);

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("Test error message", response.getBody().get("error"));
    }

    /**
     * Test for generic Exception using the exception handler.
     */
    @Test
    void testHandleGenericException() {
        // Given
        Exception ex = new Exception("Some unexpected error");

        // When
        ResponseEntity<Map<String, String>> response = bookingController.handleGenericException(ex);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().containsKey("error"));
        assertEquals("An unexpected error occurred.", response.getBody().get("error"));
    }
}
