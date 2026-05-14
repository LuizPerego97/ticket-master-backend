package com.github.luizperego97.ticket_master_backend.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;

    @NotBlank(message = "Name is required and cannot be empty.")
    @Size(max = 100, message = "Name cannot exceed {max} characters.")
    private String name;

    private LocalDateTime entryDate;
}
