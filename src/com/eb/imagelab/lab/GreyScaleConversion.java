package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.EnumGreyScale;
import com.eb.imagelab.model.MyImage;

/**
 * All types of grey scale
 * @author Enzo Borel
 *
 */
public abstract class GreyScaleConversion {
	
	public static void fromColouredToGreyScale(MyImage myImage, boolean vertical, boolean startWithGreyScale, EnumGreyScale typeGreyScale){
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
					if(!startWithGreyScale){
						x = invDR + (1 - invDR) * ((float)i / height);
						y = invDG + (1 - invDG) * ((float)i / height);
						z = invDB + (1 - invDB) * ((float)i / height);
					}
					else{
						x = invDR + (1 - invDR) * ((float)(height - i) / height);
						y = invDG + (1 - invDG) * ((float)(height - i) / height);
						z = invDB + (1 - invDB) * ((float)(height - i) / height);
					}
					
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
					if(!startWithGreyScale){
						x = invDR + (1 - invDR) * ((float)j / width);
						y = invDG +(1 - invDG) * ((float)j / width);
						z = invDB + (1 - invDB) * ((float)j / width);
					}
					else{
						x = invDR + (1 - invDR) * ((float)(width - j) / width);
						y = invDG +(1 - invDG) * ((float)(width - j) / width);
						z = invDB + (1 - invDB) * ((float)(width - j) / width);
					}
					myImage.getPixels()[i][j].setR((int) (((float)myImage.getPixels()[i][j].getR()) * diffRed * x));
					myImage.getPixels()[i][j].setG((int) (((float)myImage.getPixels()[i][j].getG()) * diffGreen * y));
					myImage.getPixels()[i][j].setB((int) (((float)myImage.getPixels()[i][j].getB()) * diffBlue * z));
				}
			}
		}
		myImage.update();
	}
	
	private static int getGrey(EnumGreyScale code, Colour colour){
		switch(code){
		case GREY_SCALE_BT709:
			return Math.round(0.2126f * (colour.getR() & 0xFF) + 0.7152f * (colour.getG() & 0xFF) + 0.0722f * (colour.getB() & 0xFF));
		case GREY_SCALE_BT601:
			return Math.round(0.299f * (colour.getR() & 0xFF) + 0.587f * (colour.getG() & 0xFF) + 0.114f * (colour.getB() & 0xFF));
		case GREY_SCALE_AVG:
			return (int) ((colour.getR() + colour.getG() + colour.getB()) / 3f);
		case GREY_SCALE_DESATURATION:
			return (int) ((float)(Math.max(colour.getR(), Math.max(colour.getG(), colour.getB())) + Math.min(colour.getR(), Math.min(colour.getG(), colour.getB()))) / 2f);
		case GREY_SCALE_DECOMPOSITION_MIN:
			return Math.min(colour.getR(), Math.min(colour.getG(), colour.getB()));
		case GREY_SCALE_DECOMPOSITION_MAX:
			return Math.max(colour.getR(), Math.max(colour.getG(), colour.getB()));
		case GREY_SCALE_RED_CHANNEL:
			return colour.getR();
		case GREY_SCALE_GREEN_CHANNEL:
			return colour.getG();
		case GREY_SCALE_BLUE_CHANNEL:
			return colour.getB();
		}
		return 0;
	}
	
	public static void toGreyScale(MyImage myImage, EnumGreyScale type) {
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
