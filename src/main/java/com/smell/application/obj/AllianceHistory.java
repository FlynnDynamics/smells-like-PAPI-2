package com.smell.application.obj;

public class AllianceHistory {

    /**
     *
     * @author  FlynnDynamics
     * @version 0.x
     * @since   24/04/24
     */

    private int alliance_id;
    private int record_id;
    private String start_date;

    @Override
    public String toString() {
        return "AllianceHistory{" +
                "alliance_id=" + alliance_id +
                ", record_id=" + record_id +
                ", start_date='" + start_date + '\'' +
                '}';
    }

    public int getRecord_id() {
        return record_id;
    }

    public String getStart_date() {
        return start_date;
    }

    public int getAlliance_id() {
        return alliance_id;
    }


}
