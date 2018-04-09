package com.kag.common.data;

public enum ZIndex {
    TOWER_BASE(30),
    TOWER_TURRET(31),
    TRUMP_TOWER(4),
    GUI_HEALTH_ICON(10),
    GUI_CURRENCY_ICON(10),
    GUI_HEALTH_LABEL(10),
    GUI_CURRENCY_LABEL(10),
    GUI_PLAYER_PANEL(5);




    public final int value;
    private ZIndex(int value){
        this.value = value;
    }

}
