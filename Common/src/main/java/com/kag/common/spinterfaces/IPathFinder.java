/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

import com.kag.common.data.Node;
import com.kag.common.data.World;

/**
 *
 * @author andre
 */
public interface IPathFinder {
    Node getPath(int startX, int startY, int endX, int endY, World world);
    Node[][] getPath(int endX, int endY, World world);
}
