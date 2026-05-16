package com.github.luizperego97.ticket_master_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode; // <-- Importante
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "CUSTOMER")
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Evita duplicar chaves no mapeamento do Hibernate
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CTR_KEY")
    @EqualsAndHashCode.Include // Garante que apenas esta coluna identifica unicamente o objeto
    private Long ctrKey;

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "ENTRY_DATE", nullable = false)
    private LocalDateTime entryDate;
}