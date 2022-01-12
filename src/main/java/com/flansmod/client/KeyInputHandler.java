package com.flansmod.client;

import org.lwjgl.input.Keyboard;

import com.flansmod.api.IControllable;
import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.network.PacketInitialiseVehicleGui;
import com.flansmod.common.network.PacketInitialiseVehicleGui.Category;
import com.flansmod.common.network.PacketReload;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.VehiclesGui;
import fr.craftyourliferp.main.ExtendedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

@SideOnly(value = Side.CLIENT)
public class KeyInputHandler
{
	public static KeyBinding downKey = new KeyBinding("Down key", Keyboard.KEY_LCONTROL, "Flan's Mod");
	public static KeyBinding inventoryKey = new KeyBinding("Inventory key", Keyboard.KEY_R, "Flan's Mod");
	public static KeyBinding controlSwitchKey = new KeyBinding("Control Switch key", Keyboard.KEY_C, "Flan's Mod");
    public static KeyBinding gearKey = new KeyBinding("Gear Up / Down Key", Keyboard.KEY_L, "Flan's Mod");
    public static KeyBinding doorKey = new KeyBinding("Door Open / Close Key", Keyboard.KEY_K, "Flan's Mod");
    public static KeyBinding modeKey = new KeyBinding("Mode Switch Key", Keyboard.KEY_J, "Flan's Mod");
    public static KeyBinding flareKey = new KeyBinding("Flare Key", Keyboard.KEY_N, "Flan's Mod");
	public static KeyBinding klaxonKey = new KeyBinding("Klaxon Key", Keyboard.KEY_M, "Flan's Mod");
	//public static KeyBinding sirenKey = new KeyBinding("Siren Key", Keyboard.KEY_O, "Flan's Mod");

    
    public static KeyBinding ownedVehicleKey = new KeyBinding("Vehicule possede", Keyboard.KEY_EQUALS, "Flan's Mod");
	
    public static KeyBinding radioKey = new KeyBinding("Radio", Keyboard.KEY_INSERT, "Flan's Mod");

    
	Minecraft mc;

	public KeyInputHandler()
	{
		ClientRegistry.registerKeyBinding(downKey);
		ClientRegistry.registerKeyBinding(inventoryKey);

		ClientRegistry.registerKeyBinding(controlSwitchKey);
		ClientRegistry.registerKeyBinding(gearKey);
		ClientRegistry.registerKeyBinding(doorKey);
		ClientRegistry.registerKeyBinding(modeKey);
		ClientRegistry.registerKeyBinding(flareKey);
		ClientRegistry.registerKeyBinding(klaxonKey);
		//ClientRegistry.registerKeyBinding(sirenKey);
		ClientRegistry.registerKeyBinding(ownedVehicleKey);
		ClientRegistry.registerKeyBinding(radioKey);

		mc = Minecraft.getMinecraft();
	}

	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event)
	{
		EntityPlayer player = mc.thePlayer;
		Entity ridingEntity = player.ridingEntity;
		

		
		if(FMLClientHandler.instance().isGUIOpen(GuiChat.class) || mc.currentScreen != null)
			return;


		if(ownedVehicleKey.isPressed())
		{
			ExtendedPlayer p = ExtendedPlayer.get(player);
			mc.displayGuiScreen(new VehiclesGui(player));
			VehiclesGui gui = (VehiclesGui)mc.currentScreen;
			Category category = null;
			if(gui.selectedCategory.getSelectedElement().equals("Avion/Hélicoptère"))
			{
				category = Category.Plane;
			}
			else
			{
				category = Category.Vehicle;
			}
			FlansMod.packetHandler.sendToServer(new PacketInitialiseVehicleGui(category));
		}


		
		//Handle driving keys
		if(ridingEntity instanceof IControllable)
		{
			IControllable riding = (IControllable)ridingEntity;
			if(mc.gameSettings.keyBindForward.isPressed())
				riding.pressKey(0, player);
			if(mc.gameSettings.keyBindBack.isPressed())
				riding.pressKey(1, player);
			if(mc.gameSettings.keyBindLeft.isPressed())
				riding.pressKey(2, player);
			if(mc.gameSettings.keyBindRight.isPressed())
				riding.pressKey(3, player);
			if(mc.gameSettings.keyBindJump.isPressed())
				riding.pressKey(4, player);
			if(downKey.isPressed())
				riding.pressKey(5, player);
			if(mc.gameSettings.keyBindSneak.isPressed())
				riding.pressKey(6, player);
			if(mc.gameSettings.keyBindInventory.isPressed() || inventoryKey.isPressed())
				riding.pressKey(7, player);
			if(controlSwitchKey.isPressed())
				riding.pressKey(10, player);
			if(gearKey.isPressed())
				riding.pressKey(13, player);
			if(doorKey.isPressed())
				riding.pressKey(14, player);
			if(modeKey.isPressed())
				riding.pressKey(15, player);
			if(klaxonKey.isPressed())
				riding.pressKey(16, player);
			if(flareKey.isPressed()) 
				riding.pressKey(18, player);
			if(radioKey.isPressed())
				riding.pressKey(17, player);
			/*if(sirenKey.isPressed())
				riding.pressKey(19, player);*/
		}
	}
}
