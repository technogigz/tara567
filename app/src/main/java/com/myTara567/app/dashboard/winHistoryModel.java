package com.myTara567.app.dashboard;

public class winHistoryModel {
    public String amount;
    public String transaction_note;
    public String tx_request_number;
    public String wining_date;

    public winHistoryModel() {
    }

    public winHistoryModel(String amount, String transaction_note, String tx_request_number, String wining_date) {
        this.amount = amount;
        this.transaction_note = transaction_note;
        this.tx_request_number = tx_request_number;
        this.wining_date = wining_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_note() {
        return transaction_note;
    }

    public void setTransaction_note(String transaction_note) {
        this.transaction_note = transaction_note;
    }

    public String getTx_request_number() {
        return tx_request_number;
    }

    public void setTx_request_number(String tx_request_number) {
        this.tx_request_number = tx_request_number;
    }

    public String getWining_date() {
        return wining_date;
    }

    public void setWining_date(String wining_date) {
        this.wining_date = wining_date;
    }
}
