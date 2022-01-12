package fr.craftyourliferp.network;

import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Base64;

import cpw.mods.fml.common.network.ByteBufUtils;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.UserSession;
import fr.craftyourliferp.game.events.EventsListener;
import fr.craftyourliferp.ingame.gui.CylrpMessageHUD;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;

public class PacketAuthentification extends PacketBase {

	
	private String cryptedPassword = "";
	
	private String connectionData;
	
	public PacketAuthentification()
	{
		
	}
	
	public PacketAuthentification(String connectionData, String password)
	{
		this.cryptedPassword = password;
		this.connectionData = connectionData;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		ByteBufUtils.writeUTF8String(data, cryptedPassword);
		ByteBufUtils.writeUTF8String(data, connectionData);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		cryptedPassword = ByteBufUtils.readUTF8String(data);
		connectionData = ByteBufUtils.readUTF8String(data);
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(playerEntity);
		
		if(CraftYourLifeRPMod.localhost)
		{
			/*playerEntity.setInvisible(false);			
			PlayerCachedData data = PlayerCachedData.getData(playerEntity);
			data.passwordReceived = true;
			EventsListener.onPlayerAuthentificated(playerEntity);
			data.connectionPos = null;
			return;*/
			playerEntity.setInvisible(false);			
			extendedPlayer.passwordReceived = true;
			EventsListener.onPlayerAuthentificated(playerEntity);
			extendedPlayer.connectionPos = null;
			return;
		}
		
		if(extendedPlayer.authentificationAttempt >= ExtendedPlayer.authentificationAttemptEveryMinutes)
		{
			if((System.currentTimeMillis() - extendedPlayer.authentificationAttemptResetTime) / 1000 >= 60)
			{
				extendedPlayer.authentificationAttempt = 0;
				extendedPlayer.authentificationAttemptResetTime = System.currentTimeMillis();
			}
			else
			{
				return;
			}
		}	
		extendedPlayer.authentificationAttempt++;
		
		/*Thread connectionThread = new Thread(new Runnable() 
		{
		      @Override
		      public void run() 
		      {
		    	    Object[] cData = UserSession.connectUser(playerEntity.getCommandSenderName(), cryptedPassword, "borutoisalegend1313@@," + connectionData);
		  		
			  		if(!(boolean)cData[3])
			  		{
			  			playerEntity.playerNetServerHandler.kickPlayerFromServer((String)cData[2]);
			  			return;
			  		}
			  		else
			  		{
			  			playerEntity.addChatMessage(new ChatComponentText((String)cData[2]));
			  		}
			  		
			  		playerEntity.setInvisible(false);
			  		
			  		PlayerCachedData data = PlayerCachedData.getData(playerEntity);
			  		data.passwordReceived = true;
			  		EventsListener.onPlayerAuthentificated(playerEntity);
			  		data.connectionPos = null;
		      }
		 }, "AuthentificationThread");

		connectionThread.setDaemon(true);
		connectionThread.start();*/
		
		Thread connectionThread = new Thread(new Runnable() 
		{
		      @Override
		      public void run() 
		      {
		    	    Object[] cData = UserSession.connectUser(playerEntity.getCommandSenderName(), cryptedPassword, "borutoisalegend1313@@," + connectionData);
		  		
			  		if(!(boolean)cData[3])
			  		{
			  			playerEntity.playerNetServerHandler.kickPlayerFromServer((String)cData[2]);
			  			return;
			  		}
			  		else
			  		{
			  			playerEntity.addChatMessage(new ChatComponentText((String)cData[2]));
			  		}
			  		
			  		playerEntity.setInvisible(false);
			  		
			  		extendedPlayer.passwordReceived = true;
			  		EventsListener.onPlayerAuthentificated(playerEntity);
			  		extendedPlayer.connectionPos = null;
		      }
		 }, "AuthentificationThread");

		connectionThread.setDaemon(true);
		connectionThread.start();
		
		
		
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) 
	{
		for(int i = 0; i < 5; i++) CraftYourLifeRPMod.getClientData().wantedLevel[i] = false;
		
		CylrpMessageHUD.sendMessage("&dCYLRP " + "v&a" + CraftYourLifeRPMod.getClientData().version, 500,(byte)1);
		
		//CraftYourLifeRPMod.getClientData().cachedData = PlayerCachedData.createCachedData(clientPlayer);
		CraftYourLifeRPMod.getClientData().cachedData = ExtendedPlayer.get(clientPlayer);

		
		
		if(CraftYourLifeRPMod.getClientData().currentSession == null)
		{
			System.out.println("Impossible de récupérer la session contacter KarmaOwner");
		}
		else
		{
			if(!Minecraft.getMinecraft().isIntegratedServerRunning())
			{
				String uid;
				try 
				{
					uid = CraftYourLifeRPClient.getComputerUID();
				} catch (IOException | InterruptedException e) {
					e.printStackTrace();
					uid = "undefined";
				}
				
				String macAdress;
				try {
					macAdress = CraftYourLifeRPClient.getMacAdress();
				} catch (UnknownHostException | SocketException e) {
					e.printStackTrace();
					macAdress = "undefined";
				}
				
				String connectionData = uid + "," + macAdress;
				CraftYourLifeRPMod.packetHandler.sendToServer(new PacketAuthentification(connectionData,CraftYourLifeRPMod.getClientData().currentSession.getCryptedPassword()));
			}
		}

	}

}
