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
@Table(name = "TRANSACTION")
public class Transaction {

    @Id
    @Column(name = "TRN_KEY")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "trn_seq")
    @SequenceGenerator(name = "trn_seq", sequenceName = "TRANSACTION_SEQ", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CTR_KEY", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SEAT_KEY", nullable = false)
    private Seat seat;
}
