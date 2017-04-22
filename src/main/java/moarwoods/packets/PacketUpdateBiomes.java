package moarwoods.packets;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateBiomes implements IMessage
{
	private ChunkPos pos;
	private Byte[] changedBiomes;
	
	public PacketUpdateBiomes()
	{
		
	}
	
	public PacketUpdateBiomes(ChunkPos pos, Byte[] changedBiomes)
	{
		this.pos = pos;
		this.changedBiomes = changedBiomes;
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.pos = new ChunkPos(buf.readInt(), buf.readInt());
		int length = buf.readByte() + 129;
		length = buf.readableBytes() <= 0 ? 0 : length;
		int index = 0;
		this.changedBiomes = new Byte[256];
		for(int i = 0; i < length; i++)
		{
			if(length == 256)
				this.changedBiomes[index++] = buf.readByte();
			else
			{
				byte[] array = new byte[2];
				buf.readBytes(array);
				this.changedBiomes[index += (array[0] + 128)] = array[1];
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.pos.chunkXPos);
		buf.writeInt(this.pos.chunkZPos);
		List<byte[]> bytes = Lists.newArrayList();
		int nonnulls = 0;
		int offset = 0;
		for(int i = 0; i < 256; i++)
		{
			Byte biome = this.changedBiomes[i];
			if(biome != null)
			{
				bytes.add(new byte[] { Integer.valueOf(offset - 128).byteValue(), biome });
				offset = 0;
				nonnulls++;
			}
			offset++;
		}
		int size = bytes.size();
		buf.writeByte(Math.max(Byte.MIN_VALUE, size - 129));
		for(byte[] array : bytes)
			if(size == 256)
				buf.writeByte(array[1]);
			else
				buf.writeBytes(array);
	}
	
	public static class Handler implements IMessageHandler<PacketUpdateBiomes, IMessage>
	{

		@Override
		public IMessage onMessage(PacketUpdateBiomes message, MessageContext context)
		{
			Minecraft.getMinecraft().addScheduledTask(() -> 
			{
				WorldClient world = Minecraft.getMinecraft().world;
				Chunk chunk = world.getChunkProvider().provideChunk(message.pos.chunkXPos, message.pos.chunkZPos);
				if(!chunk.isEmpty())
				{
					byte[] chunkBiomeArray = chunk.getBiomeArray();
					for(int i = 0; i < message.changedBiomes.length; i++)
					{
						Byte b = message.changedBiomes[i];
						if(b != null)
							chunkBiomeArray[i] = b;
					}
				}
			});
			return null;
		}
		
	}
}
