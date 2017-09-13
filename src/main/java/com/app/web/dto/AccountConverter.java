package com.app.web.dto;

import com.app.model.Account;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Sergey on 10.09.2017.
 */
public class AccountConverter {

    public static List<AccountDTO> toAccountDTO(List<Account> accounts) {
        return accounts.stream()
                .map(AccountConverter::toAccountDTO)
                .collect(Collectors.toList());
    }

    public static AccountDTO toAccountDTO(Account account){
        return new AccountDTO(account.getPhoneNumber(),account.getFirstName(),account.getLastName(),account.getAmount());
    }

    public static Account toAccount(AccountDTO account){
        return new Account(account.getPhoneNumber(),account.getFirstName(),account.getLastName(),account.getAmount());
    }
}
