package fr.craftyourliferp.data;

import java.util.ArrayList;
import java.util.List;

import fr.craftyourliferp.items.MarketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;

public class ShopData {
	
	public List<MarketItem> shopCart = new ArrayList();

	public ShopData()
	{
		
	}
	
	public void addItemToCart(MarketItem item)
	{
		if(shopCart.contains(item)) return;
		
		shopCart.add(item);
	}
	
	public void removeItemFromCart(MarketItem item)
	{
		if(!shopCart.contains(item)) return;
		
		shopCart.remove(item);
	}

	
	
	
}
