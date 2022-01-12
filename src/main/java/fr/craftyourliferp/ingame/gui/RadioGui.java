package fr.craftyourliferp.ingame.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.network.PacketRadio;

import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class RadioGui extends GuiBase {
	
	private boolean isPlaying = false;
	
	private String previousRadio = "";
	
	private EntityVehicle currentVehicle;
	
	private List<Integer> sendedPacketToEntity = new ArrayList();
	
	public RadioGui(EntityVehicle vehicle)
	{
		currentVehicle = vehicle;
	}
	
	@Override
	public void initGui()
	{
		mc.gameSettings.showDebugInfo = false;
		this.setWindowSize(200, 150);
		this.setWindowPosition(5, height-155);
		super.initGui();
	}
	
	@Override
	public void initializeComponent() 
	{ 
		this.addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/radio/background.png")).setPosition(this.guiRect.getX(), this.guiRect.getY(), this.guiRect.getWidth(), this.guiRect.getHeight()));
		
		this.addComponent(new UIButton(Type.SQUARE,null, new ResourceLocation("craftyourliferp","gui/radio/play_button.png"),null,false,
				new CallBackObject()
				{
					@Override
					public void call()
					{						
						isPlaying = true;
						CraftYourLifeRPMod.radioHandler.playRadio();
					}
				}
		).setPosition(guiRect.getX()+5, getWindowPosY() + getWindowHeight() - 30, 25, 25));
		
		this.addComponent(new UIButton(Type.SQUARE,null, new ResourceLocation("craftyourliferp","gui/radio/stop_button.png"),null,false,
				new CallBackObject()
				{
					@Override
					public void call()
					{
						CraftYourLifeRPMod.radioHandler.stop();
						RadioGui.sendRadioToVehicleEntities(-1, currentVehicle);
					}
				}
		).setPosition(guiRect.getX()+35, getWindowPosY() + getWindowHeight() - 30, 25, 25));
		
		this.addComponent(new UIButton(Type.SQUARE,null, new ResourceLocation("craftyourliferp","gui/radio/next_button.png"),null,false,
				new CallBackObject()
				{
					@Override
					public void call()
					{
						try {
							CraftYourLifeRPMod.radioHandler.nextRadio();
							currentVehicle.sendedRadioPacketToEntity.clear();
							RadioGui.sendRadioToVehicleEntities(CraftYourLifeRPMod.radioHandler.getCurrentIndex(), currentVehicle);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		).setPosition(getWindowPosX()+getWindowWidth()-52, getWindowPosY() + 52, 50, 20));
		
		this.addComponent(new UIButton(Type.SQUARE,null, new ResourceLocation("craftyourliferp","gui/radio/back_button.png"),null,false,
				new CallBackObject()
				{
					@Override
					public void call()
					{
						try {
							CraftYourLifeRPMod.radioHandler.previousRadio();
							currentVehicle.sendedRadioPacketToEntity.clear();
							RadioGui.sendRadioToVehicleEntities(CraftYourLifeRPMod.radioHandler.getCurrentIndex(), currentVehicle);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
		).setPosition(getWindowPosX()+getWindowWidth()-90, getWindowPosY() + 52, 50, 20));
		
		this.addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/radio/radio_informations_container.png")).setPosition(getWindowPosX()+getWindowWidth()-100, getWindowPosY(), 100, 50));
		this.addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/radio/undefined_logo.png")).setPosition(getWindowPosX()+getWindowWidth()-75, getWindowPosY(), 45, 45));

		this.addComponent(new UIRect(new UIColor(30,31,30,200)).setPosition(getWindowPosX(), (int)(getWindowPosY()+getWindowHeight() / 1.8f), getWindowWidth(), 15));
		
		super.initializeComponent();
	}
	
	@Override
	public void drawScreen(int x, int y, float p_73863_3_)
	{	
		super.drawScreen(x, y, p_73863_3_);

		UIRect rect = (UIRect)getComponent(7);
		GuiUtils.renderCenteredText("Radio en cours : " + (CraftYourLifeRPMod.radioHandler.getCurrentRadio() == null ? "Aucun" : CraftYourLifeRPMod.radioHandler.getCurrentRadio().getTittle()), guiRect.getX() + guiRect.getWidth() / 2, rect.getY()+4,1f);
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		
		if(CraftYourLifeRPMod.radioHandler.getCurrentRadio() == null) return;
		
		if(!previousRadio.equalsIgnoreCase(CraftYourLifeRPMod.radioHandler.getCurrentRadio().getTittle()))
		{
			previousRadio = CraftYourLifeRPMod.radioHandler.getCurrentRadio().getTittle();
			UIImage radioImg = (UIImage) getComponent(6);
			getImage(radioImg.texture);
			if(radioImg.texture != null)radioImg.texture = radioImg.texture;
		}
		
	}
	
	@Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (p_73869_2_ == 1)
        {
            this.mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(p_73869_1_, p_73869_2_);
    }
	
	public static void sendRadioToVehicleEntities(int radioIndex, EntityVehicle vehicle)
	{
		if(vehicle == null) return;

		PacketRadio radio = new PacketRadio((byte)1);
		
		for(int i = 1; i < vehicle.seats.length; i++)
		{
			if(vehicle.seats[i] == null) continue;
			
			if(vehicle.seats[i].riddenByEntity != null)
			{
				EntityPlayer player = (EntityPlayer) vehicle.seats[i].riddenByEntity;

				if(radioIndex != -1)
				{
					if(vehicle.sendedRadioPacketToEntity.contains(player.getEntityId())) continue;
					vehicle.sendedRadioPacketToEntity.add(player.getEntityId());

				}
				else
				{
					vehicle.sendedRadioPacketToEntity.clear();
				}
								
				radio.entitiesId.add(player.getEntityId());
			}
			
		}
		radio.radioIndex = radioIndex;
		
		if(radio.entitiesId.size() > 0) FlansMod.getPacketHandler().sendToServer(radio);
	}
	
	public static ThreadDownloadImageData getImage(ResourceLocation resourceLocationIn) {

		   TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
		   ThreadDownloadImageData threadDownloadImageData;
		   threadDownloadImageData = new ThreadDownloadImageData(null, CraftYourLifeRPMod.radioHandler.getCurrentRadio().getIcoUrl(), resourceLocationIn, new ImageBufferDownload() {

		   @Override
		   public BufferedImage parseUserSkin(BufferedImage image)
		   {
			   return (image);
		   }
		   });

		   textureManager.loadTexture(resourceLocationIn, threadDownloadImageData);

		   return (threadDownloadImageData);
	}
	

	 

	    	
	
}
