package com.myTara567.app.Wallet;


public class TransactionHistModel {
    public String amount;
    public String transaction_type;
    public String transaction_note;
    public String insert_date;
    public String session;
    public String openDigits;
    public String closeDigits;
    public String pana;

    // Default constructor
    public TransactionHistModel() {
    }

    // Parameterized constructor
    public TransactionHistModel(String amount, String transaction_type, String transaction_note, String insert_date, String pana, String session, String openDigits, String closeDigits) {
        this.amount = amount;
        this.transaction_type = transaction_type;
        this.transaction_note = transaction_note;
        this.insert_date = insert_date;
        this.pana = pana;
        this.session = session;
        this.openDigits = openDigits;
        this.closeDigits = closeDigits;
    }

    // Getters and Setters
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(String transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getTransaction_note() {
        return transaction_note;
    }

    public void setTransaction_note(String transaction_note) {
        this.transaction_note = transaction_note;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getOpenDigits() {
        return openDigits;
    }

    public void setOpenDigits(String openDigits) {
        this.openDigits = openDigits;
    }

    public String getCloseDigits() {
        return closeDigits;
    }

    public void setCloseDigits(String closeDigits) {
        this.closeDigits = closeDigits;
    }

    public String getPana() {
        return pana;
    }

    public void setPana(String pana) {
        this.pana = pana;
    }
}
