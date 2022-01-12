package eu.nicoszpako.armamania.common;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class itemDallas extends ItemArmor {
  public itemDallas(ItemArmor.ArmorMaterial paramArmorMaterial, int paramInt) {
    super(paramArmorMaterial, 0, paramInt);
  }
  
  public String getArmorTexture(ItemStack paramItemStack, Entity paramEntity, int paramInt, String paramString) {
    return (paramInt == 2) ? "armamania:textures/models/armor/maskrouge_layer_2.png" : "armamania:textures/models/armor/maskrouge_layer_1.png";
  }
}


/* Location:              D:\Devs\deobfuscator\ArmaMania-deobf.jar!\eu\nicoszpako\armamania\common\itemDallas.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */