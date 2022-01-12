package fr.craftyourliferp.items;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;

public class ItemNuclearPickaxe extends ItemPickaxe {

	
	public ItemNuclearPickaxe() {
		super(ModdedItems.nuclearMaterial);
		this.setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
	}
		
	public boolean getIsRepairable(ItemStack input, ItemStack repair)
	{
		return false;
	}

	
	
}
