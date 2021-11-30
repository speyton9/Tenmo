package com.techelevator.tenmo.dao;


import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;
    private Transfers transfers;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public BigDecimal getBalance(long userId) {
        String sql = "SELECT balance FROM accounts WHERE user_id = ?;";
        if (!Long.valueOf(userId).equals(null)) {
            BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
            return balance;
        } else {
            return null;
        }
    }

    @Override
    public List<String> listUsers() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT u.user_id, username, account_id FROM users u JOIN accounts a ON a.user_id = u.user_id";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            UserAccount user = mapRowToUser(results);
            users.add(user.getUsername() + " " + user.getId().toString());
        }
        return users;
    }

    @Override
    public List<String> listTransfers(long userId) {
        List<String> transferList = new ArrayList<>();
        String sql = "SELECT t.transfer_id, transfer_type_desc, transfer_status_desc, t.account_from, t.account_to, amount FROM transfers t \n" +
                "JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id \n" +
                "JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id \n" +
                "JOIN accounts a ON a.account_id = t.account_to \n" +
                "JOIN users u ON u.user_id = a.user_id \n" +
                "WHERE account_from = (SELECT account_id FROM accounts a JOIN users u ON u.user_id = a.user_id WHERE u.user_id = ?) OR \n" +
                "account_to = (SELECT account_id FROM accounts a JOIN users ON u.user_id = a.user_id WHERE u.user_id = ?)";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId);
        while (results.next()) {
            Transfers transfer = mapRowToTransfer(results);
            if(userId == transfer.getFromId()) {
            transferList.add(transfer.getTransferId() + " " + transfer.getTransferType() + ": " +
                    getUsername(transfer.getFromId()) + " $" + transfer.getAmount());
            } else {
                transferList.add(transfer.getTransferId() + " " + transfer.getTransferType() + ": " +
                        getUsername(transfer.getToId()) + " $" + transfer.getAmount());
            }
        }
        return transferList;
    }

    @Override
    public boolean transfers(Transfers transfers) {
        String sql = "INSERT INTO transfers VALUES (DEFAULT, (SELECT transfer_type_id FROM transfer_types WHERE transfer_type_desc = ?), \n" +
                "(SELECT transfer_status_id FROM transfer_statuses WHERE transfer_status_desc = ?),\n" +
                "(SELECT account_id FROM accounts WHERE user_id = ?), (SELECT account_id FROM accounts WHERE user_id = ?),\n" +
                "?);";
        String sql2 = "UPDATE accounts SET balance = (balance + ?) WHERE user_id = ?;\n" +
                "UPDATE accounts SET balance = (balance - ?) WHERE user_id = ?;";
        try {
            if (getBalance(transfers.getFromId()).compareTo(transfers.getAmount()) >= 0) {
                jdbcTemplate.update(sql, transfers.getTransferType(), transfers.getTransferStatus(), transfers.getFromId(),
                        transfers.getToId(), transfers.getAmount());
                jdbcTemplate.update(sql2, transfers.getAmount(), transfers.getToId(), transfers.getAmount(), transfers.getFromId());
                return true;
            }
        } catch (ResourceAccessException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
        return false;
    }

    @Override
    public Transfers transferInfo(Long transferId) {
        String sql = "SELECT t.transfer_id, transfer_type_desc, transfer_status_desc, t.account_from, t.account_to, amount FROM transfers t\n" +
                "JOIN transfer_types tt ON tt.transfer_type_id = t.transfer_type_id\n" +
                "JOIN transfer_statuses ts ON ts.transfer_status_id = t.transfer_status_id\n" +
                "JOIN accounts a ON a.account_id = t.account_to\n" +
                "JOIN users u ON u.user_id = a.user_id\n" +
                "WHERE transfer_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        while (results.next()) {
            Transfers transfer = mapRowToTransfer(results);
            return transfer;
        } return null;

//        Transfers transfers = jdbcTemplate.queryForObject(sql, Transfers.class, transferId);
//        return transfers;
    }

    @Override
    public String getUsername(long id) {
        String sql = "SELECT username FROM users u " +
                "JOIN accounts a ON a.user_id = u.user_id " +
                "WHERE account_id = ?";
        try {
            String username = jdbcTemplate.queryForObject(sql, String.class, id);
            return username;
        } catch (ResourceAccessException ex) {
            ex.getMessage();
        }
        return null;
    }

    private UserAccount mapRowToUser(SqlRowSet rs) {
        UserAccount user = new UserAccount();
        user.setId(rs.getLong("user_id"));
        user.setUsername(rs.getString("username"));
        user.setAccountId(rs.getLong("account_id"));
        return user;
    }

    private Transfers mapRowToTransfer(SqlRowSet rs) {
        Transfers transfers = new Transfers();
        transfers.setTransferId(rs.getLong("transfer_id"));
        transfers.setTransferType(rs.getString("transfer_type_desc"));
        transfers.setTransferStatus(rs.getString("transfer_status_desc"));
        transfers.setFromId(rs.getLong("account_from"));
        transfers.setToId(rs.getLong("account_to"));
        transfers.setAmount(rs.getBigDecimal("amount"));
        return transfers;
    }
}
