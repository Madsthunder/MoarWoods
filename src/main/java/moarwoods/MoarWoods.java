package moarwoods;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import moarwoods.blocks.living.tree.AbstractPlant;
import moarwoods.blocks.living.tree.BirchTree;
import moarwoods.blocks.living.tree.IPlant;
import moarwoods.blocks.living.tree.SmallJungleTree;
import moarwoods.blocks.living.tree.SmallOakTree;
import moarwoods.blocks.living.tree.SmallSpruceTree;
import moarwoods.capability.CapabilityFarmer;
import moarwoods.client.renderers.entity.RenderVillagerWrapper;
import moarwoods.command.CommandSetBiome;
import moarwoods.entity.ai.EntityAIRunAroundLikeCrazyWrapper;
import moarwoods.entity.ai.EntityAITameHorse;
import moarwoods.packets.PacketUpdateBiomes;
import moarwoods.villagers.VillagerFarmer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLeaf;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerVillagerArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHarvestFarmland;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
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
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
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
	public void serverAboutToStart(FMLServerAboutToStartEvent event)
	{
		BLOCK_HISTORY.clear();
	}
	
	@Mod.EventHandler
	public void serverStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandSetBiome());
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
			NETWORK_WRAPPER.registerMessage(PacketUpdateBiomes.Handler.class, PacketUpdateBiomes.class, 0, Side.CLIENT);
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
				super.pre();
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_OAK_LOG, createLogStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_SPRUCE_LOG, createLogStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_BIRCH_LOG, createLogStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_JUNGLE_LOG, createLogStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_ACACIA_LOG, createLogStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_DARKOAK_LOG, createLogStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_OAK_LEAF, createLeafStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_SPRUCE_LEAF, createLeafStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_BIRCH_LEAF, createLeafStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_JUNGLE_LEAF, createLeafStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_ACACIA_LEAF, createLeafStateMapper());
				ModelLoader.setCustomStateMapper(ObjectReferences.LIVING_DARKOAK_LEAF, createLeafStateMapper());
			}
			
			@Override
			public void init()
			{
				super.init();
				RenderManager manager = Minecraft.getMinecraft().getRenderManager();
				manager.entityRenderMap.replace(EntityVillager.class, new RenderVillagerWrapper(manager));
				{
					BlockColors colors = Minecraft.getMinecraft().getBlockColors();
					{
						IBlockColor default_color = (state, access, pos, tintIndex) ->
						{
							int c = 195 + (20 * state.getValue(BlockLivingLog.DEATH_STAGE));
							return (c * 65536) + 65280 + c;
						};
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_OAK_LOG);
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_SPRUCE_LOG);
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_BIRCH_LOG);
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_JUNGLE_LOG);
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_ACACIA_LOG);
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_DARKOAK_LOG);
						
					}
					{
						IBlockColor default_color = (state, access, pos, tintIndex) -> access != null && pos != null ? BiomeColorHelper.getFoliageColorAtPos(access, pos) : ColorizerFoliage.getFoliageColorBasic();
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_OAK_LEAF);
						colors.registerBlockColorHandler((state, access, pos, tintIndex) -> ColorizerFoliage.getFoliageColorPine(), ObjectReferences.LIVING_SPRUCE_LEAF);
						colors.registerBlockColorHandler((state, access, pos, tintIndex) -> ColorizerFoliage.getFoliageColorBirch(), ObjectReferences.LIVING_BIRCH_LEAF);
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_JUNGLE_LEAF);
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_ACACIA_LEAF);
						colors.registerBlockColorHandler(default_color, ObjectReferences.LIVING_DARKOAK_LEAF);
					}
				}
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
			
			private static IStateMapper createLeafStateMapper()
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
	
	public static final SimpleNetworkWrapper NETWORK_WRAPPER = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

@ObjectHolder(MoarWoods.MODID)
	public static class ObjectReferences
	{

		@GameRegistry.ObjectHolder("minecraft:sapling")
		public static final BlockSapling SAPLING = null;
		public static final BlockLivingLog LIVING_OAK_LOG = null;
		public static final BlockLivingLog LIVING_SPRUCE_LOG = null;
		public static final BlockLivingLog LIVING_BIRCH_LOG = null;
		public static final BlockLivingLog LIVING_JUNGLE_LOG = null;
		public static final BlockLivingLog LIVING_ACACIA_LOG = null;
		public static final BlockLivingLog LIVING_DARKOAK_LOG = null;
		public static final BlockLivingLeaf LIVING_OAK_LEAF = null;
		public static final BlockLivingLeaf LIVING_SPRUCE_LEAF = null;
		public static final BlockLivingLeaf LIVING_BIRCH_LEAF = null;
		public static final BlockLivingLeaf LIVING_JUNGLE_LEAF = null;
		public static final BlockLivingLeaf LIVING_ACACIA_LEAF = null;
		public static final BlockLivingLeaf LIVING_DARKOAK_LEAF = null;
		
	}
	@GameRegistry.ObjectHolder("minecraft:farmer")
	public static final VillagerProfession FARMER = null;
	
	@SideOnly(Side.CLIENT)
	private static LayerVillagerArmor VILLAGER_ARMOR_LAYER;
	
	@SubscribeEvent
	public static void onBlocksRegister(RegistryEvent.Register<Block> event)
	{
		List<Block> blocks = Lists.newArrayList();
		{
			blocks.add(new BlockLivingLog(new SmallOakTree()).setRegistryName("moarwoods:living_oak_log"));
			blocks.add(new BlockLivingLog(new SmallSpruceTree()).setRegistryName("moarwoods:living_spruce_log"));
			blocks.add(new BlockLivingLog(new BirchTree()).setRegistryName("moarwoods:living_birch_log"));
			blocks.add(new BlockLivingLog(new SmallJungleTree()).setRegistryName("moarwoods:living_jungle_log"));
			blocks.add(new BlockLivingLog(null).setRegistryName("moarwoods:living_acacia_log"));
			blocks.add(new BlockLivingLog(null).setRegistryName("moarwoods:living_darkoak_log"));
		}
		{
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK)).setRegistryName("moarwoods:living_oak_leaf"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE)).setRegistryName("moarwoods:living_spruce_leaf"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH)).setRegistryName("moarwoods:living_birch_leaf"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE)).setRegistryName("moarwoods:living_jungle_leaf"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.ACACIA)).setRegistryName("moarwoods:living_acacia_leaf"));
			blocks.add(new BlockLivingLeaf(Blocks.LEAVES2.getDefaultState().withProperty(BlockNewLeaf.VARIANT, BlockPlanks.EnumType.DARK_OAK)).setRegistryName("moarwoods:living_darkoak_leaf"));
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
		if(entity instanceof EntityZombie)
		{
			((EntityLivingBase)entity).setItemStackToSlot(EntityEquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
			
		}
		if(entity instanceof EntityVillager)
		{
			EntityVillager villager = (EntityVillager)entity;
			NBTTagCompound display = new NBTTagCompound();
			int color = new Color(255, 0, 0).getRGB();
			ItemStack stack = new ItemStack(Items.LEATHER_HELMET);
			stack.getOrCreateSubCompound("display").setInteger("color", color);
			villager.setItemStackToSlot(EntityEquipmentSlot.HEAD, stack);
			stack = new ItemStack(Items.LEATHER_CHESTPLATE);
			stack.getOrCreateSubCompound("display").setInteger("color", color);
			villager.setItemStackToSlot(EntityEquipmentSlot.CHEST, stack);
			stack = new ItemStack(Items.LEATHER_LEGGINGS);
			stack.getOrCreateSubCompound("display").setInteger("color", color);
			villager.setItemStackToSlot(EntityEquipmentSlot.LEGS, stack);
			stack = new ItemStack(Items.LEATHER_BOOTS);
			stack.getOrCreateSubCompound("display").setInteger("color", color);
			villager.setItemStackToSlot(EntityEquipmentSlot.FEET, stack);
			villager.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
			stack = new ItemStack(Items.SHIELD, 1, 0);
			NBTTagList patterns = new NBTTagList();
			NBTTagCompound pattern = new NBTTagCompound();
			pattern.setString("Pattern", BannerPattern.SKULL.getHashname());
			pattern.setInteger("Color", color);
			patterns.appendTag(pattern);
			stack.getOrCreateSubCompound("BlockEntityTag").setInteger("Base", 15 - EnumDyeColor.CYAN.getMetadata());
			stack.getOrCreateSubCompound("BlockEntityTag").setTag("Patterns", patterns);
			villager.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
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
					villager.setActiveHand(EnumHand.MAIN_HAND);
					if(villager.getActiveItemStack().getMaxItemUseDuration() - villager.getItemInUseCount() >= 30 && !world.playerEntities.isEmpty())
					{
						EntityPlayer player = world.playerEntities.get(0);
						if(player != null)
						{
							EntityArrow arrow = new EntityTippedArrow(villager.world, villager);
							double d0 = player.posX - villager.posX;
							double d1 = player.getEntityBoundingBox().minY + player.height / 3.0F - arrow.posY;
							double d2 = player.posZ - villager.posZ;
							double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
							arrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, 14 - villager.world.getDifficulty().getDifficultyId() * 4);
							villager.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (villager.getRNG().nextFloat() * 0.4F + 0.8F));
							villager.world.spawnEntity(arrow);
							villager.resetActiveHand();
						}
					}
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
					array[i] = Integer.valueOf(random.nextInt()).byteValue();
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
			BLOCK_HISTORY.putIfAbsent(world, block_history);
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
	public static void onChunkLoad(ChunkEvent.Load event)
	{
		World world = event.getWorld();
		if(!world.isRemote)
		{
			ChunkPos pos = event.getChunk().getPos();
			Map<ChunkPos, byte[]> map = BLOCK_HISTORY.getOrDefault(world, Maps.newHashMap());
			if(!map.containsKey(pos))
			{
				Random random = world.rand;
				byte[] array = new byte[65536];
				for(int i = 0; i < array.length; i++)
					array[i] = Integer.valueOf(random.nextInt()).byteValue();
				map.put(pos, array);
			}
			BLOCK_HISTORY.putIfAbsent(world, map);
		}
	}
	
	@SubscribeEvent
	public static void onChunkUnload(ChunkEvent.Unload event)
	{
		Chunk chunk = event.getChunk();
		BLOCK_HISTORY.getOrDefault(chunk.getWorld(), Maps.newHashMap()).remove(chunk.getPos());
	}
	
	@SubscribeEvent
	public static void onWorldUnload(WorldEvent.Unload event)
	{
		BLOCK_HISTORY.remove(event.getWorld());
	}
	
	@SubscribeEvent
	public static void onBonemealUse(BonemealEvent event)
	{
		IBlockState state = event.getBlock();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		if(state.getBlock() instanceof BlockLivingLog)
		{
			BlockLivingLog block = (BlockLivingLog)state.getBlock();
			IPlant plant = block.getPlant();
			if(plant != null && !plant.grow(world, pos, false) && AbstractPlant.isBase(world, pos, block))
			{
				long[] seeds = plant.getSeeds(world, pos);
				if(seeds != null)
				{
					int height = AbstractPlant.getHeight(world, pos, block);
					int total_energy;
					TObjectIntHashMap<BlockPos> energy_sources;
					{
						Pair<Integer, TObjectIntHashMap<BlockPos>> pair = AbstractPlant.getTotalEnergy(world, pos, plant.getLeafSearchRadius(world, pos, height, seeds), height + plant.getLeafSearchExtraHeight(world, pos, height, seeds), plant.getLeafBlock());
						total_energy = pair.getLeft();
						energy_sources = pair.getRight();
					}
					if(total_energy >= 1)
					{
						List<BlockPos> leaves = plant.getLeaves(world, pos, height, seeds);
						if(!leaves.isEmpty())
						{
							BlockPos pos1 = leaves.get(world.rand.nextInt(leaves.size()));
							if(world.getBlockState(pos1).getBlock() != plant.getLeafBlock() && AbstractPlant.setLeaves(world, pos1, plant.getLeafBlock()))
								AbstractPlant.useEnergy(world, 1, energy_sources, plant.getLeafBlock());
							event.setResult(Result.ALLOW);
						}
					}
				}
			}
			else
				event.setResult(Result.ALLOW);
		}
		else if(state.getBlock() instanceof BlockLivingLeaf)
		{
			world.setBlockState(pos, ((BlockLivingLeaf)state.getBlock()).incrementEnergy(1, state, world, pos));
			event.setResult(Result.ALLOW);
		}
	}
	
	@Nullable
	public static Byte getBlockHistory(World world, BlockPos pos)
	{
		Map<ChunkPos, byte[]> block_history = BLOCK_HISTORY.getOrDefault(world, Maps.newHashMap());
		ChunkPos cpos = new ChunkPos(pos);
		if(pos.getY() > 255 || pos.getY() < 0 || !block_history.containsKey(cpos))
			return null;
		return block_history.get(cpos)[(pos.getY() * 256) + ((pos.getX() & 15) * 16) + (pos.getZ() & 15)];
	}
	
	public static void setBlockHistory(World world, BlockPos pos, byte history)
	{
		Map<ChunkPos, byte[]> block_history = BLOCK_HISTORY.getOrDefault(world, Maps.newHashMap());
		ChunkPos cpos = new ChunkPos(pos);
		if(pos.getY() <= 255 && pos.getY() >= 0 && block_history.containsKey(cpos))
			block_history.get(cpos)[(pos.getY() * 256) + ((pos.getX() & 15) * 16) + (pos.getZ() & 15)] = history;
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
			Byte seed = getBlockHistory(world, pos.down());
			if(state.getBlock() == Blocks.SAPLING && seed != null && checkIfCanGrow(world, pos, 0) && checkIfCanGrow(world, pos.up(), 1))
			{
				BlockPlanks.EnumType type = state.getValue(BlockSapling.TYPE);
				IPlant plant;
				switch(type)
				{
					case OAK :
						plant = ObjectReferences.LIVING_OAK_LOG.getPlant();
						break;
					case SPRUCE :
						plant = ObjectReferences.LIVING_SPRUCE_LOG.getPlant();
						break;
					case BIRCH :
						plant = ObjectReferences.LIVING_BIRCH_LOG.getPlant();
						break;
					case JUNGLE :
						plant = ObjectReferences.LIVING_JUNGLE_LOG.getPlant();
						break;
					case ACACIA :
						plant = ObjectReferences.LIVING_ACACIA_LOG.getPlant();
						break;
					case DARK_OAK :
						plant = ObjectReferences.LIVING_DARKOAK_LOG.getPlant();
						break;
					default:
						plant = null;
				}
				if(plant != null)
				{
					event.setResult(Result.DENY);
					world.setBlockState(pos, plant.getLogBlock().getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y));
					AbstractPlant.setLeaves(world, pos.up(), plant.getLeafBlock());
					setBlockHistory(world, pos, Integer.valueOf(new Random(seed).nextInt()).byteValue());
					setBlockHistory(world, pos.down(), Integer.valueOf(world.rand.nextInt(256) - 128).byteValue());
				}
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
