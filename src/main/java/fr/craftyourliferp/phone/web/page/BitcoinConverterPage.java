package fr.craftyourliferp.phone.web.page;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketBitcoinPage;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class BitcoinConverterPage extends WebPageData{
	
	public ExtendedPlayer playerData;
	
	//public PlayerCachedData tempData;
	
	private float bitcoinInEuro;
	
	public BitcoinConverterPage(EntityPlayer player)
	{
		playerData = ExtendedPlayer.get(player);
		//tempData = PlayerCachedData.getData(player);		
	}
	
	public void convert(byte type, float price)
	{
		
		
		WorldData data = WorldData.get(playerData.getPlayer().worldObj);
		
		float bitcoinPrice = data.getBitcoinPrice();
		
		if(bitcoinPrice <= 0)
		{
			CraftYourLifeRPMod.packetHandler.sendTo(PacketBitcoinPage.displayError((byte)2),(EntityPlayerMP) playerData.getPlayer());
			return;
		}
		
		
		//Euro to bictoin
		if(type == 0)
		{
			/*if(tempData.serverData.money >= price)
			{
				float totalPrice = price / bitcoinPrice;
				ServerUtils.takeMoney(playerData.getPlayer(), price);
				playerData.bitcoin += totalPrice;
			}
			else 
			{
				CraftYourLifeRPMod.packetHandler.sendTo(PacketBitcoinPage.displayError((byte)0),(EntityPlayerMP) playerData.getPlayer());
			}*/
			if(playerData.serverData.money >= price)
			{
				float totalPrice = price / bitcoinPrice;
				ServerUtils.takeMoney(playerData.getPlayer(), price);
				playerData.bitcoin += totalPrice;
			}
			else 
			{
				CraftYourLifeRPMod.packetHandler.sendTo(PacketBitcoinPage.displayError((byte)0),(EntityPlayerMP) playerData.getPlayer());
			}
		}
		//Bitcoin to euro
		else
		{
			if(playerData.bitcoin >= price)
			{
				float totalPrice = price * bitcoinPrice;
				playerData.bitcoin -= price;
				ServerUtils.addMoney(playerData.getPlayer(), totalPrice);
			}
			else
			{
				CraftYourLifeRPMod.packetHandler.sendTo(PacketBitcoinPage.displayError((byte)1),(EntityPlayerMP) playerData.getPlayer());
			}
		}
		initPage();
	}
	
	
	public void initPage()
	{
		World world = playerData.getPlayer().worldObj;
		if(!world.isRemote)
		{
			bitcoinInEuro = WorldData.get(world).getBitcoinPrice();
			CraftYourLifeRPMod.packetHandler.sendTo(PacketBitcoinPage.syncPageData(bitcoinInEuro),(EntityPlayerMP) playerData.getPlayer());
			//tempData.updatePlayerData();
			playerData.updatePlayerData();
		}
	}
	
	public float getBitcoinInEuro()
	{
		return bitcoinInEuro;
	}
	
	public void initPage(float bitcoinInEuro)
	{
		this.bitcoinInEuro = bitcoinInEuro;
	}
	
	@Override
	public byte pageId() {
		return 1;
	}

	@Override
	public void updatePageData()
	{
		playerData.syncMoney();	
	}
	
	

}
