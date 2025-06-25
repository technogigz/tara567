package com.myTara567.app.mainGames;

public class bidPlacerModel {

    public String digits;
    public String closeDigits;
    public String points;
    public String session;
    public String remainWalletBal;
    public String gameTitle;

    public bidPlacerModel() {
    }

    public bidPlacerModel(String digits, String closeDigits, String points, String session, String remainWalletBal, String gameTitle) {
        this.digits = digits;
        this.closeDigits = closeDigits;
        this.points = points;
        this.session = session;
        this.remainWalletBal = remainWalletBal;
        this.gameTitle = gameTitle;
    }

    public String getDigits() {
        return digits;
    }

    public void setDigits(String digits) {
        this.digits = digits;
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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getRemainWalletBal() {
        return remainWalletBal;
    }

    public void setRemainWalletBal(String remainWalletBal) {
        this.remainWalletBal = remainWalletBal;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }
}
