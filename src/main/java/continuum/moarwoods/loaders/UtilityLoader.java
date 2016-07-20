package continuum.moarwoods.loaders;

import continuum.essentials.mod.CTMod;
import continuum.essentials.mod.ObjectLoader;
import continuum.essentials.util.CreativeTab;
import continuum.moarwoods.mod.MoarWoods_EH;
import continuum.moarwoods.mod.MoarWoods_OH;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class UtilityLoader implements ObjectLoader<MoarWoods_OH, MoarWoods_EH>
{
	
	@Override
	public void construction(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		
	}
	
	@Override
	public void pre(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		MoarWoods_OH holder = mod.getObjectHolder();
		holder.creativeTabGrids = new CreativeTab("crafing_grids", holder.crafingGridOak);
		holder.crafingGridOak.setCreativeTab(holder.creativeTabGrids);
		holder.crafingGridSpruce.setCreativeTab(holder.creativeTabGrids);
		holder.crafingGridBirch.setCreativeTab(holder.creativeTabGrids);
		holder.crafingGridJungle.setCreativeTab(holder.creativeTabGrids);
		holder.crafingGridAcacia.setCreativeTab(holder.creativeTabGrids);
		holder.crafingGridDarkOak.setCreativeTab(holder.creativeTabGrids);
	}
	
	@Override
	public void init(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(mod, mod.getEventHandler());
	}
	
	@Override
	public void post(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		
	}
	
	@Override
	public String getName()
	{
		return "Utilities";
	}
}
