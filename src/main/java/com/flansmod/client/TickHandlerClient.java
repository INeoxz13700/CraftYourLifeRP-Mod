package com.flansmod.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MouseHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.flansmod.client.gui.GuiDriveableController;
import com.flansmod.client.model.RenderGun;
import com.flansmod.common.FlansMod;
import com.flansmod.common.PlayerData;
import com.flansmod.common.PlayerHandler;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityPlane;
import com.flansmod.common.driveables.EntitySeat;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.EnumDriveablePart;
import com.flansmod.common.driveables.mechas.EntityMecha;
import com.flansmod.common.guns.AttachmentType;
import com.flansmod.common.guns.EntityBullet;
import com.flansmod.common.guns.GunType;
import com.flansmod.common.guns.ItemGun;
import com.flansmod.common.teams.ItemTeamArmour;
import com.flansmod.common.types.InfoType;
import com.flansmod.common.vector.Vector3f;
import com.flansmod.common.vector.Vector3i;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;

public class TickHandlerClient
{
	public static final ResourceLocation offHand = new ResourceLocation("flansmod","gui/offHand.png");
	public static ArrayList<Vector3i> blockLightOverrides = new ArrayList<Vector3i>();
	public static ArrayList<Vector3i>vehicleLightOverrides = new ArrayList<Vector3i>();
	public static int lightOverrideRefreshRate = 5;
	public static int vehicleLightOverrideRefreshRate = 1;
	int tickcount = 0;
	int tickcountflash = 0;
	int tickcountWounded = 0;
	boolean isInFlash;
	int flashTime;
	EntityPlayer entityPlayerFlash;
	private static GuiScreen guiDriveableController = null;

	public TickHandlerClient()
	{
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void eventHandler(MouseEvent event)
	{
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemGun)
		{
			if(((ItemGun)player.getCurrentEquippedItem().getItem()).type.oneHanded && Keyboard.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode()) && Math.abs(event.dwheel) > 0)
				event.setCanceled(true);

		}
	}

	@SubscribeEvent
	public void eventHandler(RenderGameOverlayEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();

		//Remove crosshairs if looking down the sights of a gun
		if(event.type == ElementType.CROSSHAIRS && FlansModClient.currentScope != null)
		{
			event.setCanceled(true);
			return;
		}

		ScaledResolution scaledresolution = new ScaledResolution(FlansModClient.minecraft, FlansModClient.minecraft.displayWidth, FlansModClient.minecraft.displayHeight);
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();

		Tessellator tessellator = Tessellator.instance;

		if(!event.isCancelable() && event.type == ElementType.HELMET)
		{
			//Scopes and helmet overlays
			String overlayTexture = null;
			if (FlansModClient.currentScope != null && FlansModClient.currentScope.hasZoomOverlay() && FMLClientHandler.instance().getClient().currentScreen == null && FlansModClient.zoomProgress > 0.8F)
			{
				overlayTexture = FlansModClient.currentScope.getZoomOverlay();
			}
			else if(mc.thePlayer != null)
			{
				ItemStack stack = mc.thePlayer.inventory.armorInventory[3];
				if(stack != null && stack.getItem() instanceof ItemTeamArmour)
				{
					overlayTexture = ((ItemTeamArmour)stack.getItem()).type.overlay;
				}
			}

			String overlayTextureVehicle = null;
			if(FlansModClient.currentVehicleScope != null && FMLClientHandler.instance().getClient().currentScreen == null && mc.thePlayer != null)
				overlayTextureVehicle = FlansModClient.currentVehicleScope;


			if(overlayTexture != null)
			{
				FlansModClient.minecraft.entityRenderer.setupOverlayRendering();
				GL11.glEnable(3042 /* GL_BLEND */);
				GL11.glDisable(2929 /* GL_DEPTH_TEST */);
				GL11.glDepthMask(false);
				GL11.glBlendFunc(770, 771);
				GL11.glColor4f(1F, 1F, 1F, 1.0F);
				GL11.glDisable(3008 /* GL_ALPHA_TEST */);

				mc.renderEngine.bindTexture(FlansModResourceHandler.getScope(overlayTexture));

				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(i / 2 - 2 * j, j, -90D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(i / 2 + 2 * j, j, -90D, 1.0D, 1.0D);
				tessellator.addVertexWithUV(i / 2 + 2 * j, 0.0D, -90D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(i / 2 - 2 * j, 0.0D, -90D, 0.0D, 0.0D);
				tessellator.draw();
				GL11.glDepthMask(true);
				GL11.glEnable(2929 /* GL_DEPTH_TEST */);
				GL11.glEnable(3008 /* GL_ALPHA_TEST */);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}

			if(overlayTextureVehicle != null)
			{
				FlansModClient.minecraft.entityRenderer.setupOverlayRendering();
				GL11.glEnable(3042 /* GL_BLEND */);
				GL11.glDisable(2929 /* GL_DEPTH_TEST */);
				GL11.glDepthMask(false);
				GL11.glBlendFunc(770, 771);
				GL11.glColor4f(1F, 1F, 1F, 1.0F);
				GL11.glDisable(3008 /* GL_ALPHA_TEST */);

				mc.renderEngine.bindTexture(FlansModResourceHandler.getScope(overlayTextureVehicle));

				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(i / 2 - 2 * j, j, -90D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(i / 2 + 2 * j, j, -90D, 1.0D, 1.0D);
				tessellator.addVertexWithUV(i / 2 + 2 * j, 0.0D, -90D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(i / 2 - 2 * j, 0.0D, -90D, 0.0D, 0.0D);
				tessellator.draw();
				GL11.glDepthMask(true);
				GL11.glEnable(2929 /* GL_DEPTH_TEST */);
				GL11.glEnable(3008 /* GL_ALPHA_TEST */);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}

	    if(!event.isCancelable() && event.type == ElementType.HOTBAR)
	    {
			//Player ammo overlay
			if(mc.thePlayer != null)
			{
				ItemStack stack = mc.thePlayer.inventory.getCurrentItem();
				if(stack != null && stack.getItem() instanceof ItemGun)
				{
					ItemGun gunItem = (ItemGun)stack.getItem();
					GunType gunType = gunItem.type;
					int x = 0;
					for(int n = 0; n < gunType.numAmmoItemsInGun; n++)
					{
						ItemStack bulletStack = ((ItemGun)stack.getItem()).getBulletItemStack(stack, n);
						if(bulletStack != null && bulletStack.getItem() != null && bulletStack.getItemDamage() < bulletStack.getMaxDamage())
						{
							RenderHelper.enableGUIStandardItemLighting();
							GL11.glEnable(GL12.GL_RESCALE_NORMAL);
							OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
							drawSlotInventory(mc.fontRenderer, bulletStack, i / 2 + 16 + x, j - 65);
							GL11.glDisable(GL12.GL_RESCALE_NORMAL);
							RenderHelper.disableStandardItemLighting();
							String s = (bulletStack.getMaxDamage() - bulletStack.getItemDamage()) + "/" + bulletStack.getMaxDamage();
							if(gunType.submode.length >= 2)
							{
								s = s + "[" + gunType.getFireMode(stack) + "]";
							}
							if(bulletStack.getMaxDamage() == 1)
								s = "";
							mc.fontRenderer.drawString(s, i / 2 + 32 + x, j - 59, 0x000000);
							mc.fontRenderer.drawString(s, i / 2 + 33 + x, j - 60, 0xffffff);
							x += 16 + mc.fontRenderer.getStringWidth(s);
						}
					}
					//Render secondary gun
					PlayerData data = PlayerHandler.getPlayerData(mc.thePlayer, Side.CLIENT);
					if(gunType.oneHanded && data.offHandGunSlot != 0)
					{
						ItemStack offHandStack = mc.thePlayer.inventory.getStackInSlot(data.offHandGunSlot - 1);
						if(offHandStack != null && offHandStack.getItem() instanceof ItemGun)
						{
							GunType offHandGunType = ((ItemGun)offHandStack.getItem()).type;
							x = 0;
							for(int n = 0; n < offHandGunType.numAmmoItemsInGun; n++)
							{
								ItemStack bulletStack = ((ItemGun)offHandStack.getItem()).getBulletItemStack(offHandStack, n);
								if(bulletStack != null && bulletStack.getItem() != null && bulletStack.getItemDamage() < bulletStack.getMaxDamage())
								{
									//Find the string we are displaying next to the ammo item
									String s = (bulletStack.getMaxDamage() - bulletStack.getItemDamage()) + "/" + bulletStack.getMaxDamage();
									if(gunType.submode.length >= 2)
									{
										s = s + "[" + gunType.getFireMode(offHandStack) + "]";
									}
									if(bulletStack.getMaxDamage() == 1)
										s = "";

									//Draw the slot and then move leftwards
									RenderHelper.enableGUIStandardItemLighting();
									GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
									GL11.glEnable(GL12.GL_RESCALE_NORMAL);
									OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240F, 240F);
									drawSlotInventory(mc.fontRenderer, bulletStack, i / 2 - 32 - x, j - 65);
									x += 16 + mc.fontRenderer.getStringWidth(s);

									//Draw the string
									GL11.glDisable(GL12.GL_RESCALE_NORMAL);
									RenderHelper.disableStandardItemLighting();
									mc.fontRenderer.drawString(s, i / 2 - 16 - x, j - 59, 0x000000);
									mc.fontRenderer.drawString(s, i / 2 - 17 - x, j - 60, 0xffffff);
								}
							}
						}
					}
				}
			}



			//Off-hand weapon graphics
			mc.renderEngine.bindTexture(offHand);

			ItemStack currentStack = mc.thePlayer.inventory.getCurrentItem();
			PlayerData data = PlayerHandler.getPlayerData(mc.thePlayer, Side.CLIENT);

			if(currentStack != null && currentStack.getItem() instanceof ItemGun && ((ItemGun)currentStack.getItem()).type.oneHanded)
			{
				for(int n = 0; n < 9; n++)
				{
					if(data.offHandGunSlot == n + 1)
					{
						tessellator.startDrawingQuads();
						tessellator.addVertexWithUV(i / 2 - 88 + 20 * n, j - 3, -90D, 16D / 64D, 16D / 32D);
						tessellator.addVertexWithUV(i / 2 - 72 + 20 * n, j - 3, -90D, 32D / 64D, 16D / 32D);
						tessellator.addVertexWithUV(i / 2 - 72 + 20 * n, j - 19, -90D, 32D / 64D, 0D / 32D);
						tessellator.addVertexWithUV(i / 2 - 88 + 20 * n, j - 19, -90D, 16D / 64D, 0D / 32D);
						tessellator.draw();
					}
					else if(data.isValidOffHandWeapon(mc.thePlayer, n + 1))
					{
						tessellator.startDrawingQuads();
						tessellator.addVertexWithUV(i / 2 - 88 + 20 * n, j - 3, -90D, 0D / 64D, 16D / 32D);
						tessellator.addVertexWithUV(i / 2 - 72 + 20 * n, j - 3, -90D, 16D / 64D, 16D / 32D);
						tessellator.addVertexWithUV(i / 2 - 72 + 20 * n, j - 19, -90D, 16D / 64D, 0D / 32D);
						tessellator.addVertexWithUV(i / 2 - 88 + 20 * n, j - 19, -90D, 0D / 64D, 0D / 32D);
						tessellator.draw();
					}
				}
			}

			//RenderHitCrossHair
			if(EntityBullet.hitCrossHair)
			{
				tickcount = 20;
				EntityBullet.hitCrossHair = false;
			}
			if(tickcount > 0)
			{
				FlansModClient.minecraft.entityRenderer.setupOverlayRendering();
				GL11.glEnable(3042 /* GL_BLEND */);
				GL11.glDisable(2929 /* GL_DEPTH_TEST */);
				GL11.glDepthMask(false);
				GL11.glBlendFunc(770, 771);
				GL11.glColor4f(
						FlansMod.hitCrossHairColor[1],
						FlansMod.hitCrossHairColor[2],
						FlansMod.hitCrossHairColor[3],
						FlansMod.hitCrossHairColor[0] * (float)tickcount / 20);
				GL11.glDisable(3008 /* GL_ALPHA_TEST */);

				mc.renderEngine.bindTexture(new ResourceLocation("flansmod", "gui/CrossHair.png"));

				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(i / 2 - 2 * j, j, -90D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(i / 2 + 2 * j, j, -90D, 1.0D, 1.0D);
				tessellator.addVertexWithUV(i / 2 + 2 * j, 0.0D, -90D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(i / 2 - 2 * j, 0.0D, -90D, 0.0D, 0.0D);
				tessellator.draw();
				GL11.glDepthMask(true);
				GL11.glEnable(2929 /* GL_DEPTH_TEST */);
				GL11.glEnable(3008 /* GL_ALPHA_TEST */);
				GL11.glColor4f(1F, 1F, 1F, 1F);
			}

			if(mc.thePlayer.hurtTime > 0)
			{
				tickcountWounded = 40;
			}
			if(tickcountWounded > 0)
			{
				FlansModClient.minecraft.entityRenderer.setupOverlayRendering();
				GL11.glEnable(3042 /* GL_BLEND */);
				GL11.glDisable(2929 /* GL_DEPTH_TEST */);
				GL11.glDepthMask(false);
				GL11.glBlendFunc(770, 771);
				GL11.glColor4f(
						FlansMod.hitCrossHairColor[1],
						FlansMod.hitCrossHairColor[2],
						FlansMod.hitCrossHairColor[3],
						FlansMod.hitCrossHairColor[0] * (float)tickcountWounded / 20);
				GL11.glDisable(3008 /* GL_ALPHA_TEST */);

				mc.renderEngine.bindTexture(new ResourceLocation("flansmod", "gui/Blood.png"));

				tessellator.startDrawingQuads();
				tessellator.addVertexWithUV(i / 2 - 2 * j, j, -90D, 0.0D, 1.0D);
				tessellator.addVertexWithUV(i / 2 + 2 * j, j, -90D, 1.0D, 1.0D);
				tessellator.addVertexWithUV(i / 2 + 2 * j, 0.0D, -90D, 1.0D, 0.0D);
				tessellator.addVertexWithUV(i / 2 - 2 * j, 0.0D, -90D, 0.0D, 0.0D);
				tessellator.draw();
				GL11.glDepthMask(true);
				GL11.glEnable(2929 /* GL_DEPTH_TEST */);
				GL11.glEnable(3008 /* GL_ALPHA_TEST */);
				GL11.glColor4f(1F, 1F, 1F, 1F);
			}



			//DEBUG vehicles
			if(mc.thePlayer.ridingEntity instanceof EntitySeat)
			{
				EntityDriveable ent = ((EntitySeat)mc.thePlayer.ridingEntity).driveable;
				/*float speed = (float) (ent.motionX * ent.motionX + ent.motionY * ent.motionY + ent.motionZ * ent.motionZ);
				int healthP = (int)((float)ent.getDriveableData().parts.get(EnumDriveablePart.core).health / (float)ent.getDriveableData().parts.get(EnumDriveablePart.core).maxHealth * 100);
				int colour = 0;
				if(healthP > 75){
					colour = 0xffffff;
				} else if (healthP < 75 && healthP > 50){
					colour = 0x00ff00;
				} else if (healthP < 50 && healthP > 25){
					colour = 0xdaa520;
				} else {
					colour = 0xff0000;
				}*/
//				mc.fontRenderer.drawString("Throttle : " + ent.throttle, 2, 2, 0xffffff);
				//mc.fontRenderer.drawString("Health : " + ent.getDriveableData().parts.get(EnumDriveablePart.core).health+ " / " +ent.getDriveableData().parts.get(EnumDriveablePart.core).maxHealth, 2, 12, 0xffffff);
				if(ent instanceof EntityPlane)
				{
					EntityDriveable entP = (EntityDriveable)ent;
					if(entP.getDriveableType().hasFlare)
					{
						if(entP.ticksFlareUsing <= 0 && entP.flareDelay <= 0)
							mc.fontRenderer.drawString("Flare : READY"  , 2, 32, 0x00ff00);

						if(entP.ticksFlareUsing > 0)
							mc.fontRenderer.drawString("Flare : Deploying"  , 2, 42, 0xff0000);

						if(entP.flareDelay > 0)
							mc.fontRenderer.drawString("Flare : Reloading"  , 2, 52, 0xdaa520);
					}
					Vector3f up2 = (Vector3f)entP.axes.getYAxis().normalise();
					mc.fontRenderer.drawString(String.format("Lift : %.0f%%", (float)entP.getSpeedXYZ()*(float)entP.getSpeedXYZ()*up2.y), 92, 22, 0xffffff);

				}
				if(ent instanceof EntityVehicle)
				{
					EntityDriveable entP = (EntityDriveable)ent;
					if(entP.getDriveableType().hasFlare)
					{
						if(entP.ticksFlareUsing <= 0 && entP.flareDelay <= 0)
							mc.fontRenderer.drawString("Smoke : READY"  , 2, 32, 0x00ff00);

						if(entP.ticksFlareUsing > 0)
							mc.fontRenderer.drawString("Smoke : Deploying"  , 2, 42, 0xff0000);

						if(entP.flareDelay > 0)
							mc.fontRenderer.drawString("Smoke : Reloading"  , 2, 52, 0xdaa520);
					}

					if(((EntityVehicle)ent).getVehicleType().shootWithOpenDoor)
					{
						if(((EntityVehicle)ent).getDriveableData().varDoor){
							mc.fontRenderer.drawString("Weapon : READY"  , 2, 62, 0x00ff00);
							mc.fontRenderer.drawString("["+ Keyboard.getKeyName(KeyInputHandler.doorKey.getKeyCode())+ " to disable]", 100, 62, 0x00ff00);
						}

						if(!((EntityVehicle)ent).getDriveableData().varDoor){
							mc.fontRenderer.drawString("Weapon : DISABLED"  , 2, 62, 0xff0000);
							mc.fontRenderer.drawString("["+ Keyboard.getKeyName(KeyInputHandler.doorKey.getKeyCode())+ " to activate]", 100, 62, 0xff0000);
						}
					}
				}

	
			}
	    }
	}

	@SubscribeEvent
	public void renderTick(TickEvent.RenderTickEvent event)
	{
		switch(event.phase)
		{
		case START :
			RenderGun.smoothing = event.renderTickTime;
			renderTickStart(Minecraft.getMinecraft(), event.renderTickTime);
			break;
		case END :
			renderTickEnd(Minecraft.getMinecraft());
			break;
		}
	}

	@SubscribeEvent
	public void clientTick(TickEvent.ClientTickEvent event)
	{
		switch(event.phase)
		{
		case START :
			clientTickStart(Minecraft.getMinecraft());
			break;
		case END :
			clientTickEnd(Minecraft.getMinecraft());
			break;
		}
	}

	/** Handle flashlight block light override */
	public void clientTickStart(Minecraft mc)
	{
		if(tickcount > 0)
		{
			tickcount--;
		}
		if(tickcountWounded > 0)
		{
			tickcountWounded--;
		}
		if(FlansMod.ticker % lightOverrideRefreshRate == 0 && mc.theWorld != null)
		{
			//Check graphics setting and adjust refresh rate
			lightOverrideRefreshRate = mc.gameSettings.fancyGraphics ? 10 : 20;

			//Reset old light values
			for(Vector3i v : blockLightOverrides)
			{
				mc.theWorld.updateLightByType(EnumSkyBlock.Block, v.x, v.y, v.z);
			}
			//Clear the list
			blockLightOverrides.clear();

			//Find all flashlights
			for(Object obj : mc.theWorld.playerEntities)
			{
				EntityPlayer player = (EntityPlayer)obj;
				ItemStack currentHeldItem = player.getCurrentEquippedItem();
				if(currentHeldItem != null && currentHeldItem.getItem() instanceof ItemGun)
				{
					GunType type = ((ItemGun)currentHeldItem.getItem()).type;
					AttachmentType grip = type.getGrip(currentHeldItem);
					if(grip != null && grip.flashlight)
					{
						for(int i = 0; i < 2; i++)
						{
							MovingObjectPosition ray = player.rayTrace(grip.flashlightRange / 2F * (i + 1), 1F);
							if(ray != null)
							{
								int x = ray.blockX;
								int y = ray.blockY;
								int z = ray.blockZ;
								int side = ray.sideHit;
								switch(side)
								{
								case 0 : y--; break;
								case 1 : y++; break;
								case 2 : z--; break;
								case 3 : z++; break;
								case 4 : x--; break;
								case 5 : x++; break;
								}
								blockLightOverrides.add(new Vector3i(x, y, z));
								mc.theWorld.setLightValue(EnumSkyBlock.Block, x, y, z, 12);
								mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y + 1, z);
								mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y - 1, z);
								mc.theWorld.updateLightByType(EnumSkyBlock.Block, x + 1, y, z);
								mc.theWorld.updateLightByType(EnumSkyBlock.Block, x - 1, y, z);
								mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y, z + 1);
								mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y, z - 1);
							}
						}
					}
				}
			}

			for(Object obj : mc.theWorld.loadedEntityList)
			{
				if(obj instanceof EntityBullet)
				{
					EntityBullet bullet = (EntityBullet)obj;
					//IDynamicLightSource iDynamicLightSource = new AddFlansLightSource((Entity)bullet,15);
					if(!bullet.isDead && bullet.type.hasLight)
					{
						/*int x = MathHelper.floor_double(bullet.posX);
						int y = MathHelper.floor_double(bullet.posY);
						int z = MathHelper.floor_double(bullet.posZ);
						blockLightOverrides.add(new Vector3i(x, y, z));
						mc.theWorld.setLightValue(EnumSkyBlock.Block, x, y, z, 15);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y + 1, z);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y - 1, z);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x + 1, y, z);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x - 1, y, z);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y, z + 1);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y, z - 1);*/
						//DynamicLights.addLightSource(iDynamicLightSource);
					}else if(bullet.isDead && bullet.type.hasLight){
						//DynamicLights.removeLightSource(iDynamicLightSource);
					}
				}
				else if(obj instanceof EntityMecha)
				{
					EntityMecha mecha = (EntityMecha)obj;
					int x = MathHelper.floor_double(mecha.posX);
					int y = MathHelper.floor_double(mecha.posY);
					int z = MathHelper.floor_double(mecha.posZ);
					if(mecha.lightLevel() > 0)
					{
						blockLightOverrides.add(new Vector3i(x, y, z));
						mc.theWorld.setLightValue(EnumSkyBlock.Block, x, y, z, Math.max(mc.theWorld.getBlockLightValue(x, y, z), mecha.lightLevel()));
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x + 1, y, z);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x - 1, y + 1, z);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y + 1, z);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y - 1, z);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y, z + 1);
						mc.theWorld.updateLightByType(EnumSkyBlock.Block, x, y, z - 1);
					}
					if(mecha.forceDark())
					{
						for(int i = -3; i <= 3; i++)
						{
							for(int j = -3; j <= 3; j++)
							{
								for(int k = -3; k <= 3; k++)
								{
									int xd = i + x;
									int yd = j + y;
									int zd = k + z;
									blockLightOverrides.add(new Vector3i(xd, yd, zd));
									mc.theWorld.setLightValue(EnumSkyBlock.Sky, xd, yd, zd, Math.abs(i) + Math.abs(j) + Math.abs(k));
								}
							}
						}
					}
				}
			}
		}

		if(FlansMod.ticker % vehicleLightOverrideRefreshRate == 0 && mc.theWorld != null)
		{
			//Check graphics setting and adjust refresh rate
			vehicleLightOverrideRefreshRate = mc.gameSettings.fancyGraphics ? 1 : 2;

			//Reset old light values
			for(Vector3i v : vehicleLightOverrides)
			{
				mc.theWorld.updateLightByType(EnumSkyBlock.Block, v.x, v.y, v.z);
			}
			//Clear the list
			vehicleLightOverrides.clear();
		}
	}

	public void clientTickEnd(Minecraft minecraft)
	{ 
		FlansModClient.tick();
	}

	public void renderTickStart(Minecraft mc, float smoothing)
	{
		// CAPTURE MOUSE INPUT!
		if (mc.currentScreen == null && FlansModClient.controlModeMouse)
		{
			MouseHelper mouse = mc.mouseHelper;
			Entity ridden = mc.thePlayer.ridingEntity;

			if (ridden instanceof EntityDriveable)
			{
				EntityDriveable entity = (EntityDriveable) ridden;
				entity.onMouseMoved(mouse.deltaX, mouse.deltaY);
			}
		}
		FlansModClient.renderTick(smoothing);

		if(mc.currentScreen instanceof GuiDriveableController)
		{
			guiDriveableController = mc.currentScreen;
			mc.currentScreen = null;
		}
		else
		{
			guiDriveableController = null;
		}
	}

	public void renderTickEnd(Minecraft mc)
	{
		if(mc.currentScreen==null && guiDriveableController!=null)
		{
			mc.currentScreen = guiDriveableController;
			guiDriveableController = null;
		}

		Tessellator tessellator = Tessellator.instance;
		ScaledResolution scaledresolution = new ScaledResolution(FlansModClient.minecraft, FlansModClient.minecraft.displayWidth, FlansModClient.minecraft.displayHeight);
		int i = scaledresolution.getScaledWidth();
		int j = scaledresolution.getScaledHeight();

		//FlashBang
		if(FlansModClient.isInFlash){
			isInFlash = true;
			flashTime = FlansModClient.flashTime;
			tickcountflash = 0;
			FlansModClient.isInFlash = false;
			FlansModClient.flashTime = 0;
		}
		if(isInFlash && tickcountflash < flashTime){
			FlansModClient.minecraft.entityRenderer.setupOverlayRendering();
			GL11.glEnable(3042 /* GL_BLEND */);
			GL11.glDisable(2929 /* GL_DEPTH_TEST */);
			GL11.glDepthMask(false);
			GL11.glBlendFunc(770, 771);
			GL11.glColor4f(1F, 1F, 1F, 1.0F - (float)tickcountflash / flashTime);
			GL11.glDisable(3008 /* GL_ALPHA_TEST */);

			mc.renderEngine.bindTexture(new ResourceLocation("flansmod", "gui/flash.png"));

			tessellator.startDrawingQuads();
			tessellator.addVertexWithUV(i / 2 - 2 * j, j, -90D, 0.0D, 1.0D);
			tessellator.addVertexWithUV(i / 2 + 2 * j, j, -90D, 1.0D, 1.0D);
			tessellator.addVertexWithUV(i / 2 + 2 * j, 0.0D, -90D, 1.0D, 0.0D);
			tessellator.addVertexWithUV(i / 2 - 2 * j, 0.0D, -90D, 0.0D, 0.0D);
			tessellator.draw();
			GL11.glDepthMask(true);
			GL11.glEnable(2929 /* GL_DEPTH_TEST */);
			GL11.glEnable(3008 /* GL_ALPHA_TEST */);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			tickcountflash++;
		}else{
			isInFlash = false;
			flashTime = 0;
			tickcountflash = 0;
		}
	}

	private void drawSlotInventory(FontRenderer fontRenderer, ItemStack itemstack, int i, int j)
	{
		if(itemstack == null || itemstack.getItem() == null)
			return;
		itemRenderer.renderItemIntoGUI(fontRenderer, FlansModClient.minecraft.renderEngine, itemstack, i, j);
		itemRenderer.renderItemOverlayIntoGUI(fontRenderer, FlansModClient.minecraft.renderEngine, itemstack, i, j);
	}


	private static RenderItem itemRenderer = new RenderItem();
	
}
