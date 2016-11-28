package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.EnumGreyScale;
import com.eb.imagelab.model.MyImage;

/**
 * The Class effect contains several methods about effects to apply on the picture
 * Inspired by http://lodev.org/cgtutor/filtering.html
 * @author Enzo Borel
 */

public abstract class Effect extends AbstractLab{
	
	public static void applyEmbossFilter(MyImage myImage, boolean pressed, int bumpSize){
		if(bumpSize < 0)bumpSize = 0;
		final float[][] matrixFilter = new float[bumpSize * 2 + 1][bumpSize * 2 + 1];
		for(int i = 0; i < matrixFilter.length; i++){
			for(int j = 0; j < matrixFilter[i].length; j++){
				if(j > matrixFilter.length - 1 - i){
					matrixFilter[i][j] = pressed ? -1 : 1;
				}
				else if(j < matrixFilter.length - 1 -i){
					matrixFilter[i][j] = pressed ? 1 : -1;
				}
			}
		}
		applyGenericFilter(myImage, matrixFilter, bumpSize, 1, 128);
		GreyScaleConversion.toGreyScale(myImage, EnumGreyScale.GREY_SCALE_AVG);
	}
	
	public static void applySketchEffet(MyImage myImage, int intensity){
		GreyScaleConversion.toGreyScale(myImage, EnumGreyScale.GREY_SCALE_AVG);
		MyImage clone = myImage.clone();
		Filter.negative(myImage);
		Blur.blur(myImage, intensity);
		Colour c1;
		Colour c2;
		for(int i = 0; i < myImage.getPixels().length; i++){
			for(int j = 0; j < myImage.getPixels()[i].length; j++){
				c1 = myImage.getPixels()[i][j];
				c2 = clone.getPixels()[i][j];
				myImage.getPixels()[i][j].setR(colorDodge(c2.getR(), c1.getR()));
				myImage.getPixels()[i][j].setG(colorDodge(c2.getG(), c1.getG()));
				myImage.getPixels()[i][j].setB(colorDodge(c2.getB(), c1.getB()));
			}
		}
		myImage.update();
	}
	
	private static int colorDodge(int i1, int i2){
		float image = (float)i2;	
	    float mask = (float)i1;
	    return ((int) (image == 255 ? image : Math.min(255, (((long)mask << 8 ) / (255 - image)))));
	}
	
	public static void findEdges(MyImage myImage){
		final float[][] matrixFilter = new float[][]{
			{-1, -1, -1},
			{-1, 8, -1},
			{-1, -1, -1}
		};
		applyGenericFilter(myImage, matrixFilter, 1, 1, 0);
	}
	
	public static void pixelate(MyImage myImage, int pixelDiameter){
		float r, g, b;
		int startX, endX, startY, endY;
		float nbP;
		for(int i = pixelDiameter; i < myImage.getPixels().length + pixelDiameter; i+=(pixelDiameter * 2 + 1)){
			for(int j = pixelDiameter; j < myImage.getPixels()[0].length + pixelDiameter; j+=(pixelDiameter * 2 + 1)){
				r = g = b = 0.0f;
				startX = j - pixelDiameter;
				endX = j + pixelDiameter > myImage.getPixels()[0].length - 1 ? myImage.getPixels()[0].length -1 : j + pixelDiameter;
				startY = i - pixelDiameter;
				endY = i + pixelDiameter > myImage.getPixels().length - 1 ? myImage.getPixels().length - 1 : i + pixelDiameter;
				nbP = ((endX - startX) + 1) * ((endY - startY) + 1);
				for(int p = startY; p <= endY; p++){
					for(int q = startX;  q <= endX; q++){
						Colour c = myImage.getPixels()[p][q];
						r += c.getR();
						g += c.getG();
						b += c.getB();
					}
				}				
				r /= nbP;
				g /= nbP;
				b /= nbP;
				for(int p = startY; p <= endY; p++){
					for(int q = startX;  q <= endX; q++){
						myImage.getPixels()[p][q].setR((int) r);
						myImage.getPixels()[p][q].setG((int) g);
						myImage.getPixels()[p][q].setB((int) b);
					}
				}
			}
		}
		myImage.update();
	}
}
