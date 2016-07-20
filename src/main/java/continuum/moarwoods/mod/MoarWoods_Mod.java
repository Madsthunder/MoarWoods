package continuum.moarwoods.mod;

import continuum.essentials.mod.CTMod;
import continuum.moarwoods.loaders.BlockLoader;
import continuum.moarwoods.loaders.ClientLoader;
import continuum.moarwoods.loaders.ItemLoader;
import continuum.moarwoods.loaders.RecipeLoader;
import continuum.moarwoods.loaders.UtilityLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "MoarWoods", name = "MoarWoods", version = "0.0.0")
public class MoarWoods_Mod extends CTMod<MoarWoods_OH, MoarWoods_EH>
{
	
	public MoarWoods_Mod()
	{
		super(new MoarWoods_OH(), new MoarWoods_EH(), new BlockLoader(), new ItemLoader(), new UtilityLoader(), new RecipeLoader(), new ClientLoader());
	}
	
	@Mod.EventHandler
	public void construction(FMLConstructionEvent event)
	{
		super.construction(event);
	}
	
	@Mod.EventHandler
	public void pre(FMLPreInitializationEvent event)
	{
		super.pre(event);
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		super.init(event);
	}
	
	@Mod.EventHandler
	public void post(FMLPostInitializationEvent event)
	{
		super.post(event);
	}
}
