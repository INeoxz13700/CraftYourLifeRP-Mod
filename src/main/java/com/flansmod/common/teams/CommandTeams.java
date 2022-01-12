package com.flansmod.common.teams;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import com.flansmod.common.FlansMod;
import com.flansmod.common.FlansModSettings;

public class CommandTeams extends CommandBase
{

	@Override
	public String getCommandName()
	{
		return "teams";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] split)
	{
		if(split == null || split.length == 0 || split[0].equals("help") || split[0].equals("?"))
		{
			if(split.length == 2)
				sendHelpInformation(sender, Integer.parseInt(split[1]));
			else sendHelpInformation(sender, 1);
			return;
		}
		if(split[0].equals("explosions"))
		{
			if(split.length != 2)
			{
				sender.addChatMessage(new ChatComponentText("Incorrect Usage : Should be /teams " + split[0] + " <true/false>"));
				return;
			}
			FlansModSettings.instance.explosions = Boolean.parseBoolean(split[1]);
			sender.addChatMessage(new ChatComponentText("Expolsions are now " + (FlansModSettings.instance.explosions ? "enabled" : "disabled")));
			FlansModSettings.instance.updateConfig();
			return;
		}
		if(split[0].equals("bombs") || split[0].equals("allowBombs"))
		{
			if(split.length != 2)
			{
				sender.addChatMessage(new ChatComponentText("Incorrect Usage : Should be /teams " + split[0] + " <true/false>"));
				return;
			}
			FlansModSettings.instance.bombsEnabled = Boolean.parseBoolean(split[1]);
			sender.addChatMessage(new ChatComponentText("Bombs are now " + (FlansModSettings.instance.bombsEnabled ? "enabled" : "disabled")));
			FlansModSettings.instance.updateConfig();

			return;
		}
		if(split[0].equals("bullets") || split[0].equals("bulletsEnabled"))
		{
			if(split.length != 2)
			{
				sender.addChatMessage(new ChatComponentText("Incorrect Usage : Should be /teams " + split[0] + " <true/false>"));
				return;
			}
			FlansModSettings.instance.bulletsEnabled = Boolean.parseBoolean(split[1]);
			sender.addChatMessage(new ChatComponentText("Bullets are now " + (FlansModSettings.instance.bulletsEnabled ? "enabled" : "disabled")));
			FlansModSettings.instance.updateConfig();

			return;
		}
		if(split[0].equals("canBreakGuns"))
		{
			if(split.length != 2)
			{
				sender.addChatMessage(new ChatComponentText("Incorrect Usage : Should be /teams " + split[0] + " <true/false>"));
				return;
			}
			FlansModSettings.instance.canBreakGuns = Boolean.parseBoolean(split[1]);
			sender.addChatMessage(new ChatComponentText("AAGuns and MGs can " + (FlansModSettings.instance.canBreakGuns ? "now" : "no longer") + " be broken"));
			FlansModSettings.instance.updateConfig();

			return;
		}
		if(split[0].equals("canBreakGlass"))
		{
			if(split.length != 2)
			{
				sender.addChatMessage(new ChatComponentText("Incorrect Usage : Should be /teams " + split[0] + " <true/false>"));
				return;
			}
			FlansModSettings.instance.canBreakGlass = Boolean.parseBoolean(split[1]);
			sender.addChatMessage(new ChatComponentText("Glass and glowstone can " + (FlansModSettings.instance.canBreakGlass ? "now" : "no longer") + " be broken"));
			FlansModSettings.instance.updateConfig();

			return;
		}
		if(split[0].equals("fuelNeeded"))
		{
			if(split.length != 2)
			{
				sender.addChatMessage(new ChatComponentText("Incorrect Usage : Should be /teams " + split[0] + " <true/false>"));
				return;
			}
			FlansModSettings.instance.vehiclesNeedFuel = Boolean.parseBoolean(split[1]);
			sender.addChatMessage(new ChatComponentText("Vehicles will " + (FlansModSettings.instance.vehiclesNeedFuel ? "now" : "no longer") + " require fuel"));
			FlansModSettings.instance.updateConfig();

			return;
		}
		if(split[0].equals("vehiclesBreakBlocks"))
		{
			if(split.length != 2)
			{
				sender.addChatMessage(new ChatComponentText("Incorrect Usage : Should be /teams " + split[0] + " <true/false>"));
				return;
			}
			FlansModSettings.instance.driveablesBreakBlocks = Boolean.parseBoolean(split[1]);
			sender.addChatMessage(new ChatComponentText("Vehicles will " + (FlansModSettings.instance.driveablesBreakBlocks ? "now" : "no longer") + " break blocks"));
			FlansModSettings.instance.updateConfig();

			return;
		}
		if(split[0].equals("bltss"))
		{
			if(split.length != 3)
			{
				sender.addChatMessage(new ChatComponentText("Incorrect Usage : Should be /teams bltss <0 ... 100> <0 ... 1000>"));
				sender.addChatMessage(new ChatComponentText("Bullet use player snapshot = Min[default=0] + (Ping / Divisor[default=50])"));
				return;
			}
			int bmn = Integer.parseInt(split[1]);
			int bdv = Integer.parseInt(split[2]);
			if(bmn < 0)		bmn = 0;
			if(bmn > 100)	bmn = 100;
			if(bdv < 0)		bdv = 0;
			if(bdv > 1000)	bdv = 1000;
			if(FlansModSettings.instance.bulletSnapshotMin != bmn || FlansModSettings.instance.bulletSnapshotDivisor != bdv)
			{
				FlansModSettings.instance.bulletSnapshotMin		= bmn;
				FlansModSettings.instance.bulletSnapshotDivisor	= bdv;
				FlansMod.updateBltssConfig(bmn, bdv);
			}

			sender.addChatMessage(new ChatComponentText("[BulletDelay] Min=" + bmn + " : Divisor=" + bdv));

			return;
		}

		sender.addChatMessage(new ChatComponentText(split[0] + " is not a valid teams command. Try /teams help"));
	}

	public List addTabCompletionOptions(ICommandSender sender, String[] prm)
	{
		if(prm.length <= 1)
		{
			return getListOfStringsMatchingLastWord(prm, new String[] {
					"help",
					"explosions",
					"canBreakGuns",
					"canBreakGlass",
					"fuelNeeded",
					"vehiclesBreakBlocks",
			});
		}
		
		return null;
	}

	public void sendHelpInformation(ICommandSender sender, int page)
	{
		if(page > 4 || page < 1)
		{
			ChatComponentText text = new ChatComponentText("Invalid help page, should be in the range (1-4)");
			text.getChatStyle().setColor(EnumChatFormatting.RED);
			sender.addChatMessage(text);
			return;
		}

		sender.addChatMessage(new ChatComponentText("\u00a72Listing teams commands \u00a7f[Page " + page + " of 4]"));
		switch(page)
		{
		case 1 :
		{
			sender.addChatMessage(new ChatComponentText("/teams help [page]"));
			sender.addChatMessage(new ChatComponentText("/teams off"));
			sender.addChatMessage(new ChatComponentText("/teams arena"));
			sender.addChatMessage(new ChatComponentText("/teams survival"));
			sender.addChatMessage(new ChatComponentText("/teams getSticks"));
			sender.addChatMessage(new ChatComponentText("/teams listGametypes"));
			//sender.addChatMessage(new ChatComponentText("/teams setGametype <name>"));
			//sender.addChatMessage(new ChatComponentText("/teams listAllTeams"));
			sender.addChatMessage(new ChatComponentText("/teams listTeams"));
			//sender.addChatMessage(new ChatComponentText("/teams setTeams <teamName1> <teamName2>"));
			sender.addChatMessage(new ChatComponentText("/teams addMap <shortName> <longName>"));
			sender.addChatMessage(new ChatComponentText("/teams listMaps"));
			sender.addChatMessage(new ChatComponentText("/teams removeMap <shortName>"));
			break;
		}
		case 2 :
		{

			//sender.addChatMessage(new ChatComponentText("/teams setMap <shortName>"));
			sender.addChatMessage(new ChatComponentText("/teams useRotation <true / false>"));
			sender.addChatMessage(new ChatComponentText("/teams voting <true / false>"));
			sender.addChatMessage(new ChatComponentText("/teams addRound <map> <gametype> <team1> <team2> <TimeLimit> <ScoreLimit> <isNextRoundOn true/false>"));
			sender.addChatMessage(new ChatComponentText("/teams listRounds"));
			sender.addChatMessage(new ChatComponentText("/teams removeRound <ID>"));
			sender.addChatMessage(new ChatComponentText("/teams nextMap"));
			//sender.addChatMessage(new ChatComponentText("/teams goToMap <ID>"));
			sender.addChatMessage(new ChatComponentText("/teams votingTime <time>"));
			sender.addChatMessage(new ChatComponentText("/teams scoreDisplayTime <time>"));
			break;
		}
		case 3 :
		{
			sender.addChatMessage(new ChatComponentText("/teams setVariable <variable> <value>"));
			sender.addChatMessage(new ChatComponentText("/teams forceAdventure <true / false>"));
			sender.addChatMessage(new ChatComponentText("/teams overrideHunger <true / false>"));
			sender.addChatMessage(new ChatComponentText("/teams explosions <true / false>"));
			sender.addChatMessage(new ChatComponentText("/teams canBreakGuns <true / false>"));
			sender.addChatMessage(new ChatComponentText("/teams canBreakGlass <true / false>"));
			sender.addChatMessage(new ChatComponentText("/teams armourDrops <true / false>"));
			sender.addChatMessage(new ChatComponentText("/teams weaponDrops <off / on / smart>"));
			sender.addChatMessage(new ChatComponentText("/teams fuelNeeded <true / false>"));
			sender.addChatMessage(new ChatComponentText("/teams mgLife <time>"));
			sender.addChatMessage(new ChatComponentText("/teams planeLife <time>"));
			sender.addChatMessage(new ChatComponentText("/teams vehicleLife <time>"));
			sender.addChatMessage(new ChatComponentText("/teams aaLife <time>"));

			sender.addChatMessage(new ChatComponentText("/teams vehiclesBreakBlocks <true / false>"));
			break;
		}
		case 4 :
		{
			sender.addChatMessage(new ChatComponentText("/teams ping <PlayerName>"));
			sender.addChatMessage(new ChatComponentText("/teams bltss <0 ... 100> <0 ... 1000>"));
			sender.addChatMessage(new ChatComponentText("/teams showbltss"));
			sender.addChatMessage(new ChatComponentText("/teams vehiclesCanZoom <true / false>"));
			break;
		}
		}
	}

	public EntityPlayerMP getPlayer(String name)
	{
		return MinecraftServer.getServer().getConfigurationManager().func_152612_a(name);
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender)
	{
		return "Try \"/teams help\"";
	}
}
