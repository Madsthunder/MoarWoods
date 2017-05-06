package moarwoods.client.renderers.entity;

import moarwoods.client.models.ModelVillagerWrapper;
import moarwoods.client.renderers.entity.layers.LayerVillagerArmorWrapper;
import moarwoods.client.renderers.entity.layers.LayerVillagerHeldItemWrapper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderVillager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;

public class RenderVillagerWrapper extends RenderVillager
{
	public RenderVillagerWrapper(RenderManager manager)
	{
		super(manager);
		this.mainModel = new ModelVillagerWrapper(0F);
		this.layerRenderers.clear();
		this.addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
		this.addLayer(new LayerVillagerArmorWrapper(this));
		this.addLayer(new LayerVillagerHeldItemWrapper(this));
	}
}
