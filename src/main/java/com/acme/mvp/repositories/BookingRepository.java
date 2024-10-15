package com.acme.mvp.repositories;

import com.acme.mvp.models.Booking;
import com.acme.mvp.models.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * JPA repository that contains queries for the {@link Booking} table.
 */
public interface BookingRepository extends JpaRepository<Booking, Long> {

    /**
     * Finds all bookings that have the provided meeting room name and date.
     * @param name
     * @param date
     * @return
     */
    List<Booking> findByMeetingRoomNameAndDate(String name, LocalDate date);

    /**
     * Question that returns true if there is a booking in the provided meeting room and in the provided date and with time slot
     * that interferes with the provided one.
     * @param meetingRoom
     * @param date
     * @param localTime
     * @param localTime1
     * @return
     */
    boolean existsByMeetingRoomAndDateAndTimeFromLessThanEqualAndTimeToGreaterThanEqual(MeetingRoom meetingRoom, LocalDate date, LocalTime localTime, LocalTime localTime1);
}
