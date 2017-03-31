package moarwoods;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import moarwoods.blocks.BlockLiveLeaves;
import moarwoods.blocks.BlockLiveLog;
import moarwoods.capability.CapabilityFarmer;
import moarwoods.client.renderers.entity.RenderVillagerWrapper;
import moarwoods.entity.ai.EntityAIRunAroundLikeCrazyWrapper;
import moarwoods.entity.ai.EntityAITameHorse;
import moarwoods.villagers.VillagerFarmer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber
@Mod(modid = MoarWoods.MODID, name = MoarWoods.NAME, version = MoarWoods.NAME)
public class MoarWoods
{
	public static final String MODID = "moarwoods";
	public static final String NAME = "MoarWoods";
	public static final String VERSION = "0.0.0";
	
	@Mod.EventHandler
	public void construction(FMLConstructionEvent event)
	{
		Proxy.I.constr();
	}
	
	@Mod.EventHandler
	public void pre(FMLPreInitializationEvent event)
	{
		Proxy.I.pre();
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		Proxy.I.init();
	}
	
	@Mod.EventHandler
	public void post(FMLPostInitializationEvent event)
	{
		Proxy.I.post();
	}
	
	@Mod.EventHandler
	public void serverStopping(FMLServerAboutToStartEvent event)
	{
		BLOCK_HISTORY.clear();
	}
	
	@Mod.EventHandler
	public void serverStopping(FMLServerStoppingEvent event)
	{
		BLOCK_HISTORY.clear();
	}
	
	public static class Proxy
	{
		@SidedProxy
		public static Proxy I = null;
		
		public void constr()
		{
			MinecraftForge.TERRAIN_GEN_BUS.register(TerrainGenEventHandler.class);
		}
		
		public void pre()
		{
			VillagerFarmer.addItems(FARMER, FARMER.getCareer(0), Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, Items.WHEAT, Items.BEETROOT, Items.POTATO, Items.POISONOUS_POTATO, Items.CARROT);
			VillagerFarmer.addItems(FARMER, FARMER.getCareer(1), Items.FISH);
		}
		
		public void init()
		{
			
		}
		
		public void post()
		{
			
		}
		
		@SideOnly(Side.CLIENT)
		public static class ClientProxy extends Proxy
		{
			@Override
			public void pre()
			{
				ModelLoader.setCustomStateMapper(LIVE_BIRCH_LOG, createLogStateMapper());
				ModelLoader.setCustomStateMapper(LIVE_BIRCH_LEAVES, createLeavesStateMapper());
			}
			
			@Override
			public void init()
			{
				super.init();
				RenderManager manager = Minecraft.getMinecraft().getRenderManager();
				manager.entityRenderMap.replace(EntityVillager.class, new RenderVillagerWrapper(manager));
				Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((state, access, pos, tintIndex) -> ColorizerFoliage.getFoliageColorBirch(), LIVE_BIRCH_LEAVES);
			}
			
			private static IStateMapper createLogStateMapper()
			{
				return new StateMapperBase()
				{
					
					@Override
					protected ModelResourceLocation getModelResourceLocation(IBlockState state)
					{
						return new ModelResourceLocation(state.getBlock().getRegistryName(), "axis=" + state.getValue(BlockLog.LOG_AXIS));
					}
					
				};
			}
			
			private static IStateMapper createLeavesStateMapper()
			{
				return new StateMapperBase()
				{
					
					@Override
					protected ModelResourceLocation getModelResourceLocation(IBlockState state)
					{
						return new ModelResourceLocation(state.getBlock().getRegistryName(), "normal");
					}
					
				};
			}
		}
		
		@SideOnly(Side.SERVER)
		public static class ServerProxy extends Proxy
		{
			
		}
	}
	
	@GameRegistry.ObjectHolder("moarwoods:live_birch_log")
	public static final BlockLiveLog LIVE_BIRCH_LOG = null;
	@GameRegistry.ObjectHolder("moarwoods:live_birch_leaves")
	public static final BlockLiveLeaves LIVE_BIRCH_LEAVES = null;
	
	@GameRegistry.ObjectHolder("minecraft:farmer")
	public static final VillagerProfession FARMER = null;
	
	@SideOnly(Side.CLIENT)
	private static LayerVillagerArmor VILLAGER_ARMOR_LAYER;
	
	@SubscribeEvent
	public static void onBlocksRegister(RegistryEvent.Register<Block> event)
	{
		List<Block> blocks = Lists.newArrayList();
		{
			blocks.add(new BlockLiveLog().setRegistryName("moarwoods:live_birch_log"));
		}
		{
			blocks.add(new BlockLiveLeaves(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH)).setRegistryName("moarwoods:live_birch_leaves"));
		}
		event.getRegistry().registerAll(Iterables.toArray(blocks, Block.class));
	}
	
	@SubscribeEvent
	public static void onVillagersRegisterRegister(RegistryEvent.Register<VillagerProfession> event)
	{
		List<VillagerProfession> professions = Lists.newArrayList();
		{
			VillagerProfession profession = new VillagerProfession("moarwoods:soldier", "moarwoods:textures/entity/villager/soldier.png", "moarwoods:textures/entity/zombie_villager/zombie_soldier.png");
			new VillagerCareer(profession, "moarwoods:guard");
			new VillagerCareer(profession, "moarwoods:soldier");
			professions.add(profession);
		}
		{
			VillagerProfession profession = new VillagerProfession("moarwoods:miner", "moarwoods:textrues/entity/villager/miner.png", "moarwoods:textures/entity/zombie_miner.png");
			new VillagerCareer(profession, "moarwoods:miner");
			professions.add(profession);
		}
		event.getRegistry().registerAll(Iterables.toArray(professions, VillagerProfession.class));
	}
	
	@SubscribeEvent
	public static void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		Entity entity = event.getEntity();
		World world = event.getWorld();
		if(entity instanceof EntityZombieVillager)
		{
		}
		if(entity instanceof EntityVillager)
		{
			((EntityVillager)entity).setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
			EntityVillager villager = (EntityVillager)entity;
			EntityAITasks tasks = villager.tasks;
			for(EntityAITaskEntry entry : Sets.newHashSet(tasks.taskEntries))
				if(entry.action.getClass() == EntityAIHarvestFarmland.class)
					tasks.removeTask(entry.action);
			tasks.addTask(1, new EntityAITameHorse(villager));
			tasks.addTask(1, new EntityAIMoveTowardsTarget(villager, .5, 16F)
			{
				@Override
				public boolean shouldExecute()
				{
					if(super.shouldExecute())
					{
						try
						{
							ReflectionHelper.findField(EntityAIMoveTowardsTarget.class, "movePosX").set(this, villager.getAttackTarget().posX);
							ReflectionHelper.findField(EntityAIMoveTowardsTarget.class, "movePosY").set(this, villager.getAttackTarget().posY);
							ReflectionHelper.findField(EntityAIMoveTowardsTarget.class, "movePosZ").set(this, villager.getAttackTarget().posZ);
						}
						catch(Exception exception)
						{
							exception.printStackTrace();
						}
						return true;
					}
					return false;
				}
			});
		}
		else if(entity instanceof AbstractHorse)
		{
			AbstractHorse horse = (AbstractHorse)entity;
			EntityAITasks tasks = horse.tasks;
			for(EntityAITaskEntry entry : Sets.newHashSet(tasks.taskEntries))
				if(entry.action.getClass() == EntityAIRunAroundLikeCrazy.class)
				{
					tasks.removeTask(entry.action);
					tasks.addTask(1, new EntityAIRunAroundLikeCrazyWrapper(horse, 1.2));
				}
			
		}
	}
	
	private static final Map<World, Map<ChunkPos, byte[]>> BLOCK_HISTORY = Maps.newHashMap();
	
	@SubscribeEvent
	public static void onChunkDataLoad(ChunkDataEvent.Load event)
	{
		Chunk chunk = event.getChunk();
		World world = chunk.getWorld();
		Random random = world.rand;
		NBTTagCompound compound = event.getData().getCompoundTag("moarwoods");
		{
			byte[] array;
			if(compound.hasKey("block_history", 7))
				array = compound.getByteArray("block_history");
			else
			{
				array = new byte[65536];
				for(int i = 0; i < array.length; i++)
					array[i] = ((Integer)(random.nextInt(256) - 128)).byteValue();
			}
			if(array.length < 65536)
			{
				byte[] newArray = new byte[65536];
				for(int i = 0; i < array.length; i++)
					newArray[i] = array[i];
				for(int i = array.length; i < newArray.length; i++)
					newArray[i] = ((Integer)(random.nextInt(256) - 128)).byteValue();
				array = newArray;
			}
			Map<ChunkPos, byte[]> block_history = BLOCK_HISTORY.getOrDefault(world, Maps.newHashMap());
			block_history.put(chunk.getPos(), array);
			BLOCK_HISTORY.put(world, block_history);
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
			byte[] array;
			Map<ChunkPos, byte[]> block_history = BLOCK_HISTORY.getOrDefault(world, Maps.newHashMap());
			ChunkPos pos = chunk.getPos();
			if(block_history.containsKey(pos))
				array = block_history.get(pos);
			else
			{
				array = new byte[65536];
				for(int i = 0; i < array.length; i++)
					array[i] = ((Integer)(random.nextInt(256) - 128)).byteValue();
			}
			if(array.length < 65536)
			{
				byte[] newArray = new byte[65536];
				for(int i = 0; i < array.length; i++)
					newArray[i] = array[i];
				for(int i = array.length; i < newArray.length; i++)
					newArray[i] = ((Integer)(random.nextInt(256) - 128)).byteValue();
				array = newArray;
			}
			compound.setByteArray("block_history", array);
		}
	}
	
	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event)
	{
		Chunk chunk = event.getChunk();
		World world = chunk.getWorld();
		BLOCK_HISTORY.getOrDefault(world, Maps.newHashMap()).remove(new ChunkPos(chunk.xPosition, chunk.zPosition));
	}
	
	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event)
	{
		BLOCK_HISTORY.remove(event.getWorld());
	}
	
	@Nullable
	public static Byte getBlockHistory(IBlockAccess world, BlockPos pos)
	{
		Map<ChunkPos, byte[]> block_history = BLOCK_HISTORY.getOrDefault(world, Maps.newHashMap());
		ChunkPos cpos = new ChunkPos(pos);
		if(pos.getY() > 255 || pos.getY() < 0 || !block_history.containsKey(cpos))
			return null;
		return block_history.get(cpos)[(pos.getY() * 256) + ((pos.getX() & 15) * 16) + pos.getZ() & 15];
	}
	
	public static void setBlockHistory(World world, BlockPos pos, byte history)
	{
		Map<ChunkPos, byte[]> block_history = BLOCK_HISTORY.getOrDefault(world, Maps.newHashMap());
		ChunkPos cpos = new ChunkPos(pos);
		if(pos.getY() <= 255 && pos.getY() >= 0 && block_history.containsKey(cpos))
			block_history.get(cpos)[(pos.getY() * 256) + ((pos.getX() & 15) * 16) + pos.getZ() & 15] = history;
	}
	
	@SubscribeEvent
	public static void onEntityCapabilitiesAttach(AttachCapabilitiesEvent<Entity> event)
	{
		Entity entity = event.getObject();
		if(entity instanceof EntityVillager)
		{
			EntityVillager villager = (EntityVillager)entity;
			event.addCapability(CapabilityFarmer.NAME, new CapabilityFarmer(villager));
		}
	}
	
	public static class TerrainGenEventHandler
	{
		@SubscribeEvent
		public static void onSaplingGrow(SaplingGrowTreeEvent event)
		{
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			IBlockState state = world.getBlockState(pos);
			if(state.getBlock() == Blocks.SAPLING && state.getValue(BlockSapling.TYPE) == BlockPlanks.EnumType.BIRCH)
			{
				if(checkIfCanGrow(world, pos, 0) && checkIfCanGrow(world, pos.up(), 1) && checkIfCanGrow(world, pos.up(2), 0))
				{
					world.setBlockState(pos, LIVE_BIRCH_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y));
					world.setBlockState(pos.up(), LIVE_BIRCH_LOG.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y));
					IBlockState leaves = LIVE_BIRCH_LEAVES.getDefaultState();
					world.setBlockState(pos.up(2), leaves);
					world.setBlockState(pos.up().north(), leaves);
					world.setBlockState(pos.up().south(), leaves);
					world.setBlockState(pos.up().west(), leaves);
					world.setBlockState(pos.up().east(), leaves);
					Random random = new Random(getBlockHistory(world, pos.down()));
					setBlockHistory(world, pos, Integer.valueOf(random.nextInt(256) - 128).byteValue());
					setBlockHistory(world, pos.up(), Integer.valueOf(random.nextInt(256) - 128).byteValue());
					setBlockHistory(world, pos.down(), Integer.valueOf(world.rand.nextInt(256) - 128).byteValue());
				}
				event.setResult(Result.DENY);
			}
		}
		
		public static boolean checkIfCanGrow(World world, BlockPos pos, int radius)
		{
			boolean flag = true;
			BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
			for(int l = pos.getX() - radius; l <= pos.getX() + radius && flag; l++)
				for(int i1 = pos.getZ() - radius; i1 <= pos.getZ() + radius && flag; i1++)
					if((pos.getY() < 0 && pos.getY() >= world.getHeight()) || !canGrowInto(world, blockpos$mutableblockpos.setPos(l, pos.getY(), i1).toImmutable()))
						flag = false;
			return flag;
		}
		
		public static boolean canGrowInto(World world, BlockPos pos)
		{
			IBlockState state = world.getBlockState(pos);
			Block block = state.getBlock();
			return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos) || state.getBlock().isWood(world, pos) || block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.SAPLING || block == Blocks.VINE;
		}
	}
}
