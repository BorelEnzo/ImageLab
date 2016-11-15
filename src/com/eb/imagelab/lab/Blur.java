package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.MyImage;

/**
 * Used to apply a blur on pictures.
 * All blur methods are placed here
 * @author Enzo Borel
 */
public abstract class Blur {
	
	public static void blur(MyImage myImage, int radius){
		if(radius > Math.min(myImage.getWidth() / 2, myImage.getHeight() / 2)){
			radius = Math.min(myImage.getWidth(), myImage.getHeight()) / 2;
		}
		int r, g, b, startX, endX, startY, endY;
		final float d = (float) Math.pow(2*radius + 1, 2);
		Colour c;
		Colour colourClone[][] = Utils.deepCopyColoursArray(myImage.getPixels());
		for(int i = 0; i < colourClone.length; i++){
			for(int j = 0; j < colourClone[i].length; j++){
				r = g = b = 0;
				startX = j - radius < 0 ? 0 : j - radius;
				endX = j + radius > colourClone[i].length - 1 ? colourClone[i].length -1 : j + radius;
				startY = i - radius < 0 ? 0 : i - radius;
				endY = i + radius > colourClone.length - 1 ? colourClone.length - 1 : i + radius;
				for(int k = startY; k < endY; k++){
					for(int l = startX; l < endX; l++){
						c = colourClone[k][l];
						r += c.getR();
						g += c.getG();
						b += c.getB();
					}
				}
				myImage.getPixels()[i][j].setR((int) ((float)r / d));
				myImage.getPixels()[i][j].setG((int) ((float)g / d));
				myImage.getPixels()[i][j].setB((int) ((float)b / d));
			}
		}
		myImage.update();
	}

}
