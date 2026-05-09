package org.sid.ebanking_backend.dtos;

import lombok.Data;
import org.sid.ebanking_backend.enums.AccountStatus;
import java.util.Date;



@Data
public class CurrentBankAccountDTO extends BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String type; 
    private CustomerDTO customerDTO;
    private double overDraft;
}