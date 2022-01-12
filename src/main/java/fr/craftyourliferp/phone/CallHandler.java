package fr.craftyourliferp.phone;

import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.ContactData;
import fr.craftyourliferp.data.NumberData;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketFinishCall;
import fr.craftyourliferp.network.PacketSendVoice;
import fr.craftyourliferp.network.PacketStartCall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class CallHandler {
	
	public ResourceLocation call = new ResourceLocation("craftyourliferp", "call");
	public ResourceLocation call_ringtone = new ResourceLocation("craftyourliferp", "call_ringtone");
	public ResourceLocation call_false = new ResourceLocation("craftyourliferp", "false_request");
	public ResourceLocation call_unkown_number = new ResourceLocation("craftyourliferp", "unkown_number");
	
	private ContactData cData;
	
	private String receiverPhoneNumber;
	
	private String receiverUsername;
	
	private String callerUsername;
	
	private String callerNumber;
	
	private ISound playedSound;
	
	private int elapsedSecondsCall;
	
	private int ticksSinceCall;
	
	private boolean terminate = false;
	
	public Recorder recorder;
	
	/*Call state
	 * 
	 * -1 = waiting
	 *  0 = false_request
	 *  1 = accepted_request
	 *  2 = receiver already in call
	 *  3 = receiver not connected
	 *  4 = unkown number
	 */
	public int callState = -1;
		
	
	boolean receiveCall = false;

	//public AudioFormat format;
	
	//public DataLine.Info targetInfo;
	//public DataLine.Info sourceInfo;
	//public TargetDataLine targetLine;
	//public SourceDataLine sourceLine;
	//public int numBytesRead;
	//public byte[] targetData;

	public CallHandler(String number, boolean fromServer)
	{
		if(!fromServer)
		{
			this.receiverPhoneNumber = number;	
			cData = GuiPhone.getPhone().playerData.phoneData.getContact(number);
			CraftYourLifeRPMod.packetHandler.sendToServer(new PacketStartCall(this.receiverPhoneNumber, GuiPhone.getPhone().playerData.phoneData.getNumber(), Minecraft.getMinecraft().thePlayer.getCommandSenderName()));	
		}
		else
		{
			this.callerNumber = number;
			this.receiveCall = true;
			cData = GuiPhone.getPhone().playerData.phoneData.getContact(number);
		}
		
		recorder = new Recorder(false);
	
		
	}
	
	public void stopSound()
	{
		if(playedSound == null) return;
		
		Minecraft.getMinecraft().getSoundHandler().stopSound(playedSound);
	}
	

	public void update()
	{

		this.ticksSinceCall++;
		
		if(terminate)
		{
    		if(!Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(this.playedSound))
    		{
    			if(!this.receiveCall)
    				CraftYourLifeRPMod.packetHandler.sendToServer(new PacketFinishCall());
    			((Call) GuiPhone.getPhone().currentApp).finishCall();
    		}
		}
				
		if(this.callState == 1 && !recorder.isRunning())
		{
			recorder.start();			
		}
		
		if(this.ticksSinceCall % 20 == 0)
		{
			if(this.callState == 1)
			{
				this.elapsedSecondsCall++;
			}
			else if(this.callState == 0 || this.callState == 2 || this.callState == 3)
			{
				if(this.playedSound == null)
				{
					this.playedSound = PositionedSoundRecord.func_147674_a(call_false, 1F);
					Minecraft.getMinecraft().getSoundHandler().playSound(this.playedSound);
				}
				else
				{
		    		if(!Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(this.playedSound))
		    		{
		    			((Call) GuiPhone.getPhone().currentApp).finishCall();
		    		}
				}
			}
			else if(this.callState == 4)
			{
				if(this.playedSound == null)
				{
					this.playedSound = PositionedSoundRecord.func_147674_a(call_unkown_number, 1F);
					Minecraft.getMinecraft().getSoundHandler().playSound(this.playedSound);
				}
				else
				{
		    		if(!Minecraft.getMinecraft().getSoundHandler().isSoundPlaying(this.playedSound))
		    		{
		    			((Call) GuiPhone.getPhone().currentApp).finishCall();
		    		}
				}
			}
			
		}
		if(this.ticksSinceCall % (20*5) == 0 && this.callState == -1)
		{
			if(!this.receiveCall && !this.terminate)
	    		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(call, 1F));
	    	else if(this.receiveCall)
	    		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(call_ringtone, 1F));
		}
		if(this.ticksSinceCall % ((20 * 5) * 5) == 0)
		{
			if(this.callState == -1 && !this.receiveCall)
			{
				if(this.playedSound == null)
				{
					this.playedSound = PositionedSoundRecord.func_147674_a(call_false, 1F);
					Minecraft.getMinecraft().getSoundHandler().playSound(this.playedSound);
					terminate = true;
				}
			}
			else if(this.receiveCall && this.callState == -1)
			{
				this.terminate = true;
			}
		}
	}
	
	/*public void readCallDataFromServer(byte[] data)
	{
		if(sourceLine != null)
			sourceLine.write(data, 0, data.length);
	}*/
	
	public ContactData getContactData()
	{
		return this.cData;
	}
	
	public String getNumber()
	{
		if(!this.receiveCall) return this.receiverPhoneNumber;
		return this.callerNumber;
	}
	
	public int getCallElapsedTime()
	{
		return this.elapsedSecondsCall;
	}
	
	

}
