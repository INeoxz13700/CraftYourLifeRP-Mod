package com.flansmod.common.driveables;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class VehicleOverlay {

	private Minecraft mc;
		
	
	private float rotationSpeedometer;
	
	private float rotationFuelometer;
;

	public VehicleOverlay(Minecraft minecraft)
	{
		mc = minecraft;
	}
	
	@SubscribeEvent
    public void onRenderRepa(RenderGameOverlayEvent.Post event)
    {
        if((event.type == RenderGameOverlayEvent.ElementType.ALL) && mc.thePlayer.ridingEntity instanceof EntitySeat)
        {
        	EntityDriveable driveable = ((EntitySeat) mc.thePlayer.ridingEntity).driveable;

            if(driveable instanceof EntityVehicle)
            {
               EntityVehicle vehicle = (EntityVehicle)driveable;
                 
               if(vehicle.seats[0].riddenByEntity == null) return;
               
               int width = event.resolution.getScaledWidth();
               int height = event.resolution.getScaledHeight();
               
               
               GuiUtils.drawImageWithTransparency(width-130, height-130, new ResourceLocation("craftyourliferp","gui/vehiclecontroller/speedometer.png"), 125, 125);
               
               
               
               
               rotationSpeedometer = MathsUtils.Lerp(147,  393, vehicle.getSpeed() / 120f);
               
               
               if(rotationSpeedometer < 147)
               {
            	   rotationSpeedometer = 147;   
               }

               
               
               
               rotationFuelometer = MathsUtils.Lerp(150,  390, (float) vehicle.driveableData.fuelInTank / vehicle.getDriveableType().fuelTankSize);
               
               
               
               
               GuiUtils.StartRotation(width-89, height-75, 45, 10,(int) rotationSpeedometer);
               GuiUtils.drawImageWithTransparency(width-72, height-75, new ResourceLocation("craftyourliferp","gui/vehiclecontroller/needle.png"), 50, 10);
               GuiUtils.StopRotation();
               
               GuiUtils.drawImageWithTransparency(width-93, height-55, new ResourceLocation("craftyourliferp","gui/vehiclecontroller/fuelometer.png"), 50, 50);

               GuiUtils.StartRotation(width-78, height-32, 23, 5, (int) rotationFuelometer);
               GuiUtils.drawImageWithTransparency(width-70, height-32, new ResourceLocation("craftyourliferp","gui/vehiclecontroller/needle.png"), 23, 5);
               GuiUtils.StopRotation();
               
               String displayText = (int) vehicle.speed + " km/h";
               GuiUtils.renderCenteredText((int) vehicle.speed + " km/h", width-72 + (50 - mc.fontRenderer.getStringWidth(displayText)+5) /  2, height-90);
            }

        }
    }
	
}
