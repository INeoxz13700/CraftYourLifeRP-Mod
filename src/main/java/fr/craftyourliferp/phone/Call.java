package fr.craftyourliferp.phone;

import java.awt.Color;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.ContactData;
import fr.craftyourliferp.data.SmsData;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UICheckBox;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIDropdown;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketConnectingCall;
import fr.craftyourliferp.network.PacketFinishCall;
import fr.craftyourliferp.network.PacketSendSms;
import fr.craftyourliferp.network.PacketSendVoice;
import fr.craftyourliferp.phone.Contacts.State;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionsRowList;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class Call extends Apps {
	
	private GuiPhone parent;
	
	public UIButton[] keys = new UIButton[12];
	
	public UIButton remove;
	
	public UIButton call;
	
	public UIButton hangup;
	
	public UIButton answer;
	
	private String display = "";

	private String callState = "";
	
	private int animState = 0;
		
	public ResourceLocation phonekey = new ResourceLocation("craftyourliferp", "phonekey");
	
	private int i;
	
	private int ticks;

	public Call(String name, ResourceLocation ico, GuiPhone phone)
	{
		super(name, ico, phone);	
		parent = phone;
	}
	
	
	@Override
	public void initializeComponent()
	{
		super.initializeComponent();
		
		remove = (UIButton) addComponent(new UIButton(UIButton.Type.SQUARE, "remove_button", new ResourceLocation("craftyourliferp", "gui/phone/button_remove.png"),null, false, new UIButton.CallBackObject()
		{
			@Override
			public void call()
			{
	    		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(phonekey, 1F));
				if(display.length() > 0)
					display = display.subSequence(0, display.length() - 1).toString();
			}
		}).setPosition(getWindowPosX() + getWindowWidth() - 20, getWindowPosY()+28,15,10));
		
		call = (UIButton)addComponent(new UIButton(UIButton.Type.SQUARE, "call_button", new ResourceLocation("craftyourliferp", "gui/phone/call-ico.png"),null, false,  new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				
				Call.this.call(display);
				updateGuiState();
			}
			
		}));
		
		hangup = (UIButton)addComponent(new UIButton(UIButton.Type.SQUARE, "hangup_button", new ResourceLocation("craftyourliferp", "gui/phone/call_hangup.png"),null, false, new UIButton.CallBackObject()
		{
		
			public void call()
			{
				if(!phone.getCallHandler().receiveCall || phone.getCallHandler().receiveCall && phone.getCallHandler().callState == 1)
				{
					finishCall();
					CraftYourLifeRPMod.packetHandler.sendToServer(new PacketFinishCall());
				}
				else
				{
					CraftYourLifeRPMod.packetHandler.sendToServer(new PacketConnectingCall(0));
					finishCall();
				}
			}
			
		}));
		
		answer = (UIButton)addComponent(new UIButton(UIButton.Type.SQUARE, "answer_button", new ResourceLocation("craftyourliferp", "gui/phone/call_answer.png"),null, false, new UIButton.CallBackObject()
		{
			
			public void call()
			{
				if(!(!phone.getCallHandler().receiveCall || phone.getCallHandler().receiveCall && phone.getCallHandler().callState == 1))
				{
					CraftYourLifeRPMod.packetHandler.sendToServer(new PacketConnectingCall(1));
					
				}
			}
			
		}));

		int baseX = getWindowPosX() + 1;
		int baseY = getWindowPosY() + 30;

		int j = 0;
		int k = 0;
		for(i = 0; i < 12; i++)
		{
			if(i > 0 && i < 10)
			{
				keys[i] = (UIButton)addComponent(new UIButton(UIButton.Type.SQUARE, i + "_button", new ResourceLocation("craftyourliferp", "gui/phone/" + i + ".png"), null, false, new UIButton.CallBackObject()
				{
					
					private int attribuatedNumber = i;
					
					@Override
					public void call()
					{
						
						if(display.length() >= 15) return;

						display += "" + attribuatedNumber;
			    		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(phonekey, 1F));
					}
					
				}).setPosition(baseX + 25 * (j+1), baseY + 25 * (k+1),15,15));
				if(j++ == 2)
				{
					j=0;
					k++;
				}
			}
			else if(i == 10)
			{
				keys[i] = (UIButton)addComponent(new UIButton(UIButton.Type.SQUARE, i + "_button", new ResourceLocation("craftyourliferp", "gui/phone/0.png"), null, false, new UIButton.CallBackObject()
				{			
					@Override
					public void call()
					{
						if(display.length() >= 15) return;

						
						display += "" + 0;
			    		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(phonekey, 1F));
					}
					
				}).setPosition(baseX + 18, getWindowPosY()+getWindowHeight()-25,15,15));
			}
			else if(i == 11)
			{
				keys[i] = (UIButton)addComponent(new UIButton(UIButton.Type.SQUARE, i + "_button", new ResourceLocation("craftyourliferp", "gui/phone/#.png"), null, false, new UIButton.CallBackObject()
				{			
					@Override
					public void call()
					{
					
						if(display.length() >= 15) return;
						
						display += "#";
			    		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(phonekey, 1F));
					}
					
				}).setPosition(getWindowPosX() + getWindowWidth() -35, getWindowPosY() + getWindowHeight()-25,15,15));	
			}
		}
		call.setPosition(getWindowPosX() + (getWindowWidth() - 18) / 2, getWindowPosY() + getWindowHeight() - 25,18,18);
		
		updateGuiState();
	}
	
	@Override
	public void initGui()
	{
		GuiPhone.GuiApps guiApps = (GuiPhone.GuiApps) phone.getChild(0);
		this.setWindowSize(guiApps.getWindowWidth(), guiApps.getWindowHeight());
		this.setWindowPosition(guiApps.getWindowPosX(), guiApps.getWindowPosY());
		super.initGui();
	}
	
	public void call(String number)
	{
		this.phone.setCallHandler(new CallHandler(number, false));
	}
	
	public void finishCall()
	{
		if(this.phone.getCallHandler() != null)
		{
			/*if(this.phone.getCallHandler().targetLine != null)
				this.phone.getCallHandler().targetLine.close();
		
			if(this.phone.getCallHandler().sourceLine != null)
				this.phone.getCallHandler().sourceLine.close();
			 */
			
			phone.getCallHandler().stopSound();
			
			phone.getCallHandler().recorder.setRunninng(false);
			
			phone.setCallHandler(null);
		}
		updateGuiState();
	}
	
	@Override
	public void drawScreen(int x, int y, float partialTicks) 
	{
		
		
		
		GuiPhone.GuiApps guiApps = (GuiPhone.GuiApps) phone.getChild(0);
		

		if(this.phone.getCallHandler() != null)
		{	
			GuiUtils.drawImage(guiApps.getWindowPosX()-2, guiApps.getWindowPosY()-1, new ResourceLocation("craftyourliferp", "gui/phone/call_background.png"), guiApps.getWindowWidth()+3, guiApps.getWindowHeight()+2);
			GuiUtils.drawImage(guiApps.getWindowPosX() + (guiApps.getWindowWidth() - 30) / 2, guiApps.getWindowPosY()+30, new ResourceLocation("craftyourliferp", "gui/phone/profil.png"), 30, 30);
			
			if(!this.phone.getCallHandler().receiveCall || (this.phone.getCallHandler().receiveCall && this.phone.getCallHandler().callState == 1))
			{
				
				hangup.setPosition(guiApps.getWindowPosX() + (guiApps.getWindowWidth() - 20) / 2, guiApps.getWindowPosY() + guiApps.getWindowHeight() - 30, 20, 20);

				GuiUtils.renderText(this.callState, guiApps.getWindowPosX() + 4, guiApps.getWindowPosY()+4, GuiUtils.gameColor, 0.8f);
				
				GuiUtils.renderCenteredText(this.phone.getCallHandler().getContactData() == null ? this.phone.getCallHandler().getNumber() : this.phone.getCallHandler().getContactData().name, guiApps.getWindowPosX() + guiApps.getWindowWidth() / 2, guiApps.getWindowPosY()+90);
				
				if(this.phone.getCallHandler().callState == 1) GuiUtils.renderCenteredText("" + this.TimeFormat(this.phone.getCallHandler().getCallElapsedTime()), guiApps.getWindowPosX() + guiApps.getWindowWidth() / 2, guiApps.getWindowPosY()+70,0.8f);
			}
			else
			{
				hangup.setPosition((guiApps.getWindowPosX() + (guiApps.getWindowWidth() - 20) / 2) - 25, guiApps.getWindowPosY()+120, 20, 20);
				answer.setPosition((guiApps.getWindowPosX() + (guiApps.getWindowWidth() - 20) / 2) + 25, guiApps.getWindowPosY()+120, 20, 20);
				
				GuiUtils.renderText(this.callState,  guiApps.getWindowPosX() + 4,  guiApps.getWindowPosY()+4, GuiUtils.gameColor, 0.8f);
				GuiUtils.renderCenteredText(this.phone.getCallHandler().getContactData() == null ? this.phone.getCallHandler().getNumber() : this.phone.getCallHandler().getContactData().name, ( guiApps.getWindowPosX() + guiApps.getWindowWidth() / 2), guiApps.getWindowPosY()+90);
				
				if(this.phone.getCallHandler().callState == 1) GuiUtils.renderCenteredText("" + this.TimeFormat(this.phone.getCallHandler().getCallElapsedTime()), guiApps.getWindowPosX() + guiApps.getWindowWidth() / 2,  guiApps.getWindowPosY()+70,0.8f);
			}
		}
		else
		{
			GuiUtils.drawImage(guiApps.getWindowPosX()-2, guiApps.getWindowPosY()-1, this.phone.background_apps, guiApps.getWindowWidth()+3, guiApps.getWindowHeight()+2);
			GuiUtils.drawImage(guiApps.getWindowPosX(), guiApps.getWindowPosY()+20, new ResourceLocation("craftyourliferp", "gui/phone/numero_container.png"), guiApps.getWindowWidth(), 25);
			GuiUtils.drawImage(guiApps.getWindowPosX(), guiApps.getWindowPosY()+46, new ResourceLocation("craftyourliferp", "gui/phone/key_container.png"), guiApps.getWindowWidth(), 105);
			
			GuiUtils.renderText(display, guiApps.getWindowPosX() + 5, guiApps.getWindowPosY()+30, 0);
		}
		super.drawScreen(x, y, partialTicks);
	}
	

	
	@Override
	public void mouseClickMove(int x, int y, int buttonId, long timeSinceLastClick) {
	}
	
	
	
	public String TimeFormat(int seconds)
	{
		int fmin = seconds / 60;
		int fsec = seconds % 60;
		
		return (fmin < 10 ? "0" + fmin : "" + fmin) + ":" + (fsec < 10 ? "0" + fsec : "" + fsec);
	}


	@Override
	public void onGuiClosed() {
		

	}
	

	@Override
	public void updateScreen() {
		

		
		if(this.phone.getCallHandler() != null)
		{
			this.phone.getCallHandler().update();
			if(this.phone.getCallHandler() != null && this.phone.getCallHandler().callState == -1)
			{
				if(this.phone.getGuiTicks() % 14 == 0)
				{
					if(animState == 0)
					{
						if(this.phone.getCallHandler() != null && this.phone.getCallHandler().receiveCall)
							this.callState = "Appel entrant";
						else
							this.callState = "Numérotation";
					}
					else if(animState == 1)
					{
						if(this.phone.getCallHandler() != null && this.phone.getCallHandler().receiveCall)
							this.callState = "Appel entrant.";
						else
							this.callState = "Numérotation.";
					}
					else if(animState == 2)
					{
						if(this.phone.getCallHandler() != null && this.phone.getCallHandler().receiveCall)
							this.callState = "Appel entrant..";
						else
							this.callState = "Numérotation..";
					}
					else if(animState == 3)
					{
						if(this.phone.getCallHandler() != null && this.phone.getCallHandler().receiveCall)
							this.callState = "Appel entrant...";
						else
							this.callState = "Numérotation...";
					}
					else
					{
						animState = 0;
						return;
					}
					animState++;					
				}

			}
			else
			{
				callState = "Appel en cours";
			}
		}
	}

	@Override
	public void keyTyped(char par1, int par2) {
		
	}

	
	public void updateGuiState()
	{

		if(this.phone.getCallHandler() != null)
		{	
			if(!this.phone.getCallHandler().receiveCall || (this.phone.getCallHandler().receiveCall && this.phone.getCallHandler().callState == 1))
			{

				getComponent(0).setVisible(false);
				getComponent(1).setVisible(false);
				
				getComponent(2).setVisible(true);
				
				getComponent(3).setVisible(false);

				
				for(int i = 4; i < 15; i++)
				{
					getComponent(i).setVisible(false);
				}
			}
			else
			{
				getComponent(0).setVisible(false);
				getComponent(1).setVisible(false);
				
				getComponent(2).setVisible(true);
				
				getComponent(3).setVisible(true);

				
				for(int i = 4; i < 15; i++)
				{
					getComponent(i).setVisible(false);
				}
			}
		}
		else
		{
			getComponent(0).setVisible(true);
			getComponent(1).setVisible(true);
			
			getComponent(2).setVisible(false);
			getComponent(3).setVisible(false);


			for(int i = 4; i < 15; i++)
			{
				getComponent(i).setVisible(true);
			}
		}
	
	}


	@Override
	public void back() {
		phone.currentApp = null;
	}


	@Override
	public void openApps() {
		// TODO Auto-generated method stub
		
	}
	
	

	

}
