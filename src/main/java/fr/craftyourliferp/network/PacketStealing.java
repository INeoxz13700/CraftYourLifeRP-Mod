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
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.tileentity.TileEntity;
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
import fr.craftyourliferp.blocks.tileentity.IStealingTileEntity;
import fr.craftyourliferp.blocks.tileentity.TileEntityPainting;
import fr.craftyourliferp.ingame.gui.GuiAtm;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;

public class PacketStealing extends PacketBase
{
	
    private int stealingBlockX;
    private int stealingBlockY;
    private int stealingBlockZ;

    
    public PacketStealing()
    {
    	
    }
   
    public static PacketStealing notificateClient(int x, int y, int z)
    {
    	PacketStealing packet = new PacketStealing();
    	
    	packet.stealingBlockX = x;
    	packet.stealingBlockY = y;
    	packet.stealingBlockZ = z;
    	
    	return packet;
    }
  
    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {    	
    	data.writeInt(stealingBlockX);
    	data.writeInt(stealingBlockY);
    	data.writeInt(stealingBlockZ);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
    	stealingBlockX = data.readInt();
    	stealingBlockY = data.readInt();
    	stealingBlockZ = data.readInt();
    }

    @Override
    public void handleServerSide(EntityPlayerMP player)
    {
    
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer client)
    {
    	TileEntity tile = client.worldObj.getTileEntity(stealingBlockX, stealingBlockY, stealingBlockZ);
    	
    	if(tile == null) return;
    	
    	if(tile instanceof IStealingTileEntity)
    	{
    		IStealingTileEntity stealingTile = (IStealingTileEntity) tile;

        	if(CraftYourLifeRPClient.cachedData.stealingTile != null)
        	{
        		CraftYourLifeRPClient.cachedData.setNotStealing();
        	}
        	else
        	{
        			
            	CraftYourLifeRPClient.cachedData.stealingTile = (IStealingTileEntity) tile;
            	CraftYourLifeRPClient.cachedData.stealingTile.setStealingEntity(client);
        	}
    	}

    }


         
}
