package com.kag.common.entities.parts;

import com.kag.common.entities.IPart;

public class NamePart implements IPart {

    private String name;

    public NamePart(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
