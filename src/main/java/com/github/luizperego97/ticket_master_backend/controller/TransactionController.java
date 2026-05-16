package com.github.luizperego97.ticket_master_backend.controller;

import com.github.luizperego97.ticket_master_backend.dto.TransactionDTO;
import com.github.luizperego97.ticket_master_backend.service.TicketProducerService;
import com.github.luizperego97.ticket_master_backend.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final TicketProducerService ticketProducerService; // <-- Adicione essa linha (Lombok injeta automático)


    @GetMapping
    public ResponseEntity<List<TransactionDTO>> getAll() {
        List<TransactionDTO> transactions = transactionService.getAll();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getById(@PathVariable Long id) {
        TransactionDTO transaction = transactionService.getById(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<TransactionDTO> create(@Valid @RequestBody TransactionDTO transactionDTO) {
        // 1. O ACID garante a persistência segura no Oracle aqui dentro:
        TransactionDTO newTransaction = transactionService.save(transactionDTO);

        // 2. Transação concluída e salva? Disparamos o evento para o Kafka!
        String mensagemKafka = "Transação realizada com sucesso! ID: " + newTransaction.getId();
        ticketProducerService.enviarEventoIngressoComprado(mensagemKafka);

        // 3. Devolve a resposta para o cliente (Postman/Frontend)
        return ResponseEntity.status(HttpStatus.CREATED).body(newTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }
}