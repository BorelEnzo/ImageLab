package com.eb.imagelab.model;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.eb.imagelab.lab.Utils;

/**
 * The Class MyImage.
 * MyImage is a kind of {@link BufferedImage}, which is specified by a flag named {@link #hasAlpha}, 
 * and an array containing the {@link Colour}
 * @author Enzo Borel
 */
public class MyImage extends BufferedImage implements Cloneable {
	
	private Colour[][] pixels;
	private boolean hasAlpha;

	/**
	 * Creates an empty {@link MyImage}
	 * @param width
	 * @param height
	 * @param imageType type such that {@link BufferedImage#TYPE_xxxxxx}
	 */
	public MyImage(int width, int height, int imageType) {
		super(width, height, imageType);
		pixels = new Colour[height][width];
	}

	/**
	 * Creates a new {@link MyImage} from an {@link ImageIO}
	 * @param read the stream
	 */
	public MyImage(BufferedImage read) {
		super(read.getWidth(), read.getHeight(), read.getType());
		setData(read.getData());
		pixels = new Colour[getHeight()][getWidth()];
	}
	
	/**
	 * Reads the buffered image pixel per pixel, translates the color code in a {@link Colour}, and puts this object in
	 * the matrix.
	 * Explanation on the main loop (with alpha channel):
	 * In this loop, we read each 4bytes-block to get the color, because A, R, G and B are coded with 1 byte (0-255)
	 * We want to concatenate these values in on sigle 32bits number, such that:
	 * aaaaaaaa - rrrrrrrr - gggggggg - bbbbbbbb
	 * However, a 4bytes-block representing a pixel is as follows:
	 * [a][b][g][r]
	 * Then:
	 *  - As the alpha is read, the value is placed at pos31 of argb (from bit 31 to 24), thanks the 24positions-offset : a 0 0 0
	 *  - As the blue is read, the value is added to argb without shifting: a 0 0 b (position 7)
	 *  - As the green is read, the value is placed at position 15 : a 0 g b
	 *  - As the red is read, the value is placed at position 23 : a r g b
	 *  
	 *  Let a = 1101, r = 110101, g = 100101 and b = 101, it becomes:
	 *  if a = 1101, then, by applying a 24 bits offset: argb = 1101 << 24 = 1101 00000000 00000000 00000000. 
	 *  if b = 101, then, by adding argb + b: argb = 1101 00000000 00000000 00000101
	 *  if g = 100101, then, by applying a 8 bits offset: g = 00100101 00000000 and 
	 *  	argb + g => argb = 1101 00000000 00100101 00000101
	 *  if r = 100101, then, by applying a 16bits offset: r = 00110101 00000000 00000000 and
	 *  	argb + r => argb = 1101 00110101 00100101 00000101
	 * @param bfImage the buffered image
	 */
	public void readPixels(){
		final byte[] pixelsByte = ((DataBufferByte)getRaster().getDataBuffer()).getData(); // all bytes, 1-D array
		hasAlpha = getAlphaRaster() != null;
		//Now we check if the image has an alpha channel
		if(hasAlpha){
			for (int px = 0, row = 0, col = 0; px < pixelsByte.length; px += 4) {
				Colour colour = new Colour();
				colour.setA(((int)pixelsByte[px] & 0xFF));
				colour.setR((int)pixelsByte[px + 3] & 0xFF);
				colour.setG((int)pixelsByte[px + 2] & 0xFF);
				colour.setB((int)pixelsByte[px + 1] & 0xFF);
				pixels[row][col] = colour;
	            col++;
	            if (col == getWidth()) {
	               col = 0;
	               row++;
	            }
	         }
		}
		else{
        	for (int px = 0, row = 0, col = 0; px < pixelsByte.length; px += 3) {
        		Colour colour = new Colour();
        		colour.setA(255);
        		colour.setR((int) pixelsByte[px + 2] & 0xFF);
        		colour.setG((int) pixelsByte[px + 1] & 0xFF);
        		colour.setB((int) pixelsByte[px] & 0xFF);
        		pixels[row][col] = colour;
        		col++;
        		if (col == getWidth()) {
        			col = 0;
        			row++;
        			
        		}
        	}
		}
		
	}
	
	/**
	 * Repaints the image
	 */
	public void update(){		
		if(pixels != null && pixels.length > 0){
			for(int i = 0; i < pixels.length; i++){
				for(int j = 0; j < pixels[i].length; j++){
					setRGB(j, i, pixels[i][j].getColour());
				}
			}
		}
		
	}
	
	public Colour[][] getPixels() {
		return pixels;
	}
	
	public boolean hasAlpha() {
		return hasAlpha;
	}
	
	public void setHasAlpha(boolean hasAlpha) {
		this.hasAlpha = hasAlpha;
	}
	
	/**
	 * Saves the image as PNG file
	 * @param name without extension
	 * @throws IOException 
	 */
	public void save(String name) throws IOException{
		int i = 0;
		File outputFile = new File(name + ".png");
		String tmp = name;
		while(outputFile.exists()){
			tmp = name + i + ".png";
			outputFile = new File(tmp);
			i++;
		}
		ImageIO.write(this, "png", outputFile);
	}
	
	/**
	 * Returns a new instance of {@link MyImage}, similar as this one
	 */
	@Override
	public MyImage clone() {
		MyImage result = new MyImage(getWidth(), getHeight(), getType());
		result.hasAlpha = hasAlpha;
		result.pixels = Utils.deepCopyColoursArray(pixels);
		return result;
	}
	
	/**
	 * Sets the minimum alpha to 1
	 * @param myImage
	 */
	public void setMinAlpha(){
		setMinX(0);
	}
	
	/**
	 * Sets the minimum blue to 1
	 * @param myImage
	 */
	public void setMinBlue(){
		setMinX(3);
	}
	
	/**
	 * Sets the minimum green to 1
	 * @param myImage
	 */
	public void setMinGreen(){
		setMinX(2);
	}
	
	/**
	 * Sets the minimum red to 1
	 * @param myImage
	 */
	public void setMinRed(){
		setMinX(1);
	}
	
	private void setMinX(int argb){
		for(int i = 0; i < pixels.length; i++){
			for(int j = 0; j < pixels[i].length; j++){
				switch (argb) {
				case 0:
					if(pixels[i][j].getA() == 0)pixels[i][j].setA(1);
					break;
				case 1:
					if(pixels[i][j].getR() == 0)pixels[i][j].setR(1);
					break;
				case 2:
					if(pixels[i][j].getG() == 0)pixels[i][j].setG(1);
					break;
				case 3:
					if(pixels[i][j].getB() == 0)pixels[i][j].setB(1);
				}
			}
		}
		update();
	}
}
