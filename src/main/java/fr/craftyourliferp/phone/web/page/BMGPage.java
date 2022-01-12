package fr.craftyourliferp.phone.web.page;

import java.util.ArrayList;
import java.util.List;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.items.MarketItem;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketBMGPage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class BMGPage extends WebPageData {

	//public PlayerCachedData data;
	
	public ExtendedPlayer persistentData;
	
	public EntityPlayer player;
	
	public List<MarketItem> items = new ArrayList();
	
	public float bitcoinPrice;
	
	public boolean transactionSuccess = false;
	
	public BMGPage(EntityPlayer player)
	{
		this.player = player;
		
		//data = PlayerCachedData.getData(player);
		persistentData = ExtendedPlayer.get(player);
	}
	
	/*public void addItemToCart(MarketItem item)
	{
		if(player.worldObj.isRemote)
		{
			data.shopData.addItemToCart(item);
		}
		else
		{
			if(itemAvaibleInMarket(item))
			{
				data.shopData.addItemToCart(item);
			}
		}
	}*/
	
	public void addItemToCart(MarketItem item)
	{
		if(player.worldObj.isRemote)
		{
			persistentData.shopData.addItemToCart(item);
		}
		else
		{
			if(itemAvaibleInMarket(item))
			{
				persistentData.shopData.addItemToCart(item);
			}
		}
	

	}
	
	/*public void removeItemFromCart(MarketItem item)
	{
		data.shopData.removeItemFromCart(item);
	}*/
	
	public void removeItemFromCart(MarketItem item)
	{
		persistentData.shopData.removeItemFromCart(item);
	}
	
	public boolean itemAvaibleInMarket(MarketItem item)
	{
		for(MarketItem marketItem : items)
		{
			if(marketItem.isEqual(item, player)) return true;
		}
		return false;
	}

	
	public List<MarketItem> getItemsAvaibleInMarket()
	{
		return items;
	}
	
	/*public float getTotalPrice()
	{
		float total = 0f;
		for(MarketItem data : this.data.shopData.shopCart)
		{
			total += (data.getPriceInBitcoin(bitcoinPrice) * data.getQuantity());
		}
		return total;
	}*/
	
	public float getTotalPrice()
	{
		float total = 0f;
		for(MarketItem data : persistentData.shopData.shopCart)
		{
			total += (data.getPriceInBitcoin(bitcoinPrice) * data.getQuantity());
		}
		return total;
	}
	
	/*public void confirmTransaction()
	{
		if(player.worldObj.isRemote)
		{
			CraftYourLifeRPMod.packetHandler.sendToServer(PacketBMGPage.transactionPacket(data.shopData.shopCart));
		}
		else
		{
			if(data.shopData.shopCart.size() == 0)
			{
				player.addChatComponentMessage(new ChatComponentText("§cIl n'y a pas d'article!"));
				return;
			}
			
			float price = getTotalPrice();
			
			if(persistentData.bitcoin >= price)
			{
				persistentData.bitcoin -= price;
				
				for(MarketItem item : data.shopData.shopCart)
				{
					persistentData.itemStockage.add(item.getItem());
				}
				
				data.shopData.shopCart.clear();
				
				updatePageData();

				CraftYourLifeRPMod.packetHandler.sendTo(new PacketBMGPage(5), (EntityPlayerMP)player);
			}
			else
			{
				data.shopData.shopCart.clear();

				CraftYourLifeRPMod.packetHandler.sendTo(new PacketBMGPage(6),  (EntityPlayerMP)player);
			}
		}
	}*/
	
	public void confirmTransaction()
	{
		if(player.worldObj.isRemote)
		{
			CraftYourLifeRPMod.packetHandler.sendToServer(PacketBMGPage.transactionPacket(persistentData.shopData.shopCart));
		}
		else
		{
			if(persistentData.shopData.shopCart.size() == 0)
			{
				player.addChatComponentMessage(new ChatComponentText("§cIl n'y a pas d'article!"));
				return;
			}
			
			float price = getTotalPrice();
			
			if(persistentData.bitcoin >= price)
			{
				persistentData.bitcoin -= price;
				
				for(MarketItem item : persistentData.shopData.shopCart)
				{
					persistentData.itemStockage.add(item.getItem());
				}
				
				persistentData.shopData.shopCart.clear();
				
				updatePageData();

				CraftYourLifeRPMod.packetHandler.sendTo(new PacketBMGPage(5), (EntityPlayerMP)player);
			}
			else
			{
				persistentData.shopData.shopCart.clear();

				CraftYourLifeRPMod.packetHandler.sendTo(new PacketBMGPage(6),  (EntityPlayerMP)player);
			}
		}
	}
	
	/*public void syncMarketItem()
	{
		if(!data.getPlayer().worldObj.isRemote)
		{
			CraftYourLifeRPMod.packetHandler.sendTo(PacketBMGPage.syncMarketPacket(items), (EntityPlayerMP)data.getPlayer());
		}
	}*/
	
	public void syncMarketItem()
	{
		if(!persistentData.getPlayer().worldObj.isRemote)
		{
			CraftYourLifeRPMod.packetHandler.sendTo(PacketBMGPage.syncMarketPacket(items), (EntityPlayerMP)persistentData.getPlayer());
		}
	}
	
	/*public void initPage() 
	{
		if(!data.getPlayer().worldObj.isRemote)
		{
			items = WorldData.get(data.getPlayer().worldObj).getMarketData().getItems();
			syncMarketItem();
			updatePageData();
		}
	}*/
	
	public void initPage() 
	{
		if(!persistentData.getPlayer().worldObj.isRemote)
		{
			items = WorldData.get(persistentData.getPlayer().worldObj).getMarketData().getItems();
			syncMarketItem();
			updatePageData();
		}
	}
	
	public void initPage(float bitcoinPrice) 
	{
		this.bitcoinPrice = bitcoinPrice;
	}
	
	
	@Override
	public byte pageId() 
	{
		return 2;
	}

	@Override
	public void updatePageData()
	{
		persistentData.syncMoney();
	}

}
