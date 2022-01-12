package eu.nicoszpako.armamania.common;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

public class BlockPeche extends Block {
  private Item a;
  
  private int b;
  
  private int c;
  
  protected BlockPeche(Material paramMaterial) {
    super(paramMaterial);
    setHardness(3.0F);
    this.a = ArmaMania.aj;
    this.b = 1;
    this.c = 3;
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


/* Location:              D:\Devs\deobfuscator\ArmaMania-deobf.jar!\eu\nicoszpako\armamania\common\BlockPeche.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */