package fr.craftyourliferp.mainmenu.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.utils.GuiUtils;

@SideOnly(Side.CLIENT)
public class GuiCustomButton extends GuiButton {

    public int isOver = 2;
    private ResourceLocation iconTexture = null;

    public ItemStack renderStack = null;

    public boolean drawBackground = true;
    public boolean drawShadow = true;
    public String buttonColor = "0x3399FF";
    public boolean centeredText = true;

    public boolean soundPlayed = true;

    public GuiCustomButton(int id, int x, int y, int width, int height, String displayString) {
        super(id, x, y, width, height, displayString);
    }
    
    public GuiCustomButton(int id, int x, int y, ItemStack renderStack) {
        super(id, x, y, 18, 18, "");
        this.renderStack = renderStack;
    }

    public GuiCustomButton(int id, int x, int y, int width, int height, String par6, String buttonColor) {
        super(id, x, y, width, height, par6);
        this.buttonColor = buttonColor;
    }

    public GuiCustomButton(int id, int x, int y, int width, int height, ResourceLocation iconTexture) {
        super(id, x, y, width, height, "");
        this.iconTexture = iconTexture;
    }
    
    public GuiCustomButton(int id, int x, int y, int width, int height, ResourceLocation iconTexture, boolean drawBackground) {
        super(id, x, y, width, height, "");
        this.iconTexture = iconTexture;
        this.drawBackground = drawBackground;
    }

    public void drawButton(Minecraft minecraft, int mouseX, int mouseY) {
        if (visible) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            boolean mouseHover = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            isOver = getHoverState(mouseHover);
            if (drawBackground) {
                if (drawShadow) {
                    GuiUtils.drawRectWithShadow(xPosition, yPosition, width, height, buttonColor, 1.0F);
                } else {
                    GuiUtils.drawRect(xPosition, yPosition, width, height, buttonColor, 1.0F);
                }
            }
            mouseDragged(minecraft, mouseX, mouseY);
            String displayText = displayString;
            if (isOver == 2) {
                if (!soundPlayed) {
                    Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 2.5F));
                    soundPlayed = true;
                }
            } else {
                soundPlayed = false;
            }
            if (iconTexture != null) {
                if (isOver == 2) {
                    GuiUtils.renderColor(8421504);
                }
                GuiUtils.drawImage(xPosition, yPosition, iconTexture, width, height);
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                return;
            }
            if (renderStack != null) {
                GuiUtils.renderItemStackIntoGUI(this.renderStack, xPosition, yPosition + 1,"", new RenderItem());
                return;
            }
            if (!enabled) {
                displayText = EnumChatFormatting.GRAY + displayText;
            }
            if (!centeredText) {
                GuiUtils.renderTextWithShadow(displayText, xPosition + 2, yPosition + (height - 8) / 2, isOver == 2 ? 8421504 : 16777215);
                return;
            }
            GuiUtils.renderCenteredTextWithShadow(displayText, xPosition + width / 2, yPosition + (height - 8) / 2, isOver == 2 ? 8421504 : 16777215);
        }
    }

    public void playPressSound(SoundHandler soundHandler) {
        soundHandler.playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
    }
}
