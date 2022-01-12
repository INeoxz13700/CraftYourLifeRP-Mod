package fr.craftyourliferp.items;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class MarketItem {
	
	private ItemStack item;
		
	private float priceInEuro;
	
	public MarketItem() 
	{
		
	}
	
	public MarketItem(ItemStack item, float priceInEuro)
	{
		this.setItem(item);
		this.setPriceInEuro(priceInEuro);
	}

	public ItemStack getItem() {
		return item;
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public float getPriceInEuro() {
		return priceInEuro;
	}
	
	public float getPriceQuantityInBitcoin(float bitcoinPrice)
	{
		return (float)Math.round((getPriceInEuro() * item.stackSize) / bitcoinPrice * 100) / 100;
	}
	public float getPriceInBitcoin(float bitcoinPrice)
	{
		return (float)Math.round((getPriceInEuro()) / bitcoinPrice * 100) / 100;
	}
	
	public boolean isEqual(MarketItem item, EntityPlayer player)
	{
		return this.item.getItem() == item.getItem().getItem() && this.item.hasDisplayName() == item.getItem().hasDisplayName() && this.item.getDisplayName().equalsIgnoreCase(item.getItem().getDisplayName()) && priceInEuro == item.priceInEuro;
	}
	
	public int getQuantity()
	{
		return item.stackSize;
	}

	public void setPriceInEuro(float priceInEuro) {
		this.priceInEuro = priceInEuro;
	}
	
	public void encode(ByteBuf data)
	{
		ByteBufUtils.writeItemStack(data, item);
		data.writeFloat(priceInEuro);
	}
	
	
	public void decode(ByteBuf data)
	{
		item = ByteBufUtils.readItemStack(data);
		priceInEuro = data.readFloat();
	}
		

}
