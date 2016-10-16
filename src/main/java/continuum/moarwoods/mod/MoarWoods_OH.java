package continuum.moarwoods.mod;


import org.apache.commons.lang3.tuple.Pair;

import continuum.essentials.hooks.ItemHooks;
import continuum.essentials.mod.ObjectHolder;
import continuum.essentials.util.CreativeTab;
import continuum.moarwoods.blocks.BlockAppleLeaves;
import continuum.moarwoods.blocks.BlockCraftingGrid;
import continuum.moarwoods.client.state.StateMapperCraftingGrid;
import continuum.moarwoods.client.state.StateMapperFruitLeaves;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
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
	
	public static final Pair<BlockCraftingGrid, ItemBlock> OAK_CRAFTING_GRID;
	public static final Pair<BlockCraftingGrid, ItemBlock> SPRUCE_CRAFTING_GRID;
	public static final Pair<BlockCraftingGrid, ItemBlock> BIRCH_CRAFTING_GRID;
	public static final Pair<BlockCraftingGrid, ItemBlock> JUNGLE_CRAFTING_GRID;
	public static final Pair<BlockCraftingGrid, ItemBlock> ACACIA_CRAFTING_GRID;
	public static final Pair<BlockCraftingGrid, ItemBlock> DARKOAK_CRAFTING_GRID;
	
	public static final Pair<BlockAppleLeaves, ItemBlock> APPLE_LEAVES;
	
	//public ItemStick stick;
	public static final ItemFood YELLOW_APPLE;
	public static final ItemFood GREEN_APPLE;
	
	public static final CreativeTabs CRAFTING_GRIDS;
	public static final CreativeTabs FLORA;
	
	static
	{
		if(!Loader.instance().activeModContainer().getModId().equals(mod.modid()))
			throw new IllegalStateException("MoarWoods not loaded yet!");
		BlockCraftingGrid grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
		CRAFTING_GRIDS = new CreativeTab(mod.modid() + ":crafting_grids", grid);
		(OAK_CRAFTING_GRID = ObjectHolder.setupItemBlock(grid, "oak_crafting_grid", CRAFTING_GRIDS)).getLeft().setHardness(2F);
		(SPRUCE_CRAFTING_GRID = ObjectHolder.setupItemBlock(grid, "spruce_crafting_grid", CRAFTING_GRIDS)).getLeft().setHardness(2F);
		(BIRCH_CRAFTING_GRID = ObjectHolder.setupItemBlock(grid, "birch_crafting_grid", CRAFTING_GRIDS)).getLeft().setHardness(2F);
		(JUNGLE_CRAFTING_GRID = ObjectHolder.setupItemBlock(grid, "jungle_crafting_grid", CRAFTING_GRIDS)).getLeft().setHardness(2F);
		(ACACIA_CRAFTING_GRID = ObjectHolder.setupItemBlock(grid, "acacia_crafting_grid", CRAFTING_GRIDS)).getLeft().setHardness(2F);
		(DARKOAK_CRAFTING_GRID = ObjectHolder.setupItemBlock(grid, "darkoak_crafting_grid", CRAFTING_GRIDS)).getLeft().setHardness(2F);
		BlockAppleLeaves appleleaves = new BlockAppleLeaves();
		APPLE_LEAVES = ObjectHolder.setupItemBlock(appleleaves, "apple_leaves", FLORA = new CreativeTab(mod.modid() + ":flora", appleleaves), 3);
		YELLOW_APPLE = ObjectHolder.setupItem(new ItemFood(2, .5F, false), "yellow_apple", CreativeTabs.FOOD);
		GREEN_APPLE = ObjectHolder.setupItem(new ItemFood(2, .5F, false), "green_apple", CreativeTabs.FOOD);
	}
}
