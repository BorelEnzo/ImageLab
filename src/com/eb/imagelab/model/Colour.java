package com.eb.imagelab.model;

/**
 * Th class Colour
 * Adaptation of the code found here:
 * http://stackoverflow.com/questions/5940188/how-to-convert-a-24-bit-png-to-3-bit-png-using-floyd-steinberg-dithering/5940260#5940260
 * @author Enzo
 *
 */
public class Colour implements Cloneable{
	
	private int a;
	private int r;
	private int g;
	private int b;
	
	public Colour(int colour){
		setColour(colour);
	}
	
	public Colour(int a, int r, int g, int b){
		setA(a);
		setB(b);
		setG(g);
		setR(r);
	}
	
	public Colour() {}
	
	/**
	 * Sets the green channel's value min 0, max 255
	 * @param g
	 */
	public void setG(int g) {
		if(g > 255)g = 255;
		else if(g < 0) g = 0;
		this.g = g;
	}
	
	/**
	 * Sets the blue channel's value min 0, max 255
	 * @param b
	 */
	public void setB(int b) {
		if(b > 255)b = 255;
		else if(b < 0) b = 0;
		this.b = b;
	}
	
	/**
	 * Sets the red channel's value min 0, max 255
	 * @param r
	 */
	public void setR(int r) {
		if(r > 255)r = 255;
		else if(r < 0) r = 0;
		this.r = r;
	}
	
	/**
	 * Sets the alpha channel's value min 0, max 255
	 * @param a
	 */
	public void setA(int a) {
		if(a > 255)a = 255;
		else if(a < 0) a = 0;
		this.a = a;
	}
	
	/**
	 * Sets colour's value, such that:
	 * from bit 31 to 24 : alpha, min 0, max 11111111
	 * from bit 23 to 16 : red, min 0, max 11111111
	 * from bit 15 to 8 : green, min 0, max 11111111
	 * from bit 7 to 0 : blue, min 0, max 11111111
	 * The value is passed as an integer
	 * @param colour
	 */
	public void setColour(int colour){
		setB(colour & 0xFF);
		setG((colour >> 8) & 0xFF);
		setR((colour >> 16) & 0xFF);
		setA((colour >> 24) & 0xFF);
	}
	
	/**
	 * Gets the colour's code, such that:
	 * from bit 31 to 24 : alpha
	 * from bit 23 to 16 : red
	 * from bit 15 to 8 : green
	 * from bit 7 to 0 : blue
	 * @return the color code as a single integer
	 */
	public int getColour(){
		return (a << 24) + (r << 16) + (g << 8) + b;
	}
	
	public int getB() {
		return b;
	}
	
	public int getG() {
		return g;
	}
	
	public int getR() {
		return r;
	}
	
	public int getA() {
		return a;
	}
	
	/**
	 * Performs an addition, channel per channel
	 * @param c the colour to add
	 * @return this colour, updated
	 */
	public Colour add(Colour c){
		setR(r + c.r);
		setG(g + c.g);
		setB(b + c.b);
		return this;
	}
	
	/**
	 * Performs a substraction, channel per channel
	 * @param c the colour to substract
	 * @return this colour, updated
	 */
	public Colour subtract(Colour c){
		setR(r - c.r);
		setG(g - c.g);
		setB(b - c.b);
		return this;
	}
	
	/**
	 * Performs a multiplication on each channel, channel per channel
	 * @param the delta
	 * @return a new colour
	 * */
	public Colour multiply(float delta){
		return new Colour(a, (int)(r * delta), (int)(g * delta), (int)(b * delta));
	}
	
	/**
	 * Computes the sum of the squares of the difference within each channel
	 * @param c {@link Colour} to compare
	 * @return the sum
	 */
	public int difference(Colour c){
		int rDiff = c.r - r;
        int gDiff = c.g - g;
        int bDiff = c.b - b;
        return (rDiff * rDiff + gDiff * gDiff + bDiff * bDiff);
	}
	
	/**
	 * "Clamps" the value of each channel, between 0 and 255
	 * @return
	 */
	public Colour clamp(){
		setR(clamp(r));
		setG(clamp(g));
		setB(clamp(b));
		return this;
	}
	
	/**
	 * Same as {@link #clamp()}, but computes only the value of the expected channel
	 * @param i the value of the channel, R, G, or B
	 * @return the value between 0 and 255
	 */
	public int clamp(int i){
		return Math.max(0, Math.min(255, i));
	}
	
	
	
	@Override
	public Colour clone() {
		return new Colour(a, r, g, b);
	}
}
