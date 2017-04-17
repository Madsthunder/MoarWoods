package moarwoods.blocks.living.tree;

import java.util.List;

import javax.annotation.Nullable;

import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlant
{
	
	public BlockLivingLog getLogBlock();
	
	public BlockLivingLeaf getLeafBlock();
	
	@Nullable
	public long[] getSeeds(World world, BlockPos pos);
	
	public int getLeafSearchRadius(World world, BlockPos pos, int height, long[] seeds);
	
	public int getLeafSearchExtraHeight(World world, BlockPos pos, int height, long[] seeds);
	
	public List<BlockPos> getLeaves(World world, BlockPos pos, int height, long[] seeds);
	
	public List<IPlantBranch> getBranches(World world, BlockPos pos, int height, long[] seeds);
	
	public boolean grow(World world, BlockPos pos);
}
