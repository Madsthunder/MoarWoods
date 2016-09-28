package continuum.moarwoods.client.state;

import continuum.essentials.hooks.ClientHooks;
import continuum.moarwoods.blocks.BlockAppleLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;

public class StateMapperFruitLeaves extends StateMapperBase implements ItemMeshDefinition
{
	@Override
	public ModelResourceLocation getModelResourceLocation(IBlockState state)
	{
		return new ModelResourceLocation(state.getBlock().getRegistryName(), "fancy=" + state.getValue(BlockAppleLeaves.FANCY) + ",growth=" + state.getValue(BlockAppleLeaves.GROWTH));
	}

	@Override
	public ModelResourceLocation getModelLocation(ItemStack stack)
	{
		return new ModelResourceLocation(stack.getItem().getRegistryName(), stack.getMetadata() == 0 ? "inventory" : "inventory_fruit");
	}
}
