package com.app.services;

import com.app.dao.AccountDAO;
import com.app.model.Account;
import com.app.web.AccountAdapter;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Sergey on 06.09.2017.
 */
public class AccountServiceImpl implements AccountService {

    private AccountDAO accountDAO;

    @Inject
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public Account save(Account account, AccountAdapter responseAdapter) {
        try {
            // without sync (because it is not possible to create account from two threads)
            responseAdapter.success();
            return accountDAO.save(account);
        } catch (IllegalArgumentException ex) {
            responseAdapter.clientError();
        } catch (Exception r) {
            responseAdapter.serverError();
        }
        return null;
    }

    @Override
    public List<Account> getAccounts() {
        return accountDAO.getAccounts();
    }


    @Override
    public void transfer(String fromId, String toId, BigDecimal amount, AccountAdapter responseAdapter) {
        try {
            Account ac1 = accountDAO.getAccount(fromId);
            Account ac2 = accountDAO.getAccount(toId);

            validateIncomingData(amount, ac1, ac2);

            //to prevent deadlock, order of taking monitors should be always the same
            Object lock1 = ac1.getPhoneNumber().compareTo(ac2.getPhoneNumber()) < 0 ? ac1 : ac2;
            Object lock2 = ac1.getPhoneNumber().compareTo(ac2.getPhoneNumber()) < 0 ? ac2 : ac1;

            synchronized (lock1) {
                synchronized (lock2) {
                    ac1.transfer(ac2, amount);
                }
            }
            // in real data storage we need to save it, in our case reference is enough
            accountDAO.save(ac2);
            accountDAO.save(ac1);
            responseAdapter.success();
        } catch (IllegalArgumentException ex){
            responseAdapter.clientError();
        } catch (Exception ex){
            responseAdapter.serverError();
        }
    }

    private void validateIncomingData(BigDecimal amount, Account ac1, Account ac2) {
        if(ac1 == null || ac2 == null){
            throw new IllegalArgumentException("missing account");
        }
        if(amount.compareTo(BigDecimal.ZERO) == 0 || amount.compareTo(BigDecimal.ZERO) == -1){
            throw new IllegalArgumentException("wrong amount");
        }
    }
}
