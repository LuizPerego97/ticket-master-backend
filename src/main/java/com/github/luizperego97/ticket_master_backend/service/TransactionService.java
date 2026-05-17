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
import com.github.luizperego97.ticket_master_backend.service.kafka.TicketProducerService;
import com.github.luizperego97.shared_core.exception.ResourceNotFoundException; // Importado do shared_core
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
    private final TicketProducerService ticketProducerService;

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

    @Transactional
    public TransactionDTO processTransactionAndConsumeKafka(TransactionDTO dto) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        try {
            // Passo 2: Buscar o assento aplicando o Lock Pessimista usando a exceção do Core
            Seat seat = seatRepository.findByIdWithLock(dto.getSeatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found"));

            // Passo 3: Validar se o assento já não está ocupado (Regra de Negócio -> IllegalState)
            if (seat.getStatus() == 1) {
                throw new IllegalStateException("Seat is already occupied by another user.");
            }

            seat.setStatus(1);
            seatRepository.save(seat);

            Transaction transaction = Transaction.builder()
                    .customer(customer)
                    .seat(seat)
                    .build();

            Transaction savedTransaction = transactionRepository.save(transaction);
            TransactionDTO savedDto = convertToDTO(savedTransaction);

            String mesageKafka = "Transação realizada com sucesso !";
            ticketProducerService.sendTicketPurchasedEvent(mesageKafka);

            return savedDto;

        } catch (org.springframework.dao.PessimisticLockingFailureException e) {
            // Falha no Lock Pessimista (Concorrência -> IllegalState)
            throw new IllegalStateException("The seat is temporarily locked by another user. Please try again in a few moments.");
        }
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
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
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