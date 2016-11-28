package com.eb.imagelab.model;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.eb.imagelab.lab.ImageLab;
import com.eb.imagelab.lab.Utils;

/**
 * The Class MyImage.
 * MyImage is a kind of {@link BufferedImage}, which is specified by an array containing the {@link Colour}
 * You should pay attention about one thing:
 * the vast majority of operations on a {@link MyImage} use its {@link Colour} matrix {@link #pixels}. If you
 * use some built-in function, you should update {@link #pixels}, else effects will NOT be applied if {@link #pixels} is null
 * - {@link #getSubimage(int, int, int, int)}
 * - {@link #getScaledInstance(int, int, int)}
 * already manage this case
 * @author Enzo Borel
 */
public class MyImage extends BufferedImage implements Cloneable {
	
	private Colour[][] pixels;

	/**
	 * Creates an empty {@link MyImage}
	 * @param width
	 * @param height
	 * @param imageType type such that {@link BufferedImage#TYPE_xxxxxx}
	 */
	public MyImage(BufferedImage image, int type) {
		super(image.getWidth(), image.getHeight(), type);
		setData(image.getData());
		pixels = new Colour[getHeight()][getWidth()];
	}
	
	public MyImage(int width, int height, int type){
		super(width, height, type);
		pixels = new Colour[getHeight()][getWidth()];
	}
	
	/**
	 * Reads the buffered image pixel per pixel, translates the color code in a {@link Colour}, and puts this object in
	 * the matrix.
	 */
	public void readPixels(){
		final int[] pixels = ((DataBufferInt)getRaster().getDataBuffer()).getData();
		for(int i = 0; i < getPixels().length; i++){
			for(int j = 0; j < getPixels()[i].length; j++){
				getPixels()[i][j] = new Colour(pixels[i * getPixels()[0].length + j]);
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
		MyImage result = new MyImage(this, getType());
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
	
	/**
	 * Similar as: ImageLab.crop(image, x, image.width, y, image.height)
	 */
	@Override
	public MyImage getSubimage(int x, int y, int w, int h) {
		return ImageLab.crop(this, x, w, y, h);
	}
	
	/**
	 * Returns a new MyImage, which is a scaled version of the current one.
	 * @param hints one of the constants listed below
	 * @see        java.awt.Image#SCALE_DEFAULT
     * @see        java.awt.Image#SCALE_FAST
     * @see        java.awt.Image#SCALE_SMOOTH
     * @see        java.awt.Image#SCALE_REPLICATE
     * @see        java.awt.Image#SCALE_AREA_AVERAGING
	 */
	@Override
	public MyImage getScaledInstance(int width, int height, int hints) {
		BufferedImage bfImage = new BufferedImage(width, height, getColorModel().hasAlpha() ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
		Graphics2D bGr = bfImage.createGraphics();
	    bGr.drawImage(super.getScaledInstance(width, height, hints), 0, 0, null);
	    bGr.dispose();
	    MyImage newMyImage = new MyImage(bfImage, bfImage.getType());
	    newMyImage.newPixels();
	    return newMyImage;
	}
	
	/**
	 * Reads pixels and fills the matrix
	 */
	public void newPixels(){
		final int[] pixels = ((DataBufferInt)getRaster().getDataBuffer()).getData();
		for(int i = 0; i < getPixels().length; i++){
			for(int j = 0; j < getPixels()[i].length; j++){
				getPixels()[i][j] = new Colour(pixels[i * getPixels()[0].length + j]);
			}
		}
	}
	
	
}
