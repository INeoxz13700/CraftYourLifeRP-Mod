package eu.nicoszpako.armamania.common;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockRaisin extends Block {
  protected BlockRaisin(Material paramMaterial) {
    super(paramMaterial);
    setHardness(4.0F);
    setBlockBounds(0.25F, 0.0F, 0.0F, 0.75F, 1.0F, 1.0F);
  }
  
  public void onBlockPlacedBy(World paramWorld, int paramInt1, int paramInt2, int paramInt3, EntityLivingBase paramEntityLivingBase, ItemStack paramItemStack) {
    int i = MathHelper.floor_double((paramEntityLivingBase.rotationYaw * 4.0F / 360.0F) + 2.5D) & 0x3;
    paramWorld.setBlockMetadataWithNotify(paramInt1, paramInt2, paramInt3, i, 2);
  }
  
  public Item getItemDropped(int paramInt1, Random paramRandom, int paramInt2) {
    return ArmaMania.ao;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
}


/* Location:              D:\Devs\deobfuscator\ArmaMania-deobf.jar!\eu\nicoszpako\armamania\common\BlockRaisin.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */