package fr.craftyourliferp.data;

import java.util.ArrayList;
import java.util.List;

import fr.craftyourliferp.items.MarketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class BlackMarketData {

	private List<MarketItem> items;
	
	public BlackMarketData()
	{
		this.items = new ArrayList();
	}
	
	public void writeToNBT(NBTTagCompound compound)
	{
		NBTTagList blackMarketItemsList = new NBTTagList();
		
		for(MarketItem item : items)
		{
			NBTTagCompound tag = new NBTTagCompound();
			
			item.getItem().writeToNBT(tag);
			
			tag.setFloat("Price", item.getPriceInEuro());
			
			blackMarketItemsList.appendTag(tag);
		}
		
		compound.setTag("BlackMarketData", blackMarketItemsList);
	}
	
	public void readFromNBT(NBTTagCompound compound)
	{
		NBTTagList blackMarketItemsList = (NBTTagList) compound.getTag("BlackMarketData");
		
		for(int i = 0; i < blackMarketItemsList.tagCount(); i++)
		{
			NBTTagCompound tag = blackMarketItemsList.getCompoundTagAt(i);
			
			MarketItem item = new MarketItem(ItemStack.loadItemStackFromNBT(tag),tag.getFloat("Price"));
			
			items.add(item);
		}
		
	}
	
	public boolean addItem(MarketItem item)
	{
		if(containsItem(item)) return false;
		
		items.add(item);
		return true;
	}
	
	public boolean containsItem(MarketItem item)
	{
		for(int i = 0; i < items.size(); i++)
		{
			MarketItem marketItem = items.get(i);
			if(marketItem.getItem().getItem() == item.getItem().getItem() && marketItem.getItem().getItemDamage() == item.getItem().getItemDamage() && item.getPriceInEuro() == marketItem.getPriceInEuro())
			{
				return true;
			}
			
		}
		return false;
	}
	
	public List<MarketItem> getItems()
	{
		return items;
	}
	
	public void clear()
	{
		items.clear();
	}

	
	
}


