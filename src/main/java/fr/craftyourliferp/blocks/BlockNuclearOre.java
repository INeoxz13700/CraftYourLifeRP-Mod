package fr.craftyourliferp.blocks;

import java.util.Random;

import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class BlockNuclearOre extends BlockOre {

	public BlockNuclearOre()
	{
		setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		setHardness(3.0F);
		setResistance(5.0F);
		this.setHarvestLevel("pickaxe", 3);
	}
	
	@Override
    public Item getItemDropped(int p_149650_1_, Random rand, int p_149650_3_)
    {
        return ModdedItems.nuclearIngot;
    }
	
	@Override
    public int quantityDropped(Random rand)
    {
        return 1;
    }
	
}
