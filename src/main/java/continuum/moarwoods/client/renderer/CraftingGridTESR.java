package continuum.moarwoods.client.renderer;

import java.util.List;

import continuum.moarwoods.blocks.BlockCraftingGrid;
import continuum.moarwoods.blocks.BlockCraftingGrid.CraftingGridCuboids;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;

public class CraftingGridTESR extends TileEntitySpecialRenderer<TileEntityCraftingGrid>
{
	private static final Float oneDNine = 1F / 9F;
	private final RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
	
	@Override
	public void renderTileEntityAt(TileEntityCraftingGrid grid, double x, double y, double z, float partialTicks, int destroyStage)
	{
		IBlockState state = grid.getWorld().getBlockState(grid.getPos());
		CraftingGridCuboids cuboids = CraftingGridCuboids.getCuboidsFromState(state);
		Float f;
		List<Float> gridYaws = grid.getGridYaws();
		GlStateManager.enableRescaleNormal();
		GlStateManager.alphaFunc(516, 0.1F);
		GlStateManager.enableBlend();
		RenderHelper.enableStandardItemLighting();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		if(state.getValue(BlockCraftingGrid.walled))
		{
			
		}
		else if(gridYaws != null)
		{
			ItemStack stack;
			GlStateManager.pushMatrix();
			GlStateManager.translate(x, y, z);
			for (Integer i = 0; i < grid.getSizeInventory(); i++)
				if((f = gridYaws.get(i)) != null && (stack = grid.getStackInSlot(i)) != null)
				{
					AxisAlignedBB aabb = cuboids.getGridFromStateAndSlot(state, i).getSelectableCuboid();
					GlStateManager.pushMatrix();
					if(!state.getValue(BlockCraftingGrid.walled))
					{
						GlStateManager.translate(aabb.minX + ((aabb.maxX - aabb.minX) / 2), aabb.maxY, aabb.minZ + ((aabb.maxZ - aabb.minZ) / 2));
						GlStateManager.scale(0.25, 0.25, 0.25);
						GlStateManager.rotate(f, 0F, 1F, 0F);
						this.renderItem.renderItem(grid.getStackInSlot(i), this.renderItem.getItemModelMesher().getItemModel(stack));
					}
					GlStateManager.popMatrix();
				}
			GlStateManager.pushMatrix();
			GlStateManager.translate(.5, .5, .5);
			GlStateManager.scale(.5, .5, .5);
	        GlStateManager.rotate(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
	        GlStateManager.rotate((float)(Minecraft.getMinecraft().getRenderManager().options.thirdPersonView == 2 ? -1 : 1) * Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
	        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
			if(grid.hasResult())
				this.renderItem.renderItem(stack = grid.getResult(), this.renderItem.getItemModelMesher().getItemModel(stack));
			GlStateManager.popMatrix();
			GlStateManager.popMatrix();
		}
		GlStateManager.disableRescaleNormal();
		GlStateManager.disableBlend();
		if(!Minecraft.getMinecraft().isGamePaused())
			grid.updateRenderYaw();
	}
}
