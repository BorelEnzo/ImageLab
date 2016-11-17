package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.MyImage;

public abstract class Utils {
	
	private static final Colour[] colours = new Colour[]{
			new Colour(255, 0, 0, 0),
			new Colour(255, 0, 0, 255),
			new Colour(255, 0, 255, 0),
			new Colour(255, 0, 255, 255),
			new Colour(255, 255, 0, 0),
			new Colour(255, 255, 0, 255),
			new Colour(255, 255, 255, 0),
			new Colour(255, 255, 255, 255)
		};
	
	public static Colour[][] deepCopyColoursArray(Colour[][] original){
		if(original == null){
			return  null;
		}
		if(original.length == 0){
			return new Colour[0][0];
		}
		final Colour[][] copy = new Colour[original.length][original[0].length];
		for(int i = 0; i < original.length; i++){
			for(int j = 0; j < original[i].length; j++){
				copy[i][j] = original[i][j].clone();
			}
		}
		return copy;
	}
	
	public static boolean isImageValid(MyImage myImage){
		return !(myImage == null || myImage.getPixels() == null || myImage.getPixels().length != myImage.getHeight() || myImage.getPixels()[0].length != myImage.getWidth());
	}
	
	public static Colour[] getColours() {
		return colours;
	}
}
