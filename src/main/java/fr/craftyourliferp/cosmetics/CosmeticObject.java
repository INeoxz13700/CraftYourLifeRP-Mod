package fr.craftyourliferp.cosmetics;

import java.util.List;
import java.util.stream.Collectors;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketCosmetic;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;

public class CosmeticObject {
		
	private boolean isEquipped;
	
	private boolean isLocked;
	
	private String cosmeticName;
	
	private int id;
	
	public ItemStack is;
	
	private ModelBase renderModel;
	
	/*
	 * 0:hat
	 * 1:face
	 * 2:body
	 */
	private byte type;
	
	public CosmeticObject(String name, boolean unlockedDefault, byte type, int id, ItemStack is)
	{
		this.cosmeticName = name;
		this.isLocked = unlockedDefault;
		this.type = type;
		this.id = id;
		this.is = is;
	}
	
	@SideOnly(Side.CLIENT)
	public CosmeticObject setModel(ModelBase model)
	{
		this.renderModel = model;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public ModelBase getModel()
	{
		return this.renderModel;
	}
	
	public boolean getIsLocked()
	{
		return this.isLocked;
	}
	
	public boolean getIsEquipped()
	{
		return this.isEquipped;
	}

	
	public void setEquipped(boolean equipped)
	{
		this.isEquipped = equipped;
	}
	
	public void setLocked(boolean locked)
	{
		this.isLocked = locked;
	}
	
	public String getName()
	{
		return cosmeticName;
	}
	
	public byte getType()
	{
		return type;
	}
	
	public int getId()
	{
		return this.id;
	}

	
	public void writeToNBT(NBTTagCompound compound)
	{
		compound.setBoolean("Equip", isEquipped);
		compound.setBoolean("isLocked", isLocked);
	}
	
    public void loadNBTData(NBTTagCompound compound) 
    {
    	if(compound.hasKey("Equip"))
    	{
        	isEquipped = compound.getBoolean("Equip");
    	}
    	if(compound.hasKey("isLocked"))
    	{
        	isLocked = compound.getBoolean("isLocked");
    	}
    }
    
    public static List<CosmeticObject> getEquippedCosmeticFromSameType(EntityPlayer player, byte type)
	{
		ExtendedPlayer ep = ExtendedPlayer.get(player);
		return ep.getEquippedCosmetics().stream().filter(x -> x.type == type).collect(Collectors.toList());
	}

    
    public static void setCosmetiqueUnlocked(EntityPlayer player, int id)
	{
		CosmeticObject cosmetic = ExtendedPlayer.get(player).getCosmeticById(id);
		cosmetic.setLocked(false);
		player.addChatComponentMessage(new ChatComponentText("§6" + cosmetic.getName() + " §aunlocked!"));
	}
    
    public static void setCosmetiqueLocked(EntityPlayer player, int id)
	{
		CosmeticObject cosmetic = ExtendedPlayer.get(player).getCosmeticById(id);
		cosmetic.setLocked(true);
		player.addChatComponentMessage(new ChatComponentText("§6" + cosmetic.getName() + " §alocked!"));
	}
	
	public static boolean equipCosmetic(EntityPlayer player, int id)
	{
				
		CosmeticObject cosmetic = ExtendedPlayer.get(player).getCosmeticById(id);
		
		if(cosmetic.isEquipped)
		{

			return false;
		}
		
		if(cosmetic.isLocked)
		{
			return false;
		}
		

		
		 cosmetic.isEquipped = true;
		
		 
		if(player.worldObj.isRemote)
		{
			CraftYourLifeRPMod.packetHandler.sendToServer(new PacketCosmetic((byte)0,id));
		}
		
		return true;
		
	}

	
	public static boolean unequipCosmetic(EntityPlayer player, int id)
	{
		CosmeticObject cosmetic = ExtendedPlayer.get(player).getCosmeticById(id);
		
		if(cosmetic == null) return false;
		
		if(!cosmetic.isEquipped)
		{
			return false;
		}
		
		cosmetic.isEquipped = false;
		
		if(player.worldObj.isRemote)
		{
			CraftYourLifeRPMod.packetHandler.sendToServer(new PacketCosmetic((byte)1,id));
		}
		
		return true;
	}
	


}
