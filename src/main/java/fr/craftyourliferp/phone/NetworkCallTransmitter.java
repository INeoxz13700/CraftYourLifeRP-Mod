package fr.craftyourliferp.phone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.craftyourliferp.data.NumberData;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketConnectingCall;
import fr.craftyourliferp.network.PacketFinishCall;
import fr.craftyourliferp.network.PacketSendVoice;
import fr.craftyourliferp.network.PacketStartCall;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;

public class NetworkCallTransmitter {
	
	public static List<NetworkCallTransmitter> calls = new ArrayList<NetworkCallTransmitter>();
	
	private EntityPlayer caller;
	
	private String callerNumber;
	
	private String callreceiverNumber;
	
	private EntityPlayer callreceiver;
	
	public boolean callConnected = false;
	
	public NetworkCallTransmitter(EntityPlayer caller, EntityPlayer callreceiver, String callerNumber, String receiverNumber) throws Exception
	{
		this.caller = caller;
		this.callreceiver = callreceiver;
		this.callerNumber = callerNumber;
		this.callreceiverNumber = receiverNumber;
				
		if(NetworkCallTransmitter.getByUsername(this.callreceiver.getCommandSenderName()) != null)
		{
			throw new Exception("Number has already in call");
		}
		
		this.calls.add(this);
	}
	
	public boolean continueCall()
	{		
		World world = MinecraftServer.getServer().getEntityWorld();
		if(world.getPlayerEntityByName(caller.getCommandSenderName()) == null || world.getPlayerEntityByName(callreceiver.getCommandSenderName()) == null)
			return false;
		
		return true;
	}
	
	public void finishCall(EntityPlayer packetSender)
	{
		if(packetSender == this.caller)
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketFinishCall(), (EntityPlayerMP) this.callreceiver);

		else if(packetSender == this.callreceiver)
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketFinishCall(), (EntityPlayerMP) this.caller);

		calls.remove(this);
	}
	
	public void finishCall()
	{
		CraftYourLifeRPMod.packetHandler.sendTo(new PacketFinishCall(), (EntityPlayerMP) this.caller);
		CraftYourLifeRPMod.packetHandler.sendTo(new PacketFinishCall(), (EntityPlayerMP) this.callreceiver);
		calls.remove(this);
	}
	
	public void sendCallRequest()
	{
		CraftYourLifeRPMod.packetHandler.sendTo(new PacketStartCall(this.callerNumber), (EntityPlayerMP) this.callreceiver);
	}
	
	/*
	 * Type == 0 client refused call
	 * Type == 1 client accepted call
	 * Type == 2 client isAlready in call
	 * Type == 3 client notConnected
	 * Type == 4 unknown number
	 */
	public void callRequestAnswer(int type)
	{
		if(type == 0)
		{
			this.callConnected = false;
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketConnectingCall(type), (EntityPlayerMP)caller);
		}
		else if(type == 1)
		{
			this.callConnected = true;
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketConnectingCall(type), (EntityPlayerMP)caller);
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketConnectingCall(type), (EntityPlayerMP)this.callreceiver);
		}
		else if(type == 2)
		{
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketConnectingCall(type), (EntityPlayerMP)caller);
			this.callConnected = false;
		}
	}
	
	public static NetworkCallTransmitter getByUsername(String username)
	{
		for(NetworkCallTransmitter nt : calls)
		{
			if(username.equalsIgnoreCase(nt.caller.getCommandSenderName()) || username.equalsIgnoreCase(nt.callreceiver.getCommandSenderName()))
			{
				return nt;
			}
		}
		return null;
	}
	
	public void TransmitVoiceData(EntityPlayer sender, byte[] data)
	{
		if(!this.continueCall())
		{
			this.finishCall();
			return;
		}
		
		if(sender == this.caller)
		{
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketSendVoice(data, sender.getCommandSenderName()),(EntityPlayerMP) this.callreceiver);
		}
		else
		{
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketSendVoice(data, sender.getCommandSenderName()),(EntityPlayerMP) this.caller);
		}
	}
	
	
	
}
