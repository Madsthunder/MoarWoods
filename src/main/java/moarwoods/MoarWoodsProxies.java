package moarwoods;

import moarwoods.blocks.BlockLivingLog;
import moarwoods.client.renderers.entity.RenderVillagerWrapper;
import moarwoods.packets.PacketUpdateBiomes;
import moarwoods.villagers.VillagerFarmer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MoarWoodsProxies
{
	@SideOnly(Side.CLIENT)
	private static LayerVillagerArmor VILLAGER_ARMOR_LAYER;
	@SidedProxy(modId = MoarWoods.MODID)
	public static MoarWoodsProxies I = null;
	
	public void constr()
	{
		MinecraftForge.TERRAIN_GEN_BUS.register(MoarWoodsEventHandler.TerrainGen.class);
	}
	
	public void pre()
	{
		MoarWoods.NETWORK_WRAPPER.registerMessage(PacketUpdateBiomes.Handler.class, PacketUpdateBiomes.class, 0, Side.CLIENT);
	}
	
	public void init()
	{
        VillagerFarmer.addItems(VillagerRegistry.FARMER.getCareer(0), Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.WHEAT, Items.BEETROOT, Items.POTATO, Items.POISONOUS_POTATO, Items.CARROT);
        VillagerFarmer.addItems(VillagerRegistry.FARMER.getCareer(1), Items.FISH);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MoarWoodsObjects.GRANITE_BRICKS, 1, 0), new ItemStack(MoarWoodsObjects.GRANITE_BRICKS, 1, 1), 0.1F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MoarWoodsObjects.DIORITE_BRICKS, 1, 0), new ItemStack(MoarWoodsObjects.DIORITE_BRICKS, 1, 1), 0.1F);
        FurnaceRecipes.instance().addSmeltingRecipe(new ItemStack(MoarWoodsObjects.ANDESITE_BRICKS, 1, 0), new ItemStack(MoarWoodsObjects.ANDESITE_BRICKS, 1, 1), 0.1F);
	}
	
	public void post()
	{
		
	}
	
	@SideOnly(Side.CLIENT)
	public static class ClientProxy extends MoarWoodsProxies
	{
		@Override
		public void pre()
		{
			super.pre();
		}
		
		@Override
		public void init()
		{
			super.init();
			RenderManager manager = Minecraft.getMinecraft().getRenderManager();
			manager.entityRenderMap.replace(EntityVillager.class, new RenderVillagerWrapper(manager));
			{
				BlockColors colors = Minecraft.getMinecraft().getBlockColors();
				{
					IBlockColor default_color = (state, access, pos, tintIndex) ->
					{
						int c = 195 + (20 * state.getValue(BlockLivingLog.DEATH_STAGE));
						return (c * 65536) + 65280 + c;
					};
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.OAK_TREE_SMALL_TRUNK);
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.SPRUCE_TREE_SMALL_TRUNK);
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.BIRCH_TREE_SMALL_TRUNK);
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.JUNGLE_TREE_SMALL_TRUNK);
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.ACACIA_TREE_SMALL_TRUNK);
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.DARKOAK_TREE_LARGE_TRUNK);
					
				}
				{
					IBlockColor default_color = (state, access, pos, tintIndex) -> access != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(access, pos) : ColorizerFoliage.getFoliageColorBasic();
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.OAK_TREE_SMALL_LEAVES);
					colors.registerBlockColorHandler((state, access, pos, tintIndex) -> ColorizerFoliage.getFoliageColorPine(), MoarWoodsObjects.SPRUCE_TREE_SMALL_LEAVES);
					colors.registerBlockColorHandler((state, access, pos, tintIndex) -> ColorizerFoliage.getFoliageColorBirch(), MoarWoodsObjects.BIRCH_TREE_SMALL_LEAVES);
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.JUNGLE_TREE_SMALL_LEAVES);
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.ACACIA_TREE_SMALL_LEAVES);
					colors.registerBlockColorHandler(default_color, MoarWoodsObjects.DARKOAK_TREE_LARGE_LEAVES);
				}
			}
		}
	}
	
	@SideOnly(Side.SERVER)
	public static class ServerProxy extends MoarWoodsProxies
	{
		
	}
}