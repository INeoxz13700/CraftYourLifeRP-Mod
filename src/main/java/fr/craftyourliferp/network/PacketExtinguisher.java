package fr.craftyourliferp.network;

import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityWheel;

import fr.craftyourliferp.fire.Fire;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class PacketExtinguisher extends PacketBase {

	public int xCoord;
	public int yCoord;
	public int zCoord;
	
	//1 block
	//2 entity
	public byte type;
		
	public int entityId;
	
	public PacketExtinguisher() { }
	
	public PacketExtinguisher(int posX, int posY, int posZ)
	{
		this.type = (byte)0;
		this.xCoord = posX;
		this.yCoord = posY;
		this.zCoord = posZ;
	}
	
	public PacketExtinguisher(Entity entity)
	{
		this.type = (byte)1;
		this.entityId = entity.getEntityId();
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(type);
		
		if(type == 0)
		{
			data.writeInt(xCoord);
			data.writeInt(yCoord);
			data.writeInt(zCoord);
		}
		else if(type == 1)
		{
			data.writeInt(entityId);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		type = data.readByte();
		
		if(type == 0)
		{
			xCoord = data.readInt();
			yCoord = data.readInt();
			zCoord = data.readInt();
		}
		else if(type == 1)
		{
			entityId = data.readInt();
		}

	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		World world = playerEntity.worldObj;
		
		if(type == 0)
		{
			if(world.getBlock(xCoord, yCoord, zCoord) == Blocks.fire)
			{
				System.out.println("/fireextinguished " + playerEntity.getCommandSenderName());
				
				
				world.setBlock(xCoord, yCoord, zCoord, Blocks.air);
			}
		}
		else if(type == 1)
		{
			Entity entity = world.getEntityByID(entityId);
			if(entity.isBurning())
			{
				System.out.println("/fireextinguished " + playerEntity.getCommandSenderName());
				entity.extinguish();
				
				if(entity instanceof EntityDriveable)
				{
					for(EntityWheel wheel : ((EntityDriveable) entity).wheels)
					{
						wheel.extinguish();
					}
				}

			}
			
		
		}

	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
		
	}
	
}
