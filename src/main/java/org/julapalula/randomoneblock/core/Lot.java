package org.julapalula.randomoneblock.core;

import org.bukkit.Material;

import java.util.List;

public class Lot {
    private String lotName;
    private int score;
    private List<Material> materials;

    // Getters and setters

    public String getLotName() {
        return lotName;
    }
    public void setLotName(String name) {
        this.lotName = name;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public List<Material> getMaterials() {
        return materials;
    }
    public void setMaterials(List<Material> materials) {
        this.materials = materials;
    }
    @Override
    public String toString() {
        return lotName+" Lot{" +
                "score=" + score +
                ", materials=" + materials +
                '}';
    }
}
