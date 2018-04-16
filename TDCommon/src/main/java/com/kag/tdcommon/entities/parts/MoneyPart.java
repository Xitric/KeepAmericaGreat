package com.kag.tdcommon.entities.parts;

import com.kag.common.entities.IPart;

public class MoneyPart implements IPart {

    private int money;

    public MoneyPart(int money){
        this.money = money;
    }

    public void setMoney(int newMoney){
        this.money = newMoney;
    }

    public int getMoney(){
        return money;
    }
}
