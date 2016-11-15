package com.eb.imagelab.lab;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.eb.imagelab.model.MyImage;

/**
 * Contains all methods about masks
 * @author Enzo Borel
 *
 */
public abstract class Mask {
	
	
	public static void mask(MyImage myImage, MyImage mask, int maskX, int maskY, boolean in){
		if(!Utils.isImageValid(myImage) || !Utils.isImageValid(mask)|| maskX >= myImage.getWidth() || maskY >= myImage.getHeight() || mask.getWidth() == 0 || mask.getHeight() == 0)return;
		if(in){
			for(int i = 0; i < myImage.getPixels().length; i++){
				for(int j = 0; j < myImage.getPixels()[i].length; j++){
					if(i - maskY >= 0 && j - maskX >= 0 && i - maskY < mask.getPixels().length && j - maskX < mask.getPixels()[0].length){
						if(mask.getPixels()[i - maskY][j - maskX].getColour() != -1){
							myImage.getPixels()[i][j].setColour(0);
						}
					}
				}
			}
		}
		else{
			for(int i = 0; i < myImage.getPixels().length; i++){
				for(int j = 0; j < myImage.getPixels()[i].length; j++){
					if(i - maskY >= 0 && j - maskX >= 0 && i - maskY < mask.getPixels().length && j - maskX < mask.getPixels()[0].length){
						if(mask.getPixels()[i - maskY][j - maskX].getColour() == -1){
							myImage.getPixels()[i][j].setColour(0);
						}
					}
				}
			}
		}
		myImage.update();
	}
	
	
	public static void mask(MyImage myImage, Shape shape, boolean in){
		MyImage mask = new MyImage(myImage.getWidth(), myImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = mask.createGraphics();
		graphics2d.fill(shape);
		graphics2d.dispose();
		final int[] pixelsMask = ((DataBufferInt)mask.getRaster().getDataBuffer()).getData();
		if(in){
			for(int i = 0; i < myImage.getPixels().length; i++){
				for(int j = 0; j < myImage.getPixels()[i].length; j++){
					if(pixelsMask[i * myImage.getPixels().length + j] != -1){
						myImage.getPixels()[i][j].setColour(0);
					}
				}
			}
		}
		else{
			for(int i = 0; i < myImage.getPixels().length; i++){
				for(int j = 0; j < myImage.getPixels()[i].length; j++){
					if(pixelsMask[i * myImage.getPixels().length + j] == -1){
						myImage.getPixels()[i][j].setColour(0);
					}
				}
			}
		}
		
		myImage.update();
	}

}
