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
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Time from.
     */
    @Column(nullable = false)
    private LocalTime timeFrom;

    /**
     * Time to.
     */
    @Column(nullable = false)
    private LocalTime timeTo;
}
