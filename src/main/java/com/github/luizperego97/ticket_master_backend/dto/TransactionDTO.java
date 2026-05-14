package com.github.luizperego97.ticket_master_backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {

    private Long id;

    @NotNull(message = "Customer ID is required.")
    private Long customerId;

    @NotNull(message = "Seat ID is required.")
    private Long seatId;

    private CustomerDTO customer;
    private SeatDTO seat;
}