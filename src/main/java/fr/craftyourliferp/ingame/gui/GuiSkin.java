package fr.craftyourliferp.ingame.gui;

import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.lwjgl.opengl.GL11;

import com.google.gson.Gson;

import fr.craftyourliferp.data.AvatarUpdater;
import fr.craftyourliferp.data.AvatarUpdater.AvatarType;
import fr.craftyourliferp.data.UploadDetails;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIText;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketAvatar;
import fr.craftyourliferp.utils.FileForm;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.utils.HTTPUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class GuiSkin extends GuiBase {
	
	
	private AvatarType selectedCategory = AvatarType.SKIN;;
	
	private String chosenFile = "";
	
    public ResourceLocation background = new ResourceLocation(CraftYourLifeRPMod.name + ":gui/mainmenu/background.png");
    
    private int rotation;
    
    private FileForm fileForm;
	
	public GuiSkin()
	{
		super(new UIColor(225,225,225,255));
	}
	
	@Override
	public void initGui()
	{
		if(fileForm != null) fileForm.details = null;
		setVisible(true);

		setWindowSize(280, 220);
		setWindowPosition((width-280)/2,(height-220)/2);
		
		super.initGui();
	}
	
	public void initializeComponent() 
	{ 
		
		UIRect tittleRect = (UIRect)this.addComponent(new UIRect(new UIColor(31,32,33)).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), 20));
		
		this.addComponent(new UIImage(background)).setPosition(getWindowPosX(), tittleRect.getY2(), getWindowWidth(), 90);
		this.addComponent(new UIRect(new UIColor(0,0,0,160))).setPosition(getWindowPosX(), tittleRect.getY2(), getWindowWidth(), 90);

		
		this.addComponent(new UIRect(new UIColor(64,149,100)).setPosition(getWindowPosX()+72, tittleRect.getY2() + 45, 75, 16));
		
		UIRect uploadRect = (UIRect) this.addComponent(new UIRect(new UIColor(255,255,255)).setPosition(getWindowPosX()+ (getWindowWidth() - 140) / 2, getWindowPosY() + getWindowHeight()-80, 140, 16));

		
		this.addComponent(new UIButton(Type.SQUARE,new UIRect(new UIColor(64,149,100)),"Modifier",new UIRect(new UIColor(30, 107, 100)),new CallBackObject()
		{
			
			@Override
			public void call()
			{
				upload(selectedCategory);
			}
			
		}).setPosition(getWindowPosX()+ (getWindowWidth() - 80) / 2, getWindowPosY() + getWindowHeight()-55, 80, 16));
		
	
		this.addComponent(new UIButton(Type.SQUARE,new UIRect(new UIColor(225,225,225),new UIColor(220,220,220)),"Choisir un fichier...",new UIRect(new UIColor(220, 220, 220),new UIColor(0,0,0,50)),new CallBackObject()
		{
			
			@Override
			public void call()
			{
				if(fileForm != null) fileForm.details = null;
				
				UIRect rect = (UIRect) getComponent(7);
				
				UIButton edit = (UIButton) getComponent(5);
				UIButton delete = (UIButton) getComponent(11);

				edit.setY(getWindowPosY() + getWindowHeight()-55);
				delete.setY(getWindowPosY() + getWindowHeight()-35);
				
				rect.setVisible(false);

			    Thread t = new Thread() {
			        public void run() {
						 FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
						 dialog.setMode(FileDialog.LOAD);
						 dialog.setVisible(true);
						 chosenFile = dialog.getDirectory() + dialog.getFile();
						 if(dialog.getDirectory() == null || dialog.getFile() == null) chosenFile = "";
						 
			        }
			      };
			      t.start();
			}
			
		}).setTextColor(new UIColor(71,71,71)).setPosition(uploadRect.getX() + 5, uploadRect.getY() + 3, 80, 10));
		
		UIRect messageRect = (UIRect)this.addComponent(new UIRect(new UIColor(64,149,100)).setPosition(getWindowPosX()+ (getWindowWidth() - 140) / 2, getWindowPosY() + getWindowHeight()-60, 140, 16));
		messageRect.setVisible(false);
		
		this.addComponent(new UIButton("Skin",new UIColor(255,255,255),new UIColor(64,149,100),1f,new CallBackObject()
		{
			
			@Override
			public void call()
			{
				selectedCategory = AvatarType.SKIN;
			}
			
		}).setPosition(tittleRect.getX() + 10, tittleRect.getY() + 7));
		
		this.addComponent(new UIButton("Cape",new UIColor(255,255,255),new UIColor(64,149,100),1f,new CallBackObject()
		{
			
			@Override
			public void call()
			{
				selectedCategory = AvatarType.CAPE;
			}
			
		}).setPosition(tittleRect.getX() + 40, tittleRect.getY() + 7));
		
		
		this.addComponent(new UIButton("X",new UIColor(255,255,255),new UIColor(64,149,100),1f,new CallBackObject()
		{
			
			@Override
			public void call()
			{
				mc.displayGuiScreen(null);
			}
			
		}).setPosition(tittleRect.getX2() - 10, tittleRect.getY() + 7));
		
		this.addComponent(new UIButton(Type.SQUARE,new UIRect(new UIColor(255,0,0)),"Supprimer",new UIRect(new UIColor(200, 0, 0)),new CallBackObject()
		{
			
			@Override
			public void call()
			{
				delete(selectedCategory);
			}
			
		}).setPosition(getWindowPosX()+ (getWindowWidth() - 80) / 2, getWindowPosY() + getWindowHeight()-35, 80, 16));

	}
	
	@Override
	public void drawScreen(int x, int y, float partialTicks)
	{
		UIRect tittleRect =  (UIRect) getComponent(0);		
		UIRect blueRect =  (UIRect) getComponent(3);	
		UIRect uploadRect =  (UIRect) getComponent(4);
		
		UIButton selectFileBtn = (UIButton) getComponent(6);
		UIRect messageRect = (UIRect) getComponent(7);


		super.drawScreen(x, y, partialTicks);
		
		GuiUtils.renderCenteredText("Gestion Avatar", tittleRect.getX() + tittleRect.getWidth() / 2, (tittleRect.getY() + tittleRect.getHeight() / 2) - 4,1.2f);
		
		GuiUtils.drawPlayerStatic(getWindowPosX() + 30,getWindowPosY() + 195, 40, mc.thePlayer,rotation++);
		
		GuiUtils.renderText("Obtenez dès maintenant un avatar", getWindowPosX() + 70, tittleRect.getY2() + 30,GuiUtils.gameColor,1.05f);
		GuiUtils.renderText("Gratuitement", blueRect.getX() + 7, blueRect.getY() + 4,GuiUtils.gameColor,1f);

		GuiUtils.renderText("Mon avatar", getWindowPosX() + 5, getWindowPosY() + 205,5197647,1f);
		GuiUtils.renderCenteredText("Upload :", uploadRect.getX() + uploadRect.getWidth() / 2, uploadRect.getY() - 15,1f,5197647);

		GuiUtils.setClippingRegion(uploadRect.getX(), uploadRect.getY(), uploadRect.getWidth(), uploadRect.getHeight());
		if(chosenFile != null) GuiUtils.renderText(chosenFile, selectFileBtn.getX2()+4, uploadRect.getY() + 5,5197647,0.8f);
		GuiUtils.clearClippingRegion();
		
		if(fileForm != null && fileForm.details != null) GuiUtils.renderCenteredText(fileForm.details.getUploadDetail(), messageRect.getX() + messageRect.getWidth() / 2, (messageRect.getY() + messageRect.getHeight() / 2) - 3,0.9f,GuiUtils.gameColor);

		UIButton skinBtn = (UIButton) this.getComponent(8);
		UIButton capeBtn = (UIButton) this.getComponent(9);

		if(this.selectedCategory == AvatarType.SKIN)
		{
			skinBtn.setTextColor(new UIColor(64,149,100));
			capeBtn.setTextColor(new UIColor(255,255,255));
		}
		else
		{
			capeBtn.setTextColor(new UIColor(64,149,100));
			skinBtn.setTextColor(new UIColor(255,255,255));

		}
	}
	
	public void uploadSkin()
	{
		UIRect rect = (UIRect) getComponent(7);
		UIButton edit = (UIButton) getComponent(5);
		UIButton delete = (UIButton) getComponent(11);
		
		try {
			String result = HTTPUtils.sendUserFilePost(CraftYourLifeRPMod.getClientData().currentSession.getBasicToken(),CraftYourLifeRPMod.apiIp +  "/file_upload.php", fileForm.getFile(),"type=skin");
			Gson gson = new Gson();
			Class<?> mapclass= new HashMap<String, Object>().getClass();
			HashMap<String,Object> map = (HashMap<String, Object>) gson.fromJson(result, mapclass);
			
			fileForm.details.errorCode = (Double)map.get("result");
			fileForm.details.detail = (String)map.get("msg");
						
			
			int width = (int)(mc.fontRenderer.getStringWidth(fileForm.details.detail)*0.95f);
			rect.setX(getWindowPosX()+ (getWindowWidth() - width) / 2);
			rect.setWidth(width);
			
			
			if(fileForm.details.errorCode == 200)
			{
				rect.setColor(new UIColor(64, 149, 100));
				sendUpdateRequestToServer();
			}
			else
			{
				rect.setColor(new UIColor(235, 75, 63));
			}
			
			edit.setY(getWindowPosY() + getWindowHeight() - 40);
			delete.setY(getWindowPosY() + getWindowHeight() - 20);

			rect.setVisible(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void uploadCape()
	{
		UIRect rect = (UIRect) getComponent(7);
		UIButton edit = (UIButton) getComponent(5);
		UIButton delete = (UIButton) getComponent(11);
		
		try {
			String result = HTTPUtils.sendUserFilePost(CraftYourLifeRPMod.getClientData().currentSession.getBasicToken(),CraftYourLifeRPMod.apiIp +  "/file_upload.php", fileForm.getFile(),"type=cape");
			
			Gson gson = new Gson();
			Class<?> mapclass= new HashMap<String, Object>().getClass();
			HashMap<String,Object> map = (HashMap<String, Object>) gson.fromJson(result, mapclass);
			
			fileForm.details.errorCode = (Double)map.get("result");
			fileForm.details.detail = (String)map.get("msg");
			
			int width = (int)(mc.fontRenderer.getStringWidth(fileForm.details.detail)*0.95f);
			rect.setX(getWindowPosX()+ (getWindowWidth() - width) / 2);
			rect.setWidth(width);
			
			if(fileForm.details.errorCode == 200)
			{
				rect.setColor(new UIColor(64, 149, 100));
				sendUpdateRequestToServer();
			}
			else
			{
				rect.setColor(new UIColor(235, 75, 63));
			}
			edit.setY(getWindowPosY() + getWindowHeight() - 40);
			delete.setY(getWindowPosY() + getWindowHeight() - 20);

			rect.setVisible(true);

			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void displayErrorMessage(String message)
	{
		UIRect rect = (UIRect) getComponent(7);
		UIButton edit = (UIButton) getComponent(5);

		edit.setY(getWindowPosY() + getWindowHeight()- 20);
		
		fileForm.details = new UploadDetails(false,message);
		
		int width = (int)(mc.fontRenderer.getStringWidth(fileForm.details.detail)*0.95f);
		rect.setColor(new UIColor(235, 75, 63));
		rect.setX(getWindowPosX()+ (getWindowWidth() - width) / 2);
		rect.setWidth(width);
		rect.setVisible(true);
	}
	
	private void upload(AvatarType type)
	{
		fileForm = new FileForm(chosenFile).fileMaxSize(2000000).allowedExtension(new String[] {"png"});
		
		fileForm.details = fileForm.canUploaded();
		
		UIRect rect = (UIRect) getComponent(7);
		UIButton edit = (UIButton) getComponent(5);
		UIButton delete = (UIButton) getComponent(11);

		if(fileForm.details.canUploaded())
		{
			PacketAvatar packet = new PacketAvatar();
			
			
			if(type == type.SKIN)
			{
				uploadSkin();
			}
			else
			{
				uploadCape();
			}
		}
		else
		{
			edit.setY(getWindowPosY() + getWindowHeight() - 40);
			delete.setY(getWindowPosY() + getWindowHeight() - 20);
			rect.setColor(new UIColor(235, 75, 63));
			rect.setVisible(true);
		}
	}
	
	private void delete(AvatarType type)
	{
		UIRect rect = (UIRect) getComponent(7);
		UIButton edit = (UIButton) getComponent(5);
		UIButton delete = (UIButton) getComponent(11);
		try {
			
			String result;
			
			if(type == AvatarType.SKIN)
			{
				 result = HTTPUtils.doUserPostHttp(CraftYourLifeRPMod.getClientData().currentSession.getBasicToken(),CraftYourLifeRPMod.apiIp +  "/delete_avatar.php","type=skin");
			}
			else
			{
				 result = HTTPUtils.doUserPostHttp(CraftYourLifeRPMod.getClientData().currentSession.getBasicToken(),CraftYourLifeRPMod.apiIp +  "/delete_avatar.php","type=cape");
			}
			
			Gson gson = new Gson();
			Class<?> mapclass= new HashMap<String, Object>().getClass();
			HashMap<String,Object> map = (HashMap<String, Object>) gson.fromJson(result, mapclass);
			
			fileForm = new FileForm("");
			fileForm.details = new UploadDetails();
			fileForm.details.errorCode = (Double)map.get("result");
			fileForm.details.detail = (String)map.get("msg");
			
			int width = (int)(mc.fontRenderer.getStringWidth(fileForm.details.detail)*0.95f);
			rect.setX(getWindowPosX()+ (getWindowWidth() - width) / 2);
			rect.setWidth(width);
			
			
			if(fileForm.details.errorCode == 200)
			{
				rect.setColor(new UIColor(64, 149, 100));
				sendUpdateRequestToServer();
			}
			else
			{
				rect.setColor(new UIColor(235, 75, 63));
			}
			
			edit.setY(getWindowPosY() + getWindowHeight() - 40);
			delete.setY(getWindowPosY() + getWindowHeight() - 20);

			rect.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendUpdateRequestToServer()
	{
		PacketAvatar packet = new PacketAvatar();
		packet.entityId = mc.thePlayer.getEntityId();
		packet.type = 2;
		CraftYourLifeRPMod.packetHandler.sendToServer(packet);
	}
	
	 @Override
	 protected void keyTyped(char character, int keycode)
	 {
	    if (keycode == 1)
	    {
	            this.mc.displayGuiScreen((GuiScreen)null);
	            this.mc.setIngameFocus();
	            return;
	    }
		super.keyTyped(character, keycode);
	 }
	
}
