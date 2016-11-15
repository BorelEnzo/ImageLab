package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.Constants;
import com.eb.imagelab.model.MyImage;

/**
 * All types of grey scale
 * @author Enzo Borel
 *
 */
public abstract class GreyScaleConversion {
	
	public static void fromColouredToGreyScale(MyImage myImage, boolean vertical, int typeGreyScale){
		if(typeGreyScale < Constants.GREY_SCALE_BT601 || typeGreyScale > Constants.GREY_SCALE_BLUE_CHANNEL){
			typeGreyScale = Constants.GREY_SCALE_AVG;
		}
		float grey;
		float diffRed, diffGreen, diffBlue, x, y, z, invDR, invDG, invDB;
		final float height = (float)myImage.getHeight();
		final float width = (float)myImage.getWidth();
		if (vertical) {
			for(int i = 0; i < myImage.getPixels().length; i++){
				for(int j = 0; j < myImage.getPixels()[i].length; j++){
					grey = (float)getGrey(typeGreyScale, myImage.getPixels()[i][j]);
					diffRed = grey / (float)myImage.getPixels()[i][j].getR();
					diffGreen = grey / (float)myImage.getPixels()[i][j].getG();
					diffBlue = grey / (float)myImage.getPixels()[i][j].getB();
					invDR = 1f / diffRed;
					invDG = 1f / diffGreen;
					invDB = 1f / diffBlue;
					x = invDR + (1 - invDR) * ((float)i / height);
					y = invDG + (1 - invDG) * ((float)i / height);
					z = invDB + (1 - invDB) * ((float)i / height);
					myImage.getPixels()[i][j].setR((int) (((float)myImage.getPixels()[i][j].getR()) * diffRed * x));
					myImage.getPixels()[i][j].setG((int) (((float)myImage.getPixels()[i][j].getG()) * diffGreen * y));
					myImage.getPixels()[i][j].setB((int) (((float)myImage.getPixels()[i][j].getB()) * diffBlue * z));
				}
			}
		}
		else{
			for(int j = 0; j < myImage.getPixels()[0].length; j++){
				for(int i = 0; i < myImage.getPixels().length; i++){
					grey = (float)getGrey(typeGreyScale, myImage.getPixels()[i][j]);
					diffRed = grey / (float)myImage.getPixels()[i][j].getR();
					diffGreen = grey / (float)myImage.getPixels()[i][j].getG();
					diffBlue = grey / (float)myImage.getPixels()[i][j].getB();
					invDR = 1f / diffRed;
					invDG = 1f / diffGreen;
					invDB = 1f / diffBlue;
					x = invDR + (1 - invDR) * ((float)j / width);
					y = invDG +(1 - invDG) * ((float)j / width);
					z = invDB + (1 - invDB) * ((float)j / width);
					myImage.getPixels()[i][j].setR((int) (((float)myImage.getPixels()[i][j].getR()) * diffRed * x));
					myImage.getPixels()[i][j].setG((int) (((float)myImage.getPixels()[i][j].getG()) * diffGreen * y));
					myImage.getPixels()[i][j].setB((int) (((float)myImage.getPixels()[i][j].getB()) * diffBlue * z));
				}
			}
		}
		myImage.update();
	}
	
	/**
	 * Computes the value of the color, according to the type of grey scale and the value of the actual color
	 * @param code the type of grey scale, should be a constant {@link Constants#GREY_SCALE_xxxxxxxxx}
	 * @return the code of the grey (8 bits)
	 */
	private static int getGrey(int code, Colour colour){
		switch(code){
		case Constants.GREY_SCALE_BT709:
			return Math.round(0.2126f * (colour.getR() & 0xFF) + 0.7152f * (colour.getG() & 0xFF) + 0.0722f * (colour.getB() & 0xFF));
		case Constants.GREY_SCALE_BT601:
			return Math.round(0.299f * (colour.getR() & 0xFF) + 0.587f * (colour.getG() & 0xFF) + 0.114f * (colour.getB() & 0xFF));
		case Constants.GREY_SCALE_AVG:
			return (int) ((colour.getR() + colour.getG() + colour.getB()) / 3f);
		case Constants.GREY_SCALE_DESATURATION:
			return (int) ((float)(Math.max(colour.getR(), Math.max(colour.getG(), colour.getB())) + Math.min(colour.getR(), Math.min(colour.getG(), colour.getB()))) / 2f);
		case Constants.GREY_SCALE_DECOMPOSITION_MIN:
			return Math.min(colour.getR(), Math.min(colour.getG(), colour.getB()));
		case Constants.GREY_SCALE_DECOMPOSITION_MAX:
			return Math.max(colour.getR(), Math.max(colour.getG(), colour.getB()));
		case Constants.GREY_SCALE_RED_CHANNEL:
			return colour.getR();
		case Constants.GREY_SCALE_GREEN_CHANNEL:
			return colour.getG();
		case Constants.GREY_SCALE_BLUE_CHANNEL:
			return colour.getB();
		}
		return 0;
	}
	
	public static void toGreyScale(MyImage myImage, int type) {
		if(type < Constants.GREY_SCALE_BT601 || type > Constants.GREY_SCALE_BLUE_CHANNEL){
			type = Constants.GREY_SCALE_AVG;
		}
		int grey;
		for(int i = 0;  i < myImage.getPixels().length; i++){
			for(int j = 0;  j < myImage.getPixels()[i].length; j++){
				grey = getGrey(type, myImage.getPixels()[i][j]);
	            myImage.getPixels()[i][j].setColour((myImage.getPixels()[i][j].getA() << 24) + ((grey << 16) + (grey << 8) + grey));
			}
		}
		myImage.update();
	}	

}
