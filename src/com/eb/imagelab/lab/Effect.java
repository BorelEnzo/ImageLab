package com.eb.imagelab.lab;
import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.EnumGreyScale;
import com.eb.imagelab.model.MyImage;

public abstract class Effect {
	
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
	
	public static void pixelate(MyImage myImage, int pixelDiameter){
		float r, g, b;
		int startX, endX, startY, endY;
		float nbP = (float) Math.pow(pixelDiameter * 2 + 1, 2);
		for(int i = pixelDiameter; i < myImage.getPixels().length + 1; i+=(pixelDiameter * 2 + 1)){
			for(int j = pixelDiameter; j < myImage.getPixels()[i].length + 1; j+=(pixelDiameter * 2 + 1)){
				r = g = b = 0.0f;
				startX = j - pixelDiameter < 0 ? 0 : j - pixelDiameter;
				endX = j + pixelDiameter > myImage.getPixels()[i].length - 1 ? myImage.getPixels()[i].length -1 : j + pixelDiameter;
				startY = i - pixelDiameter < 0 ? 0 : i - pixelDiameter;
				endY = i + pixelDiameter > myImage.getPixels().length - 1 ? myImage.getPixels().length - 1 : i + pixelDiameter;
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