package com.app.web;

import com.app.config.TomcatLauncher;
import com.app.model.Account;
import com.app.web.dto.AccountDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;

/***
 * Created by Sergey on 06.09.2017.
 */
public class MoneyControllerIT {

    private TomcatLauncher server;
    private WebTarget target;

    @Before
    public void setUp() throws Exception {
        server = new TomcatLauncher();
        new Thread(() -> {
            try {
                server.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        int attemptsToWaitStart = 10;
        while (!server.isStarted() && attemptsToWaitStart > 0) {
            Thread.sleep(500);
            --attemptsToWaitStart;
        }

        Client c = ClientBuilder.newClient();
        target = c.target("http://localhost:8080");
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testPing() {
        String responseMsg = target.path("/api/ping").request().get(String.class);
        assertEquals("ping is ok", "test", responseMsg);
    }

    @Test
    public void createAccount() {
        AccountDTO account = new AccountDTO("095512322","Luke", "Skywalker", new BigDecimal(22121));

        Response responseMsg = target.path("/api/createAccount").request().post(Entity.json(new Gson().toJson(account)));
        assertEquals("Response is ok", 200, responseMsg.getStatus());
    }

    @Test
    public void createAccountNull() {
        AccountDTO account = new AccountDTO("095512322",null, "Skywalker", new BigDecimal(22121));

        Response responseMsg = target.path("/api/createAccount").request().post(Entity.json(new Gson().toJson(account)));
        assertEquals("Response is not ok", 400, responseMsg.getStatus());
    }

    @Test
    public void transferMoney() {
        List<AccountDTO> resultAccounts = createAccounts();

        BigDecimal subtractAmount = new BigDecimal(123);
        AccountDTO resultAccountsAccount1 = resultAccounts.get(0);
        AccountDTO resultAccountsAccount2 = resultAccounts.get(1);
        Response responseTransfer = target.path("/api/transfer")
                                                .queryParam("from", resultAccountsAccount1.getPhoneNumber())
                                                .queryParam("to", resultAccountsAccount2.getPhoneNumber())
                                                .queryParam("amount", subtractAmount)
                                                 .request().put(Entity.json(""));
        assertEquals("Response is ok", 200, responseTransfer.getStatus());

        assertMoneyTransferred(subtractAmount, resultAccountsAccount1, resultAccountsAccount2);
    }

    private void assertMoneyTransferred(BigDecimal substractAmount, AccountDTO resultAccountsAccount1, AccountDTO resultAccountsAccount2) {
        Response responseAfterTransfer = target.path("/api/getAccounts").request().get();
        List<Account> responseAccountsAfterTransfer = new Gson().fromJson(responseAfterTransfer.readEntity(String.class), new TypeToken<List<Account>>() {}.getType());
        System.out.println(responseAccountsAfterTransfer);

        long countTransferred = responseAccountsAfterTransfer.stream()
            .filter(e -> {
                BigDecimal amountSubstracted1 = resultAccountsAccount1.getAmount().subtract(substractAmount);
                BigDecimal amountSubstracted2 = resultAccountsAccount2.getAmount().add(substractAmount);
                return e.getPhoneNumber().equals(resultAccountsAccount1.getPhoneNumber()) && e.getAmount().compareTo(amountSubstracted1) == 0
                    || e.getPhoneNumber().equals(resultAccountsAccount2.getPhoneNumber()) && e.getAmount().compareTo(amountSubstracted2) == 0;
            }).count();

        assertEquals("Money transferred", 2, countTransferred);
    }

    private List<AccountDTO> createAccounts() {
        AccountDTO account = new AccountDTO("095512322","Luke", "Skywalker", new BigDecimal(22121));
        Response responseAcc1 = target.path("/api/createAccount").request().post(Entity.json(new Gson().toJson(account)));
        assertEquals("Response is ok", 200, responseAcc1.getStatus());


        AccountDTO account2 = new AccountDTO("095512399", "Obi-Wan", "Kenobi", new BigDecimal(222000));
        Response responseAcc2 = target.path("/api/createAccount").request().post(Entity.json(new Gson().toJson(account2)));
        assertEquals("Response is ok", 200, responseAcc2.getStatus());


        Response responseAccounts = target.path("/api/getAccounts").request().get();
        List<AccountDTO> resultAccounts = new Gson().fromJson(responseAccounts.readEntity(String.class), new TypeToken<List<AccountDTO>>() {}.getType());
        System.out.println(resultAccounts);

        long countCreated = resultAccounts.stream()
                                    .filter(e -> e.equals(account) || e.equals(account2))
                                     .count();

        assertEquals("Accounts were created", 2, countCreated);
        return resultAccounts;
    }
}
