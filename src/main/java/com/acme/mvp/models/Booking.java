package com.acme.mvp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Booking Model.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    /**
     * Booking Id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Many to one Relation field to the {@link MeetingRoom} table.
     */
    @ManyToOne
    private MeetingRoom meetingRoom;

    /**
     * Many to one Relation field to the {@link Employee} table.
     */
    @ManyToOne
    private Employee employee;

    /**
     * Date.
     */
    private LocalDate date;

    /**
     * Time from.
     */
    private LocalTime timeFrom;

    /**
     * Time to.
     */
    private LocalTime timeTo;
}
