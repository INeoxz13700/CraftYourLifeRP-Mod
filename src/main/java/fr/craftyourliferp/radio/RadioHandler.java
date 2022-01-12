package fr.craftyourliferp.radio;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import fr.craftyourliferp.api.RadioApi;
import fr.craftyourliferp.utils.MathsUtils;
import jaco.mp3.player.MP3Player;

public class RadioHandler {
	
	private Radio[] radio;

	private MP3Player player;
	
	private Radio currentRadio;
	
	private int index;
	
	private float currentVolume = 1f;
	
	public boolean canPlay = true;
	
	
	public RadioHandler()
	{
		this.updateRadios();
	}
	
	public void playRadio(int index)
	{
		this.index = index;
		playRadio();
	}
	
	public void playRadio()
	{		
		canPlay = false;
		
		if(radio == null || radio.length == 0) return;
		
		try {
			setRadio();
			player.play();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void stop()
	{
		currentRadio = null;
		if(player!=null)player.stop();
	}
	
	public void nextRadio() throws IOException
	{	
		if(radio == null) return;
		
		index = MathsUtils.Clamp(index+1, 0, radio.length-1);
		playRadio();
	}
	
	public void previousRadio() throws IOException
	{	
		if(radio == null) return;

		index = MathsUtils.Clamp(index-1, 0, radio.length-1);
		playRadio();
	}
	
	public void setRadio() throws IOException
	{
		currentRadio = radio[index];
		if(player != null) player.stop();
		player = new MP3Player(new URL(currentRadio.getFlux()));
	}
	
	public Radio getCurrentRadio()
	{
		return currentRadio;
	}
	
	public int getCurrentIndex()
	{
		return this.index;
	}
	
	public void updateRadios()
	{
		try {
			List<Radio> radios = RadioApi.getRadios().radios;
			radio = new Radio[radios.size()];
			for(int i = 0; i < radio.length; i++) radio[i] = radios.get(i);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	
}
