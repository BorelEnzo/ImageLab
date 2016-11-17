package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.EnumRotation;
import com.eb.imagelab.model.MyImage;

/**
 * Contains all methods about picture formatting : crop, rotate, flip etc.
 * @author Enzo
 *
 */
public abstract class ImageFormatter {
	
	
	public static MyImage crop(MyImage myImage, int startX, int width, int startY, int height){
		if(startX < 0)startX = 0;
		if(startX + width > myImage.getWidth()){width = myImage.getWidth() - startX;}
		if(startY < 0)startY = 0;
		if(startY + height > myImage.getHeight()){height = myImage.getHeight() - startY;}
		MyImage newImage = new MyImage(width, height, myImage.getType());
		for(int i = startY; i < startY + height; i++){
			for(int j = startX; j < startX + width; j++){
				newImage.getPixels()[i - startY][j - startX] = myImage.getPixels()[i][j];
			}
		}
		newImage.update();
		return newImage;
	}
	
	public static void flip(MyImage myImage, boolean vertical){
		Colour pixelsClone[][] = Utils.deepCopyColoursArray(myImage.getPixels());
		if(!vertical){
			for(int i = 0; i < myImage.getPixels().length; i++){
				for(int j = 0;  j < myImage.getPixels()[i].length; j++){
					myImage.getPixels()[i][j] = pixelsClone[i][pixelsClone[i].length -1 - j];
				}
			}
		}
		else{
			for(int i = 0; i < myImage.getPixels().length; i++){
				for(int j = 0;  j < myImage.getPixels()[i].length; j++){
					myImage.getPixels()[i][j] = pixelsClone[pixelsClone.length - 1 -i][j];
				}
			}
		}
		myImage.update();
	}

	public static void paste(MyImage myImage, MyImage destinationImage, int x, int y){
		for(int i = y; i < myImage.getPixels().length; i++){
			for(int j = x; j < myImage.getPixels()[i].length; j++){
				destinationImage.getPixels()[i][j] = myImage.getPixels()[i - y][j - x];
			}
		}
		destinationImage.update();
	}
	

	public static MyImage rotate(MyImage myImage, EnumRotation rotation) {
		MyImage newImage;
		if(rotation == EnumRotation.ROTATE_180){
			newImage = new MyImage(myImage.getWidth(), myImage.getHeight(), myImage.getType());
		}
		else{
			newImage = new MyImage(myImage.getHeight(), myImage.getWidth(), myImage.getType());
		}
		for(int i = 0; i < newImage.getPixels().length; i++){
			for(int j = 0; j < newImage.getPixels()[i].length; j++){
				switch (rotation) {
				case ROTATE_180:
					newImage.getPixels()[i][j] = myImage.getPixels()[myImage.getPixels().length -1 -i][myImage.getPixels()[i].length -1 -j];
					break;
				case ROTATE_MINUS_90:
					newImage.getPixels()[i][j] = myImage.getPixels()[j][newImage.getPixels().length -1 -i];
					break;
				case ROTATE_PLUS_90:
					newImage.getPixels()[i][j] = myImage.getPixels()[newImage.getPixels()[i].length - 1 -j][i];
					break;
				}
			}
		}
		newImage.update();
		return newImage;
	}

}
