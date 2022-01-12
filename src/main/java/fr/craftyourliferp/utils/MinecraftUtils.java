package fr.craftyourliferp.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

public class MinecraftUtils {


	
	public static KeyBinding getKeyBinding(String description)
	{
		for(KeyBinding key : Minecraft.getMinecraft().gameSettings.keyBindings)
		{
			if(key.getKeyDescription().equalsIgnoreCase(description))
			{
				return key;
			}
		}
		return null; 
	}
	
	
	public static int getKeyBindingIndex(String description)
	{
		int i = 0;
		for(KeyBinding key : Minecraft.getMinecraft().gameSettings.keyBindings)
		{
			if(key.getKeyDescription().equalsIgnoreCase(description))
			{
				return i;
			}
			i++;
		}
		
		return -1;
	}
	
	public static void setItemStackInSlotIndex(EntityPlayer player, int index, ItemStack is)
	{
		List<Slot> slots = player.inventoryContainer.inventorySlots;
		
		for(Slot slot : slots)
		{
			if(!slot.getHasStack())
			{
				continue;
			}
			else
			{
				if(ItemStack.areItemStacksEqual(is, slot.getStack()))
				{
					ItemStack itemstackSwitch = player.inventory.getStackInSlot(index);
					if(itemstackSwitch != null)
					{
						
					}
					player.inventory.setInventorySlotContents(index, slot.getStack());
					return;
				}
			}
			
		}
	}
	
	public static List<String> getLores(ItemStack is)
    {
    	if(is.stackTagCompound == null)
    	{
    		return new ArrayList<String>();
    	}
    	
    	List<String> loresList = new ArrayList();
    	
    	if (is.stackTagCompound.hasKey("display", 10))
        {
    		NBTTagCompound display = (NBTTagCompound) is.stackTagCompound.getTag("display");
    		
            NBTTagList lores = (NBTTagList)display.getTag("Lore");
       
            for(int i = 0; i < lores.tagCount(); i++)
            {
            	String lore = lores.getStringTagAt(i);
            	loresList.add(lore);
            }
            
            return loresList;
        }
    	
    	return new ArrayList<String>();
    }
	
	   public static void setPlayerSize(EntityPlayer player, float width, float height)
	    {
	        AxisAlignedBB axisalignedbb = player.boundingBox;
	 
	        player.width = width;
	        player.height = height;
	 
	        player.boundingBox.setBB(AxisAlignedBB.getBoundingBox(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ,axisalignedbb.minX + (double) width, axisalignedbb.minY + (double) height,axisalignedbb.minZ + (double) width));
	    }
	 

	    public static void resetPlayerSize(EntityPlayer player)
	    {
	        setPlayerSize(player, 0.6F, 1.8F);
	    }
	    
	    public static boolean boundingBoxCollide(World world, AxisAlignedBB axis)
	    {    		
	    	for(int x = (int)Math.floor(axis.minX); x < axis.maxX; x++)
	    	{
		    	for(int z = (int)Math.floor(axis.minZ); z < axis.maxZ; z++)
		    	{
			    	for(int y = (int)Math.floor(axis.minY); y < axis.maxY; y++)
			    	{
			    		Block block = world.getBlock(x, y, z);
			    		if(block == Blocks.air) continue;
			    		
			    		AxisAlignedBB blockAxis = block.getCollisionBoundingBoxFromPool(world, x, y, z);
			    		return axis.intersectsWith(blockAxis);
			    	}
		    	}
	    	}
	    	return false;
	    }
	    
	    public static float getPlayerYawFromBedDirection(EntityPlayer player)
	    {
	        if (player.playerLocation != null)
	        {
	            int x = player.playerLocation.posX;
	            int y = player.playerLocation.posY;
	            int z = player.playerLocation.posZ;
	            int j = player.worldObj.getBlock(x, y, z).getBedDirection(player.worldObj, x, y, z);
 
	            switch (j)
	            {
	                case 0:
	                {
	                    return 180.0F;
	                }
	                case 1:
	                {
	                    return 270.0F;
	                }
	                case 2:
	                {
	                    return 0.0F;
	                }
	                case 3:
	                {
	                    return 90.0F;
	                }
	            }
	        }

	        return 0.0F;
	    }
	    
	    public static void dropBlockAsItem(World world, int x, int y, int z, ItemStack is)
	    {
	        if (!world.isRemote && world.getGameRules().getGameRuleBooleanValue("doTileDrops") && !world.restoringBlockSnapshots) // do not drop items while restoring blockstates, prevents item dupe
	        {
	            float f = 0.7F;
	            double d0 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	            double d1 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	            double d2 = (double)(world.rand.nextFloat() * f) + (double)(1.0F - f) * 0.5D;
	            EntityItem entityitem = new EntityItem(world, (double)x + d0, (double)y + d1, (double)z + d2, is);
	            entityitem.delayBeforeCanPickup = 10;
	            world.spawnEntityInWorld(entityitem);
	        }
	    }
	    
	    

	
	
	
}
