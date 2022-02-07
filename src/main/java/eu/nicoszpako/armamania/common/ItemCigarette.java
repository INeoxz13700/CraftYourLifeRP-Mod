package eu.nicoszpako.armamania.common;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketAlcol;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCigarette extends Item 
{

	public ItemCigarette()
	{
		setMaxStackSize(1);
		setMaxDamage(1000);
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {		
		player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
		

		
		return super.onItemRightClick(itemstack, world, player);
	}

	
	@Override
    public ItemStack onEaten(ItemStack is, World world, EntityPlayer player)
    {   
        if (!world.isRemote)
        {
        	CraftYourLifeRPMod.packetHandler.sendToAllAround(PacketAlcol.syncCigaretteUseFinish(player), player.posX, player.posY, player.posZ, 32, player.dimension);

        	is.damageItem(50, player);
	        world.playSoundAtEntity(player, "craftyourliferp:smoking_1", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        	if(is.getItemDamage() >= is.getMaxDamage())
        	{
    		    if (!player.capabilities.isCreativeMode)
    		    {
    		    	is.stackSize--;
    		    }
    	        
    	        player.setItemInUse(null, 0);
        	}
        }
        else
        {
			world.spawnParticle("cloud", player.posX + player.getLookVec().xCoord, player.posY + player.getEyeHeight() / 2 + player.getLookVec().yCoord, player.posZ + player.getLookVec().zCoord, player.getLookVec().xCoord * 0.3f , 0.6+player.getLookVec().yCoord,player.getLookVec().zCoord* 0.3f);
			world.spawnParticle("cloud", player.posX + player.getLookVec().xCoord-0.5, player.posY + player.getEyeHeight() / 2 + player.getLookVec().yCoord, player.posZ + player.getLookVec().zCoord, player.getLookVec().xCoord * 0.3f, 0.6+player.getLookVec().yCoord, player.getLookVec().zCoord* 0.3f);
			world.spawnParticle("cloud", player.posX + player.getLookVec().xCoord+0.5, player.posY + player.getEyeHeight() / 2 + player.getLookVec().yCoord, player.posZ + player.getLookVec().zCoord, player.getLookVec().xCoord * 0.3f, 0.6+player.getLookVec().yCoord, player.getLookVec().zCoord* 0.3f);
        }

        return is;
    }
	
    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return 20*2;
    }
    
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.none;
    }
    
}

