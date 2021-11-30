package com.techelevator.tenmo.model;

public class UserAccount {

    private Long id;
    private String username;
    private Long accountId;

    public UserAccount() {

    }

    public UserAccount(Long id, String username, Long accountId) {
        this.id = id;
        this.username = username;
        this.accountId = accountId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

}
