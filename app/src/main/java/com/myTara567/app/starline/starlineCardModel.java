package com.myTara567.app.starline;

import java.io.Serializable;

public class starlineCardModel implements Serializable {
    String gameName;
    String openTime;
    String closeTime;
    String openResult;
    String closeResult;
    String marketStatus;
    String market;
    String gameId;

    public starlineCardModel() {
    }

    public starlineCardModel(String gameName, String openTime,
                             String closeTime, String openResult,
                             String closeResult, String marketStatus, String market, String gameId) {
        this.gameName = gameName;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.openResult = openResult;
        this.closeResult = closeResult;
        this.market = market;
        this.marketStatus = marketStatus;
        this.gameId = gameId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getMarketStatus() {
        return marketStatus;
    }

    public void setMarketStatus(String marketStatus) {
        this.marketStatus = marketStatus;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getOpenResult() {
        return openResult;
    }

    public void setOpenResult(String openResult) {
        this.openResult = openResult;
    }

    public String getCloseResult() {
        return closeResult;
    }

    public void setCloseResult(String closeResult) {
        this.closeResult = closeResult;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
