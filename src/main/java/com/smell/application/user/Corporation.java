package com.smell.application.user;

import java.util.List;

public class Corporation {

    /**
     *
     * @author  FlynnDynamics
     * @version 0.x
     * @since   24/04/24
     */

    private long id;
    private String name;
    private String playerStartDate;
    private String playerEndDate;
    private List<Alliance> alliances;
    private boolean isSpecial;

    public Corporation(long id, String name, String playerStartDate, String playerEndDate, List<Alliance> alliances) {
        this.id = id;
        this.name = name;
        this.playerStartDate = playerStartDate;
        this.playerEndDate = playerEndDate;
        this.alliances = alliances;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Corporation{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", isSpecial='").append(isSpecial).append('\'');
        sb.append(", playerStartDate='").append(playerStartDate).append('\'');
        sb.append(", playerEndDate='").append(playerEndDate).append('\'');
        sb.append(", alliances=[");
        for (Alliance alliance : alliances) {
            sb.append(alliance.toString()).append(", ");
        }
        if (!alliances.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove the last comma and space
        }
        sb.append("]");
        sb.append('}');
        return sb.toString();
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

    public String getPlayerStartDate() {
        return playerStartDate;
    }

    public void setPlayerStartDate(String playerStartDate) {
        this.playerStartDate = playerStartDate;
    }

    public String getPlayerEndDate() {
        return playerEndDate;
    }

    public void setPlayerEndDate(String playerEndDate) {
        this.playerEndDate = playerEndDate;
    }

    public List<Alliance> getAlliances() {
        return alliances;
    }

    public void setAlliances(List<Alliance> alliances) {
        this.alliances = alliances;
    }
}
