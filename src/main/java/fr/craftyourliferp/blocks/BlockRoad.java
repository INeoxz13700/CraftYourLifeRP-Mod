package fr.craftyourliferp.blocks;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.ClientProxy;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockRoad extends Block {


    @SideOnly(Side.CLIENT)
    protected IIcon topIcon;
    
	
	public BlockRoad() {
		super(Material.rock);
		setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		
	}
	
	
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int p_149691_1_, int p_149691_2_)
    {
        return p_149691_1_ == 1 ? topIcon : blockIcon;
        
    }
    
    public void onBlockPlacedBy(World p_149689_1_, int p_149689_2_, int p_149689_3_, int p_149689_4_, EntityLivingBase p_149689_5_, ItemStack p_149689_6_)
    {
    	int meta = 0;
        int direction = MathHelper.floor_double((double)(p_149689_5_.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        
        if (direction == 0)
        {
        	meta = 0;
        }

        if (direction == 1)
        {
        	meta = 1;
        }

        if (direction == 2)
        {
        	meta = 2;
        }

        if (direction == 3)
        {
        	meta = 3;
        }
                
        
        
        p_149689_1_.setBlockMetadataWithNotify(p_149689_2_, p_149689_3_, p_149689_4_, meta, 2);

    }


   
    
    @SideOnly(Side.CLIENT)
    public int getRenderType()
    {
        return ClientProxy.renderRoadBlock;
    }

	
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister p_149651_1_)
    {
    	blockIcon = p_149651_1_.registerIcon(getTextureName() + "_side");
    	topIcon = p_149651_1_.registerIcon(getTextureName() + "_top");
    }

}
