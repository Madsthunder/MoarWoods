package moarwoods.blocks.living.tree;

import java.util.Random;

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
	public int getHeightLimit(World world, BlockPos pos, long[] seeds)
	{
		Biome biome = world.getBiome(pos);
		float humidity = Math.min(.9F, biome.getTemperature() * biome.getRainfall());
		Random random = new Random(seeds[0]);
		return 4 + random.nextInt(3) + random.nextInt(0 >= humidity ? 1 : Math.max(1, Float.valueOf(7 * (humidity + .1F)).intValue()));
	}
	
}
