package com.github.luizperego97.ticket_master_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatDTO {

    private Long id;

    @NotBlank(message = "Row label is required.")
    @Size(max = 10, message = "Row label cannot exceed 10 characters.")
    private String rowLabel;

    @NotNull(message = "Seat number is required.")
    private Integer seatNumber;

    @NotNull(message = "Status is required.")
    private Integer status;
}