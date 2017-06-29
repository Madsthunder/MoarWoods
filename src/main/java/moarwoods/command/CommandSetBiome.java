package moarwoods.command;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;

import moarwoods.MoarWoods;
import moarwoods.packets.PacketUpdateBiomes;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CommandSetBiome extends CommandBase
{

	@Override
	public String getName()
	{
		return "setbiome";
	}

	@Override
	public String getUsage(ICommandSender sender)
	{
		return "commands.moarwoods:setbiome.usage";
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
	{
		if(args.length == 0)
			return Collections.EMPTY_LIST;
		if(args.length == 1)
			return CommandBase.getListOfStringsMatchingLastWord(args, ForgeRegistries.BIOMES.getKeys());
		if(args.length <= 3)
			return CommandBase.getTabCompletionCoordinateXZ(args, 1, pos);
		if(args.length <= 5)
			return CommandBase.getTabCompletionCoordinateXZ(args, 3, pos);
		return Collections.EMPTY_LIST;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
	    System.out.println(Iterables.filter(ForgeRegistries.RECIPES.getKeys(), (Predicate<ResourceLocation>)(key) -> "moarwoods".equals(key.getResourceDomain())));
		if(args.length < 5)
			throw new WrongUsageException(this.getUsage(sender));
		byte biome = (byte)Biome.getIdForBiome(ForgeRegistries.BIOMES.getValue(new ResourceLocation(args[0])));
		World world = sender.getEntityWorld();
		int xstart = "~".equals(args[1]) ? sender.getPosition().getX() : Integer.valueOf(args[1]);
		int zstart = "~".equals(args[2]) ? sender.getPosition().getZ() : Integer.valueOf(args[2]);
		int xend = "~".equals(args[3]) ? sender.getPosition().getX() : Integer.valueOf(args[3]);
		int zend = "~".equals(args[4]) ? sender.getPosition().getZ() : Integer.valueOf(args[4]);
		if(xend < xstart)
		{
			int prevstart = xstart;
			xstart = xend;
			xend = prevstart;
		}
		if(zend < zstart)
		{
			int prevstart = zstart;
			zstart = zend;
			zend = prevstart;
		}
		Map<ChunkPos, Byte[]> toset = Maps.newHashMap();
		for(int x = xstart; x <= xend; x++)
			for(int z = zstart; z <= zend; z++)
			{
				ChunkPos pos = new ChunkPos(new BlockPos(x, 0, z));
				Byte[] array = toset.getOrDefault(pos, new Byte[256]);
				array[(x & 15) + ((z & 15) * 16)] = biome;
				toset.putIfAbsent(pos, array);
			}
		IChunkProvider provider = world.getChunkProvider();
		for(Entry<ChunkPos, Byte[]> entry : toset.entrySet())
		{
			ChunkPos pos = entry.getKey();
			Chunk chunk = provider.provideChunk(pos.x, pos.z);
			Byte[] array = entry.getValue();
			if(!chunk.isEmpty())
			{
				byte[] array1 = chunk.getBiomeArray();
				for(int i = 0; i < 256; i++)
					if(array[i] != null)
						array1[i] = array[i];
			}
			MoarWoods.NETWORK_WRAPPER.sendToDimension(new PacketUpdateBiomes(pos, array), world.provider.getDimension());
		}
	}
	
}
