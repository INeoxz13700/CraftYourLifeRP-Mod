package fr.craftyourliferp.data;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CartData {

	private ItemStack is;
	
	private float itemUnityPrice;
			
	public CartData(ItemStack itemstack, float itemUnityPrice)
	{
		is = itemstack;
		this.itemUnityPrice = itemUnityPrice;
	}
	
	public ItemStack getItemStack()
	{
		return is;
	}
	
	public int getId()
	{
		return Item.getIdFromItem(is.getItem());
	}
	
	public int getMetaId()
	{
		return is.getItemDamage();
	}
	
	public int getQuantity()
	{
		return is.stackSize;
	}
	
	public float getItemUnityPrice()
	{
		return itemUnityPrice;
	}
	
}
