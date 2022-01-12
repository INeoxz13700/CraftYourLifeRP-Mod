package fr.craftyourliferp.game.events;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.flansmod.common.driveables.EntitySeat;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.BlockAtm;
import fr.craftyourliferp.blocks.BlockPainting;
import fr.craftyourliferp.cosmetics.CosmeticCachedData;
import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.cosmetics.ModelBaseBody;
import fr.craftyourliferp.cosmetics.ModelBaseHead;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIProgressBar;
import fr.craftyourliferp.ingame.gui.CylrpMessageHUD;
import fr.craftyourliferp.ingame.gui.GuiBase;
import fr.craftyourliferp.ingame.gui.GuiPause;
import fr.craftyourliferp.ingame.gui.NotificationBox.NotificationType;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.ClientProxy;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.mainmenu.gui.GuiCheckUpdate;
import fr.craftyourliferp.network.PacketOpenCardIdentity;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.utils.MathsUtils;
import fr.craftyourliferp.guicomponents.UIRect;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockLever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

public class RendererListener {

	public static ModelBiped modelPlayer;
		
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRender(RenderPlayerEvent.Pre event)
	{	
		List<CosmeticObject> data = new ArrayList();

		if(event.entityPlayer == Minecraft.getMinecraft().thePlayer)
		{
			data = ExtendedPlayer.get(event.entityPlayer).cosmeticsData;		
		}
		else
		{			
			CosmeticCachedData cachedData = CosmeticCachedData.getData(event.entityPlayer.getEntityId());
			
			if(cachedData.cosmeticsData != null) data = cachedData.cosmeticsData;
		}
		
		
		for(int i = 0; i < data.size(); i++)
		{
			CosmeticObject obj = data.get(i);
						
			if(!obj.getIsEquipped()) continue;
			
			if(obj.getModel() instanceof ModelBaseBody)
			{
				ModelBaseBody model = (ModelBaseBody) obj.getModel();
				model.render(event.entityPlayer, event.entityPlayer.limbSwing, event.entityPlayer.limbSwingAmount, event.renderer.modelBipedMain.bipedHead.rotateAngleY,  event.renderer.modelBipedMain.bipedHead.rotateAngleY,  event.renderer.modelBipedMain.bipedHead.rotateAngleX, event.partialRenderTick);
			}
			else
			{
				ModelBaseHead model = (ModelBaseHead) obj.getModel();
				model.render(event.entityPlayer, event.entityPlayer.limbSwing, event.entityPlayer.limbSwingAmount, event.renderer.modelBipedMain.bipedHead.rotateAngleY,  event.renderer.modelBipedMain.bipedHead.rotateAngleY,  event.renderer.modelBipedMain.bipedHead.rotateAngleX, event.partialRenderTick);
			}
		}
	}
	
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		if(event.phase == Phase.START)
		{
			
			if(event.side == Side.SERVER)
			{
				//ExtendedPlayer player = ExtendedPlayer.get(event.player);
				//player.getRenderer().update();
			}
			else
			{
				Minecraft mc = Minecraft.getMinecraft();

				/*PlayerCachedData cachedData = PlayerCachedData.getData(event.player);
				if(cachedData != null)
				{
					if(cachedData.currentAnimation != 0)
					{
						event.player.isSwingInProgress = false;
					}
				}*/
				ExtendedPlayer extendedPlayer = ExtendedPlayer.get(event.player);
				if(extendedPlayer != null)
				{
					if(extendedPlayer.currentAnimation != 0)
					{
						event.player.isSwingInProgress = false;
					}
				}
				
				if(event.player == mc.thePlayer && !(event.player.ridingEntity instanceof EntitySeat))
				{
					if(CraftYourLifeRPMod.radioHandler.getCurrentRadio() != null)
					{
						CraftYourLifeRPMod.radioHandler.stop();
					}
				}
			}
		}

	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderAnimation(RenderPlayerEvent.Pre event)
	{
		modelPlayer = event.renderer.modelBipedMain;	
		
		//PlayerCachedData data = PlayerCachedData.getData(event.entityPlayer);
		ExtendedPlayer data = ExtendedPlayer.get(event.entityPlayer);

		if(data == null) return;
		
		
		if(data.currentPlayingAnimation != null)
		{
			data.currentPlayingAnimation.oldTime = data.currentPlayingAnimation.newTime;
			data.currentPlayingAnimation.newTime = Minecraft.getSystemTime();
			data.currentPlayingAnimation.deltaTime = data.currentPlayingAnimation.newTime - data.currentPlayingAnimation.oldTime;
			
			if(data.currentPlayingAnimation.getAnimationisPlaying())
			{
				data.currentPlayingAnimation.playAnimation(modelPlayer, data, event.entityPlayer);
			}
		}
		
	}
	
	
}
