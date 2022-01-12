package eu.nicoszpako.armamania.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockDistrib extends Block {
  public IIcon a;
  
  public IIcon b;
  
  protected BlockDistrib(Material paramMaterial) {
    super(paramMaterial);
    setBlockBounds(0.05F, 0.0F, 0.05F, 0.95F, 1.8F, 0.95F);
    setHardness(8.0F);
  }
  
  public void onBlockPlacedBy(World paramWorld, int paramInt1, int paramInt2, int paramInt3, EntityLivingBase paramEntityLivingBase, ItemStack paramItemStack) {
    int i = MathHelper.floor_double((paramEntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 0x3;
    paramWorld.setBlockMetadataWithNotify(paramInt1, paramInt2, paramInt3, i, 2);
  }
  
  public void registerBlockIcons(IIconRegister paramIIconRegister) {
    this.blockIcon = paramIIconRegister.registerIcon("armamania:distribCotes");
    this.a = paramIIconRegister.registerIcon("armamania:distribFace");
    this.b = paramIIconRegister.registerIcon("armamania:distribCotes");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int paramInt1, int paramInt2) {
    return ((paramInt1 == 3 && paramInt2 == 0) || (paramInt1 == 4 && paramInt2 == 1) || (paramInt1 == 2 && paramInt2 == 2) || (paramInt1 == 5 && paramInt2 == 3)) ? this.a : this.b;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public Item getItemDropped(int paramInt1, Random paramRandom, int paramInt2) {
    return ArmaMania.e;
  }
}


/* Location:              D:\Devs\deobfuscator\ArmaMania-deobf.jar!\eu\nicoszpako\armamania\common\BlockDistrib.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */