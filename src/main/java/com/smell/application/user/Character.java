package com.smell.application.user;

import java.util.List;

public class Character {

    /**
     *
     * @author  FlynnDynamics
     * @version 0.x
     * @since   24/04/24
     */

    private long id;
    private String name;
    private List<Corporation> corporations;

    public Character(long id, String name, List<Corporation> corporations) {
        this.id = id;
        this.name = name;
        this.corporations = corporations;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Character{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", corporations=[");
        for (Corporation corporation : corporations) {
            sb.append(corporation.toString()).append(", ");
        }
        if (!corporations.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append("]");
        sb.append('}');
        return sb.toString();
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

    public List<Corporation> getCorporations() {
        return corporations;
    }

    public void setCorporations(List<Corporation> corporations) {
        this.corporations = corporations;
    }
}
