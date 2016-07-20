package continuum.moarwoods.client.state;

import continuum.moarwoods.blocks.BlockCraftingGrid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;

public class StateMapperCraftingGrid extends StateMapperBase
{
	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		String unlocalizedName = state.getBlock().getUnlocalizedName();
		return new ModelResourceLocation("moarwoods:" + unlocalizedName.substring(5, unlocalizedName.length() - 5) + "_crafting_grid", (state.getValue(BlockCraftingGrid.walled) ? "walled_" : "grounded_") + state.getValue(BlockCraftingGrid.direction));
	}
}
