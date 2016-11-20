package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.MyImage;

public abstract class AbstractLab {
	
	public static void applyGenericFilter(MyImage myImage, float[][] matrixFilter, int radius, float factor, float bias){
		int startX, endX, startY, endY;
		float r, g, b;
		Colour colour;
		final Colour[][] clone = Utils.deepCopyColoursArray(myImage.getPixels());
		for(int i = 0; i < clone.length; i++){
			for(int j = 0; j < clone[i].length; j++){
				r = g = b = 0.0f;
				startX = j - radius < 0 ? 0 : j - radius;
				endX = j + radius > clone[i].length - 1 ? clone[i].length -1 : j + radius;
				startY = i - radius < 0 ? 0 : i - radius;
				endY = i + radius > clone.length - 1 ? clone.length - 1 : i + radius;
				for(int fY = startY; fY <= endY; fY++){
					for(int fX = startX;  fX <= endX; fX++){
						colour = clone[fY][fX];
						r += colour.getR() * matrixFilter[fY - startY][fX - startX];
						g += colour.getG() * matrixFilter[fY - startY][fX - startX];
						b += colour.getB()* matrixFilter[fY - startY][fX - startX];
					}
				}
				myImage.getPixels()[i][j].setR((int) (factor *  r + bias));
				myImage.getPixels()[i][j].setG((int) (factor *  g + bias));
				myImage.getPixels()[i][j].setB((int) (factor *  b + bias));	
			}
		}
		myImage.update();
	}
}
