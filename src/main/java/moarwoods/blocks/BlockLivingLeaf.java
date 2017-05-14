package moarwoods.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.base.Preconditions;

import moarwoods.blocks.living.tree.IPlant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLivingLeaf extends BlockLeaves
{
	public static final PropertyInteger ENERGY = PropertyInteger.create("energy", 0, 7);
	private final BlockLeaves baseBlock;
	public final IBlockState baseState;
	public final int searchArea;
	private final int[][][] surroundings1;
	
	public BlockLivingLeaf(IBlockState baseState)
	{
		this(baseState, 4);
		this.setDefaultState(this.withEnergy(0, null, null));
	}
	
	public BlockLivingLeaf(IBlockState baseState, int searchArea)
	{
		this.setDefaultState(this.getDefaultState().withProperty(DECAYABLE, true));
		Preconditions.checkArgument(baseState.getBlock() instanceof BlockLeaves, "'%s' must implement net.minecraft.block.BlockLeaves", baseState.getBlock());
		this.baseBlock = (BlockLeaves)baseState.getBlock();
		this.baseState = baseState;
		this.searchArea = searchArea;
		this.surroundings1 = new int[(searchArea * 2) + 1][(searchArea * 2) + 1][(searchArea * 2) + 1];
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return NonNullList.withSize(1, new ItemStack(this, 1, this.baseState.getValue(this.baseBlock instanceof BlockOldLeaf ? BlockOldLeaf.VARIANT : BlockNewLeaf.VARIANT).getMetadata()));
	}
	
	@Override
	public EnumType getWoodType(int meta)
	{
		return null;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(CHECK_DECAY) ? 8 : 0) + (state.getValue(ENERGY));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(CHECK_DECAY, meta >= 8).withProperty(ENERGY, meta & 7);
	}
	
	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		if(!world.isRemote)
		{
			if(state.getValue(CHECK_DECAY) && state.getValue(DECAYABLE))
			{
				if(world.isAreaLoaded(pos.add(-this.searchArea - 1, -this.searchArea - 1, -this.searchArea - 1), pos.add(this.searchArea + 1, this.searchArea + 1, this.searchArea + 1)))
				{
					for(int x = 0; x <= this.searchArea * 2; x++)
						for(int y = 0; y <= this.searchArea * 2; y++)
							for(int z = 0; z <= this.searchArea * 2; z++)
							{
								int i;
								IBlockState state1 = world.getBlockState(pos.add(x - this.searchArea, y - this.searchArea, z - this.searchArea));
								Block block = state1.getBlock();
								if(block == this)
									i = -2;
								else if(block instanceof BlockLivingLog)
								{
									IPlant plant = ((BlockLivingLog)block).getPlant();
									i = plant != null && plant.getLeafBlock() == this ? 4 : -1;
								}
								else
									i = -1;
								this.surroundings1[x][y][z] = i;
							}
					for(int i = 4; i >= 1; i--)
						for(int x = 0; x <= this.searchArea * 2; x++)
							for(int y = 0; y <= this.searchArea * 2; y++)
								for(int z = 0; z <= this.searchArea * 2; z++)
									if(this.surroundings1[x][y][z] == i)
									{
										if(--x >= 0 && this.surroundings1[x][y][z] == -2)
											this.surroundings1[x][y][z] = i - 1;
										x++;
										if(this.surroundings1.length > ++x && this.surroundings1[x][y][z] == -2)
											this.surroundings1[x][y][z] = i - 1;
										x--;
										if(--y >= 0 && this.surroundings1[x][y][z] == -2)
											this.surroundings1[x][y][z] = i - 1;
										y++;
										if(this.surroundings1[x].length > ++y && this.surroundings1[x][y][z] == -2)
											this.surroundings1[x][y][z] = i - 1;
										y--;
										if(--z >= 0 && this.surroundings1[x][y][z] == -2)
											this.surroundings1[x][y][z] = i - 1;
										z++;
										if(this.surroundings1[x][y].length > ++z && this.surroundings1[x][y][z] == -2)
											this.surroundings1[x][y][z] = i - 1;
										z--;
									}
					/**
					 * for(int i3 = 1; i3 <= 4; i3++) for(int x1 = -4; x1 <= 4;
					 * x1++) for(int y1 = -4; y1 <= 4; y1++) for(int z1 = -4; z1
					 * <= 4; ++z1) if(this.surroundings[(x1 + 16) * 1024 + (y1 +
					 * 16) * 32 + z1 + 16] == i3 - 1) { if(this.surroundings[(x1
					 * + 16 - 1) * 1024 + (y1 + 16) * 32 + z1 + 16] == -2)
					 * this.surroundings[(x1 + 16 - 1) * 1024 + (y1 + 16) * 32 +
					 * z1 + 16] = i3; if(this.surroundings[(x1 + 16 + 1) * 1024
					 * + (y1 + 16) * 32 + z1 + 16] == -2) this.surroundings[(x1
					 * + 16 + 1) * 1024 + (y1 + 16) * 32 + z1 + 16] = i3;
					 * if(this.surroundings[(x1 + 16) * 1024 + (y1 + 16 - 1) *
					 * 32 + z1 + 16] == -2) this.surroundings[(x1 + 16) * 1024 +
					 * (y1 + 16 - 1) * 32 + z1 + 16] = i3;
					 * if(this.surroundings[(x1 + 16) * 1024 + (y1 + 16 + 1) *
					 * 32 + z1 + 16] == -2) this.surroundings[(x1 + 16) * 1024 +
					 * (y1 + 16 + 1) * 32 + z1 + 16] = i3;
					 * if(this.surroundings[(x1 + 16) * 1024 + (y1 + 16) * 32 +
					 * (z1 + 16 - 1)] == -2) this.surroundings[(x1 + 16) * 1024
					 * + (y1 + 16) * 32 + (z1 + 16 - 1)] = i3;
					 * if(this.surroundings[(x1 + 16) * 1024 + (y1 + 16) * 32 +
					 * z1 + 16 + 1] == -2) this.surroundings[(x1 + 16) * 1024 +
					 * (y1 + 16) * 32 + z1 + 16 + 1] = i3; }
					 */
				}
				if(this.surroundings1[this.searchArea][this.searchArea][this.searchArea] >= 0)
					world.setBlockState(pos, state = state.withProperty(CHECK_DECAY, false), 4);
				else
				{
					this.dropBlockAsItem(world, pos, state, 0);
					world.setBlockToAir(pos);
					return;
				}
			}
		}
		
		if(!world.isRemote)
		{
			int skylight = world.getLightFor(EnumSkyBlock.SKY, pos);
			int blocklight = world.getLightFor(EnumSkyBlock.BLOCK, pos);
			world.setBlockState(pos, this.incrementEnergy((!world.isRaining() && 20 - skylight > 0 ? random.nextInt(20 - skylight) == 0 ? 1 : 0 : 1) + (30 - blocklight > 0 ? random.nextInt(30 - blocklight) == 0 ? 1 : 0 : 1), state, world, pos));
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return this.baseState == null ? false : this.baseState.isOpaqueCube();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return this.baseState.getBlock().getBlockLayer();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side)
	{
		return this.baseState.shouldSideBeRendered(access, pos, side);
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return this.baseState.getValue(this.baseBlock instanceof BlockOldLeaf ? BlockOldLeaf.VARIANT : BlockNewLeaf.VARIANT).getMetadata();
	}
	
	public int getEnergy(IBlockState state, @Nullable IBlockAccess access, @Nullable BlockPos pos)
	{
		return state.getValue(ENERGY);
	}
	
	public int getMaxEnergy(@Nullable IBlockAccess access, @Nullable BlockPos pos)
	{
		return 7;
	}
	
	public IBlockState withEnergy(int energy, @Nullable IBlockAccess access, @Nullable BlockPos pos)
	{
		return this.getDefaultState().withProperty(ENERGY, Math.max(0, Math.min(this.getMaxEnergy(access, pos), energy)));
	}
	
	public IBlockState incrementEnergy(int i, IBlockState state, @Nullable IBlockAccess access, @Nullable BlockPos pos)
	{
		return this.withEnergy(this.getEnergy(state, access, pos) + i, access, pos);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { ENERGY, CHECK_DECAY, DECAYABLE });
	}
}
