package com.myTara567.app.dashboard;

public class bidHistoryModel {

    public String game_name;
    public String pana;
    public String session;
    public String openDigits;
    public String closeDigits;
    public String points;
    public String bid_date;


    public bidHistoryModel() {
    }

    public bidHistoryModel(String game_name, String pana,
                           String session, String openDigits,
                           String closeDigits, String points, String bid_date) {
        this.game_name = game_name;
        this.pana = pana;
        this.session = session;
        this.openDigits = openDigits;
        this.closeDigits = closeDigits;
        this.points = points;
        this.bid_date = bid_date;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getPana() {
        return pana;
    }

    public void setPana(String pana) {
        this.pana = pana;
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

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getBid_date() {
        return bid_date;
    }

    public void setBid_date(String bid_date) {
        this.bid_date = bid_date;
    }
}
