package fr.craftyourliferp.network;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.phone.Tor;
import fr.craftyourliferp.phone.web.page.BitcoinConverterPage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketBitcoinPage extends PacketBase 
{
	/*
	 *  0 : openPage
	 *  1 : syncDataFromPage
	 *  2 : convert euro to bictoin
	 *  3 : display error message
	 *  4 : syncUserMoney
	 */
	public byte action;
	
	public byte pageId;
	
	public float price;
	
	public float bitcoinInEuro;
	
	private float userBitcoin;
	
	private float userEuro;
	
	/*
	 *  0: euro to bitcoins
	 *  1: bitcoins to euro
	 */
	public byte convertType;
	
	/*
	 * 0: Not suffiency euro
	 * 1: Not suffiency bitcoin
	 * 2: app maintenance
	 */
	public byte errorType;
	
	public static PacketBitcoinPage convertMoneyToBitcoin(byte type, float price)
	{
		PacketBitcoinPage packet = new PacketBitcoinPage();
		packet.price = price;
		packet.action = 2;
		packet.convertType = type;
		return packet;
	}
	
	public static PacketBitcoinPage syncPlayerMoney(float bitcoin, float euro)
	{
		PacketBitcoinPage packet = new PacketBitcoinPage();
		packet.userBitcoin = bitcoin;
		packet.userEuro = euro;
		packet.action = 4;
		return packet;
	}
	
	public static PacketBitcoinPage syncPlayerBitcoin()
	{
		return syncPlayerMoney(0,0);
	}
	
	
	public static PacketBitcoinPage syncPageData(float bitcoinInEuro)
	{
		PacketBitcoinPage packet = new PacketBitcoinPage();
		packet.action = 1;
		packet.bitcoinInEuro = bitcoinInEuro;
		return packet;
	}
	
	public static PacketBitcoinPage displayError(byte errorType)
	{
		PacketBitcoinPage packet = new PacketBitcoinPage();
		packet.action = 3;
		packet.errorType = errorType;
		return packet;
	}
	
	public static PacketBitcoinPage openPage()
	{
		PacketBitcoinPage packet = new PacketBitcoinPage();
		packet.action = 0;
		return packet;
	}
	
	
	
	public PacketBitcoinPage()
	{
		
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(action);
		if(action == 0)
		{
			data.writeByte(pageId);
		}
		else if(action == 1)
		{
			data.writeFloat(bitcoinInEuro);
		}
		else if(action == 2)
		{
			data.writeFloat(price);
			data.writeByte(convertType);
		}
		else if(action == 3)
		{
			data.writeByte(errorType);
		}
		else if(action == 4)
		{
			data.writeFloat(userBitcoin);
			data.writeFloat(userEuro);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		action = data.readByte();
		if(action == 0)
		{
			pageId = data.readByte();
		}
		else if(action == 1)
		{
			bitcoinInEuro = data.readFloat();
		}
		else if(action == 2)
		{
			price = data.readFloat();
			convertType = data.readByte();
		}
		else if(action == 3)
		{
			errorType = data.readByte();
		}
		else if(action == 4)
		{
			userBitcoin = data.readFloat();
			userEuro = data.readFloat();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		/*if(action == 0)
		{
			PlayerCachedData.getData(playerEntity).openPage(pageId);

		}
		else if(action == 1)
		{
			
		}
		else if(action == 2)
		{


			PlayerCachedData data = PlayerCachedData.getData(playerEntity);
			if(data.currentPageData instanceof BitcoinConverterPage)
			{

				BitcoinConverterPage page = (BitcoinConverterPage) data.currentPageData;
				page.convert(convertType, price);
			}
		}
		else if(action == 4)
		{
			ExtendedPlayer.get(playerEntity).syncMoney();
		}*/
		
		if(action == 0)
		{
			ExtendedPlayer.get(playerEntity).openPage(pageId);
		}
		else if(action == 1)
		{
			
		}
		else if(action == 2)
		{
			ExtendedPlayer data = ExtendedPlayer.get(playerEntity);
			if(data.currentPageData instanceof BitcoinConverterPage)
			{
				BitcoinConverterPage page = (BitcoinConverterPage) data.currentPageData;
				page.convert(convertType, price);
			}
		}
		else if(action == 4)
		{
			ExtendedPlayer.get(playerEntity).syncMoney();
		}
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) 
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(mc.currentScreen instanceof GuiPhone)
		{
			GuiPhone phone = GuiPhone.getPhone();
			if(phone.currentApp instanceof Tor)
			{
				Tor tor = (Tor) phone.currentApp;
				if(tor.getCurrentOpenPageData() instanceof BitcoinConverterPage)
				{
					BitcoinConverterPage pageData = (BitcoinConverterPage)tor.getCurrentOpenPageData();
					if(action == 1)
					{
						pageData.initPage(bitcoinInEuro);
					}
					else if(action == 3)
					{
						if(errorType == 0)
						{
							phone.displayToast("Vous n'avez pas suffisament d'euro", 4);
						}
						else if(errorType == 1)
						{
							phone.displayToast("Vous n'avez pas suffisament de bitcoin", 4);
						}
						else
						{
							phone.displayToast("Application en maintenance", 4);
						}
					}
				}
			}
		}
		
		if(action == 4)
		{
			ExtendedPlayer.get(clientPlayer).bitcoin = userBitcoin;
			
			if(CraftYourLifeRPClient.cachedData != null) CraftYourLifeRPClient.cachedData.serverData.money = userEuro;
		}
	}
	
	
}
