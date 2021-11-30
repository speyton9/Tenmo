package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.security.jwt.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private UserDao userDao;
    private AccountDao accountDao;

    public AccountController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserDao userDao, AccountDao accountDao) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDao = userDao;
        this.accountDao = accountDao;
    }

    @RequestMapping(value = "/accounts/{userId}", method = RequestMethod.GET)
    public BigDecimal getBalance(@PathVariable Long userId) {
            return accountDao.getBalance(userId);
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public List<String> listUsers() {
        return accountDao.listUsers();
    }

//    @RequestMapping(value = "/usersId", method = RequestMethod.GET)
//    public List<Long> listId() {
//        return accountDao.listId();
//    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "/transfers", method = RequestMethod.POST)
    public Transfers transfers (@Valid @RequestBody Transfers transfers) {
        accountDao.transfers(transfers);
    return transfers;}


    @RequestMapping(value = "/transfers/history/{userId}", method = RequestMethod.GET)
    public List<String> listTransfers(@PathVariable Long userId) {
        return accountDao.listTransfers(userId);
    }

    @RequestMapping(value = "/transfers/info/{transferId}", method = RequestMethod.GET)
    public Transfers transferInfo(@PathVariable Long transferId) {
        return accountDao.transferInfo(transferId);
    }

    @RequestMapping(value = "/username/{accountId}", method = RequestMethod.GET)
    public String getUsername(@PathVariable Long accountId) {
        return accountDao.getUsername(accountId);
    }
}
