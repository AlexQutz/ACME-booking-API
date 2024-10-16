package com.acme.mvp

import com.acme.mvp.dtos.CreateBookingRequestDTO
import com.acme.mvp.models.Booking
import com.acme.mvp.models.Employee
import com.acme.mvp.models.MeetingRoom
import com.acme.mvp.repositories.BookingRepository
import com.acme.mvp.repositories.EmployeeRepository
import com.acme.mvp.repositories.MeetingRoomRepository
import com.acme.mvp.services.BookingService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

/**
 * Integration tests for Service Layer.
 */
@SpringBootTest
@ContextConfiguration(classes = MvpApplication)
@TestPropertySource("classpath:application-test.properties")
class BookingServiceIntegrationTest extends Specification {

    @Autowired
    BookingService bookingService

    @Autowired
    MeetingRoomRepository meetingRoomRepository

    @Autowired
    EmployeeRepository employeeRepository

    @Autowired
    BookingRepository bookingRepository

    def setup() {
        // Clean the database before each test
        bookingRepository.deleteAll()
        employeeRepository.deleteAll()
        meetingRoomRepository.deleteAll()
    }

    def "should create a booking and retrieve it correctly"() {
        given: "A new meeting room, employee, and booking details"
        MeetingRoom room = meetingRoomRepository.save(new MeetingRoom(null, "Conference Room B"))
        Employee employee = employeeRepository.save(new Employee(null, "jane.doe@acme.com"))
        CreateBookingRequestDTO createBookingDTO = new CreateBookingRequestDTO(room.getName(), employee.getEmail(),
                LocalDate.of(2024,10,20), LocalTime.of(10,0), LocalTime.of(12, 0))

        when: "A booking is created"
        def createdBooking = bookingService.createBooking(createBookingDTO)

        then: "The booking is saved correctly and can be retrieved"
        createdBooking.meetingRoomName == "Conference Room B"
        createdBooking.employeeEmail == "jane.doe@acme.com"
        createdBooking.date == LocalDate.of(2024, 10, 20)
        createdBooking.timeFrom == LocalTime.of(10, 0)
        createdBooking.timeTo == LocalTime.of(12, 0)
    }

    def "should throw an exception if the meeting room is not found"() {
        given: "An employee exists but the meeting room does not"
        Employee employee = employeeRepository.save(new Employee(null, "jane.doe@acme.com"))
        CreateBookingRequestDTO createBookingDTO = new CreateBookingRequestDTO("Non-Existent Room", employee.getEmail(),
                LocalDate.of(2024, 10, 20), LocalTime.of(10, 0), LocalTime.of(12, 0))

        when: "Attempting to create a booking"
        bookingService.createBooking(createBookingDTO)

        then: "An exception is thrown"
        def ex = thrown(IllegalArgumentException)
        ex.message == "Meeting room not found"
    }

    def "should cancel a booking successfully"() {
        given: "A booking exists"
        MeetingRoom room = meetingRoomRepository.save(new MeetingRoom(null, "Conference Room B"))
        Employee employee = employeeRepository.save(new Employee(null, "john.doe@acme.com"))
        Booking booking = bookingRepository.save(new Booking(null, room, employee,
                LocalDate.of(2024, 10, 21), LocalTime.of(14, 0), LocalTime.of(16, 0)))

        when: "The booking is canceled"
        def result = bookingService.cancelBooking(booking.id)

        then: "The booking is canceled successfully and the repository is empty"
        result == "Booking was canceled successfully!"
        !bookingRepository.findById(booking.id).isPresent()
    }

    def "should throw an exception if trying to cancel a past booking"() {
        given: "A past booking exists"
        MeetingRoom room = meetingRoomRepository.save(new MeetingRoom(null, "Conference Room A"))
        Employee employee = employeeRepository.save(new Employee(null, "john.doe@acme.com"))
        Booking booking = bookingRepository.save(new Booking(null, room, employee,
                LocalDate.now().minusDays(1), LocalTime.of(9, 0), LocalTime.of(11, 0)))

        when: "Attempting to cancel the past booking"
        bookingService.cancelBooking(booking.id)

        then: "An exception is thrown"
        def ex = thrown(IllegalArgumentException)
        ex.message == "Cannot cancel a booking in the past."
    }

    def "should find bookings by room and date"() {
        given: "Multiple bookings exist for the same room on different dates"
        MeetingRoom room = meetingRoomRepository.save(new MeetingRoom(null, "Conference Room C"))
        Employee employee = employeeRepository.save(new Employee(null, "alice.doe@acme.com"))

        bookingRepository.save(new Booking(null, room, employee, LocalDate.of(2024, 10, 20),
                LocalTime.of(9, 0), LocalTime.of(10, 0)))
        bookingRepository.save(new Booking(null, room, employee, LocalDate.of(2024, 10, 21),
                LocalTime.of(10, 0), LocalTime.of(11, 0)))

        when: "Fetching bookings by room name and date"
        def bookings = bookingService.findBookingsByRoomAndDate("Conference Room C", LocalDate.of(2024, 10, 20))

        then: "Only the booking for the specified date is returned"
        bookings.size() == 1
        bookings[0].meetingRoomName == "Conference Room C"
        bookings[0].date == LocalDate.of(2024, 10, 20)
        bookings[0].timeFrom == LocalTime.of(9, 0)
        bookings[0].timeTo == LocalTime.of(10, 0)
    }
}
