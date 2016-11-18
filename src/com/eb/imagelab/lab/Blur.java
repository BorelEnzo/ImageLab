package com.eb.imagelab.lab;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.Motion;
import com.eb.imagelab.model.MyImage;

/**
 * Used to apply a blur effect on pictures.
 * Mainly inspired by lodev.org:
 * http://lodev.org/cgtutor/filtering.html
 * @author Enzo Borel
 */
public abstract class Blur {
	
	/**
	 * Applies effectively the blur effect.
	 * @param myImage the picture
	 * @param shape the area of the blur
	 * @param radius 
	 * @param in inside or not the area defined by the {@link Shape}
	 * @param matrixFilter the matrix filter
	 */
	private static void applyBlur(MyImage myImage, Shape shape, int radius, boolean in, float[][] matrixFilter, float factor, float bias){
		MyImage mask = new MyImage(myImage.getWidth(), myImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = mask.createGraphics();
		graphics2d.fill(shape);
		graphics2d.dispose();
		final int[] pixelsMask = ((DataBufferInt)mask.getRaster().getDataBuffer()).getData();
		float r, g, b;
		
		int startX, endX, startY, endY;
		Colour colour;
		Colour[][] clone = Utils.deepCopyColoursArray(myImage.getPixels());
		for(int i = 0; i < clone.length; i++){
			for(int j = 0; j < clone[i].length; j++){
				if((in &&  pixelsMask[i * myImage.getPixels().length + j] == -1) || (!in && pixelsMask[i * myImage.getPixels().length + j] != -1)){
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
					myImage.getPixels()[i][j].setR((int) (factor * r + bias));
					myImage.getPixels()[i][j].setG((int) (factor * g + bias));
					myImage.getPixels()[i][j].setB((int) (factor * b + bias));					
				}
			}
		}
		myImage.update();
	}
	
	
	public static void blur(MyImage myImage, int radius){
		applyBlur(myImage, new Rectangle(0, 0, myImage.getWidth(), myImage.getHeight()), radius, true, getSimpleBlurMatrix(radius), 1, 0);
	}
	
	public static void blurLocalShape(MyImage myImage, Shape shape, int radius, boolean in){
		applyBlur(myImage, shape, radius, in, getSimpleBlurMatrix(radius), 1, 0);
	}
	
	/**
	 * Initializes a squared matrix for a basic blur
	 * @param radius. matrix.length = 2 * radius + 1
	 * @return the matrix
	 */
	private static float[][] getSimpleBlurMatrix(int radius){
		float[][] matrix = new float[2 * radius + 1][2 * radius + 1];
		int nbP = (int) Math.pow(matrix.length, 2);
		for(int i = 0; i < matrix.length; i++){
			Arrays.fill(matrix[i], 1f/(float)nbP);
		}
		return matrix;
	}
	
	public static void motionBlurOneDirection(MyImage myImage, int radius, Motion motion){
		float[][] matrix = new float[2 * radius + 1][2 * radius + 1];
		for(int i = 0; i < matrix.length; i++){
			Arrays.fill(matrix[i], 0);
			switch (motion) {
			case DIAGONAL_TO_TOP:
				matrix[i][matrix.length - 1 -i] = 1f;
				break;
			case DIAGONAL_TO_BOTTOM:
				matrix[i][i] = 1f;
				break;
			case HORIZONTAL:
				if(i == radius)
				Arrays.fill(matrix[i], 1);
				break;
			case VERTICAL:
				matrix[i][radius] = 1f;
				break;
			}
		}		
		applyBlur(myImage, new Rectangle(0, 0, myImage.getWidth(), myImage.getHeight()), radius, true, matrix, 1f/(float)matrix.length, 0);
	}
	
	
}
