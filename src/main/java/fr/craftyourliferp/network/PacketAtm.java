package fr.craftyourliferp.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiAtm;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;

public class PacketAtm extends PacketBase
{
	
	/*
	 * 0: put
	 * 1: take
	 * 2: openAtm
	 */
    private byte action;
    
    private int value;
    
    private boolean usingAtm;

    
    public PacketAtm()
    {
    	
    }
   
    public static PacketAtm packetOpenAtm()
    {
    	PacketAtm packet = new PacketAtm();
    	packet.action = 2;
    	return packet;
    }
    
    @SideOnly(Side.CLIENT)
    public static PacketAtm packetMoneyInterraction(byte action, int value)
    {
    	PacketAtm packet = new PacketAtm();
    	packet.action = action;
    	packet.value = value;
    	packet.usingAtm = Minecraft.getMinecraft().currentScreen instanceof GuiAtm;
    	return packet;
    }
    
    public PacketAtm(byte action, int value)
    {
    	this.action = action;
    	this.value = value;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {    	
    	data.writeByte(action);
  
    	if(action != 2)
    	{
	    	data.writeInt(value);
	    	data.writeBoolean(usingAtm);
    	}
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
        action = data.readByte();
        
        if(action != 2)
        {
	        value = data.readInt();
	        usingAtm = data.readBoolean();
        }
    }

    @Override
    public void handleServerSide(EntityPlayerMP player)
    {
    	 
	    
	    
    	if(action == 0)
    	{
    		 NetHandlerPlayServer p;
    		 if(!usingAtm)
    		 {
    			 System.out.println("/atm admin put " + player.getCommandSenderName() + " " + value + " 20");
    		 }
    		 else
    		 {
    			 System.out.println("/atm admin put " + player.getCommandSenderName() + " " + value);
    		 }
    	}
        else
    	{
    		 if(!usingAtm)
    		 {
    			 System.out.println("/atm admin take " + player.getCommandSenderName() + " " + value + " 20");
    		 }
    		 else
    		 {
        		 System.out.println("/atm admin take " + player.getCommandSenderName() + " " + value); 
    		 }
    	}
    	 

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer client)
    {
    	if(action == 2)
    	{
    		Minecraft.getMinecraft().displayGuiScreen(new GuiAtm());
    	}
    }


         
}
