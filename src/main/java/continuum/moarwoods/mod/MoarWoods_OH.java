package continuum.moarwoods.mod;

import continuum.essentials.mod.ObjectHolder;
import continuum.moarwoods.blocks.BlockCraftingGrid;
import continuum.moarwoods.client.state.StateMapperCraftingGrid;
import continuum.moarwoods.items.ItemStick;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MoarWoods_OH implements ObjectHolder
{
	
	@Override
	public String getModid()
	{
		return "MoarWoods";
	}
	
	@Override
	public String getName()
	{
		return "MoarWoods";
	}
	
	@Override
	public String getVersion()
	{
		return "0.0.0";
	}
	
	public BlockCraftingGrid crafingGridOak;
	public BlockCraftingGrid crafingGridSpruce;
	public BlockCraftingGrid crafingGridBirch;
	public BlockCraftingGrid crafingGridJungle;
	public BlockCraftingGrid crafingGridAcacia;
	public BlockCraftingGrid crafingGridDarkOak;
	
	public ItemStick stick;
	
	@SideOnly(Side.CLIENT)
	public StateMapperCraftingGrid stateMapperGrid;
	
	public CreativeTabs creativeTabGrids;
}
