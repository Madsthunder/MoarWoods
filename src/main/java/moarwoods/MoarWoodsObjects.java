package moarwoods;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import moarwoods.blocks.BlockLivingBranch;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import moarwoods.blocks.BlockLivingQuarterLog;
import moarwoods.blocks.BlockQuarterLog;
import moarwoods.blocks.BlockReedBlock;
import moarwoods.blocks.living.tree.SmallBirchTree;
import moarwoods.blocks.living.tree.SmallJungleTree;
import moarwoods.blocks.living.tree.SmallOakTree;
import moarwoods.blocks.living.tree.SmallSpruceTree;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = MoarWoods.MODID)
@ObjectHolder(MoarWoods.MODID)
public class MoarWoodsObjects
{
	@GameRegistry.ObjectHolder("minecraft:sapling")
	public static final BlockSapling SAPLING = null;
	public static final BlockLivingLog OAK_TREE_SMALL_TRUNK = null;
	public static final BlockLivingLog OAK_TREE_MEDIUM_TRUNK = null;
	public static final BlockLivingLog SPRUCE_TREE_SMALL_TRUNK = null;
	public static final BlockLivingQuarterLog SPRUCE_TREE_LARGE_TRUNK = null;
	public static final BlockLivingLog BIRCH_TREE_SMALL_TRUNK = null;
	public static final BlockLivingLog JUNGLE_TREE_SMALL_TRUNK = null;
	public static final BlockLivingLog JUNGLE_TREE_MEDIUM_TRUNK = null;
	public static final BlockLivingLog JUNGLE_TREE_LARGE_TRUNK = null;
	public static final BlockLivingLog ACACIA_TREE_SMALL_TRUNK = null;
	public static final BlockLivingLog DARKOAK_TREE_SMALL_TRUNK = null;
	public static final BlockLivingQuarterLog DARKOAK_TREE_LARGE_TRUNK = null;
	public static final BlockLivingBranch OAK_TREE_MEDIUM_BRANCH = null;
	public static final BlockLivingBranch DARKOAK_TREE_LARGE_BRANCH = null;
	public static final BlockLivingLeaf OAK_TREE_SMALL_LEAVES = null;
	public static final BlockLivingLeaf SPRUCE_TREE_SMALL_LEAVES = null;
	public static final BlockLivingLeaf BIRCH_TREE_SMALL_LEAVES = null;
	public static final BlockLivingLeaf JUNGLE_TREE_SMALL_LEAVES = null;
	public static final BlockLivingLeaf ACACIA_TREE_SMALL_LEAVES = null;
	public static final BlockLivingLeaf DARKOAK_TREE_LARGE_LEAVES = null;
	public static final BlockQuarterLog OAK_QUARTER_LOG = null;
	public static final BlockQuarterLog SPRUCE_QUARTER_LOG = null;
	public static final BlockQuarterLog BIRCH_QUARTER_LOG = null;
	public static final BlockQuarterLog JUNGLE_QUARTER_LOG = null;
	public static final BlockQuarterLog ACACIA_QUARTER_LOG = null;
	public static final BlockQuarterLog DARKOAK_QUARTER_LOG = null;	
	public static final BlockRotatedPillar REED_BLOCK = null;
	
	@SubscribeEvent
	public static void onBlocksRegister(RegistryEvent.Register<Block> event)
	{
		List<Block> blocks = Lists.newArrayList();
		{
			blocks.add(new BlockLivingLog(new SmallOakTree()).setUnlocalizedName("moarwoods:oak_tree_small_trunk").setRegistryName("moarwoods:oak_tree_small_trunk"));
			blocks.add(new BlockLivingLog(null).setUnlocalizedName("moarwoods:oak_tree_medium_trunk").setRegistryName("moarwoods:oak_tree_medium_trunk"));
			blocks.add(new BlockLivingLog(new SmallSpruceTree()).setUnlocalizedName("moarwoods:spruce_tree_small_trunk").setRegistryName("moarwoods:spruce_tree_small_trunk"));
			blocks.add(new BlockLivingQuarterLog(null).setUnlocalizedName("moarwoods:spruce_tree_large_trunk").setRegistryName("moarwoods:spruce_tree_large_trunk"));
			blocks.add(new BlockLivingLog(new SmallBirchTree()).setUnlocalizedName("moarwoods:birch_tree_small_trunk").setRegistryName("moarwoods:birch_tree_small_trunk"));
			blocks.add(new BlockLivingLog(new SmallJungleTree()).setUnlocalizedName("moarwoods:jungle_tree_small_trunk").setRegistryName("moarwoods:jungle_tree_small_trunk"));
			blocks.add(new BlockLivingLog(null).setUnlocalizedName("moarwoods:jungle_tree_medium_trunk").setRegistryName("moarwoods:jungle_tree_medium_trunk"));
			blocks.add(new BlockLivingQuarterLog(null).setUnlocalizedName("moarwoods:jungle_tree_large_trunk").setRegistryName("moarwoods:jungle_tree_large_trunk"));
			blocks.add(new BlockLivingLog(null).setUnlocalizedName("moarwoods:acacia_tree_small_trunk").setRegistryName("moarwoods:acacia_tree_small_trunk"));
			blocks.add(new BlockLivingLog(null).setUnlocalizedName("moarwoods:darkoak_tree_small_trunk").setRegistryName("moarwoods:darkoak_tree_small_trunk"));
			blocks.add(new BlockLivingQuarterLog(null, true).setUnlocalizedName("moarwoods:darkoak_tree_large_trunk").setRegistryName("moarwoods:darkoak_tree_large_trunk"));
		}
		{
			blocks.add(new BlockLivingBranch(null).setUnlocalizedName("moarwoods:oak_tree_medium_branch").setRegistryName("moarwoods:oak_tree_medium_branch"));
			blocks.add(new BlockLivingBranch(null).setUnlocalizedName("moarwoods:darkoak_tree_large_branch").setRegistryName("moarwoods:darkoak_tree_large_branch"));
		}
		{
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)).setUnlocalizedName("moarwoods:oak_tree_small_leaves").setRegistryName("moarwoods:oak_tree_small_leaves"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE), 5).setUnlocalizedName("moarwoods:spruce_tree_small_leaves").setRegistryName("moarwoods:spruce_tree_small_leaves"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH)).setUnlocalizedName("moarwoods:birch_tree_small_leaves").setRegistryName("moarwoods:birch_tree_small_leaves"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)).setUnlocalizedName("moarwoods:jungle_tree_small_leaves").setRegistryName("moarwoods:jungle_tree_small_leaves"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA)).setUnlocalizedName("moarwoods:acacia_tree_small_leaves").setRegistryName("moarwoods:acacia_tree_small_leaves"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK)).setUnlocalizedName("moarwoods:darkoak_tree_large_leaves").setRegistryName("moarwoods:darkoak_tree_large_leaves"));
		}
		{
			blocks.add(new BlockQuarterLog().setUnlocalizedName("moarwoods:oak_quarter_log").setRegistryName("moarwoods:oak_quarter_log"));
			blocks.add(new BlockQuarterLog().setUnlocalizedName("moarwoods:spruce_quarter_log").setRegistryName("moarwoods:spruce_quarter_log"));
			blocks.add(new BlockQuarterLog().setUnlocalizedName("moarwoods:birch_quarter_log").setRegistryName("moarwoods:birch_quarter_log"));
			blocks.add(new BlockQuarterLog().setUnlocalizedName("moarwoods:jungle_quarter_log").setRegistryName("moarwoods:jungle_quarter_log"));
			blocks.add(new BlockQuarterLog().setUnlocalizedName("moarwoods:acacia_quarter_log").setRegistryName("moarwoods:acacia_quarter_log"));
			blocks.add(new BlockQuarterLog().setUnlocalizedName("moarwoods:darkoak_quarter_log").setRegistryName("moarwoods:darkoak_quarter_log"));
		}
		blocks.add(new BlockReedBlock().setUnlocalizedName("moarwoods:reed_block").setRegistryName("moarwoods:reed_block"));
		event.getRegistry().registerAll(Iterables.toArray(blocks, Block.class));
	}
	
	@SubscribeEvent
	public static void onItemsRegister(RegistryEvent.Register<Item> event)
	{
		List<Item> items = Lists.newArrayList();
		{
			items.add(new ItemBlock(OAK_QUARTER_LOG).setRegistryName("moarwoods:oak_quarter_log"));
			items.add(new ItemBlock(SPRUCE_QUARTER_LOG).setRegistryName("moarwoods:spruce_quarter_log"));
			items.add(new ItemBlock(BIRCH_QUARTER_LOG).setRegistryName("moarwoods:birch_quarter_log"));
			items.add(new ItemBlock(JUNGLE_QUARTER_LOG).setRegistryName("moarwoods:jungle_quarter_log"));
			items.add(new ItemBlock(ACACIA_QUARTER_LOG).setRegistryName("moarwoods:acacia_quarter_log"));
			items.add(new ItemBlock(DARKOAK_QUARTER_LOG).setRegistryName("moarwoods:darkoak_quarter_log"));
		}
		items.add(new ItemBlock(REED_BLOCK).setRegistryName("moarwoods:reed_block"));
		event.getRegistry().registerAll(Iterables.toArray(items, Item.class));
	}
	
	@SubscribeEvent
	public static void onVillagersRegisterRegister(RegistryEvent.Register<VillagerProfession> event)
	{
		List<VillagerProfession> professions = Lists.newArrayList();
		{
			VillagerProfession profession = new VillagerProfession("moarwoods:soldier", "moarwoods:textures/entity/villager/soldier.png", "moarwoods:textures/entity/zombie_villager/zombie_soldier.png");
			new VillagerCareer(profession, "moarwoods:guard");
			new VillagerCareer(profession, "moarwoods:soldier");
			professions.add(profession);
		}
		{
			VillagerProfession profession = new VillagerProfession("moarwoods:miner", "moarwoods:textrues/entity/villager/miner.png", "moarwoods:textures/entity/zombie_miner.png");
			new VillagerCareer(profession, "moarwoods:miner");
			professions.add(profession);
		}
		event.getRegistry().registerAll(Iterables.toArray(professions, VillagerProfession.class));
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onModelsRegister(ModelRegistryEvent event)
	{
        {
            IStateMapper mapper;
            {
                mapper = createLogStateMapper(new ResourceLocation("moarwoods:oak_log"));
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.OAK_QUARTER_LOG, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.OAK_TREE_SMALL_TRUNK, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.OAK_TREE_MEDIUM_TRUNK, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.OAK_TREE_MEDIUM_BRANCH, mapper);
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MoarWoodsObjects.OAK_QUARTER_LOG), 0, new ModelResourceLocation("moarwoods:oak_log", "quarter=south_east,axis=y"));
            }
            {
                mapper = createLogStateMapper(new ResourceLocation("moarwoods:spruce_log"));
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.SPRUCE_QUARTER_LOG, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.SPRUCE_TREE_SMALL_TRUNK, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.SPRUCE_TREE_LARGE_TRUNK, mapper);
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MoarWoodsObjects.SPRUCE_QUARTER_LOG), 0, new ModelResourceLocation("moarwoods:spruce_log", "quarter=south_east,axis=y"));
            }
            {
                mapper = createLogStateMapper(new ResourceLocation("moarwoods:birch_log"));
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.BIRCH_QUARTER_LOG, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.BIRCH_TREE_SMALL_TRUNK, mapper);
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MoarWoodsObjects.BIRCH_QUARTER_LOG), 0, new ModelResourceLocation("moarwoods:birch_log", "quarter=south_east,axis=y"));
            }
            {
                mapper = createLogStateMapper(new ResourceLocation("moarwoods:jungle_log"));
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.JUNGLE_QUARTER_LOG, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.JUNGLE_TREE_SMALL_TRUNK, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.JUNGLE_TREE_MEDIUM_TRUNK, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.JUNGLE_TREE_LARGE_TRUNK, mapper);
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MoarWoodsObjects.JUNGLE_QUARTER_LOG), 0, new ModelResourceLocation("moarwoods:jungle_log", "quarter=south_east,axis=y"));
            }
            {
                mapper = createLogStateMapper(new ResourceLocation("moarwoods:acacia_log"));
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.ACACIA_QUARTER_LOG, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.ACACIA_TREE_SMALL_TRUNK, mapper);
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MoarWoodsObjects.ACACIA_QUARTER_LOG), 0, new ModelResourceLocation("moarwoods:acacia_log", "quarter=south_east,axis=y"));
            }
            {
                mapper = createLogStateMapper(new ResourceLocation("moarwoods:darkoak_log"));
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.DARKOAK_QUARTER_LOG, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.DARKOAK_TREE_SMALL_TRUNK, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.DARKOAK_TREE_LARGE_TRUNK, mapper);
                ModelLoader.setCustomStateMapper(MoarWoodsObjects.DARKOAK_TREE_LARGE_BRANCH, mapper);
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MoarWoodsObjects.DARKOAK_QUARTER_LOG), 0, new ModelResourceLocation("moarwoods:darkoak_log", "quarter=south_east,axis=y"));
            }
        }
        ModelLoader.setCustomStateMapper(MoarWoodsObjects.OAK_TREE_SMALL_LEAVES, createLeafStateMapper());
        ModelLoader.setCustomStateMapper(MoarWoodsObjects.SPRUCE_TREE_SMALL_LEAVES, createLeafStateMapper());
        ModelLoader.setCustomStateMapper(MoarWoodsObjects.BIRCH_TREE_SMALL_LEAVES, createLeafStateMapper());
        ModelLoader.setCustomStateMapper(MoarWoodsObjects.JUNGLE_TREE_SMALL_LEAVES, createLeafStateMapper());
        ModelLoader.setCustomStateMapper(MoarWoodsObjects.ACACIA_TREE_SMALL_LEAVES, createLeafStateMapper());
        ModelLoader.setCustomStateMapper(MoarWoodsObjects.DARKOAK_TREE_LARGE_LEAVES, createLeafStateMapper());
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(MoarWoodsObjects.REED_BLOCK), 0, new ModelResourceLocation("moarwoods:reed_block", "inventory"));
	}
    
	@SideOnly(Side.CLIENT)
    private static IStateMapper createLogStateMapper(ResourceLocation name)
    {
        return new StateMapperBase()
        {
            
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                Collection<IProperty<?>> property = state.getPropertyKeys();
                BlockQuarterLog.EnumLogQuarter quarter = property.contains(BlockQuarterLog.QUARTER) ? state.getValue(BlockQuarterLog.QUARTER) : null;
                BlockLog.EnumAxis axis = state.getPropertyKeys().contains(BlockLog.LOG_AXIS) ? state.getValue(BlockLog.LOG_AXIS) : BlockLog.EnumAxis.NONE;
                return new ModelResourceLocation(name, axis == BlockLog.EnumAxis.NONE ? "branch" : "quarter=" + (quarter == null ? "none" : quarter) + ",axis=" + axis);
            }
            
        };
    }

    @SideOnly(Side.CLIENT)
    private static IStateMapper createLeafStateMapper()
    {
        return new StateMapperBase()
        {
            
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return new ModelResourceLocation(state.getBlock().getRegistryName(), "normal");
            }
            
        };
    }
}