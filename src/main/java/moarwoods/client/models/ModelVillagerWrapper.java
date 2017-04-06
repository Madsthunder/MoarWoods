package moarwoods.client.models;

import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelVillagerWrapper extends ModelVillager
{
	
	public ModelVillagerWrapper(float scale)
	{
		super(scale);
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		this.villagerHead.rotateAngleY = netHeadYaw * 0.017453292F;
		this.villagerHead.rotateAngleX = headPitch * 0.017453292F;
		this.villagerArms.rotationPointY = 3.0F;
		this.villagerArms.rotationPointZ = -1.0F;
		this.villagerArms.rotateAngleX = -0.75F;
		if(this.isRiding)
		{
			this.rightVillagerLeg.rotateAngleX = -1.4137167F;
			this.rightVillagerLeg.rotateAngleY = ((float)Math.PI / 10F);
			this.rightVillagerLeg.rotateAngleZ = 0.07853982F;
			this.leftVillagerLeg.rotateAngleX = -1.4137167F;
			this.leftVillagerLeg.rotateAngleY = -((float)Math.PI / 10F);
			this.leftVillagerLeg.rotateAngleZ = -0.07853982F;
		}
		else
		{
			this.rightVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
			this.leftVillagerLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
			this.rightVillagerLeg.rotateAngleY = 0.0F;
			this.leftVillagerLeg.rotateAngleY = 0.0F;
		}
	}
}
