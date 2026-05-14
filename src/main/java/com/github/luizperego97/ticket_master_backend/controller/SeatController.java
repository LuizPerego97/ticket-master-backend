package com.github.luizperego97.ticket_master_backend.controller;

import com.github.luizperego97.ticket_master_backend.dto.SeatDTO;
import com.github.luizperego97.ticket_master_backend.service.SeatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping
    public ResponseEntity<List<SeatDTO>> getAll() {
        List<SeatDTO> seats = seatService.getAll();
        return ResponseEntity.ok(seats);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatDTO> getById(@PathVariable Long id) {
        SeatDTO seat = seatService.getById(id);
        return ResponseEntity.ok(seat);
    }

    @PostMapping
    public ResponseEntity<SeatDTO> create(@Valid @RequestBody SeatDTO seatDTO) {
        SeatDTO newSeat = seatService.save(seatDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSeat);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatDTO> update(@PathVariable Long id, @Valid @RequestBody SeatDTO seatDTO) {
        SeatDTO updatedSeat = seatService.update(id, seatDTO);
        return ResponseEntity.ok(updatedSeat);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        seatService.delete(id);
        return ResponseEntity.noContent().build();
    }
}