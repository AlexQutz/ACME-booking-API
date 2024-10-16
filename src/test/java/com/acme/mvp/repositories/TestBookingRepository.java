//package com.acme.mvp.repositories;
//
//import com.acme.mvp.models.Booking;
//import com.acme.mvp.models.Employee;
//import com.acme.mvp.models.MeetingRoom;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//@ActiveProfiles("test")
//public class TestBookingRepository {
//
//    @Autowired
//    private BookingRepository bookingRepository;
//
//    @Autowired
//    private MeetingRoomRepository meetingRoomRepository;
//
//    @Autowired
//    private EmployeeRepository employeeRepository;
//
//    private MeetingRoom meetingRoom1;
//    private MeetingRoom meetingRoom2;
//    private Employee employee;
//
//    @BeforeEach
//    public void setup() {
//        meetingRoom1 = meetingRoomRepository.save(new MeetingRoom(1L,"Room A"));
//        meetingRoom2 = meetingRoomRepository.save(new MeetingRoom(2L,"Room B"));
//        employee = employeeRepository.save(new Employee(1L,"employee@acme.com"));
//    }
//
//    /**
//     * Test scenario: Finding bookings by room name and date.
//     * Validates if bookings are correctly retrieved for the given meeting room and date.
//     */
//    @Test
//    public void testFindByMeetingRoomNameAndDate() {
//        // Arrange
//        LocalDate today = LocalDate.now();
//        LocalTime startTime = LocalTime.of(10, 0);
//        LocalTime endTime = LocalTime.of(11, 0);
//
//        bookingRepository.save(new Booking(1L,meetingRoom1, employee, today, startTime, endTime));
//        bookingRepository.save(new Booking(2L,meetingRoom1, employee, today, LocalTime.of(12, 0), LocalTime.of(13, 0)));
//
//        // Act
//        List<Booking> bookings = bookingRepository.findByMeetingRoomNameAndDate("Room A", today);
//
//        // Assert
//        assertEquals(2, bookings.size());
//    }
//
//    /**
//     * Test scenario: No bookings found for the given room name and date.
//     */
//    @Test
//    public void testFindByMeetingRoomNameAndDate_NoBookings() {
//        // Arrange
//        LocalDate today = LocalDate.now();
//
//        // Act
//        List<Booking> bookings = bookingRepository.findByMeetingRoomNameAndDate("Room A", today);
//
//        // Assert
//        assertTrue(bookings.isEmpty());
//    }
//
//    /**
//     * Test scenario: Finding bookings by room name and date for a different meeting room.
//     * Ensures that bookings are specific to each room.
//     */
//    @Test
//    public void testFindByMeetingRoomNameAndDate_DifferentRoom() {
//        // Arrange
//        LocalDate today = LocalDate.now();
//        LocalTime startTime = LocalTime.of(10, 0);
//        LocalTime endTime = LocalTime.of(11, 0);
//
//        bookingRepository.save(new Booking(1L,meetingRoom1, employee, today, startTime, endTime));
//        bookingRepository.save(new Booking(2L,meetingRoom2, employee, today, startTime, endTime));
//
//        // Act
//        List<Booking> bookingsForRoom1 = bookingRepository.findByMeetingRoomNameAndDate("Room A", today);
//        List<Booking> bookingsForRoom2 = bookingRepository.findByMeetingRoomNameAndDate("Room B", today);
//
//        // Assert
//        assertEquals(1, bookingsForRoom1.size());
//        assertEquals(1, bookingsForRoom2.size());
//    }
//
//    /**
//     * Test scenario: Conflict exists for the same meeting room and overlapping time slot.
//     * Validates that the existsBy method correctly identifies time slot conflicts.
//     */
//    @Test
//    public void testExistsByMeetingRoomAndDateAndTimeSlot_ConflictExists() {
//        // Arrange
//        LocalDate today = LocalDate.now();
//        LocalTime startTime = LocalTime.of(10, 0);
//        LocalTime endTime = LocalTime.of(11, 0);
//
//        bookingRepository.save(new Booking(1L,meetingRoom1, employee, today, startTime, endTime));
//
//        // Act
//        boolean conflictExists = bookingRepository.existsByMeetingRoomAndDateAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(
//                meetingRoom1, today, LocalTime.of(10, 30), LocalTime.of(11, 30));
//
//        // Assert
//        assertTrue(conflictExists);
//    }
//
//    /**
//     * Test scenario: No conflict for the same meeting room with a non-overlapping time slot.
//     */
//    @Test
//    public void testExistsByMeetingRoomAndDateAndTimeSlot_NoConflict() {
//        // Arrange
//        LocalDate today = LocalDate.now();
//        LocalTime startTime = LocalTime.of(10, 0);
//        LocalTime endTime = LocalTime.of(11, 0);
//
//        bookingRepository.save(new Booking(1L,meetingRoom1, employee, today, startTime, endTime));
//
//        // Act
//        boolean conflictExists = bookingRepository.existsByMeetingRoomAndDateAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(
//                meetingRoom1, today, LocalTime.of(11, 0), LocalTime.of(12, 0));
//
//        // Assert
//        assertFalse(conflictExists);
//    }
//
//    /**
//     * Test scenario: Conflict exists when the booking time exactly matches the existing booking.
//     */
//    @Test
//    public void testExistsByMeetingRoomAndDateAndTimeSlot_ExactMatch() {
//        // Arrange
//        LocalDate today = LocalDate.now();
//        LocalTime startTime = LocalTime.of(10, 0);
//        LocalTime endTime = LocalTime.of(11, 0);
//
//        bookingRepository.save(new Booking(1L,meetingRoom1, employee, today, startTime, endTime));
//
//        // Act
//        boolean conflictExists = bookingRepository.existsByMeetingRoomAndDateAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(
//                meetingRoom1, today, startTime, endTime);
//
//        // Assert
//        assertTrue(conflictExists);
//    }
//
//    /**
//     * Test scenario: Conflict exists when a new booking fully overlaps an existing booking.
//     */
//    @Test
//    public void testExistsByMeetingRoomAndDateAndTimeSlot_FullOverlap() {
//        // Arrange
//        LocalDate today = LocalDate.now();
//        LocalTime startTime = LocalTime.of(10, 0);
//        LocalTime endTime = LocalTime.of(11, 0);
//
//        bookingRepository.save(new Booking(1L,meetingRoom1, employee, today, startTime, endTime));
//
//        // Act
//        boolean conflictExists = bookingRepository.existsByMeetingRoomAndDateAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(
//                meetingRoom1, today, LocalTime.of(9, 0), LocalTime.of(12, 0));
//
//        // Assert
//        assertTrue(conflictExists);
//    }
//}
