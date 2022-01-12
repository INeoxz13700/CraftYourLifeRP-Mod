package fr.craftyourliferp.entities;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.items.IItemCollider;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityItemCollider extends Entity 
{
	
	public EntityPlayer owner;
	
	private IItemCollider itemCollider;

	public EntityItemCollider(World p_i1582_1_) 
	{
		super(p_i1582_1_);
	}
	
	public EntityItemCollider(World p_i1582_1_, EntityPlayer owner, IItemCollider collider) 
	{
		super(p_i1582_1_);
		this.owner = owner;
		this.itemCollider = collider;
        isImmuneToFire = true;
		setSize(collider.getSize()[0], collider.getSize()[1]);
		setPositionAndRotation(owner.posX, owner.posY, owner.posZ ,owner.rotationYaw,owner.rotationPitch);
	}

	@Override
	protected void entityInit() 
	{
		
	}
	
	
	@Override
	public void onEntityUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		
		prevRotationYaw = rotationYaw;
		prevRotationPitch = rotationPitch;
		
		setInvisible(true);

		if(!worldObj.isRemote)
		{
			if(owner == null)
			{
				setDead();
				return;
			}
			
			//PlayerCachedData cachedData = PlayerCachedData.getData(owner);
			ExtendedPlayer cachedData = ExtendedPlayer.get(owner);

			if(cachedData != null && cachedData.isProning())
			{
				this.setSize(itemCollider.getSize()[0], itemCollider.getSize()[1] / 2F);
				
				Vec3 relativePosFromPlayer = itemCollider.getRelativePositionFromPlayer();
		
				float rotationYaw = owner.rotationYaw + 90;
				
				Vec3 dirVector = Vec3.createVectorHelper(Math.cos(rotationYaw * MathsUtils.Deg2Rad), 0, Math.sin(rotationYaw * MathsUtils.Deg2Rad));
				setPositionAndRotation(owner.posX + dirVector.xCoord, owner.posY + dirVector.yCoord, owner.posZ + dirVector.zCoord,owner.rotationYaw,owner.rotationPitch);
			}
			else
			{
				this.setSize(itemCollider.getSize()[0], itemCollider.getSize()[1]);
				
				Vec3 relativePosFromPlayer = itemCollider.getRelativePositionFromPlayer();
		
				float rotationYaw = owner.rotationYaw + 90;
				
				Vec3 dirVector = Vec3.createVectorHelper(Math.cos(owner.rotationPitch * MathsUtils.Deg2Rad) * Math.cos(rotationYaw * MathsUtils.Deg2Rad), Math.sin(-owner.rotationPitch * MathsUtils.Deg2Rad), Math.cos(owner.rotationPitch* MathsUtils.Deg2Rad) * Math.sin(rotationYaw * MathsUtils.Deg2Rad));
				setPositionAndRotation(owner.posX + dirVector.xCoord, owner.posY + dirVector.yCoord, owner.posZ + dirVector.zCoord,owner.rotationYaw,owner.rotationPitch);
			}
			
		
		}

	}
	
	public void applyEntityCollision(Entity p_70108_1_)
	{
		super.applyEntityCollision(p_70108_1_);
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) 
	{
		
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) 
	{
	
	}
	
    public boolean canBeCollidedWith()
    {
        return true;
    }
    
    public boolean isEntityInvulnerable()
    {
        return true;
    }
    
    public boolean canBePushed()
    {
        return false;
    }
    
    @Override
    public boolean hitByEntity(Entity p_85031_1_)
    {
        return false;
    }
    
    public boolean attackEntityFrom(DamageSource source, float p_70097_2_)
    {
    	if(source == null) return true;
    	
    	if(source.getEntity() != null)
    	{
    		if(source.getEntity() == owner) return true;
    		
	    	if(owner != null && owner.getHeldItem() != null)
	    	{
	    		if(owner.getHeldItem().getItem() instanceof IItemCollider) ((IItemCollider)owner.getHeldItem().getItem()).onItemAttacked(owner.getHeldItem(), owner, source.getEntity(), p_70097_2_);
	    	}
    	}
    	return true;
    }



}
