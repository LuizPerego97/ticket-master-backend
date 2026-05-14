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
    @Column(name = "SEAT_KEY")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seat_seq")
    @SequenceGenerator(name = "seat_seq", sequenceName = "SEAT_SEQ", allocationSize = 1)
    private Long id;

    @Column(name = "ROW_LABEL", nullable = false)
    private String rowLabel;

    @Column(name = "SEAT_NUMBER", nullable = false)
    private Integer seatNumber;

    @Column(name = "STATUS", nullable = false)
    private Integer status;

}
