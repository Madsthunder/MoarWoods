package moarwoods.client.renderers.entity.layers;

import moarwoods.client.models.ModelVillagerWrapper;
import moarwoods.client.renderers.entity.RenderVillagerWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;

public class LayerVillagerHeldItemWrapper implements LayerRenderer<EntityVillager>
{
	private final ModelVillagerWrapper model;
	
	public LayerVillagerHeldItemWrapper(RenderVillagerWrapper renderer)
	{
		this.model = ((ModelVillagerWrapper)renderer.getMainModel());
	}
	
	@Override
	public void doRenderLayer(EntityVillager entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		ItemStack left_stack = entity.isLeftHanded() ? entity.getHeldItemMainhand() : entity.getHeldItemOffhand();
		ItemStack right_stack = entity.isLeftHanded() ? entity.getHeldItemOffhand() : entity.getHeldItemMainhand();
		this.renderHeldItem(entity, left_stack, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, right_stack.isEmpty() && entity.isLeftHanded() ? null : EnumHandSide.LEFT, true);
		this.renderHeldItem(entity, right_stack, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, left_stack.isEmpty() && !entity.isLeftHanded() ? null : EnumHandSide.RIGHT, false);
	}
	
	private void renderHeldItem(EntityVillager entity, ItemStack stack, ItemCameraTransforms.TransformType transformtype, EnumHandSide side, boolean lefthanded)
	{
		if(!stack.isEmpty())
		{
			GlStateManager.pushMatrix();
			if(entity.isSneaking())
				GlStateManager.translate(0.0F, 0.2F, 0.0F);
			(side == EnumHandSide.LEFT ? this.model.leftVillagerArm : side == EnumHandSide.RIGHT ? this.model.rightVillagerArm : this.model.villagerArms).postRender(0.0625F);
			GlStateManager.translate(0.0F, -0.3F, 0.0F);
			boolean activestack = (lefthanded ? entity.isLeftHanded() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND : entity.isLeftHanded() ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND) == entity.getActiveHand();
			switch(stack.getItemUseAction())
			{
				case BOW :
				{
					if(activestack)
					{
						GlStateManager.rotate(-135.0F + MathHelper.wrapDegrees(entity.rotationPitch), 1.0F, 0.0F, 0.0F);
						break;
					}
				}
				default:
					GlStateManager.rotate(-90.0F, 1.0F, 0.0F, 0.0F);
			}
			GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			GlStateManager.translate((side == EnumHandSide.LEFT ? -1 : side == EnumHandSide.RIGHT ? 1 : 0) / 16.0F, 0.125F, -0.625F);
			Minecraft.getMinecraft().getItemRenderer().renderItemSide(entity, stack, transformtype, lefthanded);
			GlStateManager.popMatrix();
		}
	}
	
	@Override
	public boolean shouldCombineTextures()
	{
		return true;
	}
}
