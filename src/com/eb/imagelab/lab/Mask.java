package com.eb.imagelab.lab;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.MyImage;

/**
 * Contains all methods about masks
 * A mask allows you to keep only a part of a picture thanks to a shape (or another picture), which defines an area
 * with pixels to keep/hide.
 * @author Enzo Borel
 *
 */
public abstract class Mask {
	
	
	public static void mask(MyImage myImage, MyImage mask, Colour maskColour, int maskX, int maskY, boolean in){
		for(int i = 0; i < myImage.getPixels().length; i++){
			for(int j = 0; j < myImage.getPixels()[i].length; j++){
				if(i - maskY >= 0 && j - maskX >= 0 && i - maskY < mask.getPixels().length && j - maskX < mask.getPixels()[0].length){
					if((in && mask.getPixels()[i - maskY][j - maskX].getColour() != maskColour.getColour()) || 
						(!in && mask.getPixels()[i - maskY][j - maskX].getColour() == maskColour.getColour())){
						myImage.getPixels()[i][j].setColour(0);
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
		for(int i = 0; i < myImage.getPixels().length; i++){
			for(int j = 0; j < myImage.getPixels()[i].length; j++){
				if((in &&  pixelsMask[i * myImage.getPixels().length + j] != -1) || (!in && pixelsMask[i * myImage.getPixels().length + j] == -1)){
					myImage.getPixels()[i][j].setColour(0);
				}
			}
		}
		
		myImage.update();
	}
	
	public static void mask(MyImage myImage, String text, Font font, int x, int y){
		MyImage mask = new MyImage(myImage.getWidth(), myImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics2d = mask.createGraphics();
		if(font != null){
			graphics2d.setFont(font);
			
		}
		graphics2d.drawString(text, x, y);
		graphics2d.dispose();
		final int[] pixelsMask = ((DataBufferInt)mask.getRaster().getDataBuffer()).getData();
		for(int i = 0; i < myImage.getPixels().length; i++){
			for(int j = 0; j < myImage.getPixels()[i].length; j++){
				if(pixelsMask[i * myImage.getPixels().length + j] != -1){
					myImage.getPixels()[i][j].setColour(0);
				}
			}
		}
		myImage.update();
	}
}
