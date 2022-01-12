package fr.craftyourliferp.network;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.cosmetics.CosmeticCachedData;
import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.ingame.gui.GuiCosmetics;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketCosmetic extends PacketBase {
	
	/*
	 * type = 0 equipCosmetic
	 * type = 1 unequipCosmetic
	 * type = 2 unlock cosmetic
	 * type = 3 open interface
	 *
	 */
	public byte type;
	
	public int cosmeticId;
	
	public int entityId;
	
	
	public List<CosmeticObject> cosmeticsToSynchronise = new ArrayList();
	
	
	
	public PacketCosmetic()
	{
				
	}		
			
	public PacketCosmetic(byte type, int id)
	{
		this.type = type;
		this.cosmeticId = id;
	}
	
	public PacketCosmetic(byte type)
	{
		this.type = type;
	}
	
	public PacketCosmetic putList(List<CosmeticObject> list)
	{
		this.cosmeticsToSynchronise = list;
		return this;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(type);		
		if(type == 3)
		{
			data.writeInt(cosmeticsToSynchronise.size());
			for(CosmeticObject obj : cosmeticsToSynchronise)
			{
				data.writeInt(obj.getId());
				data.writeBoolean(obj.getIsLocked());
				data.writeBoolean(obj.getIsEquipped());
			}
		}
		else if(type == 4)
		{
			data.writeInt(entityId);
			data.writeInt(cosmeticsToSynchronise.size());
			for(CosmeticObject obj : cosmeticsToSynchronise)
			{
				data.writeInt(obj.getId());
				data.writeBoolean(obj.getIsEquipped());
			}
		}
		else
		{
			data.writeInt(cosmeticId);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		type = data.readByte();
		if(type == 3)
		{
			int cosmeticCount = data.readInt();
			for(int i = 0; i < cosmeticCount; i++)
			{
				int id = data.readInt();
				boolean locked = data.readBoolean();
				boolean equipped = data.readBoolean();
				CosmeticObject obj = CraftYourLifeRPMod.getCosmeticManager().getCopy(id);
				obj.setLocked(locked);
				obj.setEquipped(equipped);
				cosmeticsToSynchronise.add(obj);
			}
		}
		else if(type == 4)
		{
			entityId = data.readInt();
			int cosmeticCount = data.readInt();
			for(int i = 0; i < cosmeticCount; i++)
			{
				int id = data.readInt();
				boolean equipped = data.readBoolean();
				CosmeticObject obj = CraftYourLifeRPMod.getCosmeticManager().getCopy(id);
				obj.setEquipped(equipped);
				cosmeticsToSynchronise.add(obj);
			}
		}
		else
		{
			cosmeticId = data.readInt();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		if(type == 0) 
		{
			CosmeticObject.equipCosmetic(playerEntity, cosmeticId);
			ExtendedPlayer.get(playerEntity).updateRendererDatas();
		}
		else if(type == 1)
		{
			CosmeticObject.unequipCosmetic(playerEntity, cosmeticId);
			ExtendedPlayer.get(playerEntity).updateRendererDatas();
		}
		else if(type == 3)
		{
			ExtendedPlayer.get(playerEntity).displayCosmeticsGui();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		if(type == 0)
		{
			CosmeticObject.equipCosmetic(clientPlayer, cosmeticId);
		}
		else if(type == 1)
		{
			CosmeticObject.unequipCosmetic(clientPlayer, cosmeticId);
		}
		else if(type == 2)
		{
			CosmeticObject.setCosmetiqueUnlocked(clientPlayer,cosmeticId);
		}
		else if(type == 3)
		{
			ExtendedPlayer ep = ExtendedPlayer.get(clientPlayer);
			ep.cosmeticsData = this.cosmeticsToSynchronise;
			Minecraft.getMinecraft().displayGuiScreen(new GuiCosmetics(ep));
		}
		else if(type == 4)
		{
			
			if(clientPlayer.getEntityId() == entityId)
			{
				ExtendedPlayer ep = ExtendedPlayer.get(clientPlayer);
				for(int i = 0; i < cosmeticsToSynchronise.size(); i++)
				{
					CosmeticObject obj = cosmeticsToSynchronise.get(i);
					CosmeticObject cosmetic = ep.getCosmeticById(obj.getId());
					cosmetic.setEquipped(true);
				}
				return;
			}
			
			CosmeticCachedData data = CosmeticCachedData.getData(entityId);
			data.cosmeticsData = cosmeticsToSynchronise;
		}
	}
	
}