package com.acme.mvp.services;

import com.acme.mvp.dtos.CreateBookingRequestDTO;
import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.mappers.BookingMapper;
import com.acme.mvp.models.Booking;
import com.acme.mvp.models.Employee;
import com.acme.mvp.models.MeetingRoom;
import com.acme.mvp.repositories.BookingRepository;
import com.acme.mvp.repositories.EmployeeRepository;
import com.acme.mvp.repositories.MeetingRoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Service for the {@link Booking} endpoints.
 */
@Service
@RequiredArgsConstructor
public class BookingService {

    private final MeetingRoomRepository meetingRoomRepository;
    private final EmployeeRepository employeeRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;


    /**
     * Returns a {@link List} of all the bookings of a specific room on a specific date.
     * @param roomName
     * @param date
     * @return
     */
    public List<GetBookingsResponseDTO> findBookingsByRoomAndDate(String roomName, LocalDate date) {
        List<Booking> bookings = bookingRepository.findByMeetingRoomNameAndDate(roomName, date);

        return bookings.stream()
                .map(bookingMapper::toDTO)
                .toList();
    }


    /**
     * Creates a {@link Booking} from a {@link CreateBookingRequestDTO} after appropriate validations or throws an error
     * and doesn't persist to the database.
     * @param createBookingDTO
     * @return
     */
    @Transactional
    public GetBookingsResponseDTO createBooking(CreateBookingRequestDTO createBookingDTO) {

        MeetingRoom meetingRoom = meetingRoomRepository.findByName(createBookingDTO.meetingRoomName())
                .orElseThrow(() -> new IllegalArgumentException("Meeting room not found"));

        Employee employee = employeeRepository.findByEmail(createBookingDTO.employeeEmail())
                .orElseThrow(() -> new IllegalArgumentException("Employee not found"));

        validateConflict(createBookingDTO, meetingRoom);
        validateTimeToIsAfterTimeFrom(createBookingDTO);
        validateTimeInterval(createBookingDTO.timeFrom(), createBookingDTO.timeTo());

        Booking booking = bookingMapper.toEntity(createBookingDTO);
        booking.setMeetingRoom(meetingRoom);
        booking.setEmployee(employee);

        Booking savedBooking = bookingRepository.save(booking);


        return bookingMapper.toDTO(savedBooking);
    }

    /**
     * Deletes the {@link Booking} with the corresponding id, if an error is thrown no change is made on the database.
     * @param bookingId
     * @return message of success.
     */
    @Transactional
    public String cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        validateBookingNotInThePast(booking);

        bookingRepository.delete(booking);

        return "Booking was canceled successfully!";
    }

    /**
     * Validates that the booking up for deletion is not in the past, if it is throws an exception with the appropriate
     * message.
     * @param booking
     */
    private void validateBookingNotInThePast(Booking booking) {

        LocalDate bookingDate = booking.getDate();
        LocalTime bookingEndTime = booking.getTimeTo();

        if (bookingDate.isBefore(LocalDate.now()) ||
                (bookingDate.isEqual(LocalDate.now()) && bookingEndTime.isBefore(LocalTime.now()))) {
            throw new IllegalArgumentException("Cannot cancel a booking in the past.");
        }
    }

    /**
     * Validates that timeTo field presents a time that comes after the timeFrom field.
     * @param createBookingDTO
     */
    private static void validateTimeToIsAfterTimeFrom(CreateBookingRequestDTO createBookingDTO) {
        boolean timeToBeforeTheTimeFrom = createBookingDTO.timeTo().isBefore(createBookingDTO.timeFrom());

        if (timeToBeforeTheTimeFrom) {
            throw  new IllegalArgumentException("The time to is before the time from");
        }
    }

    /**
     * Validates there are no bookings for the specific time slot on the specific {@link MeetingRoom}.
     * @param createBookingDTO
     * @param meetingRoom
     */
    private void validateConflict(CreateBookingRequestDTO createBookingDTO, MeetingRoom meetingRoom) {
        boolean conflict = bookingRepository.existsByMeetingRoomAndDateAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(
                meetingRoom, createBookingDTO.date(), createBookingDTO.timeTo(), createBookingDTO.timeFrom());

        if (conflict) {
            throw new IllegalArgumentException("This room is already booked for the specified time slot.");
        }
    }

    /**
     * Validates that the time slot is a multiple of the hour.
     * @param timeFrom
     * @param timeTo
     */
    private void validateTimeInterval(LocalTime timeFrom, LocalTime timeTo) {

        long hoursDifference = ChronoUnit.HOURS.between(timeFrom, timeTo);

        if (hoursDifference < 1 || hoursDifference % 1 != 0) {
            throw new IllegalArgumentException("The booking must be for at least 1 hour and in multiples of 1 hour.");
        }
    }
}
