package com.hindbyte.redfun.matches;

public class MatchModel {
    private String id;
    private String teamA;
    private String flagA;
    private String teamB;
    private String flagB;
    private String series;
    private String time;
    private String status;
    private int viewType;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTeamA() {
        return teamA;
    }

    public void setTeamA(String teamA) {
        this.teamA = teamA;
    }

    public String getFlagA() {
        return flagA;
    }

    public void setFlagA(String flagA) {
        this.flagA = flagA;
    }

    public String getTeamB() {
        return teamB;
    }

    public void setTeamB(String teamB) {
        this.teamB = teamB;
    }

    public String getFlagB() {
        return flagB;
    }

    public void setFlagB(String flagB) {
        this.flagB = flagB;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
