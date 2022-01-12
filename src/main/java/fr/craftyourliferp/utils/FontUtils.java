package fr.craftyourliferp.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;

@SideOnly(Side.CLIENT)
public class FontUtils {
	
	public static int getNumberCharacterInWidth(int width, float font_scale)
	{
		return Math.round((float)width / (float)(6 * font_scale));
	}
	
    public static String trimStringToWidth(String str, int width, float scale)
    {
        return trimStringToWidth(str, width, false, scale);
    }
    
    public static String trimStringToWidth(String p_78262_1_, int p_78262_2_, boolean p_78262_3_, float scale)
    {
        StringBuilder stringbuilder = new StringBuilder();
        int j = 0;
        int k = p_78262_3_ ? p_78262_1_.length() - 1 : 0;
        int l = p_78262_3_ ? -1 : 1;
        boolean flag1 = false;
        boolean flag2 = false;

        for (int i1 = k; i1 >= 0 && i1 < p_78262_1_.length() && j < p_78262_2_; i1 += l)
        {
            char c0 = p_78262_1_.charAt(i1);
            int j1 = (int) (Minecraft.getMinecraft().fontRenderer.getCharWidth(c0) * scale);

            if (flag1)
            {
                flag1 = false;

                if (c0 != 108 && c0 != 76)
                {
                    if (c0 == 114 || c0 == 82)
                    {
                        flag2 = false;
                    }
                }
                else
                {
                    flag2 = true;
                }
            }
            else if (j1 < 0)
            {
                flag1 = true;
            }
            else
            {
                j += j1;

                if (flag2)
                {
                    ++j;
                }
            }

            if (j > p_78262_2_)
            {
                break;
            }

            if (p_78262_3_)
            {
                stringbuilder.insert(0, c0);
            }
            else
            {
                stringbuilder.append(c0);
            }
        }

        return stringbuilder.toString();
    }

}
