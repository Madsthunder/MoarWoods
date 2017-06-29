package moarwoods.entity.ai;

import java.util.List;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.math.Vec3d;

public class EntityAITameHorse extends EntityAIBase
{
	private final EntityVillager villager;
	private AbstractHorse target;
	
	public EntityAITameHorse(EntityVillager villager)
	{
		this.villager = villager;
	}
	
	@Override
	public boolean shouldExecute()
	{
		List<AbstractHorse> entities = this.villager.getEntityWorld().getEntities(AbstractHorse.class, horse -> new Vec3d(horse.posX, horse.posY, horse.posZ).squareDistanceTo(new Vec3d(this.villager.posX, this.villager.posY, this.villager.posZ)) <= 256);
		if(!entities.isEmpty())
		{
			this.target = entities.get(this.villager.getRNG().nextInt(entities.size()));
			if(!this.target.isBeingRidden() && this.target.isChild() == this.villager.isChild())
				this.villager.setAttackTarget(this.target);
			else
			{
				this.target = null;
				return false;
			}
			return true;
		}
		return false;
	}
	
	@Override
	public boolean shouldContinueExecuting()
	{
		return this.target != null && ((this.villager.isRidingSameEntity(this.target) && !this.target.isTame()) || this.villager.getAttackTarget() == this.target);
	}
	
	@Override
	public void updateTask()
	{
		if(this.target != null)
		{
			if(this.villager.isRiding())
			{
				if(this.villager.isRidingSameEntity(this.target))
				{
					if(this.target.isTame())
						this.villager.setAttackTarget(this.target = null);
				}
				else
					this.villager.dismountRidingEntity();
			}
			else if(this.target.getEntityBoundingBox().intersects(this.villager.getEntityBoundingBox().grow(.5)))
			{
				this.villager.startRiding(this.target);
			}
		}
	}
}
