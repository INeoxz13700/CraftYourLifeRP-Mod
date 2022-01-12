package fr.craftyourliferp.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.animations.AnimationManager;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketAnimation extends PacketBase {
	
	public byte animation;
	
	public int entityId;
	
	public PacketAnimation() {
		
	}
	
	
	//Client side packet update animation
	public static PacketAnimation setAnimation(byte animation)
	{
		PacketAnimation packet = new PacketAnimation();
		packet.animation = animation;
		return packet;
	}
	
	public static PacketAnimation setAnimation(byte animation, int entityId)
	{
		PacketAnimation packet = new PacketAnimation();
		packet.animation = animation;
		packet.entityId = entityId;
		return packet;
	}
	

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(animation);
		data.writeInt(entityId);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		animation = data.readByte();
		entityId = data.readInt();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		/*PlayerCachedData.getData(playerEntity).currentAnimation = animation;
		ExtendedPlayer.get(playerEntity).updateRendererDatas();*/
		
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(playerEntity);
		extendedPlayer.currentAnimation = animation;
		extendedPlayer.updateRendererDatas();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleClientSide(EntityPlayer clientPlayer)
	{				
		/*PlayerCachedData cachedData = PlayerCachedData.getData("Client-" + entityId);
		
		
		if(cachedData != null)
		{
			try 
			{
				cachedData.currentAnimation = animation;

				if(animation == 0) return;
							
				cachedData.currentPlayingAnimation = AnimationManager.getAnimation((int)animation);
			} 
			catch (InstantiationException | IllegalAccessException e) 
			{
				e.printStackTrace();
			}
		}*/
		
		Entity entity = clientPlayer.worldObj.getEntityByID(entityId);
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer entityPlayer = (EntityPlayer)entity;
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get(entityPlayer);
			if(extendedPlayer != null)
			{
				try 
				{
					extendedPlayer.currentAnimation = animation;

					if(animation == 0) return;
								
					extendedPlayer.currentPlayingAnimation = AnimationManager.getAnimation((int)animation);
				} 
				catch (InstantiationException | IllegalAccessException e) 
				{
					e.printStackTrace();
				}
			}
		}
		else
		{
			System.out.println("error not a player");
		}
		
		
	}


}
