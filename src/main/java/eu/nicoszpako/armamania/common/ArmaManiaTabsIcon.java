package eu.nicoszpako.armamania.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ArmaManiaTabsIcon extends CreativeTabs {
  public ArmaManiaTabsIcon(String paramString) {
    super(paramString);
  }
  
  public Item getTabIconItem() {
    return ArmaMania.bm;
  }
  
  public boolean hasSearchBar() {
    return true;
  }
}


/* Location:              D:\Devs\deobfuscator\ArmaMania-deobf.jar!\eu\nicoszpako\armamania\common\ArmaManiaTabsIcon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */