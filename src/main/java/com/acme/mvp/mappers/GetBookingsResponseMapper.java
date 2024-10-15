package com.acme.mvp.mappers;

import com.acme.mvp.dtos.GetBookingsResponseDTO;
import com.acme.mvp.models.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for {@link GetBookingsResponseDTO} to {@link Booking} and vice versa.
 */
@Mapper(componentModel = "spring")
public interface GetBookingsResponseMapper {

    GetBookingsResponseMapper INSTANCE = Mappers.getMapper(GetBookingsResponseMapper.class);

    @Mapping(source = "meetingRoom.name", target = "meetingRoomName")
    @Mapping(source = "employee.email", target = "employeeEmail")

    GetBookingsResponseDTO toDTO(Booking booking);

    @Mapping(source = "meetingRoomName", target = "meetingRoom.name")
    @Mapping(source = "employeeEmail", target = "employee.email")
    Booking toEntity(GetBookingsResponseDTO bookingDTO);
}
