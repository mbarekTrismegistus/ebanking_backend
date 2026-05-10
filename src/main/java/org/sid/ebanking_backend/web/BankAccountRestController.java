package org.sid.ebanking_backend.web;

import org.sid.ebanking_backend.dtos.*;
import org.sid.ebanking_backend.exceptions.BalanceNotSufficientException;
import org.sid.ebanking_backend.exceptions.BankAccountNotFoundException;
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
public class BankAccountRestController {

    private final BankAccountService bankAccountService;

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) {
        try {
            return bankAccountService.getBankAccount(accountId);
        } catch (BankAccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }


    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PostMapping("/accounts/current")
    public CurrentBankAccountDTO saveCurrentAccount(
            @RequestParam double initialBalance,
            @RequestParam double overDraft,
            @RequestParam Long customerId) {
        try {
            return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
        } catch (CustomerNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PostMapping("/accounts/saving")
    public SavingBankAccountDTO saveSavingAccount(
            @RequestParam double initialBalance,
            @RequestParam double interestRate,
            @RequestParam Long customerId) {
        try {
            return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
        } catch (CustomerNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }


    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @GetMapping("/accounts/{accountId}/pageOperations")
    public AccountHistoryDTO getAccountHistory(
            @PathVariable String accountId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size) {
        try {
            return bankAccountService.getAccountHistory(accountId, page, size);
        } catch (BankAccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PostMapping("/accounts/debit")
    public DebitDTO debit(@RequestBody DebitDTO debitDTO) {
        try {
            bankAccountService.debit(
                    debitDTO.getAccountId(),
                    debitDTO.getAmount(),
                    debitDTO.getDescription());
            return debitDTO;
        } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PostMapping("/accounts/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) {
        try {
            bankAccountService.credit(
                    creditDTO.getAccountId(),
                    creditDTO.getAmount(),
                    creditDTO.getDescription());
            return creditDTO;
        } catch (BankAccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('SCOPE_USER')")
    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO transferRequestDTO) {
        try {
            bankAccountService.transfer(
                    transferRequestDTO.getAccountSource(),
                    transferRequestDTO.getAccountDestination(),
                    transferRequestDTO.getAmount());
        } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
