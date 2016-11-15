package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.MyImage;

/**
 * Contains all methods about filters applied on pictures (except gray scale @see {@link GreyScaleConversion})
 * @author Enzo Borel
 *
 */
public abstract class Filter {
	
	public static void applyColoredFilter(MyImage myImage, Colour colour){
		final float coeffRed = ((float)colour.getR() / 255f);
		final float coeffGreen = ((float)colour.getG() / 255f);
		final float coeffBlue = ((float)colour.getB() / 255f);
		for(int i = 0;  i < myImage.getPixels().length; i++){
			for(int j = 0;  j < myImage.getPixels()[i].length; j++){
				myImage.getPixels()[i][j].setR((int) (((float)myImage.getPixels()[i][j].getR()) * coeffRed));
				myImage.getPixels()[i][j].setG((int) (((float)myImage.getPixels()[i][j].getG()) * coeffGreen));
				myImage.getPixels()[i][j].setB((int) (((float)myImage.getPixels()[i][j].getB()) * coeffBlue));
			}
		}
		myImage.update();
	}
	

}
