package moarwoods.blocks.living.tree;

import moarwoods.MoarWoods;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class SmallJungleTree extends SmallOakTree
{
	
	@Override
	public BlockLivingLog getLogBlock()
	{
		return MoarWoods.LIVING_JUNGLE_LOG;
	}
	
	@Override
	public BlockLivingLeaf getLeafBlock()
	{
		return MoarWoods.LIVING_JUNGLE_LEAF;
	}
	
	@Override
	public int getDynamicHeightLimitFactor(World world, BlockPos pos)
	{
		Biome biome = world.getBiome(pos);
		float humidity = Math.min(.9F, biome.getTemperature() * biome.getRainfall());
		return 0 >= humidity ? 0 : Float.valueOf(7 * (humidity + .1F)).intValue();
	}
	
}
