package fr.craftyourliferp.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.game.events.OverlayRendererListener;
import fr.craftyourliferp.ingame.gui.CylrpMessageHUD;
import fr.craftyourliferp.ingame.gui.NotificationBox.NotificationType;

public class PacketMessageDisplay extends PacketBase
{
    private String message;
    
    public int duration;
    
    /*
     * 0: hudmessage
     * 1: hudsubmessage
     * 2: hudnotification
     */
    public byte type;
    
    public PacketMessageDisplay()
    {
    	
    }
   
    public PacketMessageDisplay(String message, int duration, byte type)
    {
    	this.message = message;
    	this.duration = duration;
    	this.type = type;
    }


    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
    	ByteBufUtils.writeUTF8String(data, message);
    	data.writeInt(duration);
    	data.writeByte(type);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
        message = ByteBufUtils.readUTF8String(data);
        duration = data.readInt();
        type = data.readByte();
    }

    @Override
    public void handleServerSide(EntityPlayerMP player)
    {
     
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer client)
    {
    	if(type == 0 || type == 1) CylrpMessageHUD.sendMessage(message,duration,type);
    }


         
}
