package org.sid.ebanking_backend.dtos;

import org.sid.ebanking_backend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;


@Data
public class BankAccountDTO {
    private String id;
    private double balance;
    private Date createdAt;
    private AccountStatus status;
    private String currency;
    private CustomerDTO customerDTO;


    private String type;
}