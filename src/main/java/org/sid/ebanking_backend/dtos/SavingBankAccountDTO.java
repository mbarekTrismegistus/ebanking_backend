package org.sid.ebanking_backend.dtos;

import lombok.Data;
import org.sid.ebanking_backend.enums.AccountStatus;
import java.util.Date;


@Data
public class SavingBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private String type; 
    private Date createdAt;
    private AccountStatus status;
    private CustomerDTO customerDTO;
    private double interestRate;
}