package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    BigDecimal getBalance(long id);

    List<String> listUsers();

    List<String> listTransfers(long id);

    String getUsername(long id);

    boolean transfers(Transfers transfers);

    Transfers transferInfo(Long transferId);
}
