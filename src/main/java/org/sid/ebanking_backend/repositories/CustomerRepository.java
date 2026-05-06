package org.sid.ebanking_backend.repositories;

import org.sid.ebanking_backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CustomerRepository extends JpaRepository<Customer, Long> {


    @Query("SELECT c FROM Customer c WHERE c.name LIKE :keyword")
    List<Customer> searchCustomers(String keyword);
}