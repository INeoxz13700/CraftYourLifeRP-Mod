package fr.craftyourliferp.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.blocks.tileentity.TileEntityCashRegister;
import fr.craftyourliferp.blocks.tileentity.TileEntityRoadBalise;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockRoadBalise extends BlockContainer {

	public BlockRoadBalise(Material material)
	{
		super(material);
		setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		setBlockName("RoadBalise");
	    GameRegistry.registerBlock(this, "RoadBalise");
	    this.setBlockBounds(0.25f, 0, 0.25f, 0.75f, 0.75f, 0.75f);
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
		return new TileEntityRoadBalise();
	}
	
	@Override
    public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase entityliving, ItemStack is) 
	{
		super.onBlockPlacedBy(world, posX, posY, posZ, entityliving, is);
		TileEntity tileentity = world.getTileEntity(posX, posY, posZ);
		if(tileentity instanceof TileEntityRoadBalise)
		{
			TileEntityRoadBalise trb = (TileEntityRoadBalise) tileentity;
			trb.direction = MathHelper.floor_double((entityliving.rotationYaw * 4F) / 360F + 0.5D) & 3;
			world.setBlockMetadataWithNotify(posX, posY, posZ, trb.direction, 1);
		}
	}

	
	
}
