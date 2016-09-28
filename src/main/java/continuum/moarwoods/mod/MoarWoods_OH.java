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
	
	public static final BlockCraftingGrid craftingGridOak = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid craftingGridSpruce = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid craftingGridBirch = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid craftingGridJungle = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid craftingGridAcacia = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	public static final BlockCraftingGrid craftingGridDarkOak = new BlockCraftingGrid(Material.WOOD, SoundType.WOOD);
	
	public static final BlockAppleLeaves appleLeaves = new BlockAppleLeaves();
	
	//public ItemStick stick;
	public static final ItemFood yellow_apple = new ItemFood(2, .5F, false);
	public static final ItemFood green_apple = new ItemFood(1, 1F, false);
	
	@SideOnly(Side.CLIENT)
	public StateMapperCraftingGrid stateMapperCraftingGrid;
	@SideOnly(Side.CLIENT)
	public StateMapperFruitLeaves stateMapperFruitLeaves;
	
	public static final CreativeTabs crafting_grids = new CreativeTab("crafting_grids", craftingGridOak);
	public static final CreativeTabs flora = new CreativeTab("flora", appleLeaves);
	
	static
	{
		craftingGridOak.setRegistryName(new ResourceLocation(mod.modid(), "oak_crafting_grid"));
		craftingGridOak.setUnlocalizedName("oak_crafting_grid");
		craftingGridOak.setHardness(2F);
		craftingGridOak.setCreativeTab(crafting_grids);
		craftingGridSpruce.setRegistryName(new ResourceLocation(mod.modid(), "spruce_crafting_grid"));
		craftingGridSpruce.setUnlocalizedName("spruce_crafting_grid");
		craftingGridSpruce.setHardness(2F);
		craftingGridSpruce.setCreativeTab(crafting_grids);
		craftingGridBirch.setRegistryName(new ResourceLocation(mod.modid(), "birch_crafting_grid"));
		craftingGridBirch.setUnlocalizedName("birch_crafting_grid");
		craftingGridBirch.setHardness(2F);
		craftingGridBirch.setCreativeTab(crafting_grids);
		craftingGridJungle.setRegistryName(new ResourceLocation(mod.modid(), "jungle_crafting_grid"));
		craftingGridJungle.setUnlocalizedName("jungle_crafting_grid");
		craftingGridJungle.setHardness(2F);
		craftingGridJungle.setCreativeTab(crafting_grids);
		craftingGridAcacia.setRegistryName(new ResourceLocation(mod.modid(), "acacia_crafting_grid"));
		craftingGridAcacia.setUnlocalizedName("acacia_crafting_grid");
		craftingGridAcacia.setHardness(2F);
		craftingGridAcacia.setCreativeTab(crafting_grids);
		craftingGridDarkOak.setRegistryName(new ResourceLocation(mod.modid(), "darkoak_crafting_grid"));
		craftingGridDarkOak.setUnlocalizedName("darkoak_crafting_grid");
		craftingGridDarkOak.setHardness(2F);
		craftingGridDarkOak.setCreativeTab(crafting_grids);
		appleLeaves.setRegistryName(new ResourceLocation(mod.modid(), "apple_leaves"));
		appleLeaves.setUnlocalizedName("apple_leaves");
		appleLeaves.setCreativeTab(flora);
		yellow_apple.setRegistryName(new ResourceLocation(mod.modid(), "yellow_apple"));
		green_apple.setRegistryName(new ResourceLocation(mod.modid(), "green_apple"));
	}
}
