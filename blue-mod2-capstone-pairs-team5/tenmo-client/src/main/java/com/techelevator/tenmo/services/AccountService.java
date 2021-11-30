package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.baseUrl = url;
    }

    private String authToken = null;

    public void setAuthToken(String authToken) {this.authToken = authToken;}

    public BigDecimal getBalance(AuthenticatedUser authenticatedUser) {
        BigDecimal balance = null;
        setAuthToken(authenticatedUser.getToken());
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(baseUrl + "accounts/" + authenticatedUser.getUser().getId(), HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            ex.getMessage();
        }
        return balance;
    }

    public String[] usersList (AuthenticatedUser authenticatedUser){
        String[] users = null;
        setAuthToken(authenticatedUser.getToken());
        try {
            ResponseEntity<String[]> response = restTemplate.exchange(baseUrl + "users", HttpMethod.GET, makeAuthEntity(), String[].class);
            users = response.getBody();
        } catch (ResourceAccessException | RestClientResponseException ex) {
            System.out.println(ex.getMessage());
        }
        return users;
    }

    public Transfers transfers(Transfers newTransfer) {
        HttpEntity<Transfers> entity = makeTransferEntity(newTransfer);
//        Transfers returnedTransfer = null;
        try {
            newTransfer = restTemplate.postForObject(baseUrl + "transfers", entity, Transfers.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            System.out.println(e.getMessage());
        }
        return newTransfer;
    }

    public Transfers transfersInfo(AuthenticatedUser authenticatedUser, Long transferId) {
        Transfers view = null;
        setAuthToken(authenticatedUser.getToken());
        try {
            ResponseEntity<Transfers> response =
                    restTemplate.exchange(baseUrl + "transfers/info/" + transferId, HttpMethod.GET, makeAuthEntity(), Transfers.class);
            view = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            ex.getMessage();
        }
        return view;
    }

    public String matchUser(AuthenticatedUser authenticatedUser, Long accountId) {
        String name = "";
        setAuthToken(authenticatedUser.getToken());
        try {
            ResponseEntity<String> response =  restTemplate.exchange(baseUrl + "username/" + accountId, HttpMethod.GET, makeAuthEntity(), String.class);
            name = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException ex) {
            ex.getMessage();
        }
        return name;
    }

    public String[] transferList (AuthenticatedUser authenticatedUser) {
        String[] transfers = null;
        setAuthToken(authenticatedUser.getToken());
        try {
            ResponseEntity<String[]> response = restTemplate.exchange(baseUrl + "transfers/history/" + authenticatedUser.getUser().getId(),
                    HttpMethod.GET, makeAuthEntity(), String[].class);
            transfers = response.getBody();
        } catch (ResourceAccessException | RestClientResponseException ex) {
            System.out.println(ex.getMessage());
        }
        return transfers;
    }

    private HttpEntity<Transfers> makeTransferEntity(Transfers transfers) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfers, headers);
    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

//    @java.lang.Override
//    public java.lang.String toString() {
//        return
//    }
}
