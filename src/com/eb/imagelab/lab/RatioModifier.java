package com.eb.imagelab.lab;

import com.eb.imagelab.model.MyImage;

/**
 * Used to change color's ratio
 * @author Enzo Borel
 *
 */
public abstract class RatioModifier {
	
	public static void changeAlpha(MyImage myImage, float delta){
		if(delta < 0)delta = 0;
		for(int i = 0; i < myImage.getPixels().length; i++){
			for(int j = 0; j < myImage.getPixels()[i].length; j++){
				myImage.getPixels()[i][j].setA((int) (((float)myImage.getPixels()[i][j].getA()) * delta));
			}
		}
		myImage.update();
	}
	
	public static void changeBrigthness(MyImage myImage, float delta){
		if(delta < 0)delta = 0;
		for(int i = 0; i < myImage.getPixels().length; i++){
			for(int j = 0; j < myImage.getPixels()[i].length; j++){
				myImage.getPixels()[i][j].setR((int) (((float)myImage.getPixels()[i][j].getR()) * delta));
				myImage.getPixels()[i][j].setG((int) (((float)myImage.getPixels()[i][j].getG()) * delta));
				myImage.getPixels()[i][j].setB((int) (((float)myImage.getPixels()[i][j].getB()) * delta));
			}
		}
		myImage.update();
	}
	
	public static void changeRatio(MyImage myImage, float deltaR, float deltaG, float deltaB){
		if(deltaB < 0)deltaB = 0;
		if(deltaG < 0)deltaG = 0;
		if(deltaR < 0)deltaR = 0;
		for(int i = 0; i < myImage.getPixels().length; i++){
			for(int j = 0; j < myImage.getPixels()[i].length; j++){
				myImage.getPixels()[i][j].setR((int) (((float)myImage.getPixels()[i][j].getR()) * deltaR));
				myImage.getPixels()[i][j].setG((int) (((float)myImage.getPixels()[i][j].getG()) * deltaG));
				myImage.getPixels()[i][j].setB((int) (((float)myImage.getPixels()[i][j].getB()) * deltaB));
			}
		}
		myImage.update();
	}
	
	
	public static void changeShades(MyImage myImage, int shades){
		if(shades < 2){
			shades = 2;
		}
		else if(shades > 256){
			shades = 256;
		}
		final float conversion = 255f / ((float)(shades - 1));
		for(int i = 0; i < myImage.getPixels().length; i++){
			for(int j = 0; j < myImage.getPixels()[i].length; j++){
				myImage.getPixels()[i][j].setR((int) (Math.ceil((myImage.getPixels()[i][j].getR() / conversion)) * conversion));
				myImage.getPixels()[i][j].setG((int) (Math.ceil((myImage.getPixels()[i][j].getG() / conversion)) * conversion));
				myImage.getPixels()[i][j].setB((int) (Math.ceil((myImage.getPixels()[i][j].getB() / conversion)) * conversion));
			}
		}
		myImage.update();
	}
	

}
