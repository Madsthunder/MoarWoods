package continuum.moarwoods.mod;

import continuum.essentials.mod.CTMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "moarwoods", name = "MoarWoods", version = "0.0.0")
public class MoarWoods_Mod extends CTMod<MoarWoods_OH, MoarWoods_EH>
{
	public MoarWoods_Mod()
	{
		super(MoarWoods_OH.I);
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
    	MoarWoods_Proxies.I.pre(this);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {
    	super.init(event);
    	MoarWoods_Proxies.I.init(this);
    }

    @Mod.EventHandler
    public void post(FMLPostInitializationEvent event)
    {
    	super.post(event);
    	MoarWoods_Proxies.I.post(this);
    }
}
