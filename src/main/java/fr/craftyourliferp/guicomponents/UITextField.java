package fr.craftyourliferp.guicomponents;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.ingame.gui.IGuiClickableElement;
import fr.craftyourliferp.ingame.gui.IGuiKeytypeElement;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;

public class UITextField extends GraphicObject implements IGuiClickableElement, IGuiKeytypeElement {
	
	private final FontRenderer field_146211_a;
	
    /** Has the current text being edited on the textbox. */
    private String text = "";
    private int maxStringLength = 32;
    private int cursorCounter;
    /** if true the textbox can lose focus by clicking elsewhere on the screen */
    private boolean canLoseFocus = true;
    /** If this value is true along with isEnabled, keyTyped will process the keys. */
    private boolean isFocused;
    /** If this value is true along with isFocused, keyTyped will process the keys. */
    /** The current character index that should be used as start of the rendered text. */
    private int lineScrollOffset;
    private int cursorPosition;
    /** other selection position, maybe the same as the cursor */
    private int selectionEnd;
        
    public UIColor textColor = new UIColor(255,255,255,255);
    public UIColor disabledColor = new UIColor(new Color(14737632));
    
   
    public int reduce;
    
    private int inputInset;
    
    public enum Type {
    	PASSWORD,
    	NUMBER,
    	TEXT;
    }
    
    private Type type = Type.TEXT;
    
    public UIImage inputTexture;
    
    public UIRect inputRect;
    
    public float font_scale;

    public UITextField(UIRect rect, float font_scale, Type type)
    {
        this.field_146211_a = mc.fontRenderer;
        this.inputRect = rect;
        this.font_scale = font_scale;
        this.type = type;
    }
    
    public UITextField(UIImage texture, float font_scale, Type type)
    {
        this.field_146211_a = mc.fontRenderer;
        this.inputTexture = texture;
        this.font_scale = font_scale;
        this.type = type;
    }

    
    @Override
    public GraphicObject setPosition(int x, int y, int width, int height)
    {
    	super.setPosition(x, y, width, height);
    	if(isTextured())
    	{
    		this.inputTexture.setPosition(x, y, width, height);
    		return this;
    	}
    	this.inputRect.setPosition(x, y, width, height);
    	return this;
    }
    
    /**
     * Increments the cursor counter
     */
    public void updateCursorCounter()
    {
        ++this.cursorCounter;
    }

    /**
     * Sets the text of the textbox
     */
    public GraphicObject setText(String p_146180_1_)
    {
    	if(p_146180_1_.isEmpty())
    	{
    		this.text = "";
    		this.cursorPosition = 0;
    		this.selectionEnd = 0;
    		this.lineScrollOffset = 0;
    		return this;
    	}
    	
        if (p_146180_1_.length() > this.maxStringLength)
        {
            this.text = p_146180_1_.substring(0, this.maxStringLength);
        }
        else
        {
            this.text = p_146180_1_;
        }
        return this;
    }

    /**
     * Returns the contents of the textbox
     */
    public String getText()
    {
        return this.text;
    }

    /**
     * returns the text between the cursor and selectionEnd
     */
    public String getSelectedText()
    {
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.text.substring(i, j);
    }

    /**
     * replaces selected text, or inserts text at the position on the cursor
     */
    public void writeText(String p_146191_1_)
    {
        String s1 = "";
        String s2 = ChatAllowedCharacters.filerAllowedCharacters(p_146191_1_);
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        int k = this.maxStringLength - this.text.length() - (i - this.selectionEnd);
        boolean flag = false;

        if (this.text.length() > 0)
        {
            s1 = s1 + this.text.substring(0, i);
        }

        int l;

        if (k < s2.length())
        {
            s1 = s1 + s2.substring(0, k);
            l = k;
        }
        else
        {
            s1 = s1 + s2;
            l = s2.length();
        }

        if (this.text.length() > 0 && j < this.text.length())
        {
            s1 = s1 + this.text.substring(j);
        }

        this.text = s1;
        this.moveCursorBy(i - this.selectionEnd + l);
    }

    /**
     * Deletes the specified number of words starting at the cursor position. Negative numbers will delete words left of
     * the cursor.
     */
    public void deleteWords(int p_146177_1_)
    {
        if (this.text.length() != 0)
        {
            if (this.selectionEnd != this.cursorPosition)
            {
                this.writeText("");
            }
            else
            {
                this.deleteFromCursor(this.getNthWordFromCursor(p_146177_1_) - this.cursorPosition);
            }
        }
    }
    
	@Override
	public void setY(int y)
	{
		setPosition(posX,y,width,height);
	}
	
	@Override
	public void setX(int x)
	{
		setPosition(x,posY,width,height);
	}
	
	@Override
	public void setWidth(int width)
	{
		setPosition(posX,posY,width,height);
	}
	
	@Override
	public void setHeight(int height)
	{
		setPosition(posX,posY,width,height);
	}

    /**
     * delete the selected text, otherwsie deletes characters from either side of the cursor. params: delete num
     */
    public void deleteFromCursor(int p_146175_1_)
    {
        if (this.text.length() != 0)
        {
            if (this.selectionEnd != this.cursorPosition)
            {
                this.writeText("");
            }
            else
            {
                boolean flag = p_146175_1_ < 0;
                int j = flag ? this.cursorPosition + p_146175_1_ : this.cursorPosition;
                int k = flag ? this.cursorPosition : this.cursorPosition + p_146175_1_;
                String s = "";

                if (j >= 0)
                {
                    s = this.text.substring(0, j);
                }

                if (k < this.text.length())
                {
                    s = s + this.text.substring(k);
                }

                this.text = s;

                if (flag)
                {
                    this.moveCursorBy(p_146175_1_);
                }
            }
        }
    }

    /**
     * see @getNthNextWordFromPos() params: N, position
     */
    public int getNthWordFromCursor(int p_146187_1_)
    {
        return this.getNthWordFromPos(p_146187_1_, this.getCursorPosition());
    }

    /**
     * gets the position of the nth word. N may be negative, then it looks backwards. params: N, position
     */
    public int getNthWordFromPos(int p_146183_1_, int p_146183_2_)
    {
        return this.func_146197_a(p_146183_1_, this.getCursorPosition(), true);
    }

    public int func_146197_a(int p_146197_1_, int p_146197_2_, boolean p_146197_3_)
    {
        int k = p_146197_2_;
        boolean flag1 = p_146197_1_ < 0;
        int l = Math.abs(p_146197_1_);

        for (int i1 = 0; i1 < l; ++i1)
        {
            if (flag1)
            {
                while (p_146197_3_ && k > 0 && this.text.charAt(k - 1) == 32)
                {
                    --k;
                }

                while (k > 0 && this.text.charAt(k - 1) != 32)
                {
                    --k;
                }
            }
            else
            {
                int j1 = this.text.length();
                k = this.text.indexOf(32, k);

                if (k == -1)
                {
                    k = j1;
                }
                else
                {
                    while (p_146197_3_ && k < j1 && this.text.charAt(k) == 32)
                    {
                        ++k;
                    }
                }
            }
        }

        return k;
    }
    
    @Override
    public void draw(int x, int y)
    {
    	if(!visible) return;
    	
    	this.drawTextBox(x, y);
    	super.draw(x, y);
    }

    /**
     * Moves the text cursor by a specified number of characters and clears the selection
     */
    public void moveCursorBy(int p_146182_1_)
    {
        this.setCursorPosition(this.selectionEnd + p_146182_1_);
    }

    /**
     * sets the position of the cursor to the provided index
     */
    public void setCursorPosition(int p_146190_1_)
    {
        this.cursorPosition = p_146190_1_;
        int j = this.text.length();

        if (this.cursorPosition < 0)
        {
            this.cursorPosition = 0;
        }

        if (this.cursorPosition > j)
        {
            this.cursorPosition = j;
        }

        this.setSelectionPos(this.cursorPosition);
    }

    /**
     * sets the cursors position to the beginning
     */
    public void setCursorPositionZero()
    {
        this.setCursorPosition(0);
    }

    /**
     * sets the cursors position to after the text
     */
    public void setCursorPositionEnd()
    {
        this.setCursorPosition(this.text.length());
    }

    /**
     * Call this method from your GuiScreen to process the keys into the textbox
     */
    

    public boolean keyTyped(char p_146201_1_, int p_146201_2_)
    {
        if (!this.isFocused)
        {
            return false;
        }
        else
        {
        	
        	if(!(p_146201_1_ >= 48 && p_146201_1_ <= 57) && this.type == Type.NUMBER)
        	{
        		if(p_146201_2_ != 14 && p_146201_2_ != 203 &&  p_146201_2_ != 205)
        			return false;
        	}
        		
            switch (p_146201_1_)
            {
                case 1:
                    this.setCursorPositionEnd();
                    this.setSelectionPos(0);
                    return true;
                case 3:
                    GuiScreen.setClipboardString(this.getSelectedText());
                    return true;
                case 22:
                    if (this.isEnabled())
                    {
                        this.writeText(GuiScreen.getClipboardString());
                    }

                    return true;
                case 24:
                    GuiScreen.setClipboardString(this.getSelectedText());

                    if (this.isEnabled())
                    {
                        this.writeText("");
                    }

                    return true;
                default:
                    switch (p_146201_2_)
                    {
                        case 14:
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                if (this.isEnabled())
                                {
                                    this.deleteWords(-1);
                                }
                            }
                            else if (this.isEnabled())
                            {
                                this.deleteFromCursor(-1);
                            }

                            return true;
                        case 199:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setSelectionPos(0);
                            }
                            else
                            {
                                this.setCursorPositionZero();
                            }

                            return true;
                        case 203:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                                }
                                else
                                {
                                    this.setSelectionPos(this.getSelectionEnd() - 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {

                                this.setCursorPosition(this.getNthWordFromCursor(-1));
                            }
                            else
                            {
                                this.moveCursorBy(-1);
                            }

                            return true;
                        case 205:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                if (GuiScreen.isCtrlKeyDown())
                                {
                                    this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                                }
                                else
                                {
                                    this.setSelectionPos(this.getSelectionEnd() + 1);
                                }
                            }
                            else if (GuiScreen.isCtrlKeyDown())
                            {
                                this.setCursorPosition(this.getNthWordFromCursor(1));
                            }
                            else
                            {
                                this.moveCursorBy(1);
                            }

                            return true;
                        case 207:
                            if (GuiScreen.isShiftKeyDown())
                            {
                                this.setSelectionPos(this.text.length());
                            }
                            else
                            {
                                this.setCursorPositionEnd();
                            }

                            return true;
                        case 211:
                            if (GuiScreen.isCtrlKeyDown())
                            {
                                if (this.isEnabled())
                                {
                                    this.deleteWords(1);
                                }
                            }
                            else if (this.isEnabled())
                            {
                                this.deleteFromCursor(1);
                            }

                            return true;
                        default:
                            if (ChatAllowedCharacters.isAllowedCharacter(p_146201_1_))
                            {
                                if (this.isEnabled())
                                {
                                    this.writeText(Character.toString(p_146201_1_));
                                }

                                return true;
                            }
                            else
                            {
                                return false;
                            }
                    }
            }
        }
    }

    /**
     * Args: x, y, buttonClicked
     */
    public boolean onLeftClick(int x, int y)
    {
        boolean flag = x >= this.posX && x < this.posX + this.width && y >= this.posY && y < this.posY + this.height;
        
        if (this.canLoseFocus)
        {  
        	if(!visible || !isEnabled())
        	{
        		return false;
        	}
        	
            this.setFocused(flag);
            
            if (this.isFocused)
            {
                int l = x - (this.posX + this.inputInset);

                l = (int) (l / this.font_scale);

                String s = field_146211_a.trimStringToWidth(this.type == Type.PASSWORD ? this.convertToPasswordDisplay(this.text).substring(this.lineScrollOffset) : this.text.substring(this.lineScrollOffset), this.getWidthText() - this.inputInset - 4);
                this.setCursorPosition(this.field_146211_a.trimStringToWidth(s, l).length() + this.lineScrollOffset);
                return true;
            }
            
            return false;
        }

        
        return false;
    }

    /**
     * Draws the textbox
     */
    public void drawTextBox(int x, int y)
    {
        if (this.isVisible())
        {
        	if(isTextured())
        	{
        		this.inputTexture.draw(x, y);
        	}
        	else
        	{
        		this.inputRect.draw(x, y);
        	}

            int i = isEnabled() ? this.textColor.toRGB() : this.disabledColor.toRGB();
            int j = this.cursorPosition - this.lineScrollOffset;
            int k = this.selectionEnd - this.lineScrollOffset;
            String s = field_146211_a.trimStringToWidth(this.type == Type.PASSWORD ? this.convertToPasswordDisplay(this.text).substring(lineScrollOffset) : this.text.substring(this.lineScrollOffset),(this.getWidthText() - this.inputInset - 4));
            boolean flag = j >= 0 && j <= s.length();
            boolean flag1 = this.isFocused && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.posX + 2 + this.inputInset;
            int i1 = this.posY + (this.height - (int) (this.field_146211_a.FONT_HEIGHT * this.font_scale)) / 2;
            int j1 = l;

            if (k > s.length())
            {
                k = s.length();
            }

            if (s.length() > 0)
            {
                String s1 = flag ? s.substring(0, j) : s;
                GL11.glPushMatrix();
                GL11.glScalef(font_scale, font_scale, font_scale);
                j1 = this.field_146211_a.drawString(s1, (int) (l / font_scale),  (int) (i1 / font_scale), i);
                j1 *= font_scale;
                GL11.glPopMatrix();
            }

            
            boolean flag2 = this.cursorPosition < this.text.length() || this.text.length() >= this.getMaxStringLength();
            
            
            int k1 = j1;

            if (!flag)
            {
                k1 = j > 0 ? l + this.width : l;
            }
            else if (flag2)
            {
                k1 = j1 - 1;
            }

            if (s.length() > 0 && flag && j < s.length())
            {
            	GuiUtils.renderText(s.substring(j), j1, i1, this.textColor.toRGB(), this.font_scale);
            }

            if (flag1)
            {
                if (flag2)
                {
                    Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + (int) (this.field_146211_a.FONT_HEIGHT * this.font_scale), this.textColor.toRGB());
                }
                else
                {
                	GuiUtils.renderText("_", k1, this.font_scale != 1 ? i1 : i1-1, this.textColor.toRGB(), this.font_scale);
                }
            }

            if (k != j)
            {
                int l1 = l + (int) (this.field_146211_a.getStringWidth(s.substring(0, k)) * this.font_scale) ;
                this.drawCursorVertical(k1, i1 - 1, l1 - 1, i1 + 1 + (int) (this.field_146211_a.FONT_HEIGHT * this.font_scale));
            }
        }
    }

    /**
     * draws the vertical line cursor in the textbox
     */
    private void drawCursorVertical(int p_146188_1_, int p_146188_2_, int p_146188_3_, int p_146188_4_)
    {
        int i1;

        if (p_146188_1_ < p_146188_3_)
        {
            i1 = p_146188_1_;
            p_146188_1_ = p_146188_3_;
            p_146188_3_ = i1;
        }

        if (p_146188_2_ < p_146188_4_)
        {
            i1 = p_146188_2_;
            p_146188_2_ = p_146188_4_;
            p_146188_4_ = i1;
        }

        if (p_146188_3_ > this.posX + this.width)
        {
            p_146188_3_ = this.posX + this.width;
        }

        if (p_146188_1_ > this.posX + this.width)
        {
            p_146188_1_ = this.posX + this.width;
        }

        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_REVERSE);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double)p_146188_1_, (double)p_146188_4_, 0.0D);
        tessellator.addVertex((double)p_146188_3_, (double)p_146188_4_, 0.0D);
        tessellator.addVertex((double)p_146188_3_, (double)p_146188_2_, 0.0D);
        tessellator.addVertex((double)p_146188_1_, (double)p_146188_2_, 0.0D);
        tessellator.draw();
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public GraphicObject setMaxStringLength(int p_146203_1_)
    {
        this.maxStringLength = p_146203_1_;

        if (this.text.length() > p_146203_1_)
        {
            this.text = this.text.substring(0, p_146203_1_);
        }
        return this;
    }

    /**
     * returns the maximum number of character that can be contained in this textbox
     */
    public int getMaxStringLength()
    {
        return this.maxStringLength;
    }

    /**
     * returns the current position of the cursor
     */
    public int getCursorPosition()
    {
        return this.cursorPosition;
    }


    /**
     * Sets the text colour for this textbox (disabled text will not use this colour)
     */
    public UITextField setTextColor(UIColor color)
    {
        this.textColor = color;
        return this;
    }

    public UITextField setDisabledTextColour(UIColor color)
    {
        this.disabledColor = color;
        return this;
    }

    /**
     * Sets focus to this gui element
     */
    public void setFocused(boolean p_146195_1_)
    {
        if (p_146195_1_ && !this.isFocused)
        {
            this.cursorCounter = 0;
        }
        

        this.isFocused = p_146195_1_;
    }
    
    @Override
    public GraphicObject setColor(UIColor color)
    {
    	this.inputRect.color = color;
    	super.setColor(color);
    	return this;
    }

    /**
     * the side of the selection that is not the cursor, may be the same as the cursor
     */
    public int getSelectionEnd()
    {
        return this.selectionEnd;
    }

    /**
     * returns the width of the textbox depending on if background drawing is enabled
     */
    public int getWidthText()
    {
        return (int) (this.width / this.font_scale);
    }

    /**
     * Sets the position of the selection anchor (i.e. position the selection was started at)
     */
    public void setSelectionPos(int p_146199_1_)
    {
        int j = this.text.length();

        if (p_146199_1_ > j)
        {
            p_146199_1_ = j;
        }

        if (p_146199_1_ < 0)
        {
            p_146199_1_ = 0;
        }

        this.selectionEnd = p_146199_1_;

        if (this.field_146211_a != null)
        {
            if (this.lineScrollOffset > j)
            {
                this.lineScrollOffset = j;
            }

            int k = this.getWidthText() - this.inputInset - 4;
            String s = field_146211_a.trimStringToWidth(this.type == Type.PASSWORD ? this.convertToPasswordDisplay(this.text).substring(this.lineScrollOffset) : this.text.substring(this.lineScrollOffset), k);
            int l = s.length() + this.lineScrollOffset;

            if (p_146199_1_ == this.lineScrollOffset)
            {
                this.lineScrollOffset -= field_146211_a.trimStringToWidth(this.type == Type.PASSWORD ? this.convertToPasswordDisplay(this.text) : this.text, k, true).length();
            }

            if (p_146199_1_ > l)
            {
                this.lineScrollOffset += p_146199_1_ - l;
            }
            else if (p_146199_1_ <= this.lineScrollOffset)
            {
                this.lineScrollOffset -= this.lineScrollOffset - p_146199_1_;
            }

            if (this.lineScrollOffset < 0)
            {
                this.lineScrollOffset = 0;
            }

            if (this.lineScrollOffset > j)
            {
                this.lineScrollOffset = j;
            }
        }
    }

    /**
     * if true the textbox can lose focus by clicking elsewhere on the screen
     */
    public void setCanLoseFocus(boolean p_146205_1_)
    {
        this.canLoseFocus = p_146205_1_;
    }
    
    private boolean isTextured()
    {
    	return this.inputTexture != null;
    }
    
    public String convertToPasswordDisplay(String text)
    {
    	StringBuilder sb = new StringBuilder();
    	
    	for(char ch : text.toCharArray())
    	{
    		sb.append("*");
    	}
    	
    	return sb.toString();
    }
    
    public void setInputInset(int inset)
    {
    	this.inputInset = inset;
    }

	@Override
	public boolean onRightClick(int x, int y) {
		return false;
	}

	@Override
	public boolean onWheelClick(int x, int y) {
		return false;
	}
	
	public boolean isFocused()
	{
		return this.isFocused;
	}
	
}
