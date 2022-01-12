package fr.craftyourliferp.blocks;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;

public class BlockNuclear extends Block {

	public BlockNuclear(Material p_i45394_1_) {
		super(p_i45394_1_);
		setHardness(10.0F);
		setResistance(10.0F);
		setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		this.setHarvestLevel("pickaxe", 4);
	}

}
