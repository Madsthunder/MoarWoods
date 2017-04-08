package moarwoods.blocks;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoods;
import moarwoods.blocks.living.tree.AbstractPlant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLivingLeaf extends BlockLeaves
{
	public static final PropertyInteger ENERGY = PropertyInteger.create("energy", 0, 7);
	public final IBlockState baseState;
	private int[] surroundings;
	
	public BlockLivingLeaf(IBlockState baseState)
	{
		this.setDefaultState(this.getDefaultState().withProperty(DECAYABLE, true).withProperty(ENERGY, 0));
		this.baseState = baseState;
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return Lists.newArrayList();
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
        if (!world.isRemote)
        {
            if(state.getValue(CHECK_DECAY) && state.getValue(DECAYABLE))
            {
                this.surroundings = this.surroundings == null ? new int[32768] : this.surroundings;
                if(world.isAreaLoaded(pos.add(-5, -5, -5), pos.add(5, 5, 5)))
                {
                    int x = pos.getX();
                    int y = pos.getY();
                    int z = pos.getZ();
                    TObjectIntHashMap<BlockPos> markedtrees = new TObjectIntHashMap<BlockPos>();
                    BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
                    for(int x1 = -4; x1 <= 4; x1++)
                        for(int y2 = -4; y2 <= 4; y2++)
                            for(int y3 = -4; y3 <= 4; y3++)
                            {
                                IBlockState iblockstate = world.getBlockState(blockpos$mutableblockpos.setPos(x + x1, y + y2, z + y3));
                                Block block = iblockstate.getBlock();
                                if(block == this)
                                {
                                	this.surroundings[(x1 + 16) * 1024 + (y2 + 16) * 32 + y3 + 16] = -2;
                                	continue;
                                }
                                else if(block instanceof BlockLivingLog)
                                {
                                	BlockLivingLog log = (BlockLivingLog)block;
                                	if(log.getPlant().getLeafBlock() == this)
                                	{
                                    	BlockPos base = blockpos$mutableblockpos.toImmutable();
                                    	while(world.getBlockState(base.down()).getBlock() == log)
                                    		base = base.down();
                                    	if(markedtrees.contains(base))
                                    	{
                                    		this.surroundings[(x1 + 16) * 1024 + (y2 + 16) * 32 + y3 + 16] = markedtrees.get(base);
                                    		continue;
                                    	}
                                    	Byte seed = MoarWoods.getBlockHistory(world, base);
                                    	IBlockState down = world.getBlockState(base.down());
                                    	if(seed != null && down.getBlock().canSustainPlant(down, world, base.down(), EnumFacing.UP, MoarWoods.SAPLING))
                                    	{
                                    		long[] seeds = new long[3];
                                    		{
                                    			Random random1 = new Random(seed);
                                    			for(int i = 0; i < seeds.length; i++)
                                    				seeds[i] = random1.nextLong();
                                    		}
                                    		if(log.getPlant().getLeaves(world, base, AbstractPlant.getHeight(world, base, log), seeds).contains(pos))
                                    		{
                                    			markedtrees.put(base, 0);
                                        		this.surroundings[(x1 + 16) * 1024 + (y2 + 16) * 32 + y3 + 16] = 0;
                                        		continue;
                                    		}
                                		}
                                		markedtrees.put(base, -1);
                                	}
                                }
                                this.surroundings[(x1 + 16) * 1024 + (y2 + 16) * 32 + y3 + 16] = -1;
                            }
                    for(int i3 = 1; i3 <= 4; i3++)
                        for(int x1 = -4; x1 <= 4; x1++)
                            for(int y1 = -4; y1 <= 4; y1++)
                                for(int z1 = -4; z1 <= 4; ++z1)
                                    if(this.surroundings[(x1 + 16) * 1024 + (y1 + 16) * 32 + z1 + 16] == i3 - 1)
                                    {
                                        if(this.surroundings[(x1 + 16 - 1) * 1024 + (y1 + 16) * 32 + z1 + 16] == -2)
                                            this.surroundings[(x1 + 16 - 1) * 1024 + (y1 + 16) * 32 + z1 + 16] = i3;
                                        if(this.surroundings[(x1 + 16 + 1) * 1024 + (y1 + 16) * 32 + z1 + 16] == -2)
                                            this.surroundings[(x1 + 16 + 1) * 1024 + (y1 + 16) * 32 + z1 + 16] = i3;
                                        if(this.surroundings[(x1 + 16) * 1024 + (y1 + 16 - 1) * 32 + z1 + 16] == -2)
                                            this.surroundings[(x1 + 16) * 1024 + (y1 + 16 - 1) * 32 + z1 + 16] = i3;
                                        if(this.surroundings[(x1 + 16) * 1024 + (y1 + 16 + 1) * 32 + z1 + 16] == -2)
                                            this.surroundings[(x1 + 16) * 1024 + (y1 + 16 + 1) * 32 + z1 + 16] = i3;
                                        if(this.surroundings[(x1 + 16) * 1024 + (y1 + 16) * 32 + (z1 + 16 - 1)] == -2)
                                            this.surroundings[(x1 + 16) * 1024 + (y1 + 16) * 32 + (z1 + 16 - 1)] = i3;
                                        if(this.surroundings[(x1 + 16) * 1024 + (y1 + 16) * 32 + z1 + 16 + 1] == -2)
                                            this.surroundings[(x1 + 16) * 1024 + (y1 + 16) * 32 + z1 + 16 + 1] = i3;
                                    }
                                }
                if(this.surroundings[16912] >= 0)
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
			world.setBlockState(pos, state.withProperty(ENERGY, Math.min(state.getValue(ENERGY) + (!world.isRaining() && 20 - skylight > 0 ? random.nextInt(20 - skylight) == 0 ? 1 : 0 : 1) + (30 - blocklight > 0 ? random.nextInt(30 - blocklight) == 0 ? 1 : 0 : 1), 7)));
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
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { ENERGY, CHECK_DECAY, DECAYABLE });
	}
}
