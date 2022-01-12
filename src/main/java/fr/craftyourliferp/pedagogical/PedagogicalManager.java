package fr.craftyourliferp.pedagogical;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;

public class PedagogicalManager {

	
	public HashMap<String,Tab> tabs;
		
	
	public static PedagogicalManager newInstance(URL url) throws IOException
	{
		Gson gson = new Gson();
        InputStreamReader reader = new InputStreamReader(url.openStream(),"UTF-8");
        PedagogicalManager pedagogical = gson.fromJson(reader, PedagogicalManager.class);

        return pedagogical;
	}
	
	public List<Tab> getTabsFromRegion(String regionName)
	{
		List<Tab> tabs = this.tabs.values().stream().filter(x -> x.regions.contains(regionName)).collect(Collectors.toList());
		if(tabs.size() == 0)
		{
			return new ArrayList<> (this.tabs.values());
		}
		return tabs;
	}
	
	public List<String> getCategoriesFromTabs(List<Tab> tabs)
	{
		List<String> categories = new ArrayList();
		for(Tab tab : tabs)
		{
			if(!categories.contains(tab.category))
			{
				categories.add(tab.category);
			}
		}
		return categories;
	}
	
	public List<Tab> getTabsFromSelectedCategory(List<Tab> tabs , String category)
	{
		return tabs.stream().filter(x -> x.category.equalsIgnoreCase(category)).collect(Collectors.toList());
	}
	


}
