package moarwoods;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = MoarWoods.MODID)
public class MoarWoodsBlockSeeds
{
	protected static final Map<World, Map<ChunkPos, byte[][]>> BLOCK_SEED_CACHE = Maps.newHashMap();
	protected static final Map<World, Map<ChunkPos, NBTTagCompound>> BLOCK_SEED_NBT = Maps.newHashMap();
	
	@SubscribeEvent
	public static void onChunkDataLoad(ChunkDataEvent.Load event)
	{
		Chunk chunk = event.getChunk();
		World world = chunk.getWorld();
		Random random = world.rand;
		NBTTagCompound compound = event.getData().getCompoundTag("moarwoods");
		{
			if(compound.hasKey("block_seeds"))
			{
				Map<ChunkPos, NBTTagCompound> block_history = BLOCK_SEED_NBT.getOrDefault(world, Maps.newHashMap());
				block_history.put(chunk.getPos(), compound.getCompoundTag("block_seeds"));
				BLOCK_SEED_NBT.put(world, block_history);
			}
		}
	}
	
	@SubscribeEvent
	public static void onChunkDataSave(ChunkDataEvent.Save event)
	{
		Chunk chunk = event.getChunk();
		World world = event.getWorld();
		Random random = world.rand;
		NBTTagCompound compound = event.getData().getCompoundTag("moarwoods");
		{
			NBTTagCompound compound1 = BLOCK_SEED_NBT.getOrDefault(world, Maps.newHashMap()).get(chunk.getPos());
			if(compound1 != null)
				compound.setTag("block_seeds", compound1);
		}
		event.getData().setTag("moarwoods", compound);
	}
	
	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event)
	{
		Chunk chunk = event.getChunk();
		World world = chunk.getWorld();
		ChunkPos pos = chunk.getPos();
		BLOCK_SEED_CACHE.getOrDefault(world, Maps.newHashMap()).remove(pos);
		BLOCK_SEED_NBT.getOrDefault(world, Maps.newHashMap()).remove(pos);
	}
	
	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event)
	{
		World world = event.getWorld();
		BLOCK_SEED_CACHE.remove(world);
		BLOCK_SEED_NBT.remove(world);
	}
	
	private static byte[][] getYBlockSeedArray(World world, ChunkPos pos)
	{
		byte[][] y_array = BLOCK_SEED_CACHE.computeIfAbsent(world, (world1) -> Maps.newHashMap()).computeIfAbsent(pos, (pos1) ->
		{
			byte[][] array = new byte[256][256];
			Arrays.fill(array, null);
			return array;
		});
		return y_array;
	}
	
	private static byte[] getXZBlockSeedArray(World world, BlockPos pos)
	{
		ChunkPos cpos = new ChunkPos(pos);
		byte[][] y_array = getYBlockSeedArray(world, cpos);
		int y = pos.getY();
		byte[] xz_array = y_array[y];
		if(xz_array == null)
		{
			NBTTagCompound compound = BLOCK_SEED_NBT.computeIfAbsent(world, (world1) -> Maps.newHashMap()).computeIfAbsent(cpos, (cpos1) -> new NBTTagCompound());
			byte[] array = compound.getByteArray(String.valueOf(y));
			if(array == null)
			{
				Arrays.fill(array = new byte[256], Byte.MIN_VALUE);
				compound.setByteArray(String.valueOf(y), array);
			}
			else if(array.length != 256)
			{
				byte[] array1 = new byte[256];
				if(array.length < 256)
					Arrays.fill(array1, Byte.MIN_VALUE);
				int max = Math.min(256, array.length);
				for(int i = 0; i < max; i++)
					array1[i] = array[i];
				array = array1;
			}
			y_array[y] = xz_array = array;
		}
		return xz_array;
	}
	
	public static byte randomBlockSeed(Random random)
	{
		return Integer.valueOf(random.nextInt(256) - 127).byteValue();
	}

	public static byte getBlockSeed(World world, BlockPos pos)
	{
		if(world.isRemote || pos.getY() > 255 || pos.getY() < 0)
			return Byte.MIN_VALUE;
		byte[] xz_array = getXZBlockSeedArray(world, pos);
		int xz = ((pos.getX() & 15) * 16) + (pos.getZ() & 15);
		if(xz_array[xz] == Byte.MIN_VALUE)
			xz_array[xz] = randomBlockSeed(world.rand);
		return xz_array[xz];
	}

	public static void setBlockSeed(World world, BlockPos pos, byte history)
	{
		if(world.isRemote || pos.getY() > 255 || pos.getY() < 0)
			return;
		getXZBlockSeedArray(world, pos)[((pos.getX() & 15) * 16) + (pos.getZ() & 15)] = history;
	}
}
