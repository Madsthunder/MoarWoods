package continuum.moarwoods.mod;

import continuum.essentials.client.colors.LeavesColorMultiplier;
import continuum.essentials.hooks.ClientHooks;
import continuum.essentials.hooks.ItemHooks;
import continuum.essentials.mod.Proxy;
import continuum.moarwoods.client.renderer.CraftingGridTESR;
import continuum.moarwoods.client.state.StateMapperCraftingGrid;
import continuum.moarwoods.client.state.StateMapperFruitLeaves;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.Side;

public class MoarWoods_Proxies
{
	public static final Proxy I = FMLLaunchHandler.side() == Side.CLIENT ? new Client() : new Common();
	
	private static class Common extends Proxy<MoarWoods_Mod>
	{
		@Override
		public void init(MoarWoods_Mod mod)
		{
			NetworkRegistry.INSTANCE.registerGuiHandler(mod.getModid(), mod.getEventHandler());
		}
	}
	
	private static class Client extends Common
	{
		@Override
		public void pre(MoarWoods_Mod mod)
		{
			super.pre(mod);
			ClientHooks.assignAllBlocksToStateMapper(new StateMapperCraftingGrid(), MoarWoods_OH.oak_crafting_grid, MoarWoods_OH.spruce_crafting_grid, MoarWoods_OH.birch_crafting_grid, MoarWoods_OH.jungle_crafting_grid, MoarWoods_OH.acacia_crafting_grid, MoarWoods_OH.darkoak_crafting_grid);
			ClientHooks.assignAllItemsToVariant("walled_south", MoarWoods_OH.oak_crafting_grid, MoarWoods_OH.spruce_crafting_grid, MoarWoods_OH.birch_crafting_grid, MoarWoods_OH.jungle_crafting_grid, MoarWoods_OH.acacia_crafting_grid, MoarWoods_OH.darkoak_crafting_grid);
			ModelLoader.setCustomStateMapper(MoarWoods_OH.apple_leaves, new StateMapperFruitLeaves());
			ModelLoader.registerItemVariants(Item.getItemFromBlock(MoarWoods_OH.apple_leaves), new ModelResourceLocation(new ResourceLocation("oak_leaves"), "inventory"), new ModelResourceLocation(MoarWoods_OH.apple_leaves.getRegistryName(), "inventory_green"), new ModelResourceLocation(MoarWoods_OH.apple_leaves.getRegistryName(), "inventory_yellow"), new ModelResourceLocation(MoarWoods_OH.apple_leaves.getRegistryName(), "inventory_red"));
		}
		
		@Override
		public void init(MoarWoods_Mod mod)
		{
			super.init(mod);
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCraftingGrid.class, new CraftingGridTESR());
			ClientHooks.registerColorMultiplier(LeavesColorMultiplier.I, MoarWoods_OH.apple_leaves);
			ClientHooks.registerColorMultiplier(LeavesColorMultiplier.I, ItemHooks.allItemsToBlocks(MoarWoods_OH.apple_leaves));
		}
	}
}
