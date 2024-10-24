package com.acme.mvp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import com.acme.mvp.dtos.CreateBookingRequestDTO;
import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.mappers.BookingMapper;
import com.acme.mvp.models.Booking;
import com.acme.mvp.models.Employee;
import com.acme.mvp.models.MeetingRoom;
import com.acme.mvp.repositories.BookingRepository;
import com.acme.mvp.repositories.EmployeeRepository;
import com.acme.mvp.repositories.MeetingRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestBookingService {

    @Mock
    private MeetingRoomRepository meetingRoomRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingService bookingService;

    private Booking booking;
    private MeetingRoom meetingRoom;
    private Employee employee;
    private CreateBookingRequestDTO createBookingDTO;

    /**
     * Initialization.
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);


        meetingRoom = new MeetingRoom(1L, "Conference Room");
        employee = new Employee(1L, "employee@acme.com");

        booking = new Booking();
        booking.setId(1L);
        booking.setMeetingRoom(meetingRoom);
        booking.setEmployee(employee);
        booking.setDate(LocalDate.now());
        booking.setTimeFrom(LocalTime.now());
        booking.setTimeTo(LocalTime.now().plusHours(1));

        createBookingDTO = new CreateBookingRequestDTO(
                "Conference Room",
                "employee@acme.com",
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now().plusHours(1)
        );
    }

    /**
     * Tests for {@link BookingService#findBookingsByRoomAndDate(String, LocalDate)}.
     */
    @Test
    public void testFindBookingsByRoomAndDateSuccess() {

        when(bookingRepository.findByMeetingRoomNameAndDate("Conference Room", LocalDate.now()))
                .thenReturn(List.of(booking));
        when(bookingMapper.toDTO(booking)).thenReturn(new GetBookingsResponseDTO(
                1L,
                "Conference Room",
                "employee@acme.com",
                LocalDate.now(),
                LocalTime.now(),
                LocalTime.now().plusHours(1)
        ));

        List<GetBookingsResponseDTO> result = bookingService.findBookingsByRoomAndDate("Conference Room", LocalDate.now());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("employee@acme.com", result.get(0).employeeEmail());
    }

    /**
     * Test for {@link BookingService#createBooking(CreateBookingRequestDTO)} happy scenario.
     */
    @Test
    public void testCreateBookingSuccess() {

        when(meetingRoomRepository.findByName("Conference Room")).thenReturn(Optional.of(meetingRoom));
        when(employeeRepository.findByEmail("employee@acme.com")).thenReturn(Optional.of(employee));
        when(bookingMapper.toEntity(createBookingDTO)).thenReturn(booking);
        when(bookingRepository.save(booking)).thenReturn(booking);
        when(bookingMapper.toDTO(booking)).thenReturn(new GetBookingsResponseDTO(1L,"Conference Room","employee@acme.com",LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0)));


        GetBookingsResponseDTO responseDTO = bookingService.createBooking(createBookingDTO);


        assertNotNull(responseDTO);
        assertEquals("employee@acme.com", responseDTO.employeeEmail());
    }

    /**
     * Test for {@link BookingService#createBooking(CreateBookingRequestDTO)} invalid time to and from.
     */
    @Test
    public void testCreateBookingWithInvalidTime() {

        CreateBookingRequestDTO invalidDTO = new CreateBookingRequestDTO(
                "Conference Room",
                "employee@acme.com",
                LocalDate.of(2020, 10, 20),
                LocalTime.of(12, 0),
                LocalTime.of(11, 0)  // Invalid
        );
        when(meetingRoomRepository.findByName("Conference Room")).thenReturn(Optional.of(meetingRoom));
        when(employeeRepository.findByEmail("employee@acme.com")).thenReturn(Optional.of(employee));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(invalidDTO);
        });

        assertEquals("The time to is before the time from", exception.getMessage());
    }

    /**
     * Test for {@link BookingService#createBooking(CreateBookingRequestDTO)} with conflict.
     */
    @Test
    public void testCreateBookingConflict() {

        when(meetingRoomRepository.findByName("Conference Room")).thenReturn(Optional.of(meetingRoom));
        when(employeeRepository.findByEmail("employee@acme.com")).thenReturn(Optional.of(employee));
        when(bookingRepository.existsByMeetingRoomAndDateAndTimeFromLessThanAndTimeToGreaterThan(
                meetingRoom, createBookingDTO.date(), createBookingDTO.timeTo(), createBookingDTO.timeFrom()
        )).thenReturn(true);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.createBooking(createBookingDTO);
        });

        assertEquals("This room is already booked for the specified time slot.", exception.getMessage());
    }

    /**
     * Test for {@link BookingService#cancelBooking(Long)} happy scenario.
     */
    @Test
    public void testCancelBookingSuccess() {

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        String result = bookingService.cancelBooking(1L);

        assertEquals("Booking was canceled successfully!", result);
        verify(bookingRepository, times(1)).delete(booking);
    }


    /**
     * Test for {@link BookingService#cancelBooking(Long)} past booking.
     */
    @Test
    public void testCancelBookingInThePastThrowsException() {

        booking.setDate(LocalDate.of(2023, 10, 15));

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.cancelBooking(1L);
        });

        assertEquals("Cannot cancel a booking in the past.", exception.getMessage());
        verify(bookingRepository, never()).delete(any());
    }

    /**
     * Test {@link BookingService#validateTimeInterval(LocalTime, LocalTime)} method happy senario.
     */
    @Test
    public void testValidateTimeIntervalSuccess() {

        bookingService.validateTimeInterval(LocalTime.of(10, 0), LocalTime.of(11, 0));
    }

    /**
     * Test {@link BookingService#validateTimeInterval(LocalTime, LocalTime)} not multiple of an hour.
     */
    @Test
    public void testValidateTimeIntervalFailure() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.validateTimeInterval(LocalTime.of(10, 0), LocalTime.of(10, 30));
        });


        assertEquals("The booking must be for at least 1 hour and in multiples of 1 hour.", exception.getMessage());
    }
}
