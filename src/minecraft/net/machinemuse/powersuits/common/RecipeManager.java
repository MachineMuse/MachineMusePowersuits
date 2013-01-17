package net.machinemuse.powersuits.common;

import ic2.api.Ic2Recipes;
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
		ItemStack gold = new ItemStack(Item.goldNugget);
		ItemStack redstone = new ItemStack(Item.redstone);
		ItemStack wool = new ItemStack(Block.cloth);
		ItemStack string = new ItemStack(Item.silk);
		ItemStack paper = new ItemStack(Item.paper);
		ItemStack glass = new ItemStack(Block.glass);
		ItemStack glowstone = new ItemStack(Item.lightStoneDust);
		ItemStack emerald = new ItemStack(Item.emerald);
		ItemStack lapis = new ItemStack(Item.dyePowder, 1, 4); // metadata 4 =
																// 'blue'
		ItemStack enderPearl = new ItemStack(Item.enderPearl);

		if (ModCompatability.vanillaRecipesEnabled()) {
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
					"GRG", 'G', gold, 'R', redstone);

			GameRegistry.addRecipe(ItemComponent.parachute,
					"WWW", "S S", 'W', wool, 'S', string);

			GameRegistry.addRecipe(ItemComponent.lvcapacitor,
					"WPI",
					"W W",
					'W', ItemComponent.wiring,
					'I', iron,
					'P', paper,
					'L', lapis);

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.mvcapacitor,
					"GPL",
					"W W",
					'W', ItemComponent.wiring,
					'G', gold,
					'P', paper,
					'L', lapis));
			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.hvcapacitor,
					"EPG",
					"W W",
					'W', ItemComponent.wiring,
					'E', enderPearl,
					'P', glass,
					'G', glowstone));

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
					" EE",
					"IG ",
					"IEE",
					'I', iron, 'E', ItemComponent.solenoid, 'G', glowstone);
		}
		if (ModCompatability.UERecipesEnabled()) {
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
					"GWG", 'G', gold, 'W', "copperWire"));

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
					" EE",
					"CG ",
					"IEE", 'I', "plateSteel", 'E', ItemComponent.solenoid, 'G', glowstone, 'C', "advancedCircuit"));
		}
		if (ModCompatability.IC2RecipesEnabled()) {
			circuit = ModCompatability.getIC2Item("electronicCircuit").copy();
			ItemStack advCircuit = ModCompatability.getIC2Item("advancedCircuit").copy();
			gold = ModCompatability.getIC2Item("goldCableItem").copy();
			String refIron = "ingotRefinedIron";
			String tin = "ingotTin";
			String copper = "ingotCopper";
			ItemStack reBattery = ModCompatability.getIC2Item("reBattery").copy();
			ItemStack energyCrystal = ModCompatability.getIC2Item("energyCrystal").copy();
			ItemStack lapotronCrystal = ModCompatability.getIC2Item("lapotronCrystal").copy();
			ItemStack iridiumOre = ModCompatability.getIC2Item("iridiumOre").copy();
			ItemStack carbonFiber = ModCompatability.getIC2Item("carbonPlate").copy();
			ItemStack machine = ModCompatability.getIC2Item("machine").copy();
			ItemStack advMachine = ModCompatability.getIC2Item("advancedMachine").copy();

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.tinkerTable), true,
					"E",
					"C",
					"M",
					'E', emerald, 'C', circuit.copy(), 'M', machine));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorHead), true,
					"III",
					"C C",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorTorso), true,
					"I I",
					"CIC",
					"III",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorLegs), true,
					"III",
					"C C",
					"I I",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorFeet), true,
					"C C",
					"I I",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerTool), true,
					" C ",
					"CI ",
					" IC",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(copyAndResize(ItemComponent.wiring, 2), true,
					"GRG", 'G', gold.copy(), 'R', redstone));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.parachute, true,
					"WWW", "S S", 'W', wool, 'S', string));

			Ic2Recipes.addCraftingRecipe(ItemComponent.lvcapacitor,
					"WBW",
					'W', ItemComponent.wiring.copy(),
					'B', reBattery);

			Ic2Recipes.addCraftingRecipe(ItemComponent.mvcapacitor,
					"WBW",
					'W', ItemComponent.wiring.copy(),
					'B', energyCrystal);
			
			Ic2Recipes.addCraftingRecipe(ItemComponent.hvcapacitor,
					"WBW",
					'W', ItemComponent.wiring.copy(),
					'B', lapotronCrystal);

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.solenoid, true,
					" W ",
					"WIW",
					" W ",
					'W', ItemComponent.wiring,
					'I', machine));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.gliderWing, true,
					" CC",
					"CCI",
					"C  ",
					'C', carbonFiber.copy(),
					'I', ItemComponent.solenoid));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.servoMotor, true,
					" W ",
					"EME",
					'M', machine.copy(), 'E', ItemComponent.solenoid, 'W', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.ionThruster, true,
					" EE",
					"MG ",
					"CEE",
					'I', iron,
					'E', ItemComponent.solenoid,
					'G', glowstone,
					'C', advCircuit.copy(),
					'M', advMachine.copy()));
		}
		if (ModCompatability.GregTechRecipesEnabled()) {
			// This means Gregtech is installed, and GregoriusT in his infinite
			// wisdom has registered literally everything in the universe with
			// the ore dictionary, so we can just use strings here :D ...once we decide what to put.
			circuit = ModCompatability.getIC2Item("electronicCircuit");
			ItemStack advCircuit = ModCompatability.getIC2Item("advancedCircuit");
			gold = ModCompatability.getIC2Item("goldCableItem");
			String refIron = "ingotRefinedIron";
			String tin = "ingotTin";
			String copper = "ingotCopper";
			ItemStack reBattery = ModCompatability.getIC2Item("reBattery").copy();
			ItemStack energyCrystal = ModCompatability.getIC2Item("energyCrystal").copy();
			ItemStack lapotronCrystal = ModCompatability.getIC2Item("lapotronCrystal").copy();
			
			ItemStack iridiumOre = ModCompatability.getIC2Item("iridiumOre");
			ItemStack carbonFiber = ModCompatability.getIC2Item("carbonPlate");
			ItemStack machine = ModCompatability.getIC2Item("machine");
			ItemStack advMachine = ModCompatability.getIC2Item("advancedMachine");

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.tinkerTable), true,
					"E", "C", "M",
					'E', "gemEmerald", 'C', "circuitTier04", 'M', "rawMachineTier02"));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorHead), true,
					"III",
					"C C",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorTorso), true,
					"I I",
					"CIC",
					"III",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorLegs), true,
					"III",
					"C C",
					"I I",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerArmorFeet), true,
					"C C",
					"I I",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(PowersuitsMod.powerTool), true,
					" C ",
					"CI ",
					" IC",
					'I', refIron, 'C', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(copyAndResize(ItemComponent.wiring, 2), true,
					"GRG", 'G', gold.copy(), 'R', redstone));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.parachute, true,
					"WWW", "S S", 'W', wool, 'S', string));
			
			Ic2Recipes.addCraftingRecipe(ItemComponent.lvcapacitor,
					"WBW",
					'W', ItemComponent.wiring.copy(),
					'B', reBattery);

			Ic2Recipes.addCraftingRecipe(ItemComponent.mvcapacitor,
					"WBW",
					'W', ItemComponent.wiring.copy(),
					'B', energyCrystal);
			
			Ic2Recipes.addCraftingRecipe(ItemComponent.hvcapacitor,
					"WBW",
					'W', ItemComponent.wiring.copy(),
					'B', lapotronCrystal);


			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.solenoid, true,
					" W ",
					"WIW",
					" W ",
					'W', ItemComponent.wiring,
					'I', machine));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.gliderWing, true,
					" CC",
					"CCI",
					"C  ",
					'C', carbonFiber.copy(),
					'I', ItemComponent.solenoid));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.servoMotor, true,
					" W ",
					"EME",
					'M', machine.copy(), 'E', ItemComponent.solenoid, 'W', circuit.copy()));

			GameRegistry.addRecipe(new ShapedOreRecipe(ItemComponent.ionThruster, true,
					" EE",
					"MG ",
					"CEE",
					'I', iron,
					'E', ItemComponent.solenoid,
					'G', glowstone,
					'C', advCircuit.copy(),
					'M', advMachine.copy()));
		}
	}
}
