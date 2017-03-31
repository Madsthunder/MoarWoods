package moarwoods.client.models;

import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ModelZombieVillagerArmorWrapper extends ModelZombieVillager
{
	public ModelZombieVillagerArmorWrapper(float scale, float rotation_point)
	{
		super(scale, rotation_point, true);
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;
		this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount * 0.5F;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;
		this.bipedRightArm.rotationPointY = 3.0F;
		this.bipedLeftArm.rotationPointY = 3.0F;
		this.bipedRightArm.rotationPointZ = -1.0F;
		this.bipedLeftArm.rotationPointZ = -1.0F;
		this.bipedRightArm.rotateAngleX = -0.75F;
		this.bipedLeftArm.rotateAngleX = -0.75F;
		copyModelAngles(this.bipedHead, this.bipedHeadwear);
	}
}
