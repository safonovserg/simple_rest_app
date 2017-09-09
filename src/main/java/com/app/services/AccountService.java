package com.app.services;

import com.app.model.Account;
import com.app.web.AccountAdapter;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Sergey on 06.09.2017.
 */
public interface AccountService {

     Account create(Account account, AccountAdapter responseAdapter);

     List<Account> getAccounts();

     void transfer(String fromId, String toId, BigDecimal amount, AccountAdapter responseAdapter);
}
