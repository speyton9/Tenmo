package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfers {

    private String transferType;
    private String transferStatus;
    private long fromId;
    private long toId;
    private BigDecimal amount;

    public Transfers(){}

    public Transfers(String transferType, String transferStatus, long fromId, long toId, BigDecimal amount) {
        this.transferType = transferType;
        this.transferStatus = transferStatus;
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "Transfer{" + "transferType=" + transferType + ", transferStatus='" + transferStatus + '\'' + ", fromId='" + fromId + '\''
                + ", toId='" + toId + '\'' + ", amount=" + amount + '}';
    }

}

