package com.github.luizperego97.ticket_master_backend.service;

import com.github.luizperego97.ticket_master_backend.dto.SeatDTO;
import com.github.luizperego97.ticket_master_backend.entity.Seat;
import com.github.luizperego97.ticket_master_backend.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    @Transactional(readOnly = true)
    public List<SeatDTO> getAll() {
        return seatRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SeatDTO getById(Long id) {
        Seat seat = getEntityById(id);
        return convertToDTO(seat);
    }

    @Transactional
    public SeatDTO save(SeatDTO dto) {
        Seat seat = Seat.builder()
                .rowLabel(dto.getRowLabel())
                .seatNumber(dto.getSeatNumber())
                .status(dto.getStatus())
                .build();

        Seat savedSeat = seatRepository.save(seat);
        return convertToDTO(savedSeat);
    }

    @Transactional
    public SeatDTO update(Long id, SeatDTO updatedData) {
        Seat existingSeat = getEntityById(id);
        existingSeat.setRowLabel(updatedData.getRowLabel());
        existingSeat.setSeatNumber(updatedData.getSeatNumber());
        existingSeat.setStatus(updatedData.getStatus());

        Seat savedSeat = seatRepository.save(existingSeat);
        return convertToDTO(savedSeat);
    }

    @Transactional
    public void delete(Long id) {
        Seat seat = getEntityById(id);
        seatRepository.delete(seat);
    }

    @Transactional
    public void reserve(Long seatId) {
        try {
            Seat seat = seatRepository.findByIdWithLock(seatId)
                    .orElseThrow(() -> new RuntimeException("Seat not found."));

            if (seat.getStatus() == 1) {
                throw new RuntimeException("This seat is already taken!");
            }

            seat.setStatus(1);
            seatRepository.save(seat);

        } catch (org.springframework.dao.PessimisticLockingFailureException e) {
            throw new RuntimeException("The seat is temporarily locked by another user. Please try again in a few moments.");
        }
    }

    private Seat getEntityById(Long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Seat not found."));
    }

    private SeatDTO convertToDTO(Seat seat) {
        return SeatDTO.builder()
                .id(seat.getId())
                .rowLabel(seat.getRowLabel())
                .seatNumber(seat.getSeatNumber())
                .status(seat.getStatus())
                .build();
    }
}