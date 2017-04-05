package moarwoods.blocks.living.tree;

import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IPlantBranch
{
	
	public BlockLivingLog getLogBlock();
	
	public BlockLivingLeaf getLeafBlock();
	
	public boolean grow(World world, BlockPos pos, int parentheight);
}
