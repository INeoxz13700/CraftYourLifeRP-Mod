package fr.craftyourliferp.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CRPTab extends CreativeTabs
{
   public CRPTab(String label)
   {
        super(label);
   }

   @Override
   public Item getTabIconItem() {
	   return ModdedItems.identityCard;
   }

   @SideOnly(Side.CLIENT)
   public int func_151243_f()
   {
    return 0; // mettez ici votre metadata
   }

@Override
public boolean hasSearchBar()
{
    return true;
}
}