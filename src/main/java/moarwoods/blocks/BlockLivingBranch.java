package moarwoods.blocks;

import java.util.Set;

import com.google.common.collect.Sets;

import moarwoods.MoarWoodsObjects;
import moarwoods.blocks.living.tree.Plant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLivingBranch extends BlockLivingLog
{
	public static final PropertyBool BASE = PropertyBool.create("base");
	public static final PropertyBool UPWARDS = PropertyBool.create("upwards");
	public static final PropertyBool DOWNWARDS = PropertyBool.create("downwards");
	
	public BlockLivingBranch(Plant<?, ?, ?> plant)
	{
		super(plant);
		this.setDefaultState(this.getDefaultState().withProperty(BASE, false).withProperty(UPWARDS, true).withProperty(DOWNWARDS, true).withProperty(LOG_AXIS, BlockLog.EnumAxis.NONE));
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return super.onBlockActivated(world, pos, state, player, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(DEATH_STAGE) * 8 & 8) + (state.getValue(BASE).compareTo(false) * 4 & 4) + (state.getValue(DOWNWARDS).compareTo(false) * 2 & 2) + (state.getValue(UPWARDS).compareTo(false) * 1);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		int death_stage = (meta & 15) > 7 ? 1 : 0;
		boolean base = (death_stage & 7) > 3;
		boolean upwards = (meta & 3) > 1;
		boolean downwards = (meta & 1) > 0;
		return this.getDefaultState().withProperty(DEATH_STAGE, death_stage).withProperty(BASE, base).withProperty(UPWARDS, upwards).withProperty(DOWNWARDS, downwards);
	}
	
	@Override
	public int getMaxDeathStage(IBlockState state)
	{
		return 1;
	}
	
	@Override
	public Set<BlockPos> nextPositions(IBlockState state, IBlockAccess access, BlockPos pos, AxisDirection direction)
	{
		Set<BlockPos> positions = Sets.newHashSet();
		int y_offset = direction == AxisDirection.POSITIVE && state.getValue(UPWARDS) ? 1 : direction == AxisDirection.NEGATIVE && state.getValue(DOWNWARDS) ? -1 : 0;
		for(int x_offset = -1; x_offset <= 1; x_offset += y_offset == 0 ? 2 : 1)
			for(int z_offset = -1; z_offset <= 1; z_offset++)
			{
				BlockPos pos1 = pos.add(x_offset, y_offset, z_offset);
				Block block = access.getBlockState(pos1).getBlock();
				if(block == this || block == MoarWoodsObjects.OAK_TREE_SMALL_TRUNK)
					positions.add(pos1);
			}
		return positions;
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, DEATH_STAGE, BASE, UPWARDS, DOWNWARDS, LOG_AXIS);
	}
}
