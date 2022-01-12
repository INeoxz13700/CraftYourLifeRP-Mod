package fr.craftyourliferp.network;

import java.io.IOException;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import fr.craftyourliferp.data.NumberData;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.phone.Paypal;
import fr.craftyourliferp.phone.Paypal.PaypalAccount;
import fr.craftyourliferp.utils.DataUtils;
import fr.craftyourliferp.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

public class PacketPaypal extends PacketBase 
{
	/*
	 * 0: start registration
	 * 1: sendError
	 * 2: displayGui
	 * 3: send activation code
	 * 4: finalize registering
	 * 5: disconnect account
	 * 6: ask to server datas
	 * 7: send to client datas
	 * 8: connect Account
	 * 9: start forgot password request
	 * 10: send code forgot password request
	 * 11: change password
	 * 12: send money
	 * 13: resend code request
	 */
	public byte type;
	
	public String numberPhone;
	
	public byte errorType;
	
	public byte guiId;
	
	public String code;
	
	public String password;
	public String confirmationPassword;
	
	public float money;
	public boolean isAuthentified;
	
	public static PacketPaypal startRegistration(String numberPhone)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.numberPhone = numberPhone;
		packet.type = 0;
		return packet;
	}
	
	/** server only packet **/
	public static PacketPaypal throwError(int errorType)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.errorType = (byte)errorType;
		packet.type = 1;
		return packet;
	}
	
	public static PacketPaypal sendActivationCode(String code)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.code = code;
		packet.type = 3;
		return packet;
	}
	
	public static PacketPaypal switchGui(int guiId)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.guiId = (byte)guiId;
		packet.type = 2;
		return packet;
	}
	
	public static PacketPaypal finalizeRegistering(String password, String confirmationPassword)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.password = password;
		packet.confirmationPassword = confirmationPassword;
		packet.type = 4;
		return packet;
	}
	
	public static PacketPaypal disconnectAccount()
	{
		PacketPaypal packet = new PacketPaypal();
		packet.type = 5;
		return packet;
	}	
	
	public static PacketPaypal connectAccount(String phoneNumber, String password)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.type = 8;
		packet.numberPhone = phoneNumber;
		packet.password = password;
		return packet;
	}	
	
	public static PacketPaypal changePassword(String password, String confirmationPassword)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.password = password;
		packet.confirmationPassword = confirmationPassword;
		packet.type = 11;
		return packet;
	}
	
	public static PacketPaypal startForgotPasswordRequest(String phoneNumber)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.type = 9;
		packet.numberPhone = phoneNumber;
		return packet;
	}	
	
	
	public static PacketPaypal sendCode(String code)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.type = 10;
		packet.code = code;
		return packet;
	}	
	
	
	public static PacketPaypal syncPaypalData(EntityPlayer player)
	{
		PacketPaypal packet = new PacketPaypal();
		if(player.worldObj.isRemote)
		{
			packet.type = 6;
		}
		else
		{
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
			packet.type = 7;
			packet.isAuthentified = extendedPlayer.paypalData.isAuthentified;
		}
		return packet;
	}	
	
	public static PacketPaypal sendMoney(float money, String numberPhone)
	{
		PacketPaypal packet = new PacketPaypal();
		packet.type = 12;
		packet.money = money;
		packet.numberPhone = numberPhone;
		return packet;
	}	
	

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data)
	{
		data.writeByte(type);
		if(type == 0 || type == 9)
		{
			ByteBufUtils.writeUTF8String(data, numberPhone);
		}
		else if(type == 1)
		{
			data.writeByte(errorType);
		}
		else if(type == 2)
		{
			data.writeByte(guiId);
		}
		else if(type == 3 || type == 10)
		{
			ByteBufUtils.writeUTF8String(data, code);
		}
		else if(type == 4 || type == 11)
		{
			ByteBufUtils.writeUTF8String(data, password);
			ByteBufUtils.writeUTF8String(data, confirmationPassword);
		}
		else if(type == 7)
		{
			data.writeBoolean(isAuthentified);
		}
		else if(type == 8)
		{
			ByteBufUtils.writeUTF8String(data, numberPhone);
			ByteBufUtils.writeUTF8String(data, password);
		}
		else if(type == 12)
		{
			ByteBufUtils.writeUTF8String(data, numberPhone);
			data.writeFloat(money);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data)
	{
		type = data.readByte();
		if(type == 0 || type == 9)
		{
			numberPhone = ByteBufUtils.readUTF8String(data);
		}
		else if(type == 1)
		{
			errorType = data.readByte();
		}
		else if(type == 2)
		{
			guiId = data.readByte();
		}
		else if(type == 3 || type == 10)
		{
			code = ByteBufUtils.readUTF8String(data);
		}
		else if(type == 4 || type == 11)
		{
			password =  ByteBufUtils.readUTF8String(data);
			confirmationPassword = ByteBufUtils.readUTF8String(data);
		}
		else if(type == 7)
		{
			isAuthentified = data.readBoolean();
		}
		else if(type == 8)
		{
			numberPhone =  ByteBufUtils.readUTF8String(data);
			password = ByteBufUtils.readUTF8String(data);
		}
		else if(type == 12)
		{
			numberPhone =  ByteBufUtils.readUTF8String(data);
			money = data.readFloat();
		}
		
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(playerEntity);
		if(!extendedPlayer.paypalData.accountIsRegistered())
		{
			if(type == 0)
			{
				if(!DataUtils.isValidNumber(numberPhone))
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(0), playerEntity);
				}
				else
				{
					if(!numberPhone.equals(extendedPlayer.phoneData.getNumber()))
					{
						CraftYourLifeRPMod.packetHandler.sendTo(throwError(0), playerEntity);
					}
					else
					{
						extendedPlayer.paypalData.updateSmsCode();
						CraftYourLifeRPMod.packetHandler.sendTo(new PacketSendSms("Votre code d'activation : " + extendedPlayer.paypalData.smsCode,extendedPlayer.phoneData.getNumber(),"Paypal SMS service"), playerEntity);
						CraftYourLifeRPMod.packetHandler.sendTo(switchGui(1), playerEntity);
					}
				}
			}
			else if(type == 3)
			{
				if(extendedPlayer.paypalData.smsCode.equalsIgnoreCase(code))
				{
					if(code.isEmpty())
					{
						CraftYourLifeRPMod.packetHandler.sendTo(throwError(1), playerEntity);
						return;
					}
					CraftYourLifeRPMod.packetHandler.sendTo(switchGui(2), playerEntity);
				}
				else
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(1), playerEntity);
				}
			}
			else if(type == 4)
			{
				if(password.length() < 6)
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(3), playerEntity);
				}
				else if(!password.equals(confirmationPassword))
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(4), playerEntity);
				}
				else
				{
					extendedPlayer.paypalData.password = password;
					extendedPlayer.paypalData.isAuthentified = true;
					extendedPlayer.paypalData.smsCode = "";
					CraftYourLifeRPMod.packetHandler.sendTo(switchGui(3), playerEntity);
				}
			}
			else if(type == 8)
			{
				CraftYourLifeRPMod.packetHandler.sendTo(throwError(5), playerEntity);
			}
			else if(type == 9)
			{
				CraftYourLifeRPMod.packetHandler.sendTo(throwError(6), playerEntity);	
			}
		}
		else
		{
			if(type == 0)
			{
				if(!DataUtils.isValidNumber(numberPhone))
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(0), playerEntity);
				}
				else
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(11), playerEntity);
				}
			}
			if(type == 5)
			{
				extendedPlayer.paypalData.isAuthentified = false;
				CraftYourLifeRPMod.packetHandler.sendTo(switchGui(0), playerEntity);
			}
			else if(type == 8)
			{
				if(!extendedPlayer.phoneData.getNumber().equals(numberPhone))
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(5), playerEntity);

				}
				else if(!extendedPlayer.paypalData.password.equals(password))
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(5), playerEntity);
				}
				else
				{
					extendedPlayer.paypalData.isAuthentified = true;
					CraftYourLifeRPMod.packetHandler.sendTo(switchGui(3), playerEntity);
				}
			}
			else if(type == 9)
			{
				if(numberPhone.equals(extendedPlayer.phoneData.getNumber()))
				{
					extendedPlayer.paypalData.updateSmsCode();
					CraftYourLifeRPMod.packetHandler.sendTo(new PacketSendSms("Votre code afin de vérifier votre identité : " + extendedPlayer.paypalData.smsCode,extendedPlayer.phoneData.getNumber(),"Paypal SMS service"), playerEntity);
					CraftYourLifeRPMod.packetHandler.sendTo(switchGui(5), playerEntity);
				}
				else
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(6), playerEntity);
				}
			}
			else if(type == 10)
			{
				if(extendedPlayer.paypalData.smsCode.equals(code))
				{
					if(code.isEmpty())
					{
						CraftYourLifeRPMod.packetHandler.sendTo(throwError(7), playerEntity);
						return;
					}
					CraftYourLifeRPMod.packetHandler.sendTo(switchGui(6), playerEntity);
				}
				else
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(7), playerEntity);
				}
			}
			else if(type == 11)
			{
				if(password.length() < 6)
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(3), playerEntity);
				}
				else if(!password.equals(confirmationPassword))
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(4), playerEntity);
				}
				else
				{
					extendedPlayer.paypalData.password = password;
					extendedPlayer.paypalData.isAuthentified = true;
					extendedPlayer.paypalData.smsCode = "";
					CraftYourLifeRPMod.packetHandler.sendTo(switchGui(3), playerEntity);
				}
			}
			else if(type == 12)
			{
				if(money <= 0)
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(9), playerEntity);
					return;
				}
				
				String username = null;
				try 
				{
					username = NumberData.getUsernameByNumber(numberPhone);
				} catch (IOException e) {
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(8), playerEntity);
					e.printStackTrace();
					return;
				}
				
				if(username == null)
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(8), playerEntity);
					return;
				}
				else if(username.equalsIgnoreCase(playerEntity.getCommandSenderName()))
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(100), playerEntity);
					return;
				}
				
				
				
				EntityPlayer receiverPlayer = playerEntity.worldObj.getPlayerEntityByName(username);
				
				if(receiverPlayer == null)
				{
					CraftYourLifeRPMod.packetHandler.sendTo(throwError(8), playerEntity);
				}
				else
				{
					ExtendedPlayer receiverPlayerExtended = ExtendedPlayer.get(receiverPlayer);
					if(!receiverPlayerExtended.paypalData.accountIsRegistered())
					{
						CraftYourLifeRPMod.packetHandler.sendTo(throwError(8), playerEntity);
					}
					else
					{
						if(DataUtils.isLimitValue(money))
						{
							CraftYourLifeRPMod.packetHandler.sendTo(throwError(10), playerEntity);
						}
						else
						{
							if(!ServerUtils.playerHaveMoney(playerEntity, money))
							{
								CraftYourLifeRPMod.packetHandler.sendTo(throwError(12), playerEntity);
							}
							else
							{
								ServerUtils.takeMoney(playerEntity, money);
								ServerUtils.addMoney(receiverPlayer, money);
								//PlayerCachedData.getData(playerEntity).updatePlayerData();
								ExtendedPlayer.get(playerEntity).updatePlayerData();
							}
						}
					}
				}
				
			}
			else if(type == 6)
			{
				/*PlayerCachedData.getData(playerEntity).updatePlayerData();

				if(extendedPlayer.paypalData.isAuthentified)
				{
					PlayerCachedData.getData(playerEntity).syncData();
				}
				CraftYourLifeRPMod.packetHandler.sendTo(PacketPaypal.syncPaypalData(playerEntity), playerEntity);*/
				
				ExtendedPlayer cachedData = CraftYourLifeRPMod.getClientData().cachedData;
				
				cachedData.updatePlayerData();

				if(extendedPlayer.paypalData.isAuthentified)
				{
					cachedData.syncData();
				}
				CraftYourLifeRPMod.packetHandler.sendTo(PacketPaypal.syncPaypalData(playerEntity), playerEntity);
			}
		}
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer)
	{
		if(Minecraft.getMinecraft().currentScreen instanceof GuiPhone)
		{
			if(GuiPhone.getPhone().currentApp instanceof Paypal)
			{
				Paypal paypal = (Paypal) GuiPhone.getPhone().currentApp;
				if(type == 1)
				{
					paypal.displayError(errorType);
				}
				else if(type == 2)
				{
					paypal.setPage(guiId);
				}
				else if(type == 7)
				{
					ExtendedPlayer.get(clientPlayer).paypalData.isAuthentified = isAuthentified;
					
					if(isAuthentified && !(paypal.currentPage instanceof PaypalAccount))
					{
						paypal.setPage(3);
						return;
					}
				}
			}
		}
	}

}
