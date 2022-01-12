package fr.craftyourliferp.blocks;

import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.tileentity.TileEntityComputer;
import fr.craftyourliferp.blocks.tileentity.TileEntityGoldIngot;
import fr.craftyourliferp.blocks.tileentity.TileEntityPainting;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketStealing;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockComputer extends BlockContainer  {

	
    public BlockComputer(Material material) {
        super(material);
        this.setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        this.setBlockName("Computer");
        this.setBlockBounds(0, 0, 0, 0.5f, 1f, 0.5f);
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
    public TileEntity createNewTileEntity(World world, int par2) {
    	TileEntityComputer computer = new TileEntityComputer();
        return computer;
    }
    
    @Override
    public boolean hasTileEntity(int metadata)
    {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isBlockNormalCube()
    {
    	return false;
    }
  
    public boolean isNormalCube()
    {
        return false;
    }
    
    public boolean isCollidable()
    {
        return true;
    }
    
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World p_149633_1_, int p_149633_2_, int p_149633_3_, int p_149633_4_)
    {
        return AxisAlignedBB.getBoundingBox((double)p_149633_2_ + this.minX, (double)p_149633_3_ + this.minY, (double)p_149633_4_ + this.minZ, (double)p_149633_2_ + this.maxX, (double)p_149633_3_ + this.maxY, (double)p_149633_4_ + this.maxZ);
    }
    
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World p_149668_1_, int p_149668_2_, int p_149668_3_, int p_149668_4_)
    {
        return AxisAlignedBB.getBoundingBox((double)p_149668_2_ + this.minX, (double)p_149668_3_ + this.minY, (double)p_149668_4_ + this.minZ, (double)p_149668_2_ + this.maxX, (double)p_149668_3_ + this.maxY, (double)p_149668_4_ + this.maxZ);
    }
    
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x, int y, int z) 
    {
    	TileEntityComputer tile = (TileEntityComputer) blockAccess.getTileEntity(x, y, z);
        this.setBlockBounds(0, 0, 0, 1, 0.8f, 1);
    }
    
    public boolean canConnectRedstone(IBlockAccess world, int x, int y, int z, int side)
    {
        return false;
    }
    
    public boolean canPlaceTorchOnTop(World world, int x, int y, int z)
    {
       return false;
    }

    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {
    	TileEntity tile = world.getTileEntity(x, y, z);
    	

	    if(tile instanceof TileEntityComputer)
	    {
	    	TileEntityComputer tilep = (TileEntityComputer) tile;
	
	    	if(!world.isRemote)
	    	{	    	
		    	return true;
	    	}
	  
	    	
    	}
        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
    	    	
    	if (entity == null) return;
    	
    	TileEntityComputer tile = (TileEntityComputer) world.getTileEntity(x, y, z);
    	tile.direction = MathHelper.floor_double((entity.rotationYaw * 4F) / 360F + 0.5D) & 3;

		world.setBlockMetadataWithNotify(x, y, z, tile.direction, 1);
    	
    }


    



}