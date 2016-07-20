package continuum.moarwoods.blocks;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import continuum.essentials.block.CuboidSelector;
import continuum.essentials.block.IBlockBoundable;
import continuum.essentials.block.ICuboid;
import continuum.essentials.mod.CTMod;
import continuum.moarwoods.cuboids.CraftingGridCuboid;
import continuum.moarwoods.mod.MoarWoods_EH;
import continuum.moarwoods.mod.MoarWoods_OH;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockStateContainer.Builder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCraftingGrid extends Block implements IBlockBoundable
{
	public static final PropertyBool walled = PropertyBool.create("walled");
	public static final PropertyDirection direction = PropertyDirection.create("direction", EnumFacing.Plane.HORIZONTAL);
	private final CTMod<MoarWoods_OH, MoarWoods_EH> mod;
	private AxisAlignedBB bounds = Block.FULL_BLOCK_AABB;
	
	public BlockCraftingGrid(CTMod<MoarWoods_OH, MoarWoods_EH> mod, Material material, SoundType sound, String name)
	{
		super(material);
		this.mod = mod;
		this.setSoundType(sound);
		this.setUnlocalizedName(name + "_grid");
		this.setRegistryName(name + "_grid");
		this.setDefaultState(this.getDefaultState().withProperty(walled, false).withProperty(direction, EnumFacing.NORTH));
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}
	
	@Override
	public boolean hasTileEntity(IBlockState state)
	{
		return true;
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state)
	{
		return new TileEntityCraftingGrid(world.isRemote);
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new Builder(this).add(walled, direction).build();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = this.getDefaultState();
		if(meta >= 4)
		{
			state = state.withProperty(walled, true);
			meta -= 4;
		}
		return state.withProperty(direction, EnumFacing.values()[meta + 2]);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		Integer meta = 0;
		if(state.getValue(walled))
			meta += 4;
		return meta + state.getValue(direction).ordinal() - 2;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack stack, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		TileEntity entity = world.getTileEntity(pos);
		if(!world.isRemote && entity instanceof TileEntityCraftingGrid)
		{
			Object hitInfo = player.rayTrace(player.capabilities.isCreativeMode ? 4.5 : 5, 1).hitInfo;
			TileEntityCraftingGrid grid = (TileEntityCraftingGrid)entity;
			EnumFacing facing = state.getValue(direction);
			if(stack != null && stack.stackSize > 0)
			{
				ItemStack stack1 = stack.copy();
				stack1.stackSize = 1;
				if(!state.getValue(walled))
					if(hitInfo instanceof Integer[])
					{
						ItemStack stack2;
						if((stack2 = grid.getStackInSlot(((Integer[])hitInfo)[facing.ordinal() - 2])) == null)
						{
							if(!player.isCreative())
								stack.stackSize--;
							grid.setInventorySlotContents(((Integer[])hitInfo)[facing.ordinal() - 2], stack1);
							world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, SoundCategory.PLAYERS, 1F, 1F);
						}
						else if(stack2.isItemEqual(stack))
						{
							Integer stacksize = Math.min(stack2.getMaxStackSize(), grid.getInventoryStackLimit());
							if(!player.isCreative())
								stack.stackSize--;
							if(stack2.stackSize < stacksize)
							{
								stack2.stackSize++;
								world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, SoundCategory.PLAYERS, 1F, 1F);
							}
						}
					}
					else if(hitInfo instanceof Integer)
					{
						grid.setInventorySlotContents((Integer)hitInfo, stack1);
					}
					else
						player.openGui(this.mod, 0, world, pos.getX(), pos.getY(), pos.getZ());
				else if(hitInfo instanceof Integer)
				{
					grid.setInventorySlotContents((Integer)hitInfo, stack1);
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEMFRAME_ADD_ITEM, SoundCategory.PLAYERS, 1F, 1F);
				}
				else
					player.openGui(this.mod, 0, world, pos.getX(), pos.getY(), pos.getZ());
				world.notifyBlockUpdate(pos, state, state, 2);
			}
			else if(hitInfo instanceof String)
			{
				if(grid.hasResult() && player.getHeldItemMainhand() == null)
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, grid.getResult());
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ITEMFRAME_REMOVE_ITEM, SoundCategory.PLAYERS, 1F, 1F);
					ItemStack stack2;
					for(Integer i = 0; i < grid.getSizeInventory(); i++)
						if((stack2 = grid.getStackInSlot(i)) != null)
							if((stack2.stackSize -= 1) <= 0)
								grid.setInventorySlotContents(i, null);
					world.notifyBlockUpdate(pos, state, state, 2);
				}
				else
					player.openGui(this.mod, 0, world, pos.getX(), pos.getY(), pos.getZ());
			}
			else if(hitInfo instanceof EnumFacing && facing != hitInfo)
			{
				EnumFacing dir = (EnumFacing)hitInfo;
				IBlockState state1 = state.withProperty(direction, dir);
				TileEntityCraftingGrid grid2 = new TileEntityCraftingGrid();
				CraftingGridCuboids cuboids = CraftingGridCuboids.getCuboidsFromState(state);
				Integer[] indexes;
				for(Integer i = 0; i < grid2.getSizeInventory(); i++)
					grid2.setInventorySlotContents((indexes = (Integer[])cuboids.getGridFromStateAndSlot(state, i).getExtraData())[dir.ordinal() - 2], grid.getStackInSlot(indexes[facing.ordinal() - 2]));
				world.setBlockState(pos, state1);
				world.setTileEntity(pos, grid2);
			}
			else
				player.openGui(this.mod, 0, world, pos.getX(), pos.getY(), pos.getZ());
		}
		return true;
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase entity)
	{
		IBlockState state = this.getDefaultState();
		if(facing.getAxis() == Axis.Y)
			return state.withProperty(direction, entity.getHorizontalFacing().getOpposite());
		else
			return state.withProperty(walled, true).withProperty(direction, facing.getOpposite());
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB aabb, List<AxisAlignedBB> list, Entity entity)
	{
		AxisAlignedBB baseAABB = CraftingGridCuboids.getCuboidsFromState(state).getBaseCuboid().getShowableCuboid().offset(pos);
		if(aabb.intersectsWith(baseAABB))
			list.add(baseAABB);
	}
	
	@Override
	public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d finish)
	{
		List<ICuboid> cuboids = CraftingGridCuboids.getCuboidsFromState(state).getAllCuboids();
		TileEntity entity = world.getTileEntity(pos);
		if(entity instanceof TileEntityCraftingGrid && ((TileEntityCraftingGrid)entity).hasResult())
			cuboids.add(CraftingGridCuboid.OUTPUT);
		return CuboidSelector.getSelectionBox(this, world, pos, start, finish, cuboids);
	}
	
	@Override
	public RayTraceResult rayTrace(BlockPos pos, Vec3d start, Vec3d end, AxisAlignedBB boundingBox)
	{
		return super.rayTrace(pos, start, end, boundingBox);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos)
	{
		return this.bounds;
	}
	
	@Override
	public void setBlockBounds(AxisAlignedBB aabb)
	{
		this.bounds = aabb;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	public enum CraftingGridCuboids
	{
		GROUNDED(CraftingGridCuboid.GROUNDED_BASE, CraftingGridCuboid.GROUNDED_FRAME_NORTH, CraftingGridCuboid.GROUNDED_FRAME_SOUTH, CraftingGridCuboid.GROUNDED_FRAME_WEST, CraftingGridCuboid.GROUNDED_FRAME_EAST, CraftingGridCuboid.GROUNDED_CORNER_NW, CraftingGridCuboid.GROUNDED_CORNER_NE, CraftingGridCuboid.GROUNDED_CORNER_SW, CraftingGridCuboid.GROUNDED_CORNER_SE, CraftingGridCuboid.GROUNDED_GRID$0_0, CraftingGridCuboid.GROUNDED_GRID$0_1, CraftingGridCuboid.GROUNDED_GRID$0_2, CraftingGridCuboid.GROUNDED_GRID$1_0, CraftingGridCuboid.GROUNDED_GRID$1_1, CraftingGridCuboid.GROUNDED_GRID$1_2, CraftingGridCuboid.GROUNDED_GRID$2_0, CraftingGridCuboid.GROUNDED_GRID$2_1, CraftingGridCuboid.GROUNDED_GRID$2_2),
		
		WALLED_NORTH(CraftingGridCuboid.WALLED_BASE_NORTH, CraftingGridCuboid.WALLED_FRAME_NORTH_DOWN, CraftingGridCuboid.WALLED_FRAME_NORTH_UP, CraftingGridCuboid.WALLED_FRAME_NORTH_WEST, CraftingGridCuboid.WALLED_FRAME_NORTH_WEST, CraftingGridCuboid.WALLED_GRID_NORTH$0_0, CraftingGridCuboid.WALLED_GRID_NORTH$0_1, CraftingGridCuboid.WALLED_GRID_NORTH$0_2, CraftingGridCuboid.WALLED_GRID_NORTH$1_0, CraftingGridCuboid.WALLED_GRID_NORTH$1_1, CraftingGridCuboid.WALLED_GRID_NORTH$1_2, CraftingGridCuboid.WALLED_GRID_NORTH$2_0, CraftingGridCuboid.WALLED_GRID_NORTH$2_1, CraftingGridCuboid.WALLED_GRID_NORTH$2_2),
		
		WALLED_SOUTH(CraftingGridCuboid.WALLED_BASE_SOUTH, CraftingGridCuboid.WALLED_FRAME_SOUTH_DOWN, CraftingGridCuboid.WALLED_FRAME_SOUTH_UP, CraftingGridCuboid.WALLED_FRAME_SOUTH_WEST, CraftingGridCuboid.WALLED_FRAME_SOUTH_WEST, CraftingGridCuboid.WALLED_GRID_SOUTH$0_0, CraftingGridCuboid.WALLED_GRID_SOUTH$0_1, CraftingGridCuboid.WALLED_GRID_SOUTH$0_2, CraftingGridCuboid.WALLED_GRID_SOUTH$1_0, CraftingGridCuboid.WALLED_GRID_SOUTH$1_1, CraftingGridCuboid.WALLED_GRID_SOUTH$1_2, CraftingGridCuboid.WALLED_GRID_SOUTH$2_0, CraftingGridCuboid.WALLED_GRID_SOUTH$2_1, CraftingGridCuboid.WALLED_GRID_SOUTH$2_2),
		
		WALLED_WEST(CraftingGridCuboid.WALLED_BASE_WEST, CraftingGridCuboid.WALLED_FRAME_WEST_DOWN, CraftingGridCuboid.WALLED_FRAME_WEST_UP, CraftingGridCuboid.WALLED_FRAME_WEST_NORTH, CraftingGridCuboid.WALLED_FRAME_WEST_SOUTH, CraftingGridCuboid.WALLED_GRID_WEST$0_0, CraftingGridCuboid.WALLED_GRID_WEST$0_1, CraftingGridCuboid.WALLED_GRID_WEST$0_2, CraftingGridCuboid.WALLED_GRID_WEST$1_0, CraftingGridCuboid.WALLED_GRID_WEST$1_1, CraftingGridCuboid.WALLED_GRID_WEST$1_2, CraftingGridCuboid.WALLED_GRID_WEST$2_0, CraftingGridCuboid.WALLED_GRID_WEST$2_1, CraftingGridCuboid.WALLED_GRID_WEST$2_2),
		
		WALLED_EAST(CraftingGridCuboid.WALLED_BASE_EAST, CraftingGridCuboid.WALLED_FRAME_EAST_DOWN, CraftingGridCuboid.WALLED_FRAME_EAST_UP, CraftingGridCuboid.WALLED_FRAME_EAST_NORTH, CraftingGridCuboid.WALLED_FRAME_EAST_SOUTH, CraftingGridCuboid.WALLED_GRID_EAST$0_0, CraftingGridCuboid.WALLED_GRID_EAST$0_1, CraftingGridCuboid.WALLED_GRID_EAST$0_2, CraftingGridCuboid.WALLED_GRID_EAST$1_0, CraftingGridCuboid.WALLED_GRID_EAST$1_1, CraftingGridCuboid.WALLED_GRID_EAST$1_2, CraftingGridCuboid.WALLED_GRID_EAST$2_0, CraftingGridCuboid.WALLED_GRID_EAST$2_1, CraftingGridCuboid.WALLED_GRID_EAST$2_2);
		
		private final CraftingGridCuboid base;
		private final List<CraftingGridCuboid> frame;
		private final List<CraftingGridCuboid> grid = Lists.newArrayListWithCapacity(9);
		
		private CraftingGridCuboids(CraftingGridCuboid... cuboids)
		{
			Integer i = 0;
			this.base = cuboids[i++];
			this.frame = Lists.newArrayListWithCapacity(cuboids.length - 10);
			for (; i < cuboids.length - 9;)
				frame.add(cuboids[i++]);
			for (; i < cuboids.length;)
				grid.add(cuboids[i++]);
		}
		
		public CraftingGridCuboid getBaseCuboid()
		{
			return this.base;
		}
		
		public List<CraftingGridCuboid> getFrameCuboids()
		{
			return this.frame;
		}
		
		public List<CraftingGridCuboid> getGridCuboids()
		{
			return this.grid;
		}
		
		public void addAllCuboidsToList(List<ICuboid> list)
		{
			list.add(this.getBaseCuboid());
			list.addAll(this.getFrameCuboids());
			list.addAll(this.getGridCuboids());
		}
		
		public List<ICuboid> getAllCuboids()
		{
			ArrayList<ICuboid> list = Lists.newArrayListWithCapacity(18);
			this.addAllCuboidsToList(list);
			return list;
		}
		
		public CraftingGridCuboid getGridFromStateAndSlot(IBlockState state, Integer slot)
		{
			Integer index = state.getValue(direction).ordinal() - 2;
			Object extdata;
			if(state.getValue(walled))
				return this.grid.get(slot);
			for (CraftingGridCuboid cuboid : this.grid)
				if((extdata = cuboid.getExtraData()) instanceof Integer[])
				{
					if(((Integer[])extdata)[index] == slot)
						return cuboid;
				}
				else if(extdata instanceof Integer && slot == index)
					return cuboid;
			return null;
		}
		
		public static CraftingGridCuboids getCuboidsFromState(IBlockState state)
		{
			return getCuboidsFromValues(state.getValue(walled), state.getValue(direction));
		}
		
		public static CraftingGridCuboids getCuboidsFromValues(Boolean walled, EnumFacing direction)
		{
			if(walled)
				if(direction == EnumFacing.EAST)
					return WALLED_EAST;
				else if(direction == EnumFacing.WEST)
					return WALLED_WEST;
				else if(direction == EnumFacing.SOUTH)
					return WALLED_SOUTH;
				else
					return WALLED_NORTH;
			else
				return GROUNDED;
		}
	}
}
