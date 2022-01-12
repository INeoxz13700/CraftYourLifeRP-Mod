package fr.craftyourliferp.api;

import java.io.IOException;
import java.nio.file.FileSystemException;
import java.util.ArrayList;
import java.util.List;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.radio.Radio;
import fr.craftyourliferp.radio.RadioFileReader;



public class RadioApi {
	
	public List<Radio> radios = new ArrayList<Radio>();

	private RadioApi()
	{
		
	}
	
	public static RadioApi getRadios() throws IOException
	{
		RadioApi api = new RadioApi();
		String[] radioList = new RadioFileReader(CraftYourLifeRPMod.apiIp + "/cylrp.radio").read().getContent();
		int index = 0;
		for(String radioData : radioList)
		{
			String[] radioInformations = radioData.split("\n");
			if(radioInformations.length != 4)
			{
				throw new FileSystemException("Syntax file error contact administrator");
			}
			Radio radio = new Radio(radioInformations[0], radioInformations[2], radioInformations[1],radioInformations[3],index);
			api.radios.add(radio);
			index++;
		}
		
		return api;
	}
	
}
