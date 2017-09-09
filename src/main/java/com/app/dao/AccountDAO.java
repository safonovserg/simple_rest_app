package com.app.dao;

import com.app.model.Account;

import java.util.List;

/**
 * Created by Sergey on 06.09.2017.
 */
public interface AccountDAO {

    Account create(Account account);

    List<Account> getAccounts();

    Account getAccount(String id);

}
