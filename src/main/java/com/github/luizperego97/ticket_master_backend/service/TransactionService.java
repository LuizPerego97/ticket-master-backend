package com.github.luizperego97.ticket_master_backend.service;

import com.github.luizperego97.ticket_master_backend.dto.CustomerDTO;
import com.github.luizperego97.ticket_master_backend.dto.SeatDTO;
import com.github.luizperego97.ticket_master_backend.dto.TransactionDTO;
import com.github.luizperego97.ticket_master_backend.entity.Customer;
import com.github.luizperego97.ticket_master_backend.entity.Seat;
import com.github.luizperego97.ticket_master_backend.entity.Transaction;
import com.github.luizperego97.ticket_master_backend.repository.CustomerRepository;
import com.github.luizperego97.ticket_master_backend.repository.SeatRepository;
import com.github.luizperego97.ticket_master_backend.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final SeatRepository seatRepository;
    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<TransactionDTO> getAll() {
        return transactionRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TransactionDTO getById(Long id) {
        Transaction transaction = getEntityById(id);
        return convertToDTO(transaction);
    }

    // 3. THE ACID TRANSACTION WITH PESSIMISTIC LOCK
    @Transactional
    public TransactionDTO save(TransactionDTO dto) {
        // Passo 1: Buscar a entidade pura do Cliente pelo ID vindo do DTO
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        // Passo 2: Buscar o assento aplicando o Lock Pessimista direto no Oracle (Garante o ISOLAMENTO)
        Seat seat = seatRepository.findByIdWithLock(dto.getSeatId())
                .orElseThrow(() -> new RuntimeException("Seat not found."));

        // Passo 3: Validar se o assento já não está ocupado (Garante a CONSISTÊNCIA)
        if (seat.getStatus() == 1) { // 1 = OCCUPIED
            throw new RuntimeException("Seat is already occupied by another user.");
        }

        seat.setStatus(1);
        seatRepository.save(seat);

        Transaction transaction = Transaction.builder()
                .customer(customer)
                .seat(seat)
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return convertToDTO(savedTransaction);
    }

    @Transactional
    public void delete(Long id) {
        Transaction transaction = getEntityById(id);
        Seat seat = transaction.getSeat();
        seat.setStatus(0);
        seatRepository.save(seat);

        transactionRepository.delete(transaction);
    }

    private Transaction getEntityById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found."));
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        Customer customer = transaction.getCustomer();
        Seat seat = transaction.getSeat();

        return TransactionDTO.builder()
                .id(transaction.getTrnKey())
                .customerId(customer.getCtrKey())
                .seatId(seat.getSetKey())
                .customer(CustomerDTO.builder()
                        .id(customer.getCtrKey())
                        .name(customer.getName())
                        .entryDate(customer.getEntryDate())
                        .build())
                .seat(SeatDTO.builder()
                        .id(seat.getSetKey())
                        .rowLabel(seat.getRowLabel())
                        .seatNumber(seat.getSeatNumber())
                        .status(seat.getStatus())
                        .build())
                .build();
    }
}