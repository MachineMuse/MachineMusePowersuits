package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;
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

		if (Config.vanillaRecipesEnabled()) {
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

			GameRegistry.addRecipe(ItemComponent.lvcapacitor,
					"GPL",
					"W W",
					'W', ItemComponent.wiring,
					'G', goldNugget,
					'P', paper,
					'L', lapis);
			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.mvcapacitor,
					"WBW",
					'W', ItemComponent.wiring,
					'B', "advancedBattery"));
			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.hvcapacitor,
					"WBW",
					'W', ItemComponent.wiring,
					'B', "eliteBattery"));

			GameRegistry.addRecipe(ItemComponent.solenoid,
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
					'I', iron, 'E', ItemComponent.solenoid, 'W', ItemComponent.wiring);

			GameRegistry.addRecipe(ItemComponent.ionThruster,
					"III",
					"EGE",
					"E E",
					'I', iron, 'E', ItemComponent.solenoid, 'G', glowstone);
		}
		if (Config.UERecipesEnabled()) {
			String basicCircuit = "basicCircuit";
			String advancedCircuit = "advancedCircuit";
			String eliteCircuit = "eliteCircuit";
			ItemStack lapisBlock = new ItemStack(Block.blockLapis);

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.tinkerTable), true,
					"ILI",
					"LEL",
					"ILI",
					'I', "plateSteel", 'L', lapisBlock, 'E', emerald));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorHead), true,
					"III",
					"C C",
					'I', "plateSteel", 'C', "basicCircuit"));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorTorso), true,
					"I I",
					"CIC",
					"III",
					'I', "plateSteel", 'C', "basicCircuit"));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorLegs), true,
					"III",
					"C C",
					"I I",
					'I', "plateSteel", 'C', "basicCircuit"));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorFeet), true,
					"C C",
					"I I",
					'I', "plateSteel", 'C', "basicCircuit"));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerTool), true,
					" C ",
					"CI ",
					" IC",
					'I', "plateSteel", 'C', "basicCircuit"));

			GameRegistry.addRecipe(new ShapedOreRecipe(copyAndResize(ItemComponent.wiring, 4), true,
					"GWG", 'G', goldNugget, 'W', "copperWire"));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.parachute, true,
					"WWW",
					"S S", 'W', wool, 'S', string));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.lvcapacitor,
					"WBW",
					'W', ItemComponent.wiring,
					'B', "battery"));
			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.mvcapacitor,
					"WBW",
					'W', ItemComponent.wiring,
					'B', "advancedBattery"));
			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.hvcapacitor,
					"WBW",
					'W', ItemComponent.wiring,
					'B', "eliteBattery"));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.solenoid, true,
					"WIW",
					"WIW",
					"WIW",
					'W', ItemComponent.wiring,
					'I', iron));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.gliderWing, true,
					" SI",
					"SI ",
					"S  ",
					'I', iron, 'S', "plateSteel"));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.servoMotor, true,
					" C ",
					"EIE", 'I', iron, 'E', ItemComponent.solenoid, 'C', "basicCircuit"));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.ionThruster, true,
					"ICI",
					"EGE",
					"E E", 'I', "plateSteel", 'E', ItemComponent.solenoid, 'G', glowstone, 'C', "eliteCircuit"));
		}

	}
}
