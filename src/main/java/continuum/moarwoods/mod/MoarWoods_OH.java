package continuum.moarwoods.mod;

import continuum.essentials.mod.ObjectHolder;
import continuum.essentials.util.CreativeTab;
import continuum.moarwoods.blocks.BlockAppleLeaves;
import continuum.moarwoods.blocks.BlockCraftingGrid;
import continuum.moarwoods.client.state.StateMapperCraftingGrid;
import continuum.moarwoods.client.state.StateMapperFruitLeaves;
import continuum.moarwoods.items.ItemStick;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
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
	
	public static final BlockCraftingGrid oak_crafting_grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid spruce_crafting_grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid birch_crafting_grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid jungle_crafting_grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid acacia_crafting_grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid darkoak_crafting_grid = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	
	public static final BlockAppleLeaves apple_leaves = new BlockAppleLeaves();
	
	//public ItemStick stick;
	public static final ItemFood yellow_apple = new ItemFood(2, .5F, false);
	public static final ItemFood green_apple = new ItemFood(1, 1F, false);
	
	@SideOnly(Side.CLIENT)
	public StateMapperCraftingGrid stateMapperCraftingGrid;
	@SideOnly(Side.CLIENT)
	public StateMapperFruitLeaves stateMapperFruitLeaves;
	
	public static final CreativeTabs crafting_grids = new CreativeTab("crafting_grids", oak_crafting_grid);
	public static final CreativeTabs flora = new CreativeTab("flora", apple_leaves);
	
	static
	{
		oak_crafting_grid.setRegistryName(new ResourceLocation(mod.modid(), "oak_crafting_grid"));
		oak_crafting_grid.setUnlocalizedName("oak_crafting_grid");
		oak_crafting_grid.setHardness(2F);
		oak_crafting_grid.setCreativeTab(crafting_grids);
		spruce_crafting_grid.setRegistryName(new ResourceLocation(mod.modid(), "spruce_crafting_grid"));
		spruce_crafting_grid.setUnlocalizedName("spruce_crafting_grid");
		spruce_crafting_grid.setHardness(2F);
		spruce_crafting_grid.setCreativeTab(crafting_grids);
		birch_crafting_grid.setRegistryName(new ResourceLocation(mod.modid(), "birch_crafting_grid"));
		birch_crafting_grid.setUnlocalizedName("birch_crafting_grid");
		birch_crafting_grid.setHardness(2F);
		birch_crafting_grid.setCreativeTab(crafting_grids);
		jungle_crafting_grid.setRegistryName(new ResourceLocation(mod.modid(), "jungle_crafting_grid"));
		jungle_crafting_grid.setUnlocalizedName("jungle_crafting_grid");
		jungle_crafting_grid.setHardness(2F);
		jungle_crafting_grid.setCreativeTab(crafting_grids);
		acacia_crafting_grid.setRegistryName(new ResourceLocation(mod.modid(), "acacia_crafting_grid"));
		acacia_crafting_grid.setUnlocalizedName("acacia_crafting_grid");
		acacia_crafting_grid.setHardness(2F);
		acacia_crafting_grid.setCreativeTab(crafting_grids);
		darkoak_crafting_grid.setRegistryName(new ResourceLocation(mod.modid(), "darkoak_crafting_grid"));
		darkoak_crafting_grid.setUnlocalizedName("darkoak_crafting_grid");
		darkoak_crafting_grid.setHardness(2F);
		darkoak_crafting_grid.setCreativeTab(crafting_grids);
		apple_leaves.setRegistryName(new ResourceLocation(mod.modid(), "apple_leaves"));
		apple_leaves.setUnlocalizedName("apple_leaves");
		apple_leaves.setCreativeTab(flora);
		yellow_apple.setRegistryName(new ResourceLocation(mod.modid(), "yellow_apple"));
		green_apple.setRegistryName(new ResourceLocation(mod.modid(), "green_apple"));
	}
}
