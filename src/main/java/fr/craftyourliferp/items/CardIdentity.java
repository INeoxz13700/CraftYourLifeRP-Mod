package fr.craftyourliferp.items;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiIdentityCard;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class CardIdentity extends Item {
	
	public CardIdentity()
	{
		this.setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		this.setMaxStackSize(1);
		this.setUnlocalizedName("IdentityCard");
	}
	
    /*public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player)
    {
    	if(FMLCommonHandler.instance().getSide().isClient())
    	{
	    	if(worldIn.isRemote)
	    	{
	    		Minecraft.getMinecraft().displayGuiScreen(new GuiIdentityCard(player));
	    	}
    	}
    	return itemStackIn;
    }*/

}
