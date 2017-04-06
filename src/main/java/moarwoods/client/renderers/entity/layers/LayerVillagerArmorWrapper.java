package moarwoods.client.renderers.entity.layers;

import moarwoods.client.models.ModelZombieVillagerArmorWrapper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

public class LayerVillagerArmorWrapper extends LayerBipedArmor
{
	public LayerVillagerArmorWrapper(RenderLivingBase<?> renderer)
	{
		super(renderer);
	}
	
	@Override
	public void initArmor()
	{
		this.modelArmor = new ModelZombieVillagerArmorWrapper(1F, 0);
		this.modelLeggings = new ModelZombieVillagerArmorWrapper(.5F, 0);
	}
}
