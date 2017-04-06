package continuum.moarwoods.mod;

import continuum.essentials.hooks.ItemHooks;
import continuum.moarwoods.client.gui.GuiCraftingGrid;
import continuum.moarwoods.containers.ContainerCraftingGrid;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber
public class MoarWoods_EH implements IGuiHandler
{
	public static final MoarWoods_EH I = new MoarWoods_EH();
	
	private MoarWoods_EH()
	{
		
	}
	
	@SubscribeEvent
	public static void onBlocksRegister(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().register(MoarWoods_OH.OAK_CRAFTING_GRID.getLeft());
		event.getRegistry().register(MoarWoods_OH.SPRUCE_CRAFTING_GRID.getLeft());
		event.getRegistry().register(MoarWoods_OH.BIRCH_CRAFTING_GRID.getLeft());
		event.getRegistry().register(MoarWoods_OH.JUNGLE_CRAFTING_GRID.getLeft());
		event.getRegistry().register(MoarWoods_OH.ACACIA_CRAFTING_GRID.getLeft());
		event.getRegistry().register(MoarWoods_OH.DARKOAK_CRAFTING_GRID.getLeft());
		GameRegistry.registerTileEntity(TileEntityCraftingGrid.class, "moarwoods:crafting_grid");
	}
	
	@SubscribeEvent
	public static void onItemsRegister(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().register(MoarWoods_OH.OAK_CRAFTING_GRID.getRight());
		event.getRegistry().register(MoarWoods_OH.SPRUCE_CRAFTING_GRID.getRight());
		event.getRegistry().register(MoarWoods_OH.BIRCH_CRAFTING_GRID.getRight());
		event.getRegistry().register(MoarWoods_OH.JUNGLE_CRAFTING_GRID.getRight());
		event.getRegistry().register(MoarWoods_OH.ACACIA_CRAFTING_GRID.getRight());
		event.getRegistry().register(MoarWoods_OH.DARKOAK_CRAFTING_GRID.getRight());
		event.getRegistry().register(MoarWoods_OH.APPLE_LEAVES.getRight());
	}
	
	@Override
	public Object getServerGuiElement(int index, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		switch(index)
		{
			case 0 :
				if(entity instanceof TileEntityCraftingGrid)
					return new ContainerCraftingGrid(player, (TileEntityCraftingGrid)entity);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int index, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		switch(index)
		{
			case 0 :
				if(entity instanceof TileEntityCraftingGrid)
					return new GuiCraftingGrid(player, (TileEntityCraftingGrid)entity);
		}
		return null;
	}
}
