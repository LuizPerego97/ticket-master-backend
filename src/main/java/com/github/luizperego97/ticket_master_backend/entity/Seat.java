package com.github.luizperego97.ticket_master_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SEAT")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SET_KEY")
    private Long setKey; // Mudado de id para setKey para casar com o banco

    @Column(name = "ROW_LABEL", nullable = false)
    private String rowLabel;

    @Column(name = "SEAT_NUMBER", nullable = false)
    private Integer seatNumber;

    @Column(name = "STATUS", nullable = false)
    private Integer status;
}