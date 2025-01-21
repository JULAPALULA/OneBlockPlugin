package org.julapalula.randomoneblock.playerinfo;
import org.julapalula.randomoneblock.core.Lot;

import java.util.ArrayList;

public class PlayerData {
    private String UUID;
    private int score;
    private ArrayList<Lot> enabled_lot_list;
    private ArrayList<Lot> lot_list;

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
        return enabled_lot_list;
    }
    public void setEnabledLots(ArrayList<Lot> enabled_lot_list) {
        this.enabled_lot_list = enabled_lot_list;
    }
    public ArrayList<Lot> getLotPool() {
       return lot_list;
    }
    public void setLotPool(ArrayList<Lot> lot_list) {
        this.lot_list = lot_list;
    }
}
