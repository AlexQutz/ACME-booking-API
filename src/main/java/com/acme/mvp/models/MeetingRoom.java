package com.acme.mvp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Room Model.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingRoom {

    /**
     * Room id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Room name.
     */
    @Column(nullable = false)
    private String name;
}
