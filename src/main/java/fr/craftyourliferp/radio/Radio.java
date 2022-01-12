package fr.craftyourliferp.radio;

public class Radio {

	
	private String title;
	
	private String currentPlayedTitle;
	
	private String fluxUrl;
	
	private String icoUrl;
	
	private String parameters;
	
	private int index;
	
	public Radio(String title, String fluxUrl, String icoUrl, String parameters, int index)
	{
		this.title = title;
		this.fluxUrl = fluxUrl;
		this.icoUrl = icoUrl;
		this.parameters = parameters;
		this.index = index;
	}
	
	public String getTittle()
	{
		return this.title;
	}
	
	public void setTittle(String title)
	{
		this.title = title;
	}
	
	public void setCurrentPlayedTitle(String title)
	{
		this.currentPlayedTitle = title;
	}
	
	public String getCurrentPlayedTitle()
	{
		return this.currentPlayedTitle;
	}
	
	public String getFlux()
	{
		return this.fluxUrl;
	}
	
	public String getIcoUrl()
	{
		return this.icoUrl;
	}
	
	public int getIndex()
	{
		return this.index;
	}
	
}
