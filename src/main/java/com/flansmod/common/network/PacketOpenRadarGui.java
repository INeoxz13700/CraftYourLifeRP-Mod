package com.flansmod.common.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.tileentity.TileEntityRadar;
import fr.craftyourliferp.ingame.gui.RadarGui;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketOpenRadarGui extends PacketBase {
	
	public int x;
	public int y;
	public int z;
	
	public int radius;
	public int speedLimit;
	public int penaltyValue;
	public boolean isOpen;


	public PacketOpenRadarGui()
	{
		
	}
	
	public PacketOpenRadarGui(TileEntityRadar te)
	{
			this.x = te.xCoord;
			this.y = te.yCoord;
			this.z = te.zCoord;
						
			this.radius = te.radius;
			this.speedLimit = te.limitSpeed;
			this.penaltyValue = te.penaltyValue;
			this.isOpen = te.isOpen;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeInt(x);
		data.writeInt(y);
		data.writeInt(z);
		
		data.writeInt(radius);
		data.writeInt(speedLimit);
		data.writeInt(penaltyValue);
		data.writeBoolean(isOpen);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		this.x = data.readInt();
		this.y = data.readInt();
		this.z = data.readInt();
		
		this.radius = data.readInt();
		this.speedLimit = data.readInt();
		this.penaltyValue = data.readInt();
		this.isOpen = data.readBoolean();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		
		TileEntityRadar te = (TileEntityRadar) playerEntity.worldObj.getTileEntity(x, y, z);
		
		te.radius = this.radius;
		te.limitSpeed = this.speedLimit;
		te.isOpen = this.isOpen;
		te.penaltyValue = this.penaltyValue;		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		TileEntityRadar te = (TileEntityRadar) clientPlayer.worldObj.getTileEntity(x, y, z);
	
		te.radius = this.radius;
		te.limitSpeed = this.speedLimit;
		te.isOpen = this.isOpen;
		te.penaltyValue = this.penaltyValue;
		
		Minecraft.getMinecraft().displayGuiScreen(new RadarGui(te));
	
	}

}
