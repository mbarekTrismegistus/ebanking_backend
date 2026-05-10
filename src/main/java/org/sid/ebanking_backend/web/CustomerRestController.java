package org.sid.ebanking_backend.web;

import org.sid.ebanking_backend.dtos.CustomerDTO;
import org.sid.ebanking_backend.exceptions.CustomerNotFoundException;
import org.sid.ebanking_backend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@AllArgsConstructor
@Slf4j
@CrossOrigin("*")
public class CustomerRestController {

    private final BankAccountService bankAccountService;

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/customers")
    public List<CustomerDTO> customers() {
        return bankAccountService.listCustomers();
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/customers/search")
    public List<CustomerDTO> searchCustomers(
            @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        return bankAccountService.searchCustomers("%" + keyword + "%");
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer(@PathVariable Long id) {
        try {
            return bankAccountService.getCustomer(id);
        } catch (CustomerNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(
            @PathVariable Long customerId,
            @RequestBody CustomerDTO customerDTO) {
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        bankAccountService.deleteCustomer(id);
    }
}
