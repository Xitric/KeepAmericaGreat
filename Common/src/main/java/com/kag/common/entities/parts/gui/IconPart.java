/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.entities.parts.gui;

import com.kag.common.data.IAsset;
import com.kag.common.entities.IPart;

/**
 *
 * @author niels
 */
public class IconPart implements IPart{

    private IAsset asset;

    public IconPart(IAsset asset) {
        this.asset = asset;
    }

    public IAsset getAsset() {
        return asset;
    }

    public void setAsset(IAsset asset) {
        this.asset = asset;
    }

}
