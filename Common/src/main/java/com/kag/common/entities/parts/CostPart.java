package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

public class CostPart implements IPart {

    private int cost;

    public CostPart(int cost){
        this.cost = cost;
    }

    public void setCost(int newCost){
        this.cost = newCost;
    }

    public int getCost(){
        return cost;
    }
}
