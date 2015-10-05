package com.mygdx.game;

import com.badlogic.gdx.Input;

/**
 * Created by 0701AND on 2015/09/18.
 */
public class KeyInput {
	private boolean isUP	 = false;
	private boolean isDOWN	= false;
	private boolean isLEFT	= false;
	private boolean isRIGHT = false;
	private boolean isNUM1	 = false;
	private boolean isNUM2	 = false;

	public boolean isPressing(int iKeyCode) {
		switch (iKeyCode) {
			case Input.Keys.UP:
				return isUP;
			case Input.Keys.DOWN:
				return isDOWN;
			case Input.Keys.LEFT:
				return isLEFT;
			case Input.Keys.RIGHT:
				return isRIGHT;
			case Input.Keys.NUM_1:
				return isNUM1;
			case Input.Keys.NUM_2:
				return isNUM2;
		}
		return false;
	}
	public void keyPressed(int iKeyCode) {
		switch (iKeyCode) {
			case Input.Keys.UP:
				isUP = true;
				break;
			case Input.Keys.DOWN:
				isDOWN = true;
				break;
			case Input.Keys.LEFT:
				isLEFT = true;
				break;
			case Input.Keys.RIGHT:
				isRIGHT = true;
				break;
			case Input.Keys.NUM_1:
				isNUM1 = true;
				break;
			case Input.Keys.NUM_2:
				isNUM2 = true;
				break;
		}
	}
	public void keyReleased(int iKeyCode) {
		switch (iKeyCode) {
			case Input.Keys.UP:
				isUP = false;
				break;
			case Input.Keys.DOWN:
				isDOWN = false;
				break;
			case Input.Keys.LEFT:
				isLEFT = false;
				break;
			case Input.Keys.RIGHT:
				isRIGHT = false;
				break;
			case Input.Keys.NUM_1:
				isNUM1 = false;
				break;
			case Input.Keys.NUM_2:
				isNUM2 = false;
				break;
		}
	}
}
