package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeManager {

	public static ItemStack copyAndResize(ItemStack stack, int number) {
		ItemStack copy = stack.copy();
		copy.stackSize = number;
		return copy;
	}

	public static void addRecipes() {
		// Recipe
		ItemStack iron = new ItemStack(Item.ingotIron);
		ItemStack circuit = ItemComponent.wiring;
		ItemStack goldNugget = new ItemStack(Item.goldNugget);
		ItemStack redstone = new ItemStack(Item.redstone);
		ItemStack wool = new ItemStack(Block.cloth);
		ItemStack string = new ItemStack(Item.silk);
		ItemStack paper = new ItemStack(Item.paper);
		ItemStack glowstone = new ItemStack(Item.lightStoneDust);
		ItemStack emerald = new ItemStack(Item.emerald);
		ItemStack lapis = new ItemStack(Item.dyePowder, 1, 4); // metadata 4 =
																// 'blue'

		GameRegistry.addRecipe(new ItemStack(PowersuitsMod.tinkerTable),
				"ILI",
				"LEL",
				"ILI",
				'I', iron, 'L', lapis, 'E', emerald);

		GameRegistry.addRecipe(new ItemStack(PowersuitsMod.powerArmorHead),
				"III",
				"C C",
				'I', iron, 'C', circuit);

		GameRegistry.addRecipe(new ItemStack(PowersuitsMod.powerArmorTorso),
				"I I",
				"CIC",
				"III",
				'I', iron, 'C', circuit);

		GameRegistry.addRecipe(new ItemStack(PowersuitsMod.powerArmorLegs),
				"III",
				"C C",
				"I I",
				'I', iron, 'C', circuit);

		GameRegistry.addRecipe(new ItemStack(PowersuitsMod.powerArmorFeet),
				"C C",
				"I I",
				'I', iron, 'C', circuit);

		GameRegistry.addRecipe(new ItemStack(PowersuitsMod.powerTool),
				" C ",
				"CI ",
				" IC",
				'I', iron, 'C', circuit);

		GameRegistry.addRecipe(copyAndResize(ItemComponent.wiring, 8),
				"GRG", 'G', goldNugget, 'R', redstone);

		GameRegistry.addRecipe(ItemComponent.parachute,
				"WWW", "S S", 'W', wool, 'S', string);

		GameRegistry.addRecipe(ItemComponent.capacitor,
				"GPL",
				"W W",
				'W', ItemComponent.wiring,
				'G', goldNugget,
				'P', paper,
				'L', lapis);

		GameRegistry.addRecipe(ItemComponent.electromagnet,
				"WIW",
				"WIW",
				"WIW",
				'W', ItemComponent.wiring,
				'I', iron);

		GameRegistry.addRecipe(ItemComponent.gliderWing,
				" II",
				"II ",
				"I  ",
				'I', iron);

		GameRegistry.addRecipe(ItemComponent.servoMotor,
				" W ",
				"EIE",
				'I', iron, 'E', ItemComponent.electromagnet, 'W', ItemComponent.wiring);

		GameRegistry.addRecipe(ItemComponent.ionThruster,
				"III",
				"EGE",
				"E E",
				'I', iron, 'E', ItemComponent.electromagnet, 'G', glowstone);
	}
}
