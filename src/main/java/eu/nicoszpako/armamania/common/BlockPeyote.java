package eu.nicoszpako.armamania.common;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;

public class BlockPeyote extends Block {
  private Item c;
  
  private int d;
  
  private int e;
  
  public IIcon a;
  
  public IIcon b;
  
  protected BlockPeyote(Material paramMaterial) {
    super(paramMaterial);
    setBlockBounds(0.15F, 0.0F, 0.15F, 0.85F, 0.2F, 0.85F);
    setHardness(5.0F);
    this.c = ArmaMania.e;
    this.d = 2;
    this.e = 4;
  }
  
  public void registerBlockIcons(IIconRegister paramIIconRegister) {
    this.blockIcon = paramIIconRegister.registerIcon("armamania:PeyoteCOTE");
    this.a = paramIIconRegister.registerIcon("armamania:PeyoteTOP");
    this.b = paramIIconRegister.registerIcon("armamania:PeyoteCOTE");
  }
  
  @SideOnly(Side.CLIENT)
  public IIcon getIcon(int paramInt1, int paramInt2) {
    return ((paramInt1 == 1 && paramInt2 == 0) || (paramInt1 == 4 && paramInt2 == 1) || (paramInt1 == 2 && paramInt2 == 2) || (paramInt1 == 5 && paramInt2 == 3)) ? this.a : this.b;
  }
  
  public boolean isOpaqueCube() {
    return false;
  }
  
  public Item getItemDropped(int paramInt1, Random paramRandom, int paramInt2) {
    return this.c;
  }
  
  public int quantityDropped(int paramInt1, int paramInt2, Random paramRandom) {
    return (this.d >= this.e) ? this.d : (this.d + paramRandom.nextInt(this.e - this.d + paramInt2 + 1));
  }
}


/* Location:              D:\Devs\deobfuscator\ArmaMania-deobf.jar!\eu\nicoszpako\armamania\common\BlockPeyote.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */