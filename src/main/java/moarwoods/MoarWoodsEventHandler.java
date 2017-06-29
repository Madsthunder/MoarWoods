package moarwoods;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import moarwoods.blocks.living.tree.AbstractPlant;
import moarwoods.blocks.living.tree.Plant;
import moarwoods.capability.CapabilityFarmer;
import moarwoods.entity.ai.EntityAIRunAroundLikeCrazyWrapper;
import moarwoods.entity.ai.EntityAITameHorse;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.terraingen.SaplingGrowTreeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

@EventBusSubscriber(modid = MoarWoods.MODID)
public class MoarWoodsEventHandler
{
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
	
	@SubscribeEvent
	public static void onBonemealUse(BonemealEvent event)
	{
		IBlockState state = event.getBlock();
		World world = event.getWorld();
		BlockPos pos = event.getPos();
		if(!world.isRemote && state.getBlock() instanceof BlockLivingLog)
		{
			BlockLivingLog block = (BlockLivingLog)state.getBlock();
			Plant<?, ?, ?> plant = block.plant;
			if(plant != null && !plant.grow(world, pos, false) && AbstractPlant.isBase(world, pos))
			{
				long[] seeds = plant.getSeeds(world, pos);
				if(seeds != null)
				{
					int height = AbstractPlant.getHeight(world, pos);
					int total_energy;
					TObjectIntHashMap<BlockPos> energy_sources;
					{
						Pair<Integer, TObjectIntHashMap<BlockPos>> pair = AbstractPlant.getTotalEnergy(world, pos, plant.getLeafSearchRadius(world, pos, height, seeds), height + plant.getLeafSearchExtraHeight(world, pos, height, seeds), plant.getLeafBlock());
						total_energy = pair.getLeft();
						energy_sources = pair.getRight();
					}
					if(total_energy >= 1)
					{
						Map<IBlockState, List<BlockPos>> blocks = Maps.transformValues(plant.getBlocks(world, pos, height, false, seeds), (positions) -> Lists.newArrayList(positions));
						if(!blocks.isEmpty())
						{
							int total_size = 0;
							for(List<BlockPos> positions : blocks.values())
								total_size += positions.size();
							int block_index = world.rand.nextInt(total_size);
							IBlockState state1 = null;
							BlockPos pos1 = null;
							int current_index = 0;
							for(Entry<IBlockState, List<BlockPos>> entry : blocks.entrySet())
							{
								List<BlockPos> positions = entry.getValue();
								int size = positions.size();
								if(block_index <= size)
									current_index += size;
								else
								{
									state1 = entry.getKey();
									pos1 = positions.get(block_index - current_index);
									break;
								}
							}
							if(AbstractPlant.setBlock(world, pos1, state1, (triple) -> triple.getLeft().getBlock() instanceof BlockLeaves))
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

	public static class TerrainGen
	{
		@SubscribeEvent
		public static void onSaplingGrow(SaplingGrowTreeEvent event)
		{
			World world = event.getWorld();
			BlockPos pos = event.getPos();
			IBlockState state = world.getBlockState(pos);
			byte seed = MoarWoodsBlockSeeds.getBlockSeed(world, pos.down());
			if(state.getBlock() == Blocks.SAPLING && AbstractPlant.checkIfCanGrow(world, pos, 0) && AbstractPlant.checkIfCanGrow(world, pos.up(), 1))
			{
				BlockPlanks.EnumType type = state.getValue(BlockSapling.TYPE);
				Plant<? , ?, ?> plant;
				switch(type)
				{
					case OAK :
						plant = MoarWoodsObjects.OAK_TREE_SMALL_TRUNK.plant;
						break;
					case SPRUCE :
						plant = MoarWoodsObjects.SPRUCE_TREE_SMALL_TRUNK.plant;
						break;
					case BIRCH :
						plant = MoarWoodsObjects.BIRCH_TREE_SMALL_TRUNK.plant;
						break;
					case JUNGLE :
						plant = MoarWoodsObjects.JUNGLE_TREE_SMALL_TRUNK.plant;
						break;
					case ACACIA :
						plant = MoarWoodsObjects.ACACIA_TREE_SMALL_TRUNK.plant;
						break;
					case DARK_OAK :
						plant = MoarWoodsObjects.DARKOAK_TREE_LARGE_TRUNK.plant;
						break;
					default:
						plant = null;
				}
				if(plant != null)
				{
					event.setResult(Result.DENY);
					world.setBlockState(pos, plant.getLogBlock().getDefaultState());
					AbstractPlant.setLeaves(world, pos.up(), plant.getLeafBlock());
					MoarWoodsBlockSeeds.setBlockSeed(world, pos, Integer.valueOf(new Random(seed).nextInt()).byteValue());
					MoarWoodsBlockSeeds.setBlockSeed(world, pos.down(), Integer.valueOf(world.rand.nextInt(256) - 128).byteValue());
				}
			}
		}
	}
}
