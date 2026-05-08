package com.enset.digitalbanking.web;

import com.enset.digitalbanking.dtos.*;
import com.enset.digitalbanking.exceptions.BalanceNotSufficientException;
import com.enset.digitalbanking.exceptions.BankAccountNotFoundException;
import com.enset.digitalbanking.exceptions.CustomerNotFoundException;
import com.enset.digitalbanking.services.BankAccountService;
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

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) {
        try {
            return bankAccountService.getBankAccount(accountId);
        } catch (BankAccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }


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

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> getHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }


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
