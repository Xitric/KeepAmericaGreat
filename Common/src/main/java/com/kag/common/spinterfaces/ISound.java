/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kag.common.spinterfaces;

/**
 *
 * @author andre
 */
public interface ISound {

	void playSound(String filepath);

	void playRepeatingSound(String filepath);

	void stopSound(String filepath);

	void stopAllSounds();
}
