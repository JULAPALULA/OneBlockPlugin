package org.julapalula.oneblockplugin.playerinfo;
import org.julapalula.oneblockplugin.core.Lot;

import java.util.ArrayList;

public class PlayerData {
    private String UUID;
    private int score;
    private ArrayList<Lot> lotList;

    // Getters and setters
    public String getUUID() {
        return UUID;
    }
    public void setUUID(String name) {
        this.UUID = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public ArrayList<Lot> getEnabledLots() {
        return lotList;
    }
    public void setEnabledLots(ArrayList<Lot> lotList) {
        this.lotList = lotList;
    }
}
