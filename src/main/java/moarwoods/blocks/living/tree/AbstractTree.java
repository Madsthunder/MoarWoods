package moarwoods.blocks.living.tree;

import java.util.List;

import com.google.common.collect.Lists;

import moarwoods.blocks.BlockLivingBranch;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractTree<T extends BlockLivingLog, L extends BlockLivingLeaf, B extends BlockLivingBranch> extends AbstractPlant<T, L, B>
{
	@Override
	public void placeLogsAt(World world, BlockPos pos, int from_height, int to_height, long[] seeds)
	{
		boolean removing = to_height < from_height;
		{
			int new_from_height = Math.min(from_height, to_height);
			int new_to_height = Math.max(from_height, to_height);
			from_height = new_from_height;
			to_height = new_to_height;
		}
		IBlockState state = removing ? Blocks.AIR.getDefaultState() : this.getLogBlock().getDefaultState();
		List<EnumFacing> lean_positions = this.getLeanPositions(world, pos, seeds);
		int size = lean_positions.size();
		int x_offset = 0;
		int z_offset = 0;
		for(int i = 0; i < size && i < from_height; i++)
		{
			EnumFacing facing = lean_positions.get(i);
			if(facing != null)
			{
				x_offset += facing.getFrontOffsetX();
				z_offset += facing.getFrontOffsetZ();
			}
		}
		for(int y_offset = from_height; to_height > y_offset; y_offset++)
		{
			if(y_offset < lean_positions.size())
			{
				EnumFacing facing = lean_positions.get(y_offset);
				if(facing != null)
				{
					x_offset += facing.getFrontOffsetX();
					z_offset += facing.getFrontOffsetZ();
				}
			}
			world.setBlockState(pos.add(x_offset, y_offset, z_offset), state);
		}
	}
	
	public List<EnumFacing> getLeanPositions(World world, BlockPos pos, long[] seeds)
	{
		return Lists.newArrayListWithCapacity(this.getHeightLimit(world, pos, seeds));
	}
}
