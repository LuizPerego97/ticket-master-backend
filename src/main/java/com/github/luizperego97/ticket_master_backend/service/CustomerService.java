package com.github.luizperego97.ticket_master_backend.service;

import com.github.luizperego97.ticket_master_backend.dto.CustomerDTO;
import com.github.luizperego97.ticket_master_backend.entity.Customer;
import com.github.luizperego97.ticket_master_backend.repository.CustomerRepository;
import com.github.luizperego97.shared_core.exception.ResourceNotFoundException; // Importado do shared_core
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional(readOnly = true)
    public List<CustomerDTO> getAll() {
        return customerRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CustomerDTO getById(Long id) {
        Customer customer = getEntityById(id);
        return convertToDTO(customer);
    }

    @Transactional
    public CustomerDTO save(CustomerDTO dto) {
        Customer customer = Customer.builder()
                .name(dto.getName())
                .entryDate(LocalDateTime.now())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return convertToDTO(savedCustomer);
    }

    @Transactional
    public CustomerDTO update(Long id, CustomerDTO updatedData) {
        Customer existingCustomer = getEntityById(id);
        existingCustomer.setName(updatedData.getName());

        Customer savedCustomer = customerRepository.save(existingCustomer);
        return convertToDTO(savedCustomer);
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = getEntityById(id);
        customerRepository.delete(customer);
    }

    private Customer getEntityById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return CustomerDTO.builder()
                .id(customer.getCtrKey())
                .name(customer.getName())
                .entryDate(customer.getEntryDate())
                .build();
    }
}