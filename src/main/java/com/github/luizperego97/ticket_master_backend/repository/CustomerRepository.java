package com.github.luizperego97.ticket_master_backend.repository;

import com.github.luizperego97.ticket_master_backend.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


}
