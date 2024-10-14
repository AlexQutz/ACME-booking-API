package com.acme.mvp.services;

import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.mappers.GetBookingsResponseMapper;
import com.acme.mvp.models.Booking;
import com.acme.mvp.repositories.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final GetBookingsResponseMapper bookingMapper;

    public List<GetBookingsResponseDTO> findBookingsByRoomAndDate(String roomName, LocalDate date) {
        List<Booking> bookings = bookingRepository.findByMeetingRoomNameAndDate(roomName, date);

        // Use the mapper to convert entities to DTOs
        return bookings.stream()
                .map(bookingMapper::toDTO)
                .toList();
    }

//    public BookingDTO createBooking(BookingDTO bookingDTO) {
//        // Use the mapper to convert DTO to entity
//        Booking booking = bookingMapper.toEntity(bookingDTO);
//
//        // Save the entity to the database
//        Booking savedBooking = bookingRepository.save(booking);
//
//        // Return the saved booking as DTO
//        return bookingMapper.toDTO(savedBooking);
//    }
}
