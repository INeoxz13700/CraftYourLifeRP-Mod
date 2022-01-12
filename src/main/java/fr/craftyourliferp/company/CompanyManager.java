package fr.craftyourliferp.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiCompany;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

@SideOnly(Side.CLIENT)
public class CompanyManager {

	public static void displayCompany(String companyName, int level, double levelupxp, double currentxp,int[] repartitions,double revenues, int sells,List<String> achievements,List<CompanyUser> users)
	{
		CompanyData data = new CompanyData();
		data.companyName = companyName;
		data.level = level;
		data.levelupXp = levelupxp;
		data.currentXp = currentxp;
		data.sells = sells;
		data.userCount = users.size();
		data.achievements = achievements;
		data.revenues = revenues;
		
		data.salariesRepartition = repartitions[0];
		data.stagiairesRepartition = repartitions[1];
		data.secretairesRepartition = repartitions[2];
		data.managersRepartition = repartitions[3];
		data.cogerantRepartition = repartitions[4];
		data.gerantRepartition = repartitions[5];

		
		Company company = new Company(data,users);
		Minecraft.getMinecraft().displayGuiScreen(new GuiCompany(company));
	}
	
	public static List<String> getRanks()
	{
		List<String> ranks = new ArrayList();
		ranks.add("Gerant");
		ranks.add("CoGerant");
		ranks.add("CommunityManager");
		ranks.add("Salarie");
		ranks.add("Stagiaire");
		ranks.add("Secretaire");
		return ranks;
	}
	
	public static List<String> getRepartitionElements()
	{
		List<String> repartitions = new ArrayList();
		for(int i = 0; i < 100; i+=10)
		{
			repartitions.add(i + "%");
		}
		return repartitions;
	}
	
	public static boolean PlayerHasRank(String rankName, CompanyUser user)
	{
		if(user.rank.equalsIgnoreCase(rankName)) return true;
		return false;
	}
	
	public static boolean isClientPlayer(CompanyUser user)
	{
		if(user.username.equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getCommandSenderName())) return true;
		return false;
	}
	
	public static int getRepartitionForRank(String rankname, Company company)
	{
		if(rankname.equalsIgnoreCase("salarie"))
		{
			return company.getData().salariesRepartition;
		}
		if(rankname.equalsIgnoreCase("stagiaire"))
		{
			return company.getData().stagiairesRepartition;
		}
		if(rankname.equalsIgnoreCase("secretaire"))
		{
			return company.getData().secretairesRepartition;
		}
		if(rankname.equalsIgnoreCase("communitymanager"))
		{
			return company.getData().managersRepartition;
		}
		if(rankname.equalsIgnoreCase("cogerant"))
		{
			return company.getData().cogerantRepartition;
		}
		if(rankname.equalsIgnoreCase("gerant"))
		{
			return company.getData().gerantRepartition;
		}
		return 0;
	}
	
	public static void setRepartitionForRank(String rankname,int repartition,Company company)
	{
		EntityClientPlayerMP localPlayer = (EntityClientPlayerMP) Minecraft.getMinecraft().thePlayer;
		if(rankname.equalsIgnoreCase("salarie"))
		{
			repartition = MathsUtils.Clamp(repartition, 0, getRepartitionLeft(rankname,company));
			company.getData().salariesRepartition = repartition;
		}
		if(rankname.equalsIgnoreCase("stagiaire"))
		{
			repartition = MathsUtils.Clamp(repartition, 0, getRepartitionLeft(rankname,company));

			company.getData().stagiairesRepartition = repartition;
		}
		if(rankname.equalsIgnoreCase("secretaire"))
		{
			repartition = MathsUtils.Clamp(repartition, 0, getRepartitionLeft(rankname,company));

			company.getData().secretairesRepartition =  repartition;
		}
		if(rankname.equalsIgnoreCase("communitymanager"))
		{
			repartition = MathsUtils.Clamp(repartition, 0, getRepartitionLeft(rankname,company));

			company.getData().managersRepartition = repartition;
		}
		if(rankname.equalsIgnoreCase("cogerant"))
		{
			repartition = MathsUtils.Clamp(repartition, 0, getRepartitionLeft(rankname,company));

			company.getData().cogerantRepartition = repartition;
		}
		if(rankname.equalsIgnoreCase("gerant"))
		{
			repartition = MathsUtils.Clamp(repartition, 0, getRepartitionLeft(rankname,company));

			company.getData().gerantRepartition = repartition;
		}
		
		localPlayer.sendChatMessage("/entreprise salaire " + rankname + " " + repartition);
	}
	
	public static int getRepartitionLeft(String rankName, Company company)
	{
		int repartitionForRank = getRepartitionForRank(rankName, company);
		return repartitionForRank + (100 - (company.getData().salariesRepartition + company.getData().stagiairesRepartition + company.getData().secretairesRepartition + company.getData().managersRepartition + company.getData().cogerantRepartition + company.getData().gerantRepartition));
	}
	
	public static CompanyUser getClientUser()
	{
		List<CompanyUser> users = GuiCompany.company.getUsers();
		for(int i = 0; i < users.size(); i++)
		{
			CompanyUser user = users.get(i);
			if(isClientPlayer(user)) return user;
		}
		return null;
	}
	
}
