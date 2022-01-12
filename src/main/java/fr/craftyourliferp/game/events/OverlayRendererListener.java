package fr.craftyourliferp.game.events;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.flansmod.common.driveables.EntitySeat;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.BlockPainting;
import fr.craftyourliferp.blocks.tileentity.IStealingTileEntity;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIProgressBar;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.ingame.gui.GuiBase;
import fr.craftyourliferp.ingame.gui.GuiPause;
import fr.craftyourliferp.ingame.gui.GuiSleeping;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.ClientProxy;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.mainmenu.gui.GuiCheckUpdate;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class OverlayRendererListener {
	
	private ResourceLocation thirst_empty = new ResourceLocation("craftyourliferp","gui/thirst_Empty.png");
	
	private ResourceLocation thirst_full = new ResourceLocation("craftyourliferp","gui/thirst_Full.png");
	
	private ResourceLocation pixel = new ResourceLocation("craftyourliferp","gui/pixel.png");
		
	private static UIProgressBar stealingProgress = new UIProgressBar(new UIRect(new UIColor(0,0,0,150)), new UIRect(new UIColor(39, 186, 83)));
	
	private Block lastLookedBlock;
	
	private int elapsedTimeInSeconds = 0;
	
    private float previousValue = 0, currentValue = 0;
    
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderPreChat(RenderGameOverlayEvent.Text event)
    {
	    ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft(), (Minecraft.getMinecraft()).displayWidth, (Minecraft.getMinecraft()).displayHeight);

		 ExtendedPlayer ep = ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer);
		 if(mc.thePlayer.ridingEntity == null)
	     {
	    	     if(ep.thirst.playerLookWater(mc.theWorld))
	    	     {
	    	    	 renderAction("Pour boire");
	    	     }
	    	     else
	    	     {
		    	    if(mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() == ModdedItems.identityCard)
		    	    {
		    	    	renderAction("Utiliser la carte d'identité"); 
		    	    } 
		    	    else if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit != null)
		    	    {
			    	    	 
			    	    if(mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK)
			    	    {
					    	   if(mc.thePlayer.ticksExisted % 7 == 0)
					    	   {
							       int i = mc.objectMouseOver.blockX;
							       int j =  mc.objectMouseOver.blockY;
							       int k =  mc.objectMouseOver.blockZ; 
							       lastLookedBlock = mc.theWorld.getBlock(mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ);
					    	   }
					    	    	
						            
						 if(lastLookedBlock instanceof BlockDoor)
						 {
							 renderAction("Ouvrir la porte");
						 }
						 else 
						 {
						     //PlayerCachedData tempData = CraftYourLifeRPClient.cachedData;
						     ExtendedPlayer tempData = CraftYourLifeRPClient.cachedData;
							 
							 if(!tempData.isStealing())
							 {
								 TileEntity tile = mc.thePlayer.worldObj.getTileEntity(mc.objectMouseOver.blockX, mc.objectMouseOver.blockY, mc.objectMouseOver.blockZ);
					
								        	    
							      if(tile != null && tile instanceof IStealingTileEntity) 
							      {
							    	  renderAction(((IStealingTileEntity)tile).getDisplayMessageInLook());
							      }
							  }
							  else
							  {
									  elapsedTimeInSeconds = (int) (System.currentTimeMillis() - tempData.stealingTile.getStealingStartedTime()) / 1000;
								      previousValue = stealingProgress.getValue();
								      currentValue = (float)elapsedTimeInSeconds / tempData.stealingTile.getStealingTime();
							
									          
							
									  stealingProgress.setValue(MathsUtils.Lerp(previousValue,currentValue , event.partialTicks * 0.025f));
							
									            		
									  int x = (res.getScaledWidth() - 80) / 2;
									  int y = (res.getScaledHeight() / 2) +43;
									            	    
									            	    
									  stealingProgress.setPosition(x, y, 80, 7);
									  stealingProgress.draw(0, 0);
									            		
									  GuiUtils.renderCenteredText("Vol en cours...", event.resolution.getScaledWidth() / 2, y-15);
								 }
							}
						        
			    	    }
					    else if(mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY)
					    {
					    	if(mc.objectMouseOver.entityHit instanceof EntitySeat)
					     	{
					    		renderAction("Monter dans le véhicule");
					     	}
					     	else if(Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityPlayer)
				        	{
					     		EntityPlayer target = (EntityPlayer) Minecraft.getMinecraft().objectMouseOver.entityHit;
					  		    if(target.getHeldItem()!= null && target.getHeldItem().getItem() == ModdedItems.identityCard) renderAction("Voir la carte d'identité de " + target.getCommandSenderName());
				        	}
					    }
		    	    }
	    	     }
	     	}
    }
	

	@SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRenderPre(RenderGameOverlayEvent.Pre event)
    {
		Minecraft mc = Minecraft.getMinecraft();
		
    	int width = event.resolution.getScaledWidth() , height = event.resolution.getScaledHeight();
   
	
    	
    	if(event.type == RenderGameOverlayEvent.ElementType.DEBUG)
    	{
    		event.setCanceled(true);
    		
    		if(CraftYourLifeRPClient.getBlackScreenTime() > 0)
        	{        		
        		GuiUtils.drawRect(0, 0, width, height,"#0",1f); // dessin du container de la barre de vie
        		return;
        	}
    		
    		this.drawString(Minecraft.getMinecraft().fontRenderer, EnumChatFormatting.RED + mc.debug.split(",", 2)[0], 10, 30, 0x0FC702);
        
    		int x = (int) mc.thePlayer.posX;
    		int y = (int) mc.thePlayer.posY;
    		int z = (int) mc.thePlayer.posZ;
        
    		String corx = EnumChatFormatting.RED + "X : " + EnumChatFormatting.WHITE + x;  
    		String cory = EnumChatFormatting.RED + "Y : " + EnumChatFormatting.WHITE + y; 
    		String corz = EnumChatFormatting.RED + "Z : " + EnumChatFormatting.WHITE + z;
        
    		this.drawString(mc.fontRenderer, corx, 10, 50, 0x0FC702);
    		this.drawString(mc.fontRenderer, cory, 10, 70, 0x0FC702);
    		this.drawString(mc.fontRenderer, corz, 10, 90, 0x0FC702);   
    		
    	    int var1 = MathHelper.floor_double((double) (mc.thePlayer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
    	    String direction = EnumChatFormatting.RED + "Direction : " +  EnumChatFormatting.WHITE + Direction.directions[var1];
    		this.drawString(mc.fontRenderer, direction, 10, 110, 0x0FC702);   

    	}
    	else if(event.type == RenderGameOverlayEvent.ElementType.HEALTH) 
    	{
    		int healthbar_width = 100, healthbar_height = 10, healthbar_posx = (width / 2 - healthbar_width)+9 , healthbar_posy = height - 52;
    		float health = mc.thePlayer.getHealth();
    		int healthbar_fillmax = healthbar_posx + 79, healthbar_fillamount = healthbar_posx + 1 + (int)convertMcHealth(78);
    		event.setCanceled(true);
    		Gui.drawRect(healthbar_posx, healthbar_posy, healthbar_posx + 80, healthbar_posy + 10, Integer.MIN_VALUE); // dessin du container de la barre de vie
    		Gui.drawRect(healthbar_posx + 1, healthbar_posy + 1, healthbar_fillmax, healthbar_posy + 10 - 1, Color.WHITE.getRGB());
    		Gui.drawRect(healthbar_posx + 1, healthbar_posy + 1, healthbar_fillamount, healthbar_posy + 10 - 1, Color.RED.getRGB());
    		this.drawString(mc.fontRenderer, ((int)(health*100)/100.) + "/" + (float)mc.thePlayer.getMaxHealth(), healthbar_fillmax - 40 -20 , healthbar_posy + 1, 14520201);

    		GuiUtils.drawImage(healthbar_posx+2, healthbar_posy + 1.74f,new ResourceLocation(CraftYourLifeRPMod.name + ":gui/heart.png"), 6f, 6f);
    	}
    	else if(event.type == RenderGameOverlayEvent.ElementType.FOOD)
    	{
    	
    		if(CraftYourLifeRPClient.getBlackScreenTime() > 0)
        	{
        		event.setCanceled(true);
        		
        		GuiUtils.drawRect(0, 0, width, height,"#0",1f); // dessin du container de la barre de vie
        		return;
        	}
    	
    		int absbar_width = 100, abs_height = 10, absbar_posx = (width / 2 - absbar_width) + 111, absbar_posy = height - 52;
    		float abs = (float) ExtendedPlayer.get(mc.thePlayer).shield.getShield();
    		int absbar_fillmax = absbar_posx + 78, absbar_fillamount = absbar_posx + 1 + (int)convertMcAbsorbtion(78);
    		Gui.drawRect(absbar_posx, absbar_posy, absbar_posx + 80, absbar_posy + 10, Integer.MIN_VALUE); // dessin du container de la barre de vie
    		Gui.drawRect(absbar_posx + 1, absbar_posy + 1, absbar_fillmax + 1, absbar_posy + 10 - 1, Color.WHITE.getRGB());
    		Gui.drawRect(absbar_posx + 1, absbar_posy + 1, absbar_fillamount, absbar_posy + 10 - 1, getIntFromColor(0,128,255));
    		GuiUtils.drawImage(absbar_posx + 2, absbar_posy + 1.74f,new ResourceLocation(CraftYourLifeRPMod.name + ":gui/shield.png"), 6f, 6f);
    		this.drawString(mc.fontRenderer, (int)abs + "/100.0" , absbar_fillmax - 40-20, absbar_posy + 1, getIntFromColor(156,199,227));

    		for(int i = 0; i < 5; i++) {
    			GuiUtils.drawImage(0 + (i*6.5), 0,new ResourceLocation(CraftYourLifeRPMod.name + ":gui/star_empty.png"), 8f, 8f);
    			if( CraftYourLifeRPMod.getClientData().wantedLevel[i] == true) GuiUtils.drawImage(0.9 + (i*6.5), 0,new ResourceLocation(CraftYourLifeRPMod.name + ":gui/star_fill.png"), 6f, 8f);
    		}
    		
    		
    	}
    	else if(event.type == RenderGameOverlayEvent.ElementType.AIR) 
    	{
    		event.setCanceled(true);
    	
    		float value = ((float)mc.thePlayer.getAir() / 300) * 181;
    		GuiUtils.drawRect(width/2 - 90, height-26, 181, 2, "#0",0.2f);
    		GuiUtils.drawRect(width/2 - 90, height-26, value, 1, "#0F96FC",0.4f);

    	}
    	else if(event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE) 
    	{
    		event.setCanceled(true);
    	}
    	
    	
    	if (!(mc).thePlayer.capabilities.isCreativeMode) 
    	{
    	    ExtendedPlayer ep = ExtendedPlayer.get(mc.thePlayer);
    	    	
    	    GL11.glEnable(3042);
    	    GL11.glBlendFunc(770, 771);
    	    ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft(), (Minecraft.getMinecraft()).displayWidth, (Minecraft.getMinecraft()).displayHeight);
    	    GL11.glPushMatrix();
    	    int posX = res.getScaledWidth() / 2 - 7;
    	    int posY = res.getScaledHeight() - 52;
    	        
    	    mc.renderEngine.bindTexture(thirst_full);
    	    renderFarquad(posX, posY, 15.0F, 16.0F);
    	        
    	       
    	    mc.renderEngine.bindTexture(thirst_empty);
    	        
    	    float thirst = ep.thirst.getThirstNormalized();
    	     
    	    renderFarquad(posX, posY, 15.0F, 16.0F - thirst * 16.0F);
    	            	        
    	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	    GL11.glEnable(2929);
    	    GL11.glDepthFunc(515);
    	    GL11.glPopMatrix();
    	        
    	    if (thirst <= 0.1F || ep.getgAlcolInBlood() >= 4)
    	    {
    	        GL11.glPushMatrix();
    	        float alphaLevel = (MathHelper.cos((Minecraft.getMinecraft()).thePlayer.ticksExisted / 20.0F) + 1.0F) / 2.0F / 5.0F;
    	        GL11.glColor4f(0.0F, 0.0F, 0.0F, alphaLevel);
    	        mc.renderEngine.bindTexture(pixel);
    	        renderShrek(0.0F, 0.0F, res.getScaledWidth(), res.getScaledHeight());
    	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	        GL11.glPopMatrix();
    	    } 

    	    mc.getTextureManager().bindTexture(Gui.icons);
    	}
    	

    	ExtendedPlayer extendedPlayer = ExtendedPlayer.get(mc.thePlayer);
        if (extendedPlayer.sleepingTime > 0)
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            int sleepTime = extendedPlayer.sleepingTime;
            if(sleepTime > 100) sleepTime = 100;
            
            float opacity = (float)sleepTime / 100.0F;

            if (opacity > 1.0F)
            {
                opacity = 1.0F - (float)(sleepTime - 100) / 10.0F;
            }

            int color = (int)(220.0F * opacity) << 24 | 1052704;
         
            Gui.drawRect(0, 0, width, height, color);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
    }
	
	
	
	public int getIntFromColor(int Red, int Green, int Blue){
        Red = (Red << 16) & 0x00FF0000; //Shift red 16-bits and mask out other stuff
        Green = (Green << 8) & 0x0000FF00; //Shift Green 8-bits and mask out other stuff
        Blue = Blue & 0x000000FF; //Mask out anything not blue.

        return 0xFF000000 | Red | Green | Blue; //0xFF000000 for 100% Alpha. Bitwise OR everything together.
    }

    public void drawString(FontRenderer fontRenderer, String str, int x, int y, int color)
    {
    	fontRenderer.drawString(str, x, y, color);
    } 
	
    
    private Minecraft mc = Minecraft.getMinecraft();
   
    private float convertMcHealth(int healthbarmaxfill) {
    	float pHealth = (int)mc.thePlayer.getHealth();
    	float health = pHealth / mc.thePlayer.getMaxHealth();
    	float value = health * healthbarmaxfill;
		return value;
    }
    
    
    private float convertMcAir(int airbarmaxfill) {
    	float pAir = (int)mc.thePlayer.getAir();
    	float air = pAir / 300.0f;
    	float value = air * airbarmaxfill;
    	if(air < 0) value = 0;
		return value;
    }
    
    private float convertMcAbsorbtion(int absbarmaxfill) {
    	float pAbs = (float) ExtendedPlayer.get(mc.thePlayer).shield.getShield();
    	float abs = pAbs / 100.0f;
    	float value = abs * absbarmaxfill;
    	if(abs < 0) value = 0;
    	return value;
	}
    
    public static void renderFarquad(float x, float y, float width, float height) {
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glDisable(2896);
      GL11.glDisable(2929);
      Tessellator tessLater = Tessellator.instance;
      tessLater.startDrawingQuads();
      tessLater.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
      tessLater.addVertexWithUV(x, (y + height), 0.0D, 0.0D, (height / 16.0F));
      tessLater.addVertexWithUV((x + width), (y + height), 0.0D, 1.0D, (height / 16.0F));
      tessLater.addVertexWithUV((x + width), y, 0.0D, 1.0D, 0.0D);
      tessLater.draw();
    }
    
    public static void renderShrek(float x, float y, float width, float height) {
      GL11.glDisable(2896);
      Tessellator tessLater = Tessellator.instance;
      tessLater.startDrawingQuads();
      tessLater.addVertexWithUV(x, y, 0.0D, 0.0D, 0.0D);
      tessLater.addVertexWithUV(x, (y + height), 0.0D, 0.0D, 1.0D);
      tessLater.addVertexWithUV((x + width), (y + height), 0.0D, 1.0D, 1.0D);
      tessLater.addVertexWithUV((x + width), y, 0.0D, 1.0D, 0.0D);
      tessLater.draw();
    }

    public void renderAction(String message) 
    {
      ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft(), (Minecraft.getMinecraft()).displayWidth, (Minecraft.getMinecraft()).displayHeight);
      GL11.glPushMatrix();
      String text = "[Appuyez " + GameSettings.getKeyDisplayString(ClientProxy.keyBindings[0].getKeyCode()) + "] " + message;
      int x = ((res.getScaledWidth() - mc.fontRenderer.getStringWidth(text)) / 2)+2;
      int y = (res.getScaledHeight() / 2)+43;
      int color = 16777215;
      
      mc.fontRenderer.drawStringWithShadow(text, x, y, color);
      GL11.glPopMatrix();
    }
    
	@SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderTickEvent(TickEvent.RenderTickEvent event) 
	{
        if (Minecraft.getMinecraft().currentScreen instanceof GuiMainMenu) 
        {
        	//Minecraft.getMinecraft().displayGuiScreen(new GuiCheckUpdate());
        }
        else if(Minecraft.getMinecraft().currentScreen instanceof GuiIngameMenu)
        {
        	Minecraft.getMinecraft().displayGuiScreen(new GuiPause());
        }
    }
	
	@SideOnly(Side.CLIENT)
    @SubscribeEvent
	public void onClientTick(ClientTickEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		
		RenderManager.debugBoundingBox = false;
		
		mc.gameSettings.thirdPersonView = 0;
		
		if(event.phase == event.phase.END)
		{
			if(mc.thePlayer != null)
			{			
				ExtendedPlayer extendedPlayer = ExtendedPlayer.get(mc.thePlayer);
				if(extendedPlayer.isSleeping() && !(mc.currentScreen instanceof GuiSleeping))
				{
					mc.displayGuiScreen(new GuiSleeping());
				}
				else if(!extendedPlayer.isSleeping() && mc.currentScreen instanceof GuiSleeping)
				{
					mc.displayGuiScreen(null);
				}
				
				if(Minecraft.getMinecraft().thePlayer.ticksExisted % 20 == 0) CraftYourLifeRPClient.decrementBlackScreenCounter();
			}
		}
	}
	
}
