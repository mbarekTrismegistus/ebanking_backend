package com.enset.digitalbanking.services;

import com.enset.digitalbanking.dtos.*;
import com.enset.digitalbanking.exceptions.BalanceNotSufficientException;
import com.enset.digitalbanking.exceptions.BankAccountNotFoundException;
import com.enset.digitalbanking.exceptions.CustomerNotFoundException;

import java.util.List;


public interface BankAccountService {


    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);

    List<CustomerDTO> listCustomers();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> searchCustomers(String keyword);



    CurrentBankAccountDTO saveCurrentBankAccount(
            double initialBalance,
            double overDraft,
            Long customerId) throws CustomerNotFoundException;

    SavingBankAccountDTO saveSavingBankAccount(
            double initialBalance,
            double interestRate,
            Long customerId) throws CustomerNotFoundException;



    List<BankAccountDTO> bankAccountList();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;


    void debit(String accountId, double amount, String description)
            throws BankAccountNotFoundException, BalanceNotSufficientException;


    void credit(String accountId, double amount, String description)
            throws BankAccountNotFoundException;


    void transfer(String accountIdSource, String accountIdDestination, double amount)
            throws BankAccountNotFoundException, BalanceNotSufficientException;


    List<AccountOperationDTO> accountHistory(String accountId);


    AccountHistoryDTO getAccountHistory(String accountId, int page, int size)
            throws BankAccountNotFoundException;
}
