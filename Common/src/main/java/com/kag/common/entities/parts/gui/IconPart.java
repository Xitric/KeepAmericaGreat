/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.entities.parts.gui;

/**
 *
 * @author niels
 */
public class IconPart {
    
    public String path;
    
    public IconPart(String path){
        this.path = path;
    }
    
    public String getPath(){
        return path;
    }
    
    public void setPath(String path){
        this.path = path;
    }
    
}
