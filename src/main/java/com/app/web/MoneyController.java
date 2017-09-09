package com.app.web;


import com.app.model.Account;
import com.app.services.AccountService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;


/**
 * Created by Sergey on 06.09.2017.
 */
@Path("/api")
public class MoneyController {

    private AccountService accountService;

    @Inject
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    @GET
    @Path("ping")
    @Produces(MediaType.TEXT_HTML)
    public Response test() {
        String output = "test";
        return Response.status(200).entity(output).build();
    }

    @POST
    @Path("createAccount")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createAccount(Account account) {
        AccountAdapter responseAdapter = new AccountAdapter();
        accountService.create(account, responseAdapter);

        return Response.status(responseAdapter.getStatusCode()).build();
    }

    @GET
    @Path("getAccounts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccounts() {
        return Response.status(200).entity(accountService.getAccounts()).build();
    }

    @PUT
    @Path("transfer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response transfer(@QueryParam("from") String from,
                             @QueryParam("to") String to,
                             @QueryParam("amount") BigDecimal amount) {
        AccountAdapter responseAdapter = new AccountAdapter();
        accountService.transfer(from, to, amount, responseAdapter);
        return Response.status(responseAdapter.getStatusCode()).build();
    }

}
