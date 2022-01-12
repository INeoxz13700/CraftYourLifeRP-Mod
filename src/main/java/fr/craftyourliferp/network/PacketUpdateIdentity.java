package fr.craftyourliferp.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class PacketUpdateIdentity extends PacketBase {
	
	public String lastName;
	public String name;
	public String birthday;
	public String gender;

	public PacketUpdateIdentity()
	{
		
	}
	
	public PacketUpdateIdentity(String lastName, String name, String birthday, String gender) {
		this.lastName = lastName;
		this.name = name;
		this.birthday = birthday;
		this.gender = gender;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		ByteBufUtils.writeUTF8String(data, this.lastName);
		ByteBufUtils.writeUTF8String(data, this.name);
		ByteBufUtils.writeUTF8String(data, this.birthday);
		ByteBufUtils.writeUTF8String(data, this.gender);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		this.lastName = ByteBufUtils.readUTF8String(data);
		this.name = ByteBufUtils.readUTF8String(data);
		this.birthday = ByteBufUtils.readUTF8String(data);
		this.gender = ByteBufUtils.readUTF8String(data);
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		ExtendedPlayer pData = ExtendedPlayer.get(playerEntity);
		if(pData.identityData.waitingDataFromClient)
		{
			ExtendedPlayer.get(playerEntity).identityData.updateData(lastName, name, birthday, gender);
			
			if(pData.firstJoin)
			{
				for(String i : CraftYourLifeRPMod.startItem)
				{
					String[] splitedData = i.split("-");
					int id = Integer.parseInt(splitedData[0]);
					int quantity = Integer.parseInt(splitedData[1]);
					ItemStack it = new ItemStack(Item.getItemById(id), quantity);
					playerEntity.inventory.addItemStackToInventory(it);
				}				
				pData.firstJoin = false;
			}

			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			lastName = lastName.replaceAll(" ", "_");
			name = name.replaceAll(" ", "_");
			System.out.println("/bidentity " + playerEntity.getCommandSenderName() + " " + lastName + " " + name);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		ExtendedPlayer.get(clientPlayer).identityData.updateData(lastName, name, birthday, gender);
	}

}
