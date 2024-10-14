package com.acme.mvp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Employee model.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    /**
     * Employee id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Employee e-mail.
     */
    @Column(nullable = false)
    private String email;
}
