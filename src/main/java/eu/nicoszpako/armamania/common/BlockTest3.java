package eu.nicoszpako.armamania.common;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockTest3 extends BlockBush {
  private Item a;
  
  private int b;
  
  private int c;
  
  protected BlockTest3(Material paramMaterial) {
    super(paramMaterial);
    setHardness(5.0F);
    this.a = ArmaMania.g;
    this.b = 2;
    this.c = 4;
  }
  
  protected BlockTest3() {
    float f = 0.2F;
    setBlockBounds(0.5F - f, 0.0F, 0.5F - f, 0.5F + f, f * 2.0F, 0.5F + f);
    setTickRandomly(true);
  }
  
  protected boolean canPlaceBlockOn(Block paramBlock) {
    return true;
  }
  
  public Item getItemDropped(int paramInt1, Random paramRandom, int paramInt2) {
    return this.a;
  }
  
  public int quantityDropped(int paramInt1, int paramInt2, Random paramRandom) {
    return (this.b >= this.c) ? this.b : (this.b + paramRandom.nextInt(this.c - this.b + paramInt2 + 1));
  }
}


/* Location:              D:\Devs\deobfuscator\ArmaMania-deobf.jar!\eu\nicoszpako\armamania\common\BlockTest3.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */