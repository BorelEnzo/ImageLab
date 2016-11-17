package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.MyImage;

/**
 * Contains all methods about gradients.
 * For the grandient "from grey sclale to coloured, @see {@link GreyScaleConversion#fromColouredToGreyScale(MyImage, boolean, int)}
 * @author Enzo
 *
 */
public abstract class Gradient {
	
	public static void applyLinearGradient(MyImage myImage, Colour fromColour, Colour toColour, boolean vertical, int startX, int width, int startY, int height){
		float coeffRed = ((float)fromColour.getR() / 255f);
		float coeffGreen = ((float)fromColour.getG() / 255f);
		float coeffBlue = ((float)fromColour.getB() / 255f);
		float scaleRed, scaleGreen, scaleBlue;
		if(startX < 0)startX = 0;
		if(startX + width > myImage.getWidth()){width = myImage.getWidth() - startX;}
		if(startY < 0)startY = 0;
		if(startY + height > myImage.getHeight()){height = myImage.getHeight() - startY;}
		if(vertical){
			scaleRed = ((float)toColour.getR() - fromColour.getR()) / ((float)height);
			scaleGreen = ((float)toColour.getG() - fromColour.getG()) / ((float)height);
			scaleBlue = ((float)toColour.getB() - fromColour.getB()) / ((float)height);
			for(int i = startY; i < startY + height; i++){
				for(int j = startX; j < startX + width; j++){
					myImage.getPixels()[i][j].setR((int) (((float)myImage.getPixels()[i][j].getR()) * coeffRed));
					myImage.getPixels()[i][j].setG((int) (((float)myImage.getPixels()[i][j].getG()) * coeffGreen));
					myImage.getPixels()[i][j].setB((int) (((float)myImage.getPixels()[i][j].getB()) * coeffBlue));
				}
				coeffBlue = ((float)fromColour.getB() + scaleBlue * (i - startY))/255f;
				coeffGreen = ((float)fromColour.getG() + scaleGreen * (i - startY))/255f;
				coeffRed = ((float)fromColour.getR() + scaleRed * (i - startY))/255f;
			}
		}
		else{
			scaleRed = ((float)toColour.getR() - fromColour.getR()) / ((float)width);
			scaleGreen = ((float)toColour.getG() - fromColour.getG()) / ((float)width);
			scaleBlue = ((float)toColour.getB() - fromColour.getB()) / ((float)width);
			for(int j = startX; j < startX + width; j++){
				for(int i = startY; i < startY + height; i++){
					myImage.getPixels()[i][j].setR((int) (((float)myImage.getPixels()[i][j].getR()) * coeffRed));
					myImage.getPixels()[i][j].setG((int) (((float)myImage.getPixels()[i][j].getG()) * coeffGreen));
					myImage.getPixels()[i][j].setB((int) (((float)myImage.getPixels()[i][j].getB()) * coeffBlue));
					
				}
				coeffBlue = ((float)fromColour.getB() + scaleBlue * (j - startX))/255f;
				coeffGreen = ((float)fromColour.getG() + scaleGreen * (j - startX))/255f;
				coeffRed = ((float)fromColour.getR() + scaleRed * (j - startX))/255f;
			}
		}
		myImage.update();
	}
}
