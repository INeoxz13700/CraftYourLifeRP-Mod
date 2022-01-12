package fr.craftyourliferp.cosmetics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketCosmetic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

public class CosmeticManager {

	private static List<CosmeticObject> registeredCosmetics = new ArrayList();
	

	public CosmeticObject registerCosmetic(String name, boolean unlockedDefault,byte type, int id, ItemStack is)
	{
		CosmeticObject cosmeticObj = new CosmeticObject(name, unlockedDefault, type, id, is);
		registeredCosmetics.add(cosmeticObj);
		return cosmeticObj;
	}
	
	public CosmeticObject getCopy(int id)
	{
		Optional<CosmeticObject> optional = registeredCosmetics.stream().filter(x -> x.getId() == id).findFirst();
		CosmeticObject obj = null;
		if(optional.isPresent())
		{
			obj = optional.get();
		}
		else
		{
			return null;
		}
		CosmeticObject copy = new CosmeticObject(obj.getName(),!obj.getIsLocked(),obj.getType(), obj.getId(),obj.is);
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			copy.setModel(obj.getModel());
		}
		return copy;
	}
	
	public CosmeticObject getCopy(CosmeticObject obj)
	{
		CosmeticObject copy = new CosmeticObject(obj.getName(),!obj.getIsLocked(),obj.getType(), obj.getId(),obj.is);
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT)
		{
			copy.setModel(obj.getModel());
		}
		return copy;
	}
	
	public List<CosmeticObject> getCosmetics()
	{
		return registeredCosmetics;
	}
	
	
}
