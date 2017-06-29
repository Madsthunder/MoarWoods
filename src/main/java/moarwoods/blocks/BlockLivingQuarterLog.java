package moarwoods.blocks;

import java.util.Set;

import com.google.common.collect.Sets;

import moarwoods.blocks.BlockQuarterLog.EnumLogQuarter;
import moarwoods.blocks.living.tree.Plant;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockLivingQuarterLog extends BlockLivingLog
{
	public final boolean canLean;
	
	public BlockLivingQuarterLog(Plant<?, ?, ?> plant)
	{
		this(plant, false);
	}
	
	public BlockLivingQuarterLog(Plant<?, ?, ?> plant, boolean canLean)
	{
		super(plant);
		this.canLean = canLean;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(DEATH_STAGE) * 4) + (state.getValue(BlockQuarterLog.QUARTER).ordinal() & 3);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DEATH_STAGE, Math.min(3, Math.max(0, meta / 4))).withProperty(BlockQuarterLog.QUARTER,  BlockQuarterLog.EnumLogQuarter.values()[meta & 3]);
	}
	
	@Override
	public Set<BlockPos> nextPositions(IBlockState state, IBlockAccess access, BlockPos pos, AxisDirection direction)
	{
		Set<BlockPos> positions = Sets.newHashSet();
		EnumFacing first_side;
		EnumFacing second_side;
		{
			EnumLogQuarter quarter = state.getValue(BlockQuarterLog.QUARTER);
			first_side = quarter.getFirstSide(Axis.Y);
			second_side = quarter.getSecondSide(Axis.Y);
		}
		checkPosition(access, pos.offset(direction == AxisDirection.POSITIVE ? EnumFacing.UP : EnumFacing.DOWN), first_side, second_side, positions);
		if(this.canLean)
			for(EnumFacing facing : Plane.HORIZONTAL.facings())
				checkPosition(access, pos.offset(direction == AxisDirection.POSITIVE ? EnumFacing.UP : EnumFacing.DOWN).offset(facing), first_side, second_side, positions);
		checkPosition(access, pos.offset(first_side.getOpposite()), first_side.getOpposite(), second_side, positions);
		checkPosition(access, pos.offset(second_side.getOpposite()), first_side, second_side.getOpposite(), positions);
		return positions;
	}
	
	protected void checkPosition(IBlockAccess access, BlockPos pos, EnumFacing correct_first_side, EnumFacing correct_second_side, Set<BlockPos> positions)
	{
		IBlockState state = access.getBlockState(pos);
		if(state.getBlock() != this)
			return;
		EnumLogQuarter quarter = state.getValue(BlockQuarterLog.QUARTER);
		if((state.getPropertyKeys().contains(BlockLog.LOG_AXIS) ? state.getValue(BlockLog.LOG_AXIS) : BlockLog.EnumAxis.Y).toString().toLowerCase().equals("y") && correct_first_side == quarter.getFirstSide(Axis.Y) && correct_second_side == quarter.getSecondSide(Axis.Y))
			positions.add(pos);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { BlockQuarterLog.QUARTER, DEATH_STAGE, LOG_AXIS });
	}
}
