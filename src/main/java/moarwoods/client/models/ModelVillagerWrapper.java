package moarwoods.client.models;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.math.MathHelper;

public class ModelVillagerWrapper extends ModelVillager
{
	public ModelRenderer villagerRobe;
	public ModelRenderer leftVillagerArm;
	public ModelRenderer rightVillagerArm;
	
	public ModelVillagerWrapper(float scale)
	{
		this(scale, 0.0F, 64, 64);
	}
	
	public ModelVillagerWrapper(float scale, float yoffset, int width, int height)
	{
		super(scale, yoffset, width, height);
		this.villagerArms.cubeList.remove(0);
		this.villagerArms.cubeList.remove(0);
		this.villagerBody.cubeList.remove(1);
		this.villagerRobe = new ModelRenderer(this).setTextureSize(width, height).setTextureOffset(0, 38);
		this.villagerRobe.addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, scale + 0.5F);
		this.leftVillagerArm = new ModelRenderer(this).setTextureSize(width, height).setTextureOffset(44, 22);
		this.leftVillagerArm.setRotationPoint(5.0F, 3.0F, -1.0F);
		this.leftVillagerArm.rotateAngleX = -0.75F;
		this.leftVillagerArm.addBox(-1.0F, -2.0F, -2.0F, 4, 8, 4, scale);
		this.rightVillagerArm = new ModelRenderer(this).setTextureSize(width, height).setTextureOffset(44, 22);
		this.rightVillagerArm.setRotationPoint(-5.0F, 3.0F, -1.0F);
		this.rightVillagerArm.rotateAngleX = -0.75F;
		this.rightVillagerArm.addBox(-3.0F, -2.0F, -2.0F, 4, 8, 4, scale);
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
	
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		this.villagerRobe.isHidden = (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() instanceof ItemArmor);
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		this.villagerRobe.render(scale);
		this.leftVillagerArm.render(scale);
		this.rightVillagerArm.render(scale);
	}
}
