package continuum.moarwoods.loaders;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import continuum.essentials.mod.CTMod;
import continuum.essentials.mod.ObjectLoader;
import continuum.moarwoods.blocks.BlockCraftingGrid;
import continuum.moarwoods.mod.MoarWoods_EH;
import continuum.moarwoods.mod.MoarWoods_OH;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import scala.reflect.runtime.ReflectionUtils;

public class BlockLoader implements ObjectLoader<MoarWoods_OH, MoarWoods_EH>
{
	@Override
	public void construction(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		
	}
	
	@Override
	public void pre(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		MoarWoods_OH holder = mod.getObjectHolder();
		ForgeRegistries.BLOCKS.register((holder.crafingGridOak = new BlockCraftingGrid(mod, Material.WOOD, SoundType.WOOD, "oak")).setHardness(2F));
		ForgeRegistries.BLOCKS.register((holder.crafingGridSpruce = new BlockCraftingGrid(mod, Material.WOOD, SoundType.WOOD, "spruce")).setHardness(2F));
		ForgeRegistries.BLOCKS.register((holder.crafingGridBirch = new BlockCraftingGrid(mod, Material.WOOD, SoundType.WOOD, "birch")).setHardness(2F));
		ForgeRegistries.BLOCKS.register((holder.crafingGridJungle = new BlockCraftingGrid(mod, Material.WOOD, SoundType.WOOD, "jungle")).setHardness(2F));
		ForgeRegistries.BLOCKS.register((holder.crafingGridAcacia = new BlockCraftingGrid(mod, Material.WOOD, SoundType.WOOD, "acacia")).setHardness(2F));
		ForgeRegistries.BLOCKS.register((holder.crafingGridDarkOak = new BlockCraftingGrid(mod, Material.WOOD, SoundType.WOOD, "dark_oak")).setHardness(2F));
		GameRegistry.registerTileEntity(TileEntityCraftingGrid.class, "moarwoods:crafting_grid");
	}
	
	@Override
	public String getName()
	{
		return "Blocks";
	}
}
