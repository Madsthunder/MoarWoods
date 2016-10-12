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
			ClientHooks.assignAllBlocksToStateMapper(new StateMapperCraftingGrid(), MoarWoods_OH.OAK_CRAFTING_GRID.getLeft(), MoarWoods_OH.SPRUCE_CRAFTING_GRID.getLeft(), MoarWoods_OH.BIRCH_CRAFTING_GRID.getLeft(), MoarWoods_OH.JUNGLE_CRAFTING_GRID.getLeft(), MoarWoods_OH.ACACIA_CRAFTING_GRID.getLeft(), MoarWoods_OH.DARKOAK_CRAFTING_GRID.getLeft());
			ClientHooks.assignAllItemsToVariant("walled_south", MoarWoods_OH.OAK_CRAFTING_GRID.getLeft(), MoarWoods_OH.SPRUCE_CRAFTING_GRID.getLeft(), MoarWoods_OH.BIRCH_CRAFTING_GRID.getLeft(), MoarWoods_OH.JUNGLE_CRAFTING_GRID.getLeft(), MoarWoods_OH.ACACIA_CRAFTING_GRID.getLeft(), MoarWoods_OH.DARKOAK_CRAFTING_GRID.getLeft());
			ModelLoader.setCustomStateMapper(MoarWoods_OH.APPLE_LEAVES.getLeft(), new StateMapperFruitLeaves());
			ModelLoader.registerItemVariants(MoarWoods_OH.APPLE_LEAVES.getRight(), new ModelResourceLocation(new ResourceLocation("oak_leaves"), "inventory"), new ModelResourceLocation(MoarWoods_OH.APPLE_LEAVES.getLeft().getRegistryName(), "inventory_green"), new ModelResourceLocation(MoarWoods_OH.APPLE_LEAVES.getLeft().getRegistryName(), "inventory_yellow"), new ModelResourceLocation(MoarWoods_OH.APPLE_LEAVES.getLeft().getRegistryName(), "inventory_red"));
		}
		
		@Override
		public void init(MoarWoods_Mod mod)
		{
			super.init(mod);
			ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCraftingGrid.class, new CraftingGridTESR());
			ClientHooks.registerColorMultiplier(LeavesColorMultiplier.I, MoarWoods_OH.APPLE_LEAVES.getLeft());
			ClientHooks.registerColorMultiplier(LeavesColorMultiplier.I, MoarWoods_OH.APPLE_LEAVES.getRight());
		}
	}
}
