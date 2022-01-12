package fr.craftyourliferp.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.PlayerData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.entities.EntityLootableBody;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.network.PacketMessageDisplay;
import fr.craftyourliferp.utils.HTTPUtils;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class CommandBlacklist implements ICommand {

	public CommandBlacklist()
	{
		
	}
	
	@Override
	public int compareTo(Object arg0) {
		return 0;
	}

	@Override
	public String getCommandName() {
		return "blacklist";
	}

	@Override
	public String getCommandUsage(ICommandSender var1) {
		return "§c/blacklist help";
	}


	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length == 1)
		{
			ServerUtils.sendChatMessage(sender, "§aHelp :");
			ServerUtils.sendChatMessage(sender, "-/blacklist ban <username>");
			ServerUtils.sendChatMessage(sender, "-/blacklist unban <username>");
		}
		else if(args.length == 2)
		{
			String username = args[1];
			
			if(username.isEmpty())
			{
				ServerUtils.sendChatMessage(sender, "§cEntrez un pseudo");
			}
			
			if(args[0].equals("ban"))
			{
				ServerUtils.sendChatMessage(sender, "§aJoueur en cours de blacklist...");
				Runnable r =  new Runnable() {

				    public  void run() 
				    {
						String url = CraftYourLifeRPMod.apiIp +  "/blacklist_user.php";

				    	String result = HTTPUtils.doPostHttp(url, "pseudo=" + username + "&key=darkfightisalegend13@@@&type=ban");
				    	if(result.equals("true"))
				    	{
				    		ServerUtils.sendChatMessage(sender, "§aJoueur définitivement blacklist");
				    		
				    		EntityPlayer player = MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(username);
				    		if(player != null)
				    		{
					    		((EntityPlayerMP)player).playerNetServerHandler.kickPlayerFromServer("§chmm, why ?");
				    		}
				    	}
				    	else
				    	{
				    		ServerUtils.sendChatMessage(sender, result);
				    	}
				    }

				};
				r.run();
			}
			else if(args[0].equals("unban"))
			{
				ServerUtils.sendChatMessage(sender, "§aRetrait du joueur de la blacklist en cours...");
				Runnable r =  new Runnable() {

				    public  void run() 
				    {
						String url = CraftYourLifeRPMod.apiIp +  "/blacklist_user.php";

				    	String result = HTTPUtils.doPostHttp(url, "pseudo=" + username + "&key=darkfightisalegend13@@@&type=unban");
				    	if(result.equals("true"))
				    	{
				    		ServerUtils.sendChatMessage(sender, "§aJoueur retiré de la blacklist!");
				    	}
				    	else
				    	{
				    		ServerUtils.sendChatMessage(sender, result);
				    	}
				    }

				};
				r.run();
			}
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		return true;
	}

	@Override
	public List getCommandAliases() {
		return Arrays.asList("bl");
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		return false;
	}
	
}
