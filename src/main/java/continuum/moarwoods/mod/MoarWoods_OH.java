package continuum.moarwoods.mod;


import org.apache.commons.lang3.tuple.Pair;

import continuum.essentials.hooks.ItemHooks;
import continuum.essentials.mod.ObjectHolder;
import continuum.essentials.util.CreativeTab;
import continuum.moarwoods.blocks.BlockAppleLeaves;
import continuum.moarwoods.blocks.BlockCraftingGrid;
import continuum.moarwoods.client.state.StateMapperCraftingGrid;
import continuum.moarwoods.client.state.StateMapperFruitLeaves;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MoarWoods_OH implements ObjectHolder
{
	public static final MoarWoods_OH I = new MoarWoods_OH();
	
	private MoarWoods_Mod moarWoods;
	private static final Mod mod = MoarWoods_Mod.class.getAnnotation(Mod.class);
	
	private MoarWoods_OH()
	{
		
	}
	
	void setMoarWoods(MoarWoods_Mod moarWoods)
	{
		this.moarWoods = moarWoods;
	}
	
	public MoarWoods_Mod getMoarWoods()
	{
		return this.moarWoods;
	}
	
	public Mod getMod()
	{
		return this.mod;
	}
	
	@Override
	public String getModid()
	{
		return mod.modid();
	}
	
	@Override
	public String getName()
	{
		return mod.name();
	}
	
	@Override
	public String getVersion()
	{
		return mod.version();
	}
	
	public static final Pair<BlockCraftingGrid, ItemBlock> oak_crafting_grid;
	public static final Pair<BlockCraftingGrid, ItemBlock> spruce_crafting_grid;
	public static final Pair<BlockCraftingGrid, ItemBlock> birch_crafting_grid;
	public static final Pair<BlockCraftingGrid, ItemBlock> jungle_crafting_grid;
	public static final Pair<BlockCraftingGrid, ItemBlock> acacia_crafting_grid;
	public static final Pair<BlockCraftingGrid, ItemBlock> darkoak_crafting_grid;
	
	public static final Pair<BlockAppleLeaves, ItemBlock> apple_leaves;
	
	//public ItemStick stick;
	public static final ItemFood yellow_apple;
	public static final ItemFood green_apple;
	
	@SideOnly(Side.CLIENT)
	public StateMapperCraftingGrid stateMapperCraftingGrid;
	@SideOnly(Side.CLIENT)
	public StateMapperFruitLeaves stateMapperFruitLeaves;
	
	public static final CreativeTabs crafting_grids;
	public static final CreativeTabs flora;
	
	static
	{
		BlockCraftingGrid grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
		crafting_grids = new CreativeTab(mod.modid() + ":crafting_grids", grid);
		grid.setRegistryName(new ResourceLocation(mod.modid(), "oak_crafting_grid")).setUnlocalizedName(grid.getRegistryName().toString()).setHardness(2F).setCreativeTab(crafting_grids);
		oak_crafting_grid = Pair.of(grid, ItemHooks.createItemBlock(grid));
		(grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD)).setRegistryName(new ResourceLocation(mod.modid(), "spruce_crafting_grid")).setUnlocalizedName(grid.getRegistryName().toString()).setHardness(2F).setCreativeTab(crafting_grids);
		spruce_crafting_grid = Pair.of(grid, ItemHooks.createItemBlock(grid));
		(grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD)).setRegistryName(new ResourceLocation(mod.modid(), "birch_crafting_grid")).setUnlocalizedName(grid.getRegistryName().toString()).setHardness(2F).setCreativeTab(crafting_grids);
		birch_crafting_grid = Pair.of(grid, ItemHooks.createItemBlock(grid));
		(grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD)).setRegistryName(new ResourceLocation(mod.modid(), "jungle_crafting_grid")).setUnlocalizedName(grid.getRegistryName().toString()).setHardness(2F).setCreativeTab(crafting_grids);
		jungle_crafting_grid = Pair.of(grid, ItemHooks.createItemBlock(grid));
		(grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD)).setRegistryName(new ResourceLocation(mod.modid(), "acacia_crafting_grid")).setUnlocalizedName(grid.getRegistryName().toString()).setHardness(2F).setCreativeTab(crafting_grids);
		acacia_crafting_grid = Pair.of(grid, ItemHooks.createItemBlock(grid));
		(grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD)).setRegistryName(new ResourceLocation(mod.modid(), "darkoak_crafting_grid")).setUnlocalizedName(grid.getRegistryName().toString()).setHardness(2F).setCreativeTab(crafting_grids);
		darkoak_crafting_grid = Pair.of(grid, ItemHooks.createItemBlock(grid));
		BlockAppleLeaves appleleaves = new BlockAppleLeaves();
		flora = new CreativeTab(mod.modid() + ":flora", appleleaves);
		appleleaves.setRegistryName(new ResourceLocation(mod.modid(), "apple_leaves")).setUnlocalizedName(appleleaves.getRegistryName().toString()).setCreativeTab(flora);
		apple_leaves = Pair.of(appleleaves, ItemHooks.createItemBlockMeta(appleleaves, 3));
		ItemFood apple;
		(apple = new ItemFood(2, .5F, false)).setRegistryName(new ResourceLocation(mod.modid(), "yellow_apple")).setUnlocalizedName(apple.getRegistryName().toString()).setCreativeTab(CreativeTabs.FOOD);
		yellow_apple = apple;
		(apple = new ItemFood(1, 1F, false)).setRegistryName(new ResourceLocation(mod.modid(), "green_apple")).setUnlocalizedName(apple.getRegistryName().toString()).setCreativeTab(CreativeTabs.FOOD);
		green_apple = apple;
	}
}
