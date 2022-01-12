package eu.nicoszpako.armamania.common;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockBetter extends Block {
  private Item a;
  
  private int b;
  
  private int c;
  
  protected BlockBetter(Material paramMaterial) {
    super(paramMaterial);
    setBlockBounds(0.45F, 0.0F, 0.45F, 0.55F, 0.3F, 0.55F);
    setHardness(5.0F);
    this.a = ArmaMania.aA;
    this.b = 2;
    this.c = 4;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public Item getItemDropped(int paramInt1, Random paramRandom, int paramInt2) {
    return this.a;
  }
  
  public int quantityDropped(int paramInt1, int paramInt2, Random paramRandom) {
    return (this.b >= this.c) ? this.b : (this.b + paramRandom.nextInt(this.c - this.b + paramInt2 + 1));
  }
}


/* Location:              D:\Devs\deobfuscator\ArmaMania-deobf.jar!\eu\nicoszpako\armamania\common\BlockBetter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */