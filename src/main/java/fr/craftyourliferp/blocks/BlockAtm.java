package fr.craftyourliferp.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAtm extends BlockContainer {

	public BlockAtm(Material material)
	{
		super(material);
		setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		setBlockName("ATM");
	    GameRegistry.registerBlock(this, "ATM");
	    this.setBlockBounds(0, 0, 0, 1, 2f, 1);
	}

    @Override
    public boolean renderAsNormalBlock(){
        return false;
    }

    @Override
    public int getRenderType(){
        return -1;
    }

    @Override
    public boolean isOpaqueCube(){
        return false;
    }
	
	@Override
	public TileEntity createNewTileEntity(World world, int p_149915_2_) {
		return new TileEntityAtm();
	}
	
	@Override
    public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase entityliving, ItemStack is) 
	{
		super.onBlockPlacedBy(world, posX, posY, posZ, entityliving, is);
		TileEntity tileentity = world.getTileEntity(posX, posY, posZ);
		if(tileentity instanceof TileEntityAtm)
		{
			TileEntityAtm tatm = (TileEntityAtm) tileentity;
			tatm.rotation = -entityliving.rotationYaw;
			tatm.scale = 1f;
		}
	}

	
	
}
