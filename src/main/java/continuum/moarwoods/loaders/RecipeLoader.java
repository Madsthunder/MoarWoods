package continuum.moarwoods.loaders;

import continuum.essentials.helpers.CraftingHelper;
import continuum.essentials.mod.CTMod;
import continuum.essentials.mod.ObjectLoader;
import continuum.moarwoods.mod.MoarWoods_EH;
import continuum.moarwoods.mod.MoarWoods_OH;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

public class RecipeLoader implements ObjectLoader<MoarWoods_OH, MoarWoods_EH>
{
	@Override
	public void init(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		/**OreDictionary.registerOre("stickWood", new ItemStack(Items.STICK, 1, 1));
		OreDictionary.registerOre("stickWood", new ItemStack(Items.STICK, 1, 2));
		OreDictionary.registerOre("stickWood", new ItemStack(Items.STICK, 1, 3));
		OreDictionary.registerOre("stickWood", new ItemStack(Items.STICK, 1, 4));
		OreDictionary.registerOre("stickWood", new ItemStack(Items.STICK, 1, 5));
		OreDictionary.registerOre("stickWood", new ItemStack(Items.STICK, 1, 6));*/
	}
	
	@Override
	public void post(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		CraftingHelper.removeAllRecipesForItem(new ItemStack(Items.STICK, 4));
	}
	
	@Override
	public String getName()
	{
		return "Recipes";
	}
}
