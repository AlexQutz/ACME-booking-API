import com.acme.mvp.MvpApplication
import com.acme.mvp.models.Booking
import com.acme.mvp.models.Employee
import com.acme.mvp.models.MeetingRoom
import com.acme.mvp.repositories.BookingRepository
import com.acme.mvp.repositories.EmployeeRepository
import com.acme.mvp.repositories.MeetingRoomRepository
import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.time.LocalDate
import java.time.LocalTime

/**
 * Integration tests for entity persistence and relationships using Spock.
 */
@SpringBootTest
@ContextConfiguration(classes = MvpApplication)
@TestPropertySource("classpath:application-test.properties")
class BookingEntityIntegrationTest extends Specification {

    @Autowired
    BookingRepository bookingRepository

    @Autowired
    MeetingRoomRepository meetingRoomRepository

    @Autowired
    EmployeeRepository employeeRepository

    def "should save and retrieve a booking with meeting room and employee"() {
        given: "a new meeting room, employee, and booking"
        MeetingRoom room = new MeetingRoom(null, "Conference Room A")
        Employee employee = new Employee(null, "john.doe@acme.com")
        Booking booking = new Booking(null, room, employee, LocalDate.of(2024,10,17), LocalTime.of(9,0), LocalTime.of(11, 0))

        when: "the entities are saved"
        meetingRoomRepository.save(room)
        employeeRepository.save(employee)
        bookingRepository.save(booking)

        meetingRoomRepository.flush()
        employeeRepository.flush()
        bookingRepository.flush()


        then: "they can be retrieved and are correctly linked"
        def savedBooking = bookingRepository.findById(booking.id).get()
        savedBooking.meetingRoom.name == "Conference Room A"
        savedBooking.employee.email == "john.doe@acme.com"
        savedBooking.date == LocalDate.of(2024,10,17)
        savedBooking.timeFrom == LocalTime.of(9, 0)
        savedBooking.timeTo == LocalTime.of(11, 0)
    }

    def "should delete a booking and maintain meeting room and employee"() {
        given: "a new meeting room, employee, and booking"
        MeetingRoom room = new MeetingRoom(null, "Conference Room B")
        Employee employee = new Employee(null, "jane.doe@acme.com")
        Booking booking = new Booking(null, room, employee, LocalDate.now(), LocalTime.of(12, 0), LocalTime.of(13, 0))

        when: "the entities are saved"
        meetingRoomRepository.save(room)
        employeeRepository.save(employee)
        bookingRepository.save(booking)

        then: "the booking can be deleted without affecting room and employee"
        bookingRepository.delete(booking)
        def deletedBooking = bookingRepository.findById(booking.id)
        deletedBooking.isEmpty()

        // Meeting room and employee should still exist
        def savedRoom = meetingRoomRepository.findById(room.id).get()
        def savedEmployee = employeeRepository.findById(employee.id).get()
        savedRoom.name == "Conference Room B"
        savedEmployee.email == "jane.doe@acme.com"
    }

    def "should fail to save a booking with null values for required fields"() {
        given: "a booking with null values"
        Booking invalidBooking = new Booking(null, null, null, null, null, null)

        when: "trying to save the invalid booking"
        bookingRepository.save(invalidBooking)

        then: "an exception is thrown"
        thrown(Exception)
    }

    def "should not delete meeting room or employee when booking is deleted"() {
        given: "a saved booking, meeting room, and employee"
        MeetingRoom room = new MeetingRoom(null, "Conference Room C")
        Employee employee = new Employee(null, "bob.smith@acme.com")
        Booking booking = new Booking(null, room, employee, LocalDate.now(), LocalTime.of(10, 0), LocalTime.of(11, 0))

        meetingRoomRepository.save(room)
        employeeRepository.save(employee)
        bookingRepository.save(booking)

        when: "the booking is deleted"
        bookingRepository.delete(booking)

        then: "the meeting room and employee remain in the database"
        meetingRoomRepository.findById(room.id).isPresent()
        employeeRepository.findById(employee.id).isPresent()
    }
}
