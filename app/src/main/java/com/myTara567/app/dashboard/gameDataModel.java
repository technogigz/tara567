package com.myTara567.app.dashboard;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class gameDataModel implements Serializable {

    private String game_name;
    private String openTime;
    private String closeTime;
    private String openResult;
    private String closeResult;
    private String webChartUrl;
    private String webChartUrlPana;
    private String time_srt;
    private String msg_status;
    private String game_id;
    private String marketMessage;
    private String betting_status;

    // Default Constructor
    public gameDataModel() {
    }

    // Constructor with all fields
    public gameDataModel(String game_name, String openTime,
                         String closeTime, String openResult,
                         String closeResult, String marketMessage,
                         String webChartUrl, String webChartUrlPana,
                         String time_srt, String msg_status,
                         String game_id, String betting_status) {
        this.game_name = game_name;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.openResult = openResult;
        this.closeResult = closeResult;
        this.webChartUrl = webChartUrl;
        this.webChartUrlPana = webChartUrlPana;
        this.marketMessage = marketMessage;
        this.time_srt = time_srt;
        this.msg_status = msg_status;
        this.game_id = game_id;
        this.betting_status = betting_status;
    }

    // Getters and Setters
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

    public String getWebChartUrl() {
        return webChartUrl;
    }

    public void setWebChartUrl(String webChartUrl) {
        this.webChartUrl = webChartUrl;
    }

    public String getWebChartUrlPana() {
        return webChartUrlPana;
    }

    public void setWebChartUrlPana(String webChartUrlPana) {
        this.webChartUrlPana = webChartUrlPana;
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

    public String getMarketMessage() {
        return marketMessage;
    }

    public void setMarketMessage(String marketMessage) {
        this.marketMessage = marketMessage;
    }

    public String getBetting_status() {
        return betting_status;
    }

    public void setBetting_status(String betting_status) {
        this.betting_status = betting_status;
    }

    // JSON Parsing Example
    public static gameDataModel fromJson(String jsonResponse) {
        gameDataModel dataModel = new gameDataModel();
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);

            dataModel.setWebChartUrl(jsonObject.optString("web_chart_url"));
            dataModel.setWebChartUrlPana(jsonObject.optString("web_chart_url_pana"));
            dataModel.setGame_name(jsonObject.optString("game_name"));
            dataModel.setOpenTime(jsonObject.optString("openTime"));
            dataModel.setCloseTime(jsonObject.optString("closeTime"));
            dataModel.setOpenResult(jsonObject.optString("openResult"));
            dataModel.setCloseResult(jsonObject.optString("closeResult"));
            dataModel.setMarketMessage(jsonObject.optString("marketMessage"));
            dataModel.setTime_srt(jsonObject.optString("time_srt"));
            dataModel.setMsg_status(jsonObject.optString("msg_status"));
            dataModel.setGame_id(jsonObject.optString("game_id"));
            dataModel.setBetting_status(jsonObject.optString("betting_status"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataModel;
    }
}
