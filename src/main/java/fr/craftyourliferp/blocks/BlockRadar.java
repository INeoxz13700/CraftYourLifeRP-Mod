package fr.craftyourliferp.blocks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import com.flansmod.common.FlansMod;
import com.flansmod.common.network.PacketOpenRadarGui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.tileentity.TileEntityRadar;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockRadar extends BlockContainer{

    public BlockRadar(Material material) {
        super(material);
        this.setBlockName("Radar");
        this.setBlockBounds(0, 0, 0, 1, 2f, 1);
        this.setBlockTextureName("craftyourliferp:signal");
        this.setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);

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
        return new TileEntityRadar();
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
    
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer p, int p_149727_6_, float p_149727_7_, float p_149727_8_, float p_149727_9_)
    {    
    	if(!world.isRemote)
    	{		

    		String allowedUser = "";
    		boolean canEditRadar = false;
            try {
                
                URL url = new URL(CraftYourLifeRPMod.apiIp + "/allowed-staff.txt");
                
				HttpURLConnection con = (HttpURLConnection)url.openConnection();

                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                 
                while ((allowedUser = in.readLine()) != null) {
                    if(allowedUser.equalsIgnoreCase(p.getCommandSenderName()))
                    {
                    	canEditRadar = true;
                    }
                }
                in.close();
                con.disconnect();           
            }
            catch (MalformedURLException e) {
                System.out.println("Malformed URL: " + e.getMessage());
                return false;
            }
            catch (IOException e) {
                System.out.println("I/O Error: " + e.getMessage());
                return false;
            }
            
            if(!canEditRadar) return false;
            
    		
    		TileEntityRadar te = (TileEntityRadar) world.getTileEntity(x, y, z);
    		    		
    		FlansMod.packetHandler.sendTo(new PacketOpenRadarGui(te), (EntityPlayerMP) p);
    		return true;
    	}
    	
        return false;
    }
    
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack)
    {
    	if (entity == null)
    	{
    		return;
    	}

    	TileEntityRadar tile = (TileEntityRadar) world.getTileEntity(x, y, z);
    	tile.rotation = -entity.rotationYaw;
    	
    	
    }

  
    



}