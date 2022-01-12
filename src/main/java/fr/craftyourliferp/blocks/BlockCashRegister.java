package fr.craftyourliferp.blocks;

import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.blocks.tileentity.TileEntityCashRegister;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockCashRegister extends BlockContainer {

	public BlockCashRegister(Material material)
	{
		super(material);
		setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		setBlockName("CashRegister");
	    GameRegistry.registerBlock(this, "CashRegister");
	    this.setBlockBounds(0, 0, 0, 1, 0.6f, 1);
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
		return new TileEntityCashRegister();
	}
	
	@Override
    public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase entityliving, ItemStack is) 
	{
		super.onBlockPlacedBy(world, posX, posY, posZ, entityliving, is);
		TileEntity tileentity = world.getTileEntity(posX, posY, posZ);
		if(tileentity instanceof TileEntityCashRegister)
		{
			TileEntityCashRegister tcashregister = (TileEntityCashRegister) tileentity;
			tcashregister.direction = MathHelper.floor_double((entityliving.rotationYaw * 4F) / 360F + 0.5D) & 3;
			world.setBlockMetadataWithNotify(posX, posY, posZ, tcashregister.direction, 1);
		}
	}

	
	
}
