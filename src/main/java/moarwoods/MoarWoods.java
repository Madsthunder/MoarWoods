package moarwoods;

import moarwoods.command.CommandSetBiome;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

@EventBusSubscriber
@Mod(modid = MoarWoods.MODID, name = MoarWoods.NAME, version = MoarWoods.NAME)
public class MoarWoods
{
	public static final String MODID = "moarwoods";
	public static final String NAME = "MoarWoods";
	public static final String VERSION = "0.0.0";
	
	@Mod.EventHandler
	public void construction(FMLConstructionEvent event)
	{
		MoarWoodsProxies.I.constr();
	}
	
	@Mod.EventHandler
	public void pre(FMLPreInitializationEvent event)
	{
		MoarWoodsProxies.I.pre();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		MoarWoodsProxies.I.init();
	}
	
	@Mod.EventHandler
	public void post(FMLPostInitializationEvent event)
	{
		MoarWoodsProxies.I.post();
	}
	
	@Mod.EventHandler
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		MoarWoodsBlockSeeds.BLOCK_SEED_CACHE.clear();
		MoarWoodsBlockSeeds.BLOCK_SEED_NBT.clear();
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandSetBiome());
	}
	
	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		MoarWoodsBlockSeeds.BLOCK_SEED_CACHE.clear();
		MoarWoodsBlockSeeds.BLOCK_SEED_NBT.clear();
	}
	
	public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
}
