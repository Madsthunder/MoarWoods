package moarwoods.blocks.living.tree;

import java.util.Collection;
import java.util.List;

import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlant
{
	
	public BlockLivingLog getLogBlock();
	
	public BlockLivingLeaf getLeafBlock();
	
	public int getLeafSearchRadius(int height);
	
	public int getLeafSearchExtraHeight(int height);
	
	public List<BlockPos> getLeaves(BlockPos pos, int height, long seed);
	
	public List<IPlantBranch> getBranches(World world, BlockPos pos, int height, long seed);
	
	public boolean grow(World world, BlockPos pos);
}
