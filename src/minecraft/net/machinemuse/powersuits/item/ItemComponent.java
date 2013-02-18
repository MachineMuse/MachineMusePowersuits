package net.machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemComponent extends Item {
	public static List<Integer> icons;
	public static List<String> names;
	public static List<String> descriptions;

	public static ItemStack wiring;
	public static ItemStack solenoid;
	public static ItemStack servoMotor;
	public static ItemStack gliderWing;
	public static ItemStack ionThruster;
	public static ItemStack parachute;
	public static ItemStack lvcapacitor;
	public static ItemStack mvcapacitor;
	public static ItemStack hvcapacitor;
	public static ItemStack evcapacitor;
	public static ItemStack basicPlating;
	public static ItemStack advancedPlating;
	public static ItemStack fieldEmitter;
	public static ItemStack laserHologram;
	public static ItemStack carbonMyofiber;
	public static ItemStack controlCircuit;
	public static ItemStack myofiberGel;
	public static ItemStack artificialMuscle;
	public static ItemStack solarPanel;

	public ItemComponent() {
		super(Config.getAssignedItemID(Config.Items.PowerArmorComponent));
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(Config.getCreativeTab());
		icons = new ArrayList();
		names = new ArrayList();
		descriptions = new ArrayList();
	}

	public ItemStack addComponent(String oredictName, String englishName, String description, MuseIcon icon) {
		names.add(oredictName);
		icons.add(icon.getIconIndex());
		descriptions.add(description);
		this.setTextureFile(icon.getTexturefile());
		ItemStack stack = new ItemStack(this, 1, names.size() - 1);
		LanguageRegistry.addName(stack, englishName);
		return stack;
	}

	public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
		if (Config.doAdditionalInfo()) {
			String message = "For use in Tinker Table.";
			message = MuseStringUtils.wrapMultipleFormatTags(
					message,
					MuseStringUtils.FormatCodes.Grey,
					MuseStringUtils.FormatCodes.Italic);
			currentTipList.add(message);
			int damage = stack.getItemDamage();
			if (damage < descriptions.size()) {
				String description = descriptions.get(damage);
				currentTipList.addAll(MuseStringUtils.wrapStringToLength(description, 30));
			}
		} else {
			currentTipList.add(Config.additionalInfoInstructions());
		}
	}

	public void populate() {
		// NOTE: Only add to end otherwise people's IDs will get screwed up n.n'
		wiring = addComponent("componentWiring", "Wiring",
				"A special type of wiring with high voltaic capacity and precision, necessary for the sensitive electronics in power armor.",
				MuseIcon.WIRING);
		solenoid = addComponent("componentSolenoid", "Solenoid", "Wires wound around a ferromagnetic core produces a basic electromagnet.",
				MuseIcon.SOLENOID);
		servoMotor = addComponent("componentServo", "Servo Motor",
				"A special type of motor which uses a pulse-modulated signal to enact very precise movements.", MuseIcon.SERVOMOTOR);
		gliderWing = addComponent("componentGliderWing", "Glider Wing",
				"A lightweight aerodynamic wing with an electromagnet for quick deployment and retraction.", MuseIcon.GLIDERWING);
		ionThruster = addComponent("componentIonThruster", "Ion Thruster",
				"Essentially a miniature particle accelerator. Accelerates ions to near-light speed to produce thrust.", MuseIcon.IONTHRUSTER);
		lvcapacitor = addComponent("componentLVCapacitor", "LV Capacitor",
				"A simple capacitor can store and discharge small amounts of energy rapidly.", MuseIcon.LVCAPACITOR);
		mvcapacitor = addComponent("componentMVCapacitor", "MV Capacitor",
				"A more advanced capacitor which can store more energy at higher voltages.", MuseIcon.MVCAPACITOR);
		hvcapacitor = addComponent("componentHVCapacitor", "HV Capacitor",
				"A synthetic crystal device which can store and release massive amounts of energy.", MuseIcon.HVCAPACITOR);
		parachute = addComponent("componentParachute", "Parachute", "A simple reusable parachute which can be deployed and recovered in midair.",
				MuseIcon.PARACHUTE);
		basicPlating = addComponent("componentPlatingBasic", "Basic Plating", "Some carefully-arranged metal armor plates.",
				MuseIcon.ITEM_IRON_PLATING);
		advancedPlating = addComponent("componentPlatingAdvanced", "Advanced Plating",
				"Some carefully-arranged armor plates of a rare and stronger material", MuseIcon.ITEM_DIAMOND_PLATING);
		fieldEmitter = addComponent("componentFieldEmitter", "Force Field Emitter",
				"An advanced device which directly manipulates electromagnetic and gravitational fields in an area.", MuseIcon.FIELD_GENERATOR);
		laserHologram = addComponent("componentLaserEmitter", "Hologram Emitter",
				"A multicoloured laser array which can cheaply alter the appearance of something.", MuseIcon.HOLOGRAM_EMITTER);
		carbonMyofiber = addComponent("componentCarbonMyofiber", "Carbon Myofiber",
				"A small bundle of carbon fibers, refined for use in artificial muscles.", MuseIcon.CARBON_MYOFIBER);
		controlCircuit = addComponent("componentControlCircuit", "Control Circuit",
				"A simple networkable microcontroller for coordinating an individual component.", MuseIcon.CIRCUIT);
		myofiberGel = addComponent("componentMyofiberGel", "Myofiber Gel",
				"A thick, conductive paste, perfect for fitting between myofibers in an artificial muscle.", MuseIcon.MYOFIBER_PASTE);
		artificialMuscle = addComponent("componentArtificialMuscle", "Artificial Muscle",
				"An electrical, artificial muscle, with less range of movement than human muscle but orders of magnitude more strength.",
				MuseIcon.ARTIFICIAL_MUSCLE);
		solarPanel = addComponent("componentSolarPanel", "Solar Panel",
				"A light sensitive device that will generate electricity from the sun.", new MuseIcon(MuseIcon.WC_ICON_PATH, 229));
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public int getIconFromDamage(int index)
	{
		return icons.get(index);
	}

	public String getItemNameIS(ItemStack par1ItemStack)
	{
		int index = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, names.size() - 1);
		return "powerArmorComponent." + names.get(index).replaceAll("\\s", "");
	}

	/**
	 * returns a list of items with the same ID, but different meta (eg: dye
	 * returns 16 items). For creative tab.
	 */
	public void getSubItems(int itemID, CreativeTabs tab, List listToAddTo)
	{
		for (int i = 0; i < names.size(); ++i)
		{
			listToAddTo.add(new ItemStack(itemID, 1, i));
		}
	}
}
