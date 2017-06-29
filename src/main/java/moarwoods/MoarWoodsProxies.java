package moarwoods;

import java.util.Collection;

import moarwoods.blocks.BlockLivingLog;
import moarwoods.blocks.BlockQuarterLog;
import moarwoods.client.renderers.entity.RenderVillagerWrapper;
import moarwoods.packets.PacketUpdateBiomes;
import moarwoods.villagers.VillagerFarmer;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.client.model.ModelLoader;
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
        VillagerFarmer.addItems(VillagerRegistry.FARMER, VillagerRegistry.FARMER.getCareer(0), Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.WHEAT, Items.BEETROOT, Items.POTATO, Items.POISONOUS_POTATO, Items.CARROT);
        VillagerFarmer.addItems(VillagerRegistry.FARMER, VillagerRegistry.FARMER.getCareer(1), Items.FISH);
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