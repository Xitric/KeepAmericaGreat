package com.kag.common.data;

public enum ZIndex {
    TOWER_OVERLAY(30),
    TOWER_BASE(30),
    TOWER_TURRET(31),
    TOWER_PROJECTILES(32),
    TOWER_PREVIEW(33),
    TOWER_RANGE_PREVIEW(32),
    TRUMP_TOWER(4),
    GUI_HEALTH_ICON(10),
    GUI_CURRENCY_ICON(10),
    GUI_HEALTH_LABEL(10),
    GUI_CURRENCY_LABEL(10),
    GUI_PLAYER_PANEL(5),
    GUI_PANELS(5),
    GUI_BUTTON(6),
    GUI_SELLLABEL(7),
    ENEMY_HATPART(4),
    ENEMY_ANIMATIONPART(3),
    WAVE_IMAGE(10),
    WAVE_COUNTDOWN(10);




    public final int value;
    private ZIndex(int value){
        this.value = value;
    }

}
