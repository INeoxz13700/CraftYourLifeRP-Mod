package fr.craftyourliferp.phone.web;

import java.util.Random;

import fr.craftyourliferp.phone.Tor;
import net.minecraft.util.MathHelper;

public class PageLoading 
{
	
	private boolean loaded = false;
	
	private int loadedState = 0;
	
	private Random rand;
	
	private Tor navigator;
	
	private WebPageUI page;
	
	public PageLoading(WebPageUI page, Tor navigator)
	{
		rand = new Random();
		this.navigator = navigator;
		this.page = page;
	}
	
	public void update()
	{
		if(loaded) return;
		
		loadedState += MathHelper.getRandomIntegerInRange(rand, 0, 5);
		
		if(loadedState >= 100)
		{
			loaded = true;
			displayPage();
		}
	}
	
	public void displayPage()
	{
		Tor.WebPage page = (Tor.WebPage) navigator.getChild(0);
		page.displayPage(this.page);
	}
	
	public boolean pageLoaded()
	{
		return loaded;
	}
	
	public int getState()
	{
		return loadedState;
	}

}
