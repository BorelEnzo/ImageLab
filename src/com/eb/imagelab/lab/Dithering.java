package com.eb.imagelab.lab;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.MyImage;

/**
 * Dithering Algorithm.
 * @author Enzo Borel
 * 
 * Mainly inspired by:
 * {@link #dithering(MyImage)} and {@link #findClosestColor(Colour)} was found here :
 * http://stackoverflow.com/questions/5940188/how-to-convert-a-24-bit-png-to-3-bit-png-using-floyd-steinberg-dithering/5940260#5940260
 *
 */
public abstract class Dithering {
	
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
	
	private static int findClosestColor(Colour colour){
		if(colour == null)return 0;
		int closest = 0;
		for(int c = 0; c < colours.length; c++){
			if(colours[c].difference(colour) < colours[closest].difference(colour)){
				closest = c;
			}
		}
		return closest;
	}
	
	public static void dithering(MyImage myImage){
		Colour[][] bitmap = Utils.deepCopyColoursArray(myImage.getPixels());
		Colour oldColour = new Colour(), newColour;
		for(int y = 0; y < myImage.getPixels().length; y++){
			for(int x = 0; x < myImage.getPixels()[y].length; x++){
				oldColour = bitmap[y][x];
				newColour = colours[findClosestColor(oldColour)];
				newColour.setA(myImage.getPixels()[y][x].getA());
				myImage.getPixels()[y][x] = newColour.clamp();
				oldColour = oldColour.subtract(newColour);
				if(x + 1 < myImage.getPixels()[y].length){
					bitmap[y][x + 1] = (bitmap[y][x + 1].add(oldColour.multiply(0.4375f)));
				}
				if(y + 1 < myImage.getPixels().length){
					if(x - 1 >= 0){
						bitmap[y + 1][x - 1] = (bitmap[y + 1][x - 1].add(oldColour.multiply(0.1875f)));
					}
					bitmap[y + 1][x] = (bitmap[y + 1][x].add(oldColour.multiply(0.3125f))).clamp();
					if(x + 1 < myImage.getPixels()[y].length){
						bitmap[y + 1][x + 1] = (bitmap[y + 1][x + 1].add(oldColour.multiply(0.0625f)));
					}
				}
			}
		}
		myImage.update();
	}

}
