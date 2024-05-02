package com.smell.application.obj;

public class CorporationHistory {

    /**
     *
     * @author  FlynnDynamics
     * @version 0.x
     * @since   24/04/24
     */

    private int corporation_id;
    private boolean is_deleted;
    private int record_id;
    private String start_date;

    @Override
    public String toString() {
        return "CorporationHistory{" +
                "corporation_id=" + corporation_id +
                ", is_deleted=" + is_deleted +
                ", record_id=" + record_id +
                ", start_date='" + start_date + '\'' +
                '}';
    }

    public int getCorporation_id() {
        return corporation_id;
    }

    public boolean isIs_deleted() {
        return is_deleted;
    }

    public int getRecord_id() {
        return record_id;
    }

    public String getStart_date() {
        return start_date;
    }
}
