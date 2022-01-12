package fr.craftyourliferp.entities;

import fr.craftyourliferp.utils.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityLootableBody extends EntityLiving 
{


	static final int WATCHER_ID_OWNER = 28;
	
	private static final DamageSource selfDestruct = new DamageSource(EntityLootableBody.class.getSimpleName());
	
	private static final float updateFindOwnerInSeconds = 10f;
	private static final float entitiesDeathTimeInSeconds = 600f;

	
	private long lastFindOwnerUpdateTime;
	
	private long deathTime;
	
	private EntityPlayer owner = null;
	
	private String ownerUsername;

	
	public EntityLootableBody(World w) 
	{
		super(w);
		
		this.setSize(0.85f, 0.75f);
		
		
		this.getDataWatcher().addObject(WATCHER_ID_OWNER, "");
		deathTime = System.currentTimeMillis();
	}
	
	public float getRotation() 
	{
		return this.rotationYaw;
	}
		
	public void setRotation(float newRot) 
	{
		this.rotationYaw = newRot;
		
		this.renderYawOffset = this.rotationYaw;
		
		this.prevRenderYawOffset = this.rotationYaw;
		
		this.prevRotationYaw = this.rotationYaw;
		
		this.newRotationYaw = this.rotationYaw;
		
		if (worldObj.isRemote) 
		{ 
			this.setRotationYawHead(this.rotationYaw);
		}
	}


	@Override
	protected boolean canDespawn() 
	{
		return true;
	}
	
	@Override
	protected void updateEntityActionState() 
	{
		if((System.currentTimeMillis() - deathTime) / 1000 >= EntityLootableBody.entitiesDeathTimeInSeconds)
		{
			setDead();
		}
	}
	
	@Override
    public boolean handleWaterMovement()
    {
		if(isInWater())
		{
			this.motionY +=0.05f;
		}
		return super.handleWaterMovement();
    }
	

	
	public void setOwner(EntityPlayer player) 
	{
		try {
			owner = player;
			if (owner.getCommandSenderName() != null)
			{
				this.getDataWatcher().updateObject(WATCHER_ID_OWNER, owner.getCommandSenderName());
			}
			else
			{
				this.getDataWatcher().updateObject(WATCHER_ID_OWNER, "");
			}
		} catch (Exception e) {
			this.getDataWatcher().updateObject(WATCHER_ID_OWNER, "");
		}
	}

	public EntityPlayer getOwner() 
	{
		if (owner == null)
		{
			return this.worldObj.getPlayerEntityByName(this.getDataWatcher().getWatchableObjectString(WATCHER_ID_OWNER));
		}
		return owner;
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		
				
		if(owner == null && ownerUsername != null)
		{
			if((System.currentTimeMillis() - lastFindOwnerUpdateTime) / 1000 >= EntityLootableBody.updateFindOwnerInSeconds)
			{
				setOwner(worldObj.getPlayerEntityByName(ownerUsername));
				lastFindOwnerUpdateTime = System.currentTimeMillis();
			}
		}
	}
	

	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public boolean allowLeashing() {
		return false; // leashing not allowed
	}
	
	
	@Override
	public boolean isEntityInvulnerable()
	{
		return true;
	}
	
	@Override
	public void writeEntityToNBT(final NBTTagCompound root) {
		super.writeEntityToNBT(root);

		if (owner != null) {
			root.setString("Owner", owner.getCommandSenderName());
		}
		
		root.setLong("DeathTime", deathTime);
	}

	@Override
	public void readEntityFromNBT(final NBTTagCompound root) {
		super.readEntityFromNBT(root);

		
		if (root.hasKey("Owner"))
		{
			ownerUsername = root.getString("Owner");
			setOwner(worldObj.getPlayerEntityByName(ownerUsername));
		}
		
		deathTime = root.getLong("DeathTime");
		
		this.setRotation(this.rotationYaw);
	}
	
    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }

	
	

}
