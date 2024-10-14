package com.acme.mvp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalTime;


@JsonIgnoreProperties({"id","meetingRoomName"})
public record GetBookingsResponseDTO(
        Long id,
        String meetingRoomName,
        String employeeEmail,
        LocalDate date,
        LocalTime timeFrom,
        LocalTime timeTo
) {}
