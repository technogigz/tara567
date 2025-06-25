package com.myTara567.app.dashboard;

import java.io.Serializable;

public class gameDataModelAAAAAAAA implements Serializable {

    String game_name;
    String openTime;
    String closeTime;
    String openResult;
    String closeResult;
    String webChartUrl;
    String time_srt;
    String msg_status;
    String game_id;
    String marketMessage;
    String betting_status;

    public gameDataModelAAAAAAAA() {
    }

    public gameDataModelAAAAAAAA(String game_name, String openTime,
                                 String closeTime, String openResult,
                                 String closeResult, String marketMessage,
                                 String webChartUrl, String time_srt,
                                 String msg_status, String game_id, String betting_status) {
        this.game_name = game_name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.openResult = openResult;
        this.closeResult = closeResult;
        this.webChartUrl = webChartUrl;
        this.marketMessage = marketMessage;
        this.time_srt = time_srt;
        this.msg_status = msg_status;
        this.game_id = game_id;
        this.betting_status = betting_status;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getWebChartUrl() {
        return webChartUrl;
    }

    public void setWebChartUrl(String webChartUrl) {
        this.webChartUrl = webChartUrl;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
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

    public String getMarketMessage() {
        return marketMessage;
    }

    public void setMarketMessage(String marketMessage) {
        this.marketMessage = marketMessage;
    }

    public String getTime_srt() {
        return time_srt;
    }

    public void setTime_srt(String time_srt) {
        this.time_srt = time_srt;
    }

    public String getMsg_status() {
        return msg_status;
    }

    public void setMsg_status(String msg_status) {
        this.msg_status = msg_status;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getBetting_status() {
        return betting_status;
    }

    public void setBetting_status(String betting_status) {
        this.betting_status = betting_status;
    }
}
