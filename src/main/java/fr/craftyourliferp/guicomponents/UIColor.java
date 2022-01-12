package fr.craftyourliferp.guicomponents;

import java.awt.Color;

public class UIColor {
	
	private Color color;
	
	public UIColor(Color color) {
		this.color = color;
	}
	
	public UIColor(int r, int g, int b)
	{
		this(r, g, b, 255);
	}
	
	public UIColor(int r, int g, int b, int a)
	{
		this.color = new Color(r, g, b, a);
	}
	
	public UIColor(String hex)
	{
		this.color = Color.decode(hex);
	}
	

	public void setColor(int r, int g, int b)
	{
		setColor(r,g,b,255);
	}
	
	public void setColor(int r, int g, int b, int a)
	{
		setColor(new Color(r,g,b,a));
	}
	
	public void setColor(String hex)
	{
		setColor(Color.decode(hex));
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void InvertColor()
	{
	    int r = (color.getRGB() >> 16) & 0xFF;
	    int g = (color.getRGB() >> 8) & 0xFF;
	    int b = (color.getRGB() >> 0) & 0xFF;
	    int invertedRed = 255 - r;
	    int invertedGreen = 255 - g;
	    int invertedBlue = 255 - b;

	    this.color = new Color(invertedRed,invertedGreen,invertedBlue,color.getAlpha());
	}
	
	public UIColor setAlpha(int alpha)
	{
		color = new Color(color.getRed(),color.getGreen(),color.getBlue(),alpha);
		return this;
	}
	
	public String toHex() {
		return "#" + toBrowserHexValue(color.getRed()) + toBrowserHexValue(color.getGreen()) + toBrowserHexValue(color.getBlue());
	}
	
	public int toRGB()
	{
		return color.getRGB();
	}
	
	public int getAlpha()
	{
		return color.getAlpha();
	}
	
	public float getNormalizedAlpha()
	{
		return (float)color.getAlpha() / 255f;
	}
	
	public float getNormalizedRed()
	{
		return (float)color.getRed() / 255f;
	}
	
	public float getNormalizedGreen()
	{
		return (float)color.getGreen() / 255f;
	}
	
	
	public float getNormalizedBlue()
	{
		return (float)color.getBlue() / 255f;
	}
	
	
	public int getRed()
	{
		return color.getRed();
	}
	
	public int getGreen()
	{
		return color.getGreen();
	}
	
	public int getBlue()
	{
		return color.getBlue();
	}
	
	public boolean isColorDark(){
	    double darkness = 1-(0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue())/255;
	    if(darkness<0.5){
	        return false; // It's a light color
	    }else{
	        return true; // It's a dark color
	    }
	}
	
	public boolean isVeryDark(){
	    double darkness = 1-(0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue())/255;
	    if(darkness<=0.8){
	        return false; // It's a light color
	    }else{
	        return true; // It's a dark color
	    }
	}
	
	public boolean isVeryLight(){
	    double darkness = 1-(0.299*color.getRed() + 0.587*color.getGreen() + 0.114*color.getBlue())/255;
	    if(darkness<=0.2){
	        return true; // It's a light color
	    }else{
	        return false; // It's a dark color
	    }
	}
	
	private String toBrowserHexValue(int number) {
		StringBuilder builder = new StringBuilder(Integer.toHexString(number & 0xff));
		while (builder.length() < 2) {
			builder.append("0");
		}
		return builder.toString().toUpperCase();
	}

}
