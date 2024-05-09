package com.smell.application.user;

public class Alliance {

    /**
     *
     * @author  FlynnDynamics
     * @version ${version}
     * @since   24/04/24
     */

    private long id;
    private String name;
    private String startDate;
    private String endDate;
    private boolean isSpecial;

    public Alliance(long id, String name, String startDate, String endDate) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Alliance{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isSpecial='" + isSpecial + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }

    public boolean isSpecial() {
        return isSpecial;
    }

    public void setSpecial(boolean special) {
        isSpecial = special;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
