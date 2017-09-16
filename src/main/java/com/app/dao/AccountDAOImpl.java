package com.app.dao;

import com.app.model.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Sergey on 06.09.2017.
 */
public class AccountDAOImpl implements AccountDAO {

    private final Map<String,Account> storage = new ConcurrentHashMap<>();

    @Override
    public Account save(Account account) {
        if (!storage.containsKey(account.getPhoneNumber())) {
            return storage.putIfAbsent(account.getPhoneNumber(), account);
        }
        return account;
    }

    @Override
    public List<Account> getAccounts() {
        return  new ArrayList<>(storage.values());
    }

    @Override
    public Account getAccount(String key) {
        return storage.get(key);
    }
}
