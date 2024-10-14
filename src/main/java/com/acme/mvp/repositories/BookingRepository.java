package com.acme.mvp.repositories;

import com.acme.mvp.models.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByMeetingRoomNameAndDate(String name, LocalDate date);
}
