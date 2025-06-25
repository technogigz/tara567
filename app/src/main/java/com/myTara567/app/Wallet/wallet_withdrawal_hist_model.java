package com.myTara567.app.Wallet;

public class wallet_withdrawal_hist_model {
    String request_number;
    String request_amount;
    String request_status;
    String payment_method;
    String remark;
    String insert_date;
    String payment_receipt;
    String fund_status;

    public wallet_withdrawal_hist_model() {
    }

    public wallet_withdrawal_hist_model(String request_number, String request_amount, String insert_date) {
        this.request_number = request_number;
        this.request_amount = request_amount;
        this.insert_date = insert_date;
//        this.request_status = request_status;
//        this.payment_method = payment_method;
//        this.remark = remark;
//        this.payment_receipt = payment_receipt;
//        this.fund_status = fund_status;
    }

    public String getRequest_number() {
        return request_number;
    }

    public void setRequest_number(String Request_number) {
        this.request_number = Request_number;
    }

    public String getRequest_amount() {
        return request_amount;
    }

    public void setRequest_amount(String request_amount) {
        this.request_amount = request_amount;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getPayment_receipt() {
        return payment_receipt;
    }

    public void setPayment_receipt(String payment_receipt) {
        this.payment_receipt = payment_receipt;
    }

    public String getFund_status() {
        return fund_status;
    }

    public void setFund_status(String fund_status) {
        this.fund_status = fund_status;
    }
}
