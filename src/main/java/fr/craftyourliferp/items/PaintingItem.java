package fr.craftyourliferp.items;

import java.util.List;

import fr.craftyourliferp.blocks.BlockPainting;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class PaintingItem extends ItemBlock {

	public PaintingItem(Block block) {
		super(block);
	}
	
    @Override
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean p_77624_4_) 
    {
    	if(is.hasTagCompound()) list.add(getDescription(is));
    	
    	super.addInformation(is, player, list, p_77624_4_);
    }
    
    public String getDescription(ItemStack is)
    {
    	EnumRarity rarity = getRarity(is);
    	
    	if(rarity == EnumRarity.epic)
    	{
        	return "§l§6Tableau Légendaire";
    	}
    	else if(rarity == EnumRarity.rare)
    	{
        	return "§l§dTableau Epique";
    	}
    	else if(rarity == EnumRarity.uncommon)
    	{
        	return "§l§aTableau Rare";
    	}
    	return "Tableau";
    }
    
    public EnumRarity getRarity(ItemStack is)
    {
    	
    	if(!is.hasTagCompound()) return EnumRarity.common;
    	
    	byte type = is.stackTagCompound.getByte("PaintingType");
    	
    	if(type == 0 || type == 4 || type == 5 || type == 6)
    	{
    		return EnumRarity.epic;
    	}
    	else if(type == 7 || type == 2)
    	{
    		return EnumRarity.rare;
    	}
    	else if(type == 3 || type == 9 || type == 10 || type == 11)
    	{
    		return EnumRarity.uncommon;
    	}
    	return EnumRarity.common;
    }
    
    public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_, int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        if (p_77648_3_.isRemote)
        {
            return true;
        }
        else
        {
            BlockPainting blockPainting = (BlockPainting)ModdedItems.PaintingBlock;
            int i1 = MathHelper.floor_double((double)(p_77648_2_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
            byte b0 = 0;
            byte b1 = 0;
            
            if(i1 == 3)
            {
            	b0 = -1;
            }
            else if(i1 == 0)
            {
            	b1 = -1;
            }
            else if(i1 == 1)
            {
            	b0 = 1;
            }
            else
            {
            	b1 = 1;
            }
            
            if(ServerUtils.isOp(p_77648_2_) && p_77648_3_.getBlock(p_77648_4_, p_77648_5_, p_77648_6_).isOpaqueCube())
            {
	        	if(Blocks.air == p_77648_3_.getBlock(p_77648_4_+b0, p_77648_5_, p_77648_6_+b1))
	        	{
	        		super.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_, p_77648_9_, p_77648_10_);
	        	}
            }
            
           return true;
        }
    }

}
