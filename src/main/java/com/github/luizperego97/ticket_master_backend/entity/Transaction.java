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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRN_KEY")
    private Long trnKey;

    // Relacionamento com o Cliente especificando a coluna de destino
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CTR_KEY", referencedColumnName = "CTR_KEY", nullable = false)
    private Customer customer;

    // Relacionamento com o Assento especificando a coluna de destino
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SET_KEY", referencedColumnName = "SET_KEY", nullable = false)
    private Seat seat;
}