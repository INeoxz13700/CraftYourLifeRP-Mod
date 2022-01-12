package fr.craftyourliferp.cosmetics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CosmeticCachedData {

	private static HashMap<Integer,CosmeticCachedData> cache = new HashMap();
	
	public List<CosmeticObject> cosmeticsData = new ArrayList();
	
	public static CosmeticCachedData getData(int entityId)
	{
		if(cache.containsKey(entityId))
		{
			return cache.get(entityId);
		}
		else
		{
			CosmeticCachedData cachedData = new CosmeticCachedData();
			cache.put(entityId,cachedData);
			return cachedData;
		}
	}
	
	
}
