package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

public class PositionPart implements IPart {

    private int x;
    private int y;
    public int rotation; //0-360 degrees. Ex. 0 can indicate a downwards direction from the top.

    public PositionPart(int x, int y){
	setPos(x, y);
	this.rotation = 0;
    }
    
    public PositionPart(int x, int y, int rotation){
	setPos(x, y);
	this.rotation = Math.min(360, Math.max(0, rotation));
    }
    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }
    
   public void setPos(int x, int y){
       this.x = x;
       this.y = y;
   }
   
   public int getRotation() {
	return rotation;
    }

    public void setRotation(int rotation) {
	this.rotation = Math.min(360, Math.max(0, rotation));
    }
}
