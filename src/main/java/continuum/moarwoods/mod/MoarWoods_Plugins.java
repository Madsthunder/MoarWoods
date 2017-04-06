package continuum.moarwoods.mod;

import java.io.File;
import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.Name("MoarWoods")
@IFMLLoadingPlugin.MCVersion("1.10.2")
public class MoarWoods_Plugins implements IFMLLoadingPlugin
{
	@Override
	public String[] getASMTransformerClass()
	{
		return null;
	}

	@Override
	public String getModContainerClass()
	{
		return null;
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
	
}
