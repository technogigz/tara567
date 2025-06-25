package com.myTara567.app.dashboard.allbis.ui.wallet.response;

public class deposit_history_model {
    public String amount;
    public String transaction_type;
    public String transaction_note;
    public String insert_date;

    public deposit_history_model() {
    }

    public deposit_history_model(String amount, String transaction_type, String transaction_note, String insert_date) {
        this.amount = amount;
        this.transaction_type = transaction_type;
        this.transaction_note = transaction_note;
        this.insert_date = insert_date;
    }

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
}
