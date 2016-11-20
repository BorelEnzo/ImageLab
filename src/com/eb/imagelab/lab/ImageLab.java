package com.eb.imagelab.lab;
import java.awt.Font;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.eb.imagelab.model.Colour;
import com.eb.imagelab.model.EnumGreyScale;
import com.eb.imagelab.model.EnumRotation;
import com.eb.imagelab.model.Motion;
import com.eb.imagelab.model.MyImage;

/**
 * The Class ImageLab
 * Repertory containing all processing methods
 * @author Enzo Borel
 */

public abstract class ImageLab {
		
	/**
	 * Applies a colored filter on the whole picture.
	 * Computes the ratio of each channel of the <code>colour</code> (param), and for each pixel of the <code>myImage</code>,
	 * multiplies the value of each channel of the color by the appropriate ratio.
	 * The alpha channel is not taken into account.
	 * NB: if some pixels don't contain a color (in other words, if the channel R, G, or B is 0), and if this color is similar
	 * to the <code>colour</code> passed as parameter, maybe it will not produce the expected result, because multiplying by 0 doesn't change
	 * the channel's value. To work around this problem, you can use {@link MyImage#setMinBlue()}, {@link MyImage#setMinGreen()} or {@link MyImage#setMinRed()}
	 * @param myImage picture to deal with
	 * @param colour of the filter
	 */
	public static void applyColoredFilter(MyImage myImage, Colour colour){
		if(!Utils.isImageValid(myImage) || colour == null)return;
		Filter.applyColoredFilter(myImage, colour);
	}
	
	/**
	 * Applies an emboss filter on the picture. Then, it gives a kinf od 3d shadow effect, in grey scale.
	 * @param myImage the picture
	 * @param the 'orientation' of the bump. Depends on the picture
	 * @param the size of the bump. Should not be too big. Minimum is 0, which gives a grey picture.
	 * Recommended size = 1 or 2
	 */
	public static void applyEmbossFilter(MyImage myImage, boolean pressed, int bumpSize){
		if(!Utils.isImageValid(myImage))return;
		Effect.applyEmbossFilter(myImage, pressed, bumpSize);
	}
	
	/**
	 * Applies a linear colored gradient on a specific area
	 * NB: if a picture doesn't contain a color (in other words, if the channel R, G, or B is 0), and if this color is similar
	 * to the <code>colour</code> passed as parameter, maybe it will not produce the expected result, because multiplying by 0 doesn't change
	 * the channel's value. To work around this problem, you can use {@link MyImage#setMinBlue()}, {@link MyImage#setMinGreen()}
	 * or {@link MyImage#setMinRed()}
	 * @param myImage the picture to deal with
	 * @param fromColour begins with this colour
	 * @param toColour ends with this colour
	 * @param vertical the direction (from top to bottom : true / from left to right : false)
	 */
	public static void applyLinearGradient(MyImage myImage, Colour fromColour, Colour toColour, boolean vertical, int startX, int width, int startY, int height){
		if(!Utils.isImageValid(myImage) || fromColour == null || toColour == null || startX >= myImage.getWidth() || width <= 0 || startY >= myImage.getHeight() || height <= 0)return;
		Gradient.applyLinearGradient(myImage, fromColour, toColour, vertical, startX, width, startY, height);
	}
	
	public static void applySketchEffet(MyImage myImage, int intensity){
		if (!Utils.isImageValid(myImage))return;
		Effect.applySketchEffet(myImage, intensity);
	}


	/**
	 * Applies a blur on the picture. Can dramatically slow down the program if the radius is too high !
	 * We recommend you to start with a value < 10, and then increase the size if necessary
	 * Based on Moore neighborhood
	 * @param myImage the image to deal with
	 * @param radius the radius of the blur
	 */
	public static void blur(MyImage myImage, int radius){
		if (!Utils.isImageValid(myImage) || radius <= 0)return;
		Blur.blur(myImage, radius);
	}
	
	/**
	 * Applies ablur only on a specific area. Can dramatically slow down the program if the radius is too high !
	 * We recommend you to start with a value < 10, and then increase the size if necessary
	 * @param myImage the picture
	 * @param shape the shape used to define the blurred area
	 * @param radius of th blur
	 * @param in true = blur inside the shape
	 */
	public static void blurLocalShape(MyImage myImage, Shape shape, int radius, boolean in){
		if(!Utils.isImageValid(myImage) || shape == null || radius <= 0)return;
		Blur.blurLocalShape(myImage, shape, radius, in);
	}
	
	/**
	 * Changes the transparency of the picture, if the image has an alpha channel.
	 * To restore the default value, call this function with the multiplicative inverse of the previous value as delta
	 * NB : if the value of the alpha channel is 0 from some pixels, and if you want more opacity, it will maybe not produce the expected result
	 * on these pixels,because multiplyling by 0 doesn't change the value. To work around this problem, you can use {@link MyImage#setMinAlpha()}
	 * @param myImage the picture to deal with
	 * @param delta the variation of the transparency (delta = 1 means no changes)
	 */
	public static void changeAlpha(MyImage myImage, float delta){
		if(!Utils.isImageValid(myImage) || !myImage.hasAlpha() || delta == 1)return;
		RatioModifier.changeAlpha(myImage, delta);
	}
	
	/**
	 * Changes the brightness of the picture.
	 * Even if delta is very high, maybe the picture will still a little bit colored. It's because the value of each channel is multiplied by
	 * delta. Therefore, if the value of the channel is 0, it can't be white.
	 * To restore the default value, call this function with the multiplicative inverse of the previous value as delta
	 * NB: if some pixels don't a color (in other words, if the channel R, G, or B is 0), the multiplication by <code>delta</code>,
	 * passed as parameter, will maybe not produce the expected result, because multiplying by 0 doesn't change
	 * the channel's value. To work around this problem, you can use {@link MyImage#setMinBlue()}, {@link MyImage#setMinGreen()} or {@link MyImage#setMinRed()}
	 * @param myImage the picture the deal with
	 * @param delta variation of the brightness. 0 means a blak picture. (Can't be less than 0)
	 */
	public static void changeBrigthness(MyImage myImage, float delta){
		if(!Utils.isImageValid(myImage) || delta == 1)return;
		RatioModifier.changeBrigthness(myImage, delta);
	}
	
	/**
	 * Changes color ratios. It's quite similar to {@link #applyColoredFilter(MyImage, Colour)}
	 * If deltaR = deltaG = deltaB, it will produce the same as {@linkplain #changeBrigthness(MyImage, float)}
	 * NB: if some pixels don't a color (in other words, if the channel R, G, or B is 0), the multiplication by <code>deltaR</code>,
	 * <code>deltaG</code> or <code>deltaB</code> passed as parameter, will maybe not produce the expected result, because multiplying by 0 doesn't change
	 * the channel's value. To work around this problem, you can use {@link MyImage#setMinBlue()}, {@link MyImage#setMinGreen()} or {@link MyImage#setMinRed()}
	 * @param myImage the image to deal with
	 * @param deltaR the variation or the red channel (1 means no changes). Cannot be less than 0.
	 * @param deltaG the variation or the green channel (1 means no changes). Cannot be less than 0.
	 * @param deltaB the variation or the blue channel (1 means no changes). Cannot be less than 0.
	 */
	public static void changeRatio(MyImage myImage, float deltaR, float deltaG, float deltaB){
		if(!Utils.isImageValid(myImage))return;
		RatioModifier.changeRatio(myImage, deltaR, deltaG, deltaB);
	}
	
	
	/**
	 * Reduces the number of shades
	 * @param myImage
	 * @param shades min = 2, max = 256
	 */
	public static void changeShades(MyImage myImage, int shades){
		if(!Utils.isImageValid(myImage))return;
		RatioModifier.changeShades(myImage, shades);
	}
	
	/**
	 * Crops the picture
	 * @param myImage the picture to deal with
	 * @param startX the left of the new picture. min : 0, max : {@link MyImage#getWidth()}
	 * @param width the width of the new picture. min: 1, max : width of the actual picture
	 * @param startY the top of the new picture. Min: 0, max : {@link MyImage#getHeight()}
	 * @param height the height of the new picture. min: 1, max : height the actual picture
	 * @return the new cropped image, or the picture passed as parameter if at least on parameter is wrong
	 */
	public static MyImage crop(MyImage myImage, int startX, int width, int startY, int height){
		if(!Utils.isImageValid(myImage) || startX >= myImage.getWidth() || width <= 0 || startY >= myImage.getHeight() || height <= 0)return myImage;
		return ImageFormatter.crop(myImage, startX, width, startY, height);
	}
	
	/**
	 * Performs the Floyd Steinberg algorithm
	 * @param myImage the picture to deal with
	 */
	public static void dithering(MyImage myImage){
		if(!Utils.isImageValid(myImage))return;
		Dithering.dithering(myImage);
	}
	
	/**
	 * Transforms the pictures into a blck picture where only edges appear
	 * @param myImage the picture
	 */
	public static void findEdges(MyImage myImage){
		if(!Utils.isImageValid(myImage))return;
		Effect.findEdges(myImage);
	}
	
	/**
	 * Flips the picture
	 * @param myImage
	 * @param vertical along X axis (true) or Y axis (false)
	 */
	public static void flip(MyImage myImage, boolean vertical){
		if(!Utils.isImageValid(myImage))return;
		ImageFormatter.flip(myImage, vertical);
	}
	
	/**
	 * Applies a kind of gradient, from the colored version to the grey scale version of this picture
	 * @param myImage the picture to deal with
	 * @param vertical direction of the gradient.
	 * @param startWithGreyScale if true, the first end is the 'grey scale' one, else it's the coloured one
	 * @param typeGreyScale type of grey scale.
	 */
	public static void fromColouredToGreyScale(MyImage myImage, boolean vertical, boolean startWithGreyScale, EnumGreyScale typeGreyScale){
		if(!Utils.isImageValid(myImage) || typeGreyScale == null)return;
		GreyScaleConversion.fromColouredToGreyScale(myImage, vertical, startWithGreyScale, typeGreyScale);
	}
	
	/**
	 * Applies a mask on the picture from another picture
	 * @param myImage the image to mask
	 * @param mask the mask itself.
	 * @param colour the mask colour
	 * @param maskX position of the mask. Quit if > myImage.width
	 * @param maskY position of the mask. Quit if > myImage.height
	 * @param in if true, keep pixels inside the mask.
	 */
	public static void mask(MyImage myImage, MyImage mask, Colour colour, int maskX, int maskY, boolean in){
		if(!Utils.isImageValid(myImage) || !Utils.isImageValid(mask)|| colour == null || maskX >= myImage.getWidth() || maskY >= myImage.getHeight() || mask.getWidth() == 0 || mask.getHeight() == 0)return;
		Mask.mask(myImage, mask, colour, maskX, maskY, in);
	}
	
	/**
	 * Applies a mask on the picture from a {@link Shape}
	 * @param myImage the picture to deal with
	 * @param shape the new picture 'area'. All pixels out of bounds become white with alpha = 0
	 * (if {@link MyImage#hasAlpha()} = true
	 * @param if true, keep pixels inside the shape. Else, keep outside 
	 */
	public static void mask(MyImage myImage, Shape shape, boolean in){
		if(!Utils.isImageValid(myImage) || shape == null)return;
		Mask.mask(myImage, shape, in);
	}
	
	/**
	 * Applies a textual mask on the picture
	 * @param myImage the picture to deal with
	 * @param text the message to display
	 * @param font the font. If null, the default will be used (very small)
	 * @param x the position, must be < image.width
	 * @param y the position, must be < image.height
	 */
	public static void mask(MyImage myImage, String text, Font font, int x, int y){
		if(!Utils.isImageValid(myImage) || text == null || x >= myImage.getWidth() || y >= myImage.getHeight())return;
		Mask.mask(myImage, text, font, x, y);
	}
	
	/**
	 * Applies a blur in one direction
	 * @param myImage the picture
	 * @param radius the radius of the blur
	 * @param motion {@linkplain Motion#HORIZONTAL}, {@linkplain Motion#VERTICAL}, {@linkplain Motion#DIAGONAL_TO_BOTTOM}, or {@linkplain Motion#DIAGONAL_TO_TOP}
	 */
	public static void motionBlurOneDirection(MyImage myImage, int radius, Motion motion){
		if(!Utils.isImageValid(myImage) || radius <= 0 || motion == null)return;
		Blur.motionBlurOneDirection(myImage, radius, motion);
	}
	/**
	 * Inverts all colours' values
	 * @param myImage the picture
	 */
	public static void negative(MyImage myImage){
		if(!Utils.isImageValid(myImage))return;
		Filter.negative(myImage);
	}
	
	/**
	 * Copy and paste a picture on another.
	 * NB : dimensions are NOT updated. If <code>myImage</code> exceeds <code>destinationImage</code>'s bounds,
	 * the pixels outside will be ignored.
	 * @param myImage the picture to copy and paste. No modification will be performed on this picture
	 * @param destinationImage the destination (updated)
	 * @param x where to place image whithin the destination
	 * @param y where to place image whithin the destination
	 */
	public static void paste(MyImage myImage, MyImage destinationImage, int x, int y){
		if(!Utils.isImageValid(myImage) || !Utils.isImageValid(destinationImage) || x >= destinationImage.getWidth() || y >= destinationImage.getHeight())return;
		ImageFormatter.paste(myImage, destinationImage, x, y);
	}
	
	/**
	 * Transformes the picture as a kind of "mosaic" by averaging pixel's areas
	 * @param myImage the picture
	 * @param pixelDiameter half of new pixels' width
	 */
	public static void pixelate(MyImage myImage, int pixelDiameter){
		if(!Utils.isImageValid(myImage) || pixelDiameter <= 0)return;
		Effect.pixelate(myImage, pixelDiameter);
	}
	
	/**
	 * Reads the external picture's data and build a matrix of {@linkplain Colour}
	 * @param path of the picture
	 * @return a new {@linkplain MyImage}
	 * @throws IOException
	 */
	public static MyImage readImage(String path) throws IOException{
		if(path != null){
			try{
				File file = new File(path);
				if(file.exists()){
					MyImage myImage;
					BufferedImage bufImg = ImageIO.read(file);
				    BufferedImage convertedImg = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), bufImg.getType());
				    if(convertedImg.getAlphaRaster() != null){
				    	myImage = new MyImage(bufImg, BufferedImage.TYPE_INT_ARGB);
				    }
				    else{
				    	myImage = new MyImage(bufImg, BufferedImage.TYPE_INT_RGB);
				    }
					myImage.readPixels();
					return myImage;
				}
			}
			catch(IOException i){
				throw new IOException("Error: getImage(). The path or the type may bot be valid");
			}
		}
		return null;
	}
	
	/**
	 * Rotates the picture
	 * @param myImage the picture
	 * @param rotation the type of rotation
	 * @return the new image
	 */
	public static MyImage rotate(MyImage myImage, EnumRotation rotation) {
		if(!Utils.isImageValid(myImage) || rotation == null)return null;
		return ImageFormatter.rotate(myImage, rotation);
	}
	
	/**
	 * Transforms the image as a grey-scale picture
	 * If the <code>type</code> is wrong, the default value would be {@link Constants#GREY_SCALE_AVG}
	 * @param type type of filter
	 */
	public static void toGreyScale(MyImage myImage, EnumGreyScale type){
		if(!Utils.isImageValid(myImage) || type == null)return;
		GreyScaleConversion.toGreyScale(myImage, type);
		
	}
	
	/**
	 * Transforms the image into a sepia image
	 * @param myImage th picture
	 */
	public static void toSepia(MyImage myImage){
		if(!Utils.isImageValid(myImage))return;
		Filter.toSepia(myImage);
	}
}
