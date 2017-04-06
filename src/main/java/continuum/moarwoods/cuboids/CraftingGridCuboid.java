package continuum.moarwoods.cuboids;

import org.apache.commons.lang3.tuple.Pair;

import continuum.essentials.block.ICuboid;
import continuum.essentials.block.StaticCuboid;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

public enum CraftingGridCuboid implements ICuboid
{
	GROUNDED_BASE(1, 0, 1, 15, 2, 15, 0, 0, 0, 16, 4, 16),
	GROUNDED_FRAME_NORTH(1, 0, 0, 15, 4, 1, EnumFacing.NORTH),
	GROUNDED_FRAME_SOUTH(1, 0, 15, 15, 4, 16, EnumFacing.SOUTH),
	GROUNDED_FRAME_WEST(0, 0, 1, 1, 4, 15, EnumFacing.WEST),
	GROUNDED_FRAME_EAST(15, 0, 1, 16, 4, 15, EnumFacing.EAST),
	GROUNDED_CORNER_NW(0, 0, 0, 1, 4, 1, 0, 0, 0, 16, 4, 16),
	GROUNDED_CORNER_NE(15, 0, 0, 16, 4, 1, 0, 0, 0, 16, 4, 16),
	GROUNDED_CORNER_SW(0, 0, 15, 1, 4, 16, 0, 0, 0, 16, 4, 16),
	GROUNDED_CORNER_SE(15, 0, 15, 16, 4, 16, 0, 0, 0, 16, 4, 16),
	GROUNDED_GRID$0_0(11, 0, 11, 15, 6, 15, new Integer[] { 0, 8, 2, 6 }),
	GROUNDED_GRID$0_1(6, 0, 11, 10, 6, 15, new Integer[] { 1, 7, 5, 3 }),
	GROUNDED_GRID$0_2(1, 0, 11, 5, 6, 15, new Integer[] { 2, 6, 8, 0 }),
	GROUNDED_GRID$1_0(11, 0, 6, 15, 6, 10, new Integer[] { 3, 5, 1, 7 }),
	GROUNDED_GRID$1_1(6, 0, 6, 10, 6, 10, new Integer[] { 4, 4, 4, 4 }),
	GROUNDED_GRID$1_2(1, 0, 6, 5, 6, 10, new Integer[] { 5, 3, 7, 1 }),
	GROUNDED_GRID$2_0(11, 0, 1, 15, 6, 5, new Integer[] { 6, 2, 0, 8 }),
	GROUNDED_GRID$2_1(6, 0, 1, 10, 6, 5, new Integer[] { 7, 1, 3, 5 }),
	GROUNDED_GRID$2_2(1, 0, 1, 5, 6, 5, new Integer[] { 8, 0, 6, 2 }),
	WALLED_BASE_NORTH(1, 1, 0, 15, 15, 2, 0, 0, 0, 16, 16, 4),
	WALLED_FRAME_NORTH_DOWN(0, 0, 0, 16, 1, 4, 0, 0, 0, 16, 16, 4),
	WALLED_FRAME_NORTH_UP(0, 15, 0, 16, 16, 4, 0, 0, 0, 16, 16, 4),
	WALLED_FRAME_NORTH_WEST(0, 1, 0, 1, 15, 4, 0, 0, 0, 16, 16, 4),
	WALLED_FRAME_NORTH_EAST(15, 1, 0, 16, 15, 4, 0, 0, 0, 16, 16, 4),
	WALLED_GRID_NORTH$0_0(1, 11, 0, 5, 15, 6, 0),
	WALLED_GRID_NORTH$0_1(6, 11, 0, 10, 15, 6, 1),
	WALLED_GRID_NORTH$0_2(11, 11, 0, 15, 15, 6, 2),
	WALLED_GRID_NORTH$1_0(1, 6, 0, 5, 10, 6, 3),
	WALLED_GRID_NORTH$1_1(6, 6, 0, 10, 10, 6, 4),
	WALLED_GRID_NORTH$1_2(11, 6, 0, 15, 10, 6, 5),
	WALLED_GRID_NORTH$2_0(1, 1, 0, 5, 5, 6, 6),
	WALLED_GRID_NORTH$2_1(6, 1, 0, 10, 5, 6, 7),
	WALLED_GRID_NORTH$2_2(11, 1, 0, 15, 5, 6, 8),
	WALLED_BASE_SOUTH(1, 1, 14, 15, 15, 16, 0, 0, 12, 16, 16, 16),
	WALLED_FRAME_SOUTH_DOWN(0, 0, 12, 16, 1, 16, 0, 0, 12, 16, 16, 16),
	WALLED_FRAME_SOUTH_UP(0, 15, 12, 16, 16, 16, 0, 0, 12, 16, 16, 16),
	WALLED_FRAME_SOUTH_WEST(0, 1, 12, 1, 15, 16, 0, 0, 12, 16, 16, 16),
	WALLED_FRAME_SOUTH_EAST(15, 1, 12, 16, 15, 16, 0, 0, 12, 16, 16, 16),
	WALLED_GRID_SOUTH$0_0(11, 11, 10, 15, 15, 16, 0),
	WALLED_GRID_SOUTH$0_1(6, 11, 10, 10, 15, 16, 1),
	WALLED_GRID_SOUTH$0_2(1, 11, 10, 5, 15, 16, 2),
	WALLED_GRID_SOUTH$1_0(11, 6, 10, 15, 10, 16, 3),
	WALLED_GRID_SOUTH$1_1(6, 6, 10, 10, 10, 16, 4),
	WALLED_GRID_SOUTH$1_2(1, 6, 10, 5, 10, 16, 5),
	WALLED_GRID_SOUTH$2_0(11, 1, 10, 15, 5, 16, 6),
	WALLED_GRID_SOUTH$2_1(6, 1, 10, 10, 5, 16, 7),
	WALLED_GRID_SOUTH$2_2(1, 1, 10, 5, 5, 16, 8),
	WALLED_BASE_WEST(0, 1, 1, 2, 15, 15, 0, 0, 0, 4, 16, 16),
	WALLED_FRAME_WEST_DOWN(0, 0, 0, 4, 1, 16, 0, 0, 0, 4, 16, 16),
	WALLED_FRAME_WEST_UP(0, 15, 0, 4, 16, 16, 0, 0, 0, 4, 16, 16),
	WALLED_FRAME_WEST_NORTH(0, 1, 0, 4, 15, 1, 0, 0, 0, 4, 16, 16),
	WALLED_FRAME_WEST_SOUTH(0, 1, 15, 4, 15, 16, 0, 0, 0, 4, 16, 16),
	WALLED_GRID_WEST$0_0(0, 11, 11, 6, 15, 15, 2),
	WALLED_GRID_WEST$0_1(0, 11, 6, 6, 15, 10, 1),
	WALLED_GRID_WEST$0_2(0, 11, 1, 6, 15, 5, 0),
	WALLED_GRID_WEST$1_0(0, 6, 11, 6, 10, 15, 5),
	WALLED_GRID_WEST$1_1(0, 6, 6, 6, 10, 10, 4),
	WALLED_GRID_WEST$1_2(0, 6, 1, 6, 10, 5, 3),
	WALLED_GRID_WEST$2_0(0, 1, 11, 6, 5, 15, 8),
	WALLED_GRID_WEST$2_1(0, 1, 6, 6, 5, 10, 7),
	WALLED_GRID_WEST$2_2(0, 1, 1, 6, 5, 5, 6),
	WALLED_BASE_EAST(14, 0, 0, 16, 15, 15, 12, 0, 0, 16, 16, 16),
	WALLED_FRAME_EAST_DOWN(12, 0, 0, 16, 1, 16, 12, 0, 0, 16, 16, 16),
	WALLED_FRAME_EAST_UP(12, 15, 0, 16, 16, 16, 12, 0, 0, 16, 16, 16),
	WALLED_FRAME_EAST_NORTH(12, 1, 0, 16, 15, 1, 12, 0, 0, 16, 16, 16),
	WALLED_FRAME_EAST_SOUTH(12, 1, 15, 16, 15, 16, 12, 0, 0, 16, 16, 16),
	WALLED_GRID_EAST$0_0(10, 11, 1, 16, 15, 5, 2),
	WALLED_GRID_EAST$0_1(10, 11, 6, 16, 15, 10, 1),
	WALLED_GRID_EAST$0_2(10, 11, 11, 16, 15, 15, 0),
	WALLED_GRID_EAST$1_0(10, 6, 1, 16, 10, 5, 5),
	WALLED_GRID_EAST$1_1(10, 6, 6, 16, 10, 10, 4),
	WALLED_GRID_EAST$1_2(10, 6, 11, 16, 10, 15, 3),
	WALLED_GRID_EAST$2_0(10, 1, 1, 16, 5, 5, 8),
	WALLED_GRID_EAST$2_1(10, 1, 6, 16, 5, 10, 7),
	WALLED_GRID_EAST$2_2(10, 1, 11, 16, 5, 15, 6),
	OUTPUT(5.5, 5.5, 5.5, 10.5, 10.5, 10.5, "out");
	
	private final AxisAlignedBB selectable;
	private final AxisAlignedBB showable;
	private final Object extraData;
	
	private CraftingGridCuboid(Number minX, Number minY, Number minZ, Number maxX, Number maxY, Number maxZ)
	{
		this(minX, minY, minZ, maxX, maxY, maxZ, null);
	}
	
	private CraftingGridCuboid(Number minX, Number minY, Number minZ, Number maxX, Number maxY, Number maxZ, Object extraData)
	{
		this(minX, minY, minZ, maxX, maxY, maxZ, minX, minY, minZ, maxX, maxY, maxZ, extraData);
	}
	
	private CraftingGridCuboid(Number seminX, Number seminY, Number seminZ, Number semaxX, Number semaxY, Number semaxZ, Number shminX, Number shminY, Number shminZ, Number shmaxX, Number shmaxY, Number shmaxZ)
	{
		this(seminX, seminY, seminZ, semaxX, semaxY, semaxZ, shminX, shminY, shminZ, shmaxX, shmaxY, shmaxZ, null);
	}
	
	private CraftingGridCuboid(Number seminX, Number seminY, Number seminZ, Number semaxX, Number semaxY, Number semaxZ, Number shminX, Number shminY, Number shminZ, Number shmaxX, Number shmaxY, Number shmaxZ, Object extraData)
	{
		this.selectable = new AxisAlignedBB(seminX.doubleValue() / 16, seminY.doubleValue() / 16, seminZ.doubleValue() / 16, semaxX.doubleValue() / 16, semaxY.doubleValue() / 16, semaxZ.doubleValue() / 16);
		this.showable = new AxisAlignedBB(shminX.doubleValue() / 16, shminY.doubleValue() / 16, shminZ.doubleValue() / 16, shmaxX.doubleValue() / 16, shmaxY.doubleValue() / 16, shmaxZ.doubleValue() / 16);
		this.extraData = extraData;
	}
	
	@Override
	public AxisAlignedBB getSelectableCuboid()
	{
		return this.selectable;
	}
	
	@Override
	public AxisAlignedBB getShowableCuboid()
	{
		return this.showable;
	}
	
	@Override
	public ICuboid addExtraData(Object obj)
	{
		return this;
	}
	
	@Override
	public Object getExtraData()
	{
		return this.extraData;
	}
	
	@Override
	public ICuboid copy()
	{
		return new StaticCuboid(this);
	}
	
}
