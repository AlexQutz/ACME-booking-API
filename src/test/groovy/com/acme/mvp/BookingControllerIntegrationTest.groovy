package com.acme.mvp

import com.acme.mvp.models.Booking
import com.acme.mvp.models.Employee
import com.acme.mvp.models.MeetingRoom
import com.acme.mvp.repositories.BookingRepository
import com.acme.mvp.repositories.EmployeeRepository
import com.acme.mvp.repositories.MeetingRoomRepository
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

import java.time.LocalDate
import java.time.LocalTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


/**
 * Integration test for Controller layer.
 */
@SpringBootTest
@ContextConfiguration(classes = MvpApplication)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class BookingControllerIntegrationTest extends Specification {

    @Autowired
    MockMvc mockMvc

    @Autowired
    BookingRepository bookingRepository

    @Autowired
    MeetingRoomRepository meetingRoomRepository

    @Autowired
    EmployeeRepository employeeRepository

    def setup() {
        // Clean up the database before each test
        bookingRepository.deleteAll()
        meetingRoomRepository.deleteAll()
        employeeRepository.deleteAll()
    }

    def "should return a list of bookings for a specific room and date"() {
        given: "A meeting room, an employee, and a booking in the database"
        def room = meetingRoomRepository.save(new MeetingRoom(null, "Room A"))
        def employee = employeeRepository.save(new Employee(null, "test@example.com"))
        bookingRepository.save(new Booking(null, room, employee, LocalDate.of(2024,10,20), LocalTime.of(9,0), LocalTime.of(10, 0)))

        when: "A request is made to get bookings for that room and date"
        def result = mockMvc.perform(get("/v1/api/bookings")
                .param("roomName", "Room A")
                .param("date", "2024-10-20")
                .contentType(MediaType.APPLICATION_JSON))

        then: "The response contains the booking details"
        result.andExpect(status().isOk())
                .andExpect(jsonPath('$[0].employeeEmail').value("test@example.com"))
                .andExpect(jsonPath('$[0].date').value("2024-10-20"))
                .andExpect(jsonPath('$[0].timeFrom').value("09:00:00"))
                .andExpect(jsonPath('$[0].timeTo').value("10:00:00"))
    }

    def "should return 204 when no bookings are found for a specific room and date"() {
        when: "A request is made to get bookings for a room with no bookings on that date"
        def result = mockMvc.perform(get("/v1/api/bookings")
                .param("roomName", "Room A")
                .param("date", "2024-10-21")
                .contentType(MediaType.APPLICATION_JSON))

        then: "The response has no content"
        result.andExpect(status().isNoContent())
    }

    def "should create a new booking successfully"() {
        given: "A meeting room and an employee exist"
        def room = meetingRoomRepository.save(new MeetingRoom(null, "Room B"))
        def employee = employeeRepository.save(new Employee(null, "john.doe@acme.com"))

        def createBookingRequest = """
        {
            "meetingRoomName": "Room B",
            "employeeEmail": "john.doe@acme.com",
            "date": "2024-10-22",
            "timeFrom": "10:00:00",
            "timeTo": "12:00:00"
        }
        """

        when: "A POST request is made to create a booking"
        def result = mockMvc.perform(post("/v1/api/bookings")
                .content(createBookingRequest)
                .contentType(MediaType.APPLICATION_JSON))

        then: "The booking is created and the response is 201 with the created booking details"
        result.andExpect(status().isCreated())
                .andExpect(jsonPath('$.employeeEmail').value("john.doe@acme.com"))
                .andExpect(jsonPath('$.date').value("2024-10-22"))
                .andExpect(jsonPath('$.timeFrom').value("10:00:00"))
                .andExpect(jsonPath('$.timeTo').value("12:00:00"))
    }

    def "should cancel a booking successfully"() {
        given: "A booking exists"
        def room = meetingRoomRepository.save(new MeetingRoom(null, "Room C"))
        def employee = employeeRepository.save(new Employee(null, "jane.doe@acme.com"))
        def booking = bookingRepository.save(new Booking(null, room, employee, LocalDate.of(2024, 10, 23), LocalTime.of(14, 0), LocalTime.of(16, 0)))

        when: "A DELETE request is made to cancel the booking"
        def result = mockMvc.perform(delete("/v1/api/bookings/${booking.id}")
                .contentType(MediaType.APPLICATION_JSON))

        then: "The booking is canceled and the response is 200 with a success message"
        result.andExpect(status().isOk())
                .andExpect(content().string("Booking was canceled successfully!"))
    }

    def "should return 400 when trying to cancel a non-existing booking"() {
        when: "A DELETE request is made to cancel a non-existing booking"
        def result = mockMvc.perform(delete("/v1/api/bookings/9999")
                .contentType(MediaType.APPLICATION_JSON))

        then: "The response is 400 with an error message"
        result.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.error').value("Booking not found"))
    }
}

