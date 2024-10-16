package com.acme.mvp.mappers;

import com.acme.mvp.dtos.CreateBookingRequestDTO;
import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.models.Booking;
import com.acme.mvp.models.Employee;
import com.acme.mvp.models.MeetingRoom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TestBookingMapper {

    private BookingMapper bookingMapper;

    @BeforeEach
    void setUp() {
        bookingMapper = Mappers.getMapper(BookingMapper.class); // Instantiate the mapper
    }

    /**
     * Test case to verify that a Booking entity is correctly mapped to a GetBookingsResponseDTO.
     */
    @Test
    void testToDTO_MapsBookingToGetBookingsResponseDTO() {

        MeetingRoom room = new MeetingRoom(1L, "Room A");
        Employee employee = new Employee(1L, "employee@example.com");
        Booking booking = new Booking(1L, room, employee, LocalDate.of(2024, 10, 20), LocalTime.of(9, 0), LocalTime.of(10, 0));


        GetBookingsResponseDTO responseDTO = bookingMapper.toDTO(booking);


        assertNotNull(responseDTO);
        assertEquals(booking.getId(), responseDTO.id());
        assertEquals("Room A", responseDTO.meetingRoomName());
        assertEquals("employee@example.com", responseDTO.employeeEmail());
        assertEquals(booking.getDate(), responseDTO.date());
        assertEquals(booking.getTimeFrom(), responseDTO.timeFrom());
        assertEquals(booking.getTimeTo(), responseDTO.timeTo());
    }

    /**
     * Test case to verify that a GetBookingsResponseDTO is correctly mapped to a Booking entity.
     */
    @Test
    void testToEntity_MapsGetBookingsResponseDTOToBooking() {

        GetBookingsResponseDTO bookingDTO = new GetBookingsResponseDTO(1L, "Room A", "employee@example.com",
                LocalDate.of(2024, 10, 20), LocalTime.of(9, 0), LocalTime.of(10, 0));


        Booking booking = bookingMapper.toEntity(bookingDTO);


        assertNotNull(booking);
        assertEquals(bookingDTO.id(), booking.getId());
        assertEquals("Room A", booking.getMeetingRoom().getName());
        assertEquals("employee@example.com", booking.getEmployee().getEmail());
        assertEquals(bookingDTO.date(), booking.getDate());
        assertEquals(bookingDTO.timeFrom(), booking.getTimeFrom());
        assertEquals(bookingDTO.timeTo(), booking.getTimeTo());
    }

    /**
     * Test case to verify that a CreateBookingRequestDTO is correctly mapped to a Booking entity.
     */
    @Test
    void testToEntity_MapsCreateBookingRequestDTOToBooking() {

        CreateBookingRequestDTO createBookingDTO = new CreateBookingRequestDTO(
                "Room A", "employee@example.com", LocalDate.of(2024, 10, 20),
                LocalTime.of(9, 0), LocalTime.of(10, 0));


        Booking booking = bookingMapper.toEntity(createBookingDTO);


        assertNotNull(booking);
        assertEquals("Room A", booking.getMeetingRoom().getName());
        assertEquals("employee@example.com", booking.getEmployee().getEmail());
        assertEquals(createBookingDTO.date(), booking.getDate());
        assertEquals(createBookingDTO.timeFrom(), booking.getTimeFrom());
        assertEquals(createBookingDTO.timeTo(), booking.getTimeTo());
    }

    /**
     * Test case to verify that the mapping works for null values.
     */
    @Test
    void testToDTO_HandlesNullBooking() {

        Booking nullBooking = null;


        GetBookingsResponseDTO responseDTO = bookingMapper.toDTO(nullBooking);


        assertNull(responseDTO); // Null input should result in a null output
    }

    /**
     * Test case to verify that the mapping works for null values in the reverse direction.
     */
    @Test
    void testToEntity_HandlesNullGetBookingsResponseDTO() {

        GetBookingsResponseDTO nullBookingDTO = null;


        Booking booking = bookingMapper.toEntity(nullBookingDTO);


        assertNull(booking); // Null input should result in a null output
    }

    /**
     * Test case to verify that the mapping works for null values in the case of CreateBookingRequestDTO.
     */
    @Test
    void testToEntity_HandlesNullCreateBookingRequestDTO() {

        CreateBookingRequestDTO nullCreateBookingDTO = null;


        Booking booking = bookingMapper.toEntity(nullCreateBookingDTO);


        assertNull(booking); // Null input should result in a null output
    }
}
