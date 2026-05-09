package org.sid.ebanking_backend;

import org.sid.ebanking_backend.dtos.BankAccountDTO;
import org.sid.ebanking_backend.dtos.CurrentBankAccountDTO;
import org.sid.ebanking_backend.dtos.CustomerDTO;
import org.sid.ebanking_backend.dtos.SavingBankAccountDTO;
import org.sid.ebanking_backend.exceptions.BalanceNotSufficientException;
import org.sid.ebanking_backend.exceptions.BankAccountNotFoundException;
import org.sid.ebanking_backend.exceptions.CustomerNotFoundException;
import org.sid.ebanking_backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankingBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankingBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {


            Stream.of("Hassan", "Yassine", "Aicha").forEach(name -> {
                CustomerDTO customer = new CustomerDTO();
                customer.setName(name);
                customer.setEmail(name.toLowerCase() + "@gmail.com");
                bankAccountService.saveCustomer(customer);
            });

            bankAccountService.listCustomers().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(
                            Math.random() * 90000,
                            9000,
                            customer.getId());

                    bankAccountService.saveSavingBankAccount(
                            Math.random() * 90000,
                            5.5,
                            customer.getId());

                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });


            List<BankAccountDTO> bankAccounts = bankAccountService.bankAccountList();
            for (BankAccountDTO bankAccount : bankAccounts) {
                String accountId = bankAccount.getId();
                for (int i = 0; i < 10; i++) {
                    double amount = Math.random() * 12000;
                    try {
                        if (Math.random() > 0.5) {
                            bankAccountService.credit(accountId, amount, "Credit operation");
                        } else {
                            bankAccountService.debit(accountId, amount, "Debit operation");
                        }
                    } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
                    }
                }
            }
        };
    }
}
