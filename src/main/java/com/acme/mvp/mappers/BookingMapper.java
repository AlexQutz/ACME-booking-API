package com.acme.mvp.mappers;

import com.acme.mvp.dtos.CreateBookingRequestDTO;
import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.models.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for {@link GetBookingsResponseDTO} to {@link Booking} and vice versa.
 */
@Mapper(componentModel = "spring")
public interface BookingMapper {

    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    /**
     * Generates a {@link GetBookingsResponseDTO} from a {@link Booking} entity.
     * @param booking
     * @return
     */
    @Mapping(source = "meetingRoom.name", target = "meetingRoomName")
    @Mapping(source = "employee.email", target = "employeeEmail")
    GetBookingsResponseDTO toDTO(Booking booking);

    /**
     * Generates a {@link Booking} from a {@link GetBookingsResponseDTO}.
     * @param bookingDTO
     * @return
     */
    @Mapping(source = "meetingRoomName", target = "meetingRoom.name")
    @Mapping(source = "employeeEmail", target = "employee.email")
    Booking toEntity(GetBookingsResponseDTO bookingDTO);

    /**
     * Generates a {@link Booking} entity from a {@link CreateBookingRequestDTO}.
     * @param createBookingRequestDTO
     * @return
     */
    @Mapping(source = "meetingRoomName", target = "meetingRoom.name")
    @Mapping(source = "employeeEmail", target = "employee.email")
    Booking toEntity(CreateBookingRequestDTO createBookingRequestDTO);
}
