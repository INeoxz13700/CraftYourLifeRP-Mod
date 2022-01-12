package fr.craftyourliferp.blocks;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.blocks.tileentity.TileEntityCorpseFreezer;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCorpseFreezer extends BlockContainer {

	 public BlockCorpseFreezer(Material p_i45394_1_) {
		super(p_i45394_1_);
	    setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
	 }
	
	 @Override
     public void setBlockBoundsBasedOnState(IBlockAccess blockdata, int x, int y, int z)
     {
		TileEntityCorpseFreezer tileEntity =  (TileEntityCorpseFreezer)blockdata.getTileEntity(x, y, z);
		if(tileEntity.direction == 0)
		{
			setBlockBounds(0, 0, 0, 1, 1, 2);
		}
		else if(tileEntity.direction == 1)
		{
			setBlockBounds(-1, 0, 0, 1, 1, 1);
		}
		else if(tileEntity.direction == 2)
		{
			setBlockBounds(0, 0,-1, 1, 1, 1);
		}
		else if(tileEntity.direction == 3)
		{
			setBlockBounds(0, 0, 0, 2, 1, 1);
		}
     }
    
	 
	 @Override
	 public void addCollisionBoxesToList(World world, int x, int y, int z, AxisAlignedBB bounds, List list, Entity entity)
	 { 
		 super.addCollisionBoxesToList(world, x, y, z, bounds, list, entity);
	 }
	 
	 @Override
	 public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
	 {
	     return AxisAlignedBB.getBoundingBox((double)p_149668_2_ + this.minX, (double)p_149668_3_ + this.minY, (double)p_149668_4_ + this.minZ, (double)p_149668_2_ + this.maxX, (double)p_149668_3_ + this.maxY, (double)p_149668_4_ + this.maxZ);
	 }
	 
	 
	
	 @Override
	 public boolean renderAsNormalBlock(){
	      return false;
	 }

	 @Override
	 public int getRenderType()
	 {
		 return -1;
	 }

	 @Override
	 public boolean isOpaqueCube()
	 {
	 	return false;
	 }
		

	@Override
	public TileEntity createNewTileEntity(World p_149915_1_, int p_149915_2_) {
		return new TileEntityCorpseFreezer();
	}
	
	@Override
    public void onBlockPlacedBy(World world, int posX, int posY, int posZ, EntityLivingBase entityliving, ItemStack is) 
	{
		super.onBlockPlacedBy(world, posX, posY, posZ, entityliving, is);

		TileEntity tileentity = world.getTileEntity(posX, posY, posZ);

		if(tileentity instanceof TileEntityCorpseFreezer)
		{
			TileEntityCorpseFreezer tfreezer = (TileEntityCorpseFreezer) tileentity;
			tfreezer.direction = MathHelper.floor_double((entityliving.rotationYaw * 4F) / 360F + 0.5D) & 3;
			world.setBlockMetadataWithNotify(posX, posY, posZ, tfreezer.direction, 3);

		}
		
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
     	
    	TileEntityCorpseFreezer tileEntity = (TileEntityCorpseFreezer) world.getTileEntity(x, y, z);
    	
    	if(tileEntity.state == 0) tileEntity.state = 1;
    	else tileEntity.state = 0;
    	
        return false;
    }
	
	


}
