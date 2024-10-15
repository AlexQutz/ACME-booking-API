package com.acme.mvp.services;

import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.mappers.GetBookingsResponseMapper;
import com.acme.mvp.models.Booking;
import com.acme.mvp.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GetBookingsResponseMapper bookingMapper;

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

}
