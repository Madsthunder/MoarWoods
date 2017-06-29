package moarwoods.blocks;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockQuarterLog extends BlockLog
{
	public static final PropertyEnum<EnumLogQuarter> QUARTER = PropertyEnum.create("quarter", EnumLogQuarter.class, EnumLogQuarter.NORTH_WEST, EnumLogQuarter.NORTH_EAST, EnumLogQuarter.SOUTH_WEST, EnumLogQuarter.SOUTH_EAST);
	
	public BlockQuarterLog()
	{
		this.setDefaultState(this.getDefaultState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(QUARTER, EnumLogQuarter.NORTH_WEST));
	}
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(LOG_AXIS).ordinal() * 4) + (state.getValue(BlockQuarterLog.QUARTER).ordinal() & 3);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(LOG_AXIS, BlockQuarterLog.EnumAxis.values()[(meta / 4) & 3]).withProperty(BlockQuarterLog.QUARTER,  BlockQuarterLog.EnumLogQuarter.values()[meta & 3]);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase entity)
	{
		Axis axis = facing.getAxis();
		IBlockState state = world.getBlockState(pos.offset(facing.getOpposite()));
		Collection<IProperty<?>> properties = state.getPropertyKeys();
		if(!entity.isSneaking() && properties.contains(QUARTER) && properties.contains(LOG_AXIS) && state.getValue(LOG_AXIS).toString().toLowerCase().equals(axis.getName()))
			return this.getDefaultState().withProperty(QUARTER, state.getValue(QUARTER)).withProperty(LOG_AXIS, state.getValue(LOG_AXIS));
		state = this.getDefaultState();
		state = state.withProperty(LOG_AXIS, axis == Axis.X ? BlockLog.EnumAxis.X : axis == Axis.Z ? BlockLog.EnumAxis.Z : BlockLog.EnumAxis.Y);
		if(!entity.isSneaking())
			for(int x_offset = axis == Axis.X ? 0 : -1; x_offset <= (axis == Axis.X ? 0 : 1); x_offset++)
				for(int y_offset = axis == Axis.Y ? 0 : -1; y_offset <= (axis == Axis.Y ? 0 : 1); y_offset++)
					for(int z_offset = axis == Axis.Z ? 0 : -1; z_offset <= (axis == Axis.Z ? 0 : 1); z_offset++)
					{
						BlockPos pos1 = pos.add(x_offset, y_offset, z_offset);
						if(!pos1.equals(pos))
						{
							IBlockState state1 = world.getBlockState(pos1);
							properties = state1.getPropertyKeys();
							if(properties.contains(QUARTER) && (properties.contains(LOG_AXIS) ? state1.getValue(LOG_AXIS) : BlockLog.EnumAxis.Y).toString().toLowerCase().equals(axis.getName()))
							{
								EnumFacing first_direction = axis == Axis.Y ? z_offset == -1 ? EnumFacing.NORTH : z_offset == 1 ? EnumFacing.SOUTH : null : y_offset == -1 ? EnumFacing.DOWN : y_offset == 1 ? EnumFacing.UP : null;
								EnumFacing second_direction = axis == Axis.X ? z_offset == -1 ? EnumFacing.NORTH : z_offset == 1 ? EnumFacing.SOUTH : null : x_offset == -1 ? EnumFacing.WEST : x_offset == 1 ? EnumFacing.EAST : null;
								EnumLogQuarter quarter = state1.getValue(QUARTER);
								EnumFacing other_first_direction = quarter.getFirstSide(axis);
								EnumFacing other_second_direction = quarter.getSecondSide(axis);
								if(first_direction == null)
								{
									if(second_direction == other_second_direction)
										return state.withProperty(QUARTER, EnumLogQuarter.getQuarterFromDirections(other_first_direction, second_direction.getOpposite()));
								}
								else if(second_direction == null)
								{
									if(first_direction == other_first_direction)
										return state.withProperty(QUARTER, EnumLogQuarter.getQuarterFromDirections(other_first_direction.getOpposite(), other_second_direction));
								}
								else if(first_direction == other_first_direction && second_direction == other_second_direction)
									return state.withProperty(QUARTER, EnumLogQuarter.getQuarterFromDirections(first_direction.getOpposite(), second_direction.getOpposite()));
							}
						}
					}
		EnumFacing x_direction = pos.getX() + .5 <= entity.posX ? EnumFacing.WEST : EnumFacing.EAST;
		EnumFacing y_direction = pos.getY() + .5 <= entity.posY + entity.getEyeHeight() ? EnumFacing.DOWN : EnumFacing.UP;
		EnumFacing z_direction = pos.getZ() + .5 <= entity.posZ ? EnumFacing.NORTH : EnumFacing.SOUTH;
		return state.withProperty(QUARTER, EnumLogQuarter.getQuarterFromDirections(axis == Axis.Y ? z_direction : y_direction, axis == Axis.X ? z_direction : x_direction));
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rotation)
	{
		EnumLogQuarter quarter = state.getValue(QUARTER);
		Axis axis;
		switch(state.getValue(LOG_AXIS))
		{
			case X : axis = Axis.X; break;
			case Y : axis = Axis.Y; break;
			case Z : axis = Axis.Z; break;
			default : return state;
		}
		switch(rotation)
		{
			case CLOCKWISE_90 :
			{
				switch(quarter)
				{
					case NORTH_WEST : return state.withProperty(QUARTER, EnumLogQuarter.NORTH_EAST);
					case NORTH_EAST : return state.withProperty(QUARTER, EnumLogQuarter.SOUTH_EAST);
					case SOUTH_EAST : return state.withProperty(QUARTER, EnumLogQuarter.SOUTH_WEST);
					default : return state.withProperty(QUARTER, EnumLogQuarter.NORTH_WEST);
				}
			}
			case CLOCKWISE_180 :
			{
				switch(quarter.getSecondSide(axis))
				{
					case NORTH : return state.withProperty(QUARTER, EnumLogQuarter.DIRECTIONS_TO_QUARTER.get(quarter.getFirstSide(axis)).get(EnumFacing.SOUTH));
					case EAST : return state.withProperty(QUARTER, EnumLogQuarter.DIRECTIONS_TO_QUARTER.get(quarter.getFirstSide(axis)).get(EnumFacing.WEST));
					case WEST : return state.withProperty(QUARTER, EnumLogQuarter.DIRECTIONS_TO_QUARTER.get(quarter.getFirstSide(axis)).get(EnumFacing.EAST));
					default : return state.withProperty(QUARTER, EnumLogQuarter.DIRECTIONS_TO_QUARTER.get(quarter.getFirstSide(axis)).get(EnumFacing.NORTH));
				}
			}
			case COUNTERCLOCKWISE_90 :
			{
				switch(quarter)
				{
					case SOUTH_EAST : return state.withProperty(QUARTER, EnumLogQuarter.NORTH_EAST);
					case SOUTH_WEST : return state.withProperty(QUARTER, EnumLogQuarter.SOUTH_EAST);
					case NORTH_WEST : return state.withProperty(QUARTER, EnumLogQuarter.SOUTH_WEST);
					default : return state.withProperty(QUARTER, EnumLogQuarter.NORTH_WEST);
				}
			}
			default :
			{
				return state;
			}
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float x, float y, float z)
	{
		return false;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { LOG_AXIS, QUARTER});
	}
	
	public static enum EnumLogQuarter implements IStringSerializable
	{
		NORTH_WEST(AxisDirection.NEGATIVE, AxisDirection.NEGATIVE),
		NORTH_EAST(AxisDirection.NEGATIVE, AxisDirection.POSITIVE),
		SOUTH_WEST(AxisDirection.POSITIVE, AxisDirection.NEGATIVE),
		SOUTH_EAST(AxisDirection.POSITIVE, AxisDirection.POSITIVE);
	
		public static final Map<EnumFacing, Map<EnumFacing, EnumLogQuarter>> DIRECTIONS_TO_QUARTER;
		public static final Map<EnumFacing, Map<EnumFacing, Axis>> DIRECTIONS_TO_AXIS;
		public final AxisDirection first_direction;
		public final AxisDirection second_direction;
		
		private EnumLogQuarter(AxisDirection first_direction, AxisDirection second_direction)
		{
			this.first_direction = first_direction;
			this.second_direction = second_direction;
		}
		
		public final EnumFacing getFirstSide(Axis axis)
		{
			return this.first_direction == AxisDirection.NEGATIVE ? axis == Axis.Y ? EnumFacing.NORTH : EnumFacing.DOWN : axis == Axis.Y ? EnumFacing.SOUTH : EnumFacing.UP;
		}
		
		public final EnumFacing getSecondSide(Axis axis)
		{
			return this.second_direction == AxisDirection.NEGATIVE ? axis == Axis.X ? EnumFacing.NORTH : EnumFacing.WEST : axis == Axis.X ? EnumFacing.SOUTH : EnumFacing.EAST;
			
		}
		
		public final String getUnlocalizedName(Axis axis)
		{
			return this.getFirstSide(axis).toString() + "_" + this.getSecondSide(axis).toString();
		}
		
		public final BlockPos offset(BlockPos pos, EnumLogQuarter quarter, Axis axis)
		{
			int x_offset = axis == Axis.X || this.second_direction == quarter.second_direction ? 0 : quarter.second_direction.getOffset();
			int y_offset = axis == Axis.Y ||  this.first_direction == quarter.first_direction ? 0 : quarter.first_direction.getOffset();
			int z_offset = axis == Axis.X ? this.second_direction == quarter.second_direction ? 0 : quarter.second_direction.getOffset() : axis == Axis.Y ? this.first_direction == quarter.first_direction ? 0 : quarter.first_direction.getOffset() : 0;
			return pos.add(x_offset, y_offset, z_offset);
		}
		
		static
		{
			Map<EnumFacing, Map<EnumFacing, EnumLogQuarter>> directions_to_quarter = Maps.newHashMap();
			Map<EnumFacing, Map<EnumFacing, Axis>> directions_to_axis = Maps.newHashMap();
			for(EnumLogQuarter quarter : values())
				for(Axis axis : Axis.values())
				{
					Map<EnumFacing, EnumLogQuarter> quarter_map = directions_to_quarter.getOrDefault(quarter.getFirstSide(axis), Maps.newHashMap());
					quarter_map.put(quarter.getSecondSide(axis), quarter);
					directions_to_quarter.putIfAbsent(quarter.getFirstSide(axis), quarter_map);
					Map<EnumFacing, Axis> axis_map = directions_to_axis.getOrDefault(quarter.getFirstSide(axis), Maps.newHashMap());
					axis_map.put(quarter.getSecondSide(axis), axis);
					directions_to_axis.putIfAbsent(quarter.getFirstSide(axis), axis_map);
				}
			DIRECTIONS_TO_QUARTER = Maps.immutableEnumMap(Maps.transformValues(directions_to_quarter, (value) -> Maps.immutableEnumMap(value)));
			DIRECTIONS_TO_AXIS = Maps.immutableEnumMap(Maps.transformValues(directions_to_axis, (value) -> Maps.immutableEnumMap(value)));
		}
	
		@Override
		public String getName()
		{
			return this.getFirstSide(Axis.Y) + "_" + this.getSecondSide(Axis.Y);
		}
		
		@Nullable
		public static EnumLogQuarter getQuarterFromDirections(EnumFacing side1, EnumFacing side2)
		{
			return side1.getAxis() == side2.getAxis() ? null : DIRECTIONS_TO_QUARTER.containsKey(side1) ? DIRECTIONS_TO_QUARTER.get(side1).get(side2) : DIRECTIONS_TO_QUARTER.getOrDefault(side1, Maps.newHashMap()).get(side2);
		}
		
		@Nullable
		public static Axis getAxisFromDirections(EnumFacing side1, EnumFacing side2)
		{
			return side1.getAxis() == side2.getAxis() ? null : DIRECTIONS_TO_AXIS.containsKey(side1) ? DIRECTIONS_TO_AXIS.get(side1).get(side2) : DIRECTIONS_TO_AXIS.getOrDefault(side1, Maps.newHashMap()).get(side2);
		}
	}
}
