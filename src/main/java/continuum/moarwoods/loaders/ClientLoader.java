package continuum.moarwoods.loaders;

import continuum.essentials.helpers.ClientHelper;
import continuum.essentials.mod.CTMod;
import continuum.essentials.mod.ObjectLoader;
import continuum.moarwoods.client.renderer.CraftingGridTESR;
import continuum.moarwoods.client.state.StateMapperCraftingGrid;
import continuum.moarwoods.mod.MoarWoods_EH;
import continuum.moarwoods.mod.MoarWoods_OH;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientLoader implements ObjectLoader<MoarWoods_OH, MoarWoods_EH>
{
	@SideOnly(Side.CLIENT)
	@Override
	public void construction(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		mod.getObjectHolder().stateMapperGrid = new StateMapperCraftingGrid();
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void pre(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		MoarWoods_OH holder = mod.getObjectHolder();
		ModelLoader.setCustomStateMapper(holder.crafingGridOak, holder.stateMapperGrid);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(holder.crafingGridOak), 0, new ModelResourceLocation("moarwoods:oak_crafting_grid", "walled_south"));
		ModelLoader.setCustomStateMapper(holder.crafingGridSpruce, holder.stateMapperGrid);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(holder.crafingGridSpruce), 0, new ModelResourceLocation("moarwoods:spruce_crafting_grid", "walled_south"));
		ModelLoader.setCustomStateMapper(holder.crafingGridBirch, holder.stateMapperGrid);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(holder.crafingGridBirch), 0, new ModelResourceLocation("moarwoods:birch_crafting_grid", "walled_south"));
		ModelLoader.setCustomStateMapper(holder.crafingGridJungle, holder.stateMapperGrid);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(holder.crafingGridJungle), 0, new ModelResourceLocation("moarwoods:jungle_crafting_grid", "walled_south"));
		ModelLoader.setCustomStateMapper(holder.crafingGridAcacia, holder.stateMapperGrid);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(holder.crafingGridAcacia), 0, new ModelResourceLocation("moarwoods:acacia_crafting_grid", "walled_south"));
		ModelLoader.setCustomStateMapper(holder.crafingGridDarkOak, holder.stateMapperGrid);
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(holder.crafingGridDarkOak), 0, new ModelResourceLocation("moarwoods:dark_oak_crafting_grid", "walled_south"));
		ModelLoader.registerItemVariants(holder.stick, new ResourceLocation("moarwoods", "default_stick"), new ResourceLocation("moarwoods", "oak_stick"), new ResourceLocation("moarwoods", "spruce_stick"), new ResourceLocation("moarwoods", "birch_stick"), new ResourceLocation("moarwoods", "jungle_stick"), new ResourceLocation("moarwoods", "acacia_stick"), new ResourceLocation("moarwoods", "dark_oak_stick"));
		ClientHelper.assignAllModelsToItem(holder.stick, "default_stick", "oak_stick", "spruce_stick", "birch_stick", "jungle_stick", "acacia_stick", "dark_oak_stick");
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void init(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCraftingGrid.class, new CraftingGridTESR());
	}
	
	@Override
	public String getName()
	{
		return "Client";
	}
	
	@Override
	public Side getSide()
	{
		return Side.CLIENT;
	}
}