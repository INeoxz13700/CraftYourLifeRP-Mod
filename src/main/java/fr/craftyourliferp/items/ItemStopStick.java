package fr.craftyourliferp.items;

import fr.craftyourliferp.blocks.BlockPainting;
import fr.craftyourliferp.entities.EntityStopStick;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class ItemStopStick extends Item {

	private int size;
	
	public ItemStopStick(int size) {
		this.size = size;
		setMaxStackSize(1);
		setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		setUnlocalizedName("stop_stick_" + size);
	}
	
	
	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
	{
	        if (world.isRemote)
	        {
	            return true;
	        }
	        else
	        {
	        	
	           if(!ServerUtils.canUseStopStick(player))
	           {
	        		ServerUtils.sendChatMessage(player, "§cVous n'avez pas le métier ou grade nécessaire pour intéragir §cavec cette objet");
	        		return false;
	           }

	           int i1 = MathHelper.floor_double((double)(player.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
	           EntityStopStick entity = new EntityStopStick(world,size,x,y+1,z);
	           entity.rotationYaw = i1*90;
	           world.spawnEntityInWorld(entity);
	           player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
	           return true;
	        }
	   }

}
