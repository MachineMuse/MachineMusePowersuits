package machinemuse.powersuits.augmentation;

import java.util.ArrayList;
import java.util.List;

/**
 * An enumeration of the different types of augmentations and their IDs. Also
 * contains valid-for-slot configuration.
 * 
 * @author MachineMuse
 * 
 */
public enum AugmentationTypes {

	ArmorPlate("armorPlate", "Armor Plate", 1),
	DamageShieldPassive("shieldPassive", "Passive Shielding", 2),
	DamageShieldActive("shieldActive", "Active Shielding", 3),
	SteamGenerator("steamGenerator", "Steam Generator", 4),
	BatteryElectric("batteryElectric", "Electric Battery", 5),

	;

	public static int maxid = 6;
	public String idName;
	public String englishName;
	public int id;

	private AugmentationTypes(String idName, String englishName, int id) {
		this.idName = idName;
		this.englishName = englishName;
		this.id = id;
	}

	/**
	 * For readability and ease of editing.
	 * 
	 * @param list
	 * @param type
	 */
	private static void putList(List<AugmentationTypes> list,
			AugmentationTypes type) {
		list.add(type);
	}

	/**
	 * Returns an array of augmentations that are valid for a head slot item.
	 * 
	 * @param list
	 * @param type
	 */
	public static List<AugmentationTypes> validHeadAugmentations() {
		List<AugmentationTypes> list =
				new ArrayList<AugmentationTypes>();
		putList(list, ArmorPlate);
		putList(list, DamageShieldPassive);
		putList(list, DamageShieldActive);
		putList(list, SteamGenerator);
		putList(list, BatteryElectric);
		return list;
	}

	public static List<AugmentationTypes> validTorsoAugmentations() {
		List<AugmentationTypes> list =
				new ArrayList<AugmentationTypes>();
		putList(list, ArmorPlate);
		putList(list, DamageShieldPassive);
		putList(list, DamageShieldActive);
		putList(list, BatteryElectric);
		return list;
	}

	public static List<AugmentationTypes> validLegsAugmentations() {
		List<AugmentationTypes> list =
				new ArrayList<AugmentationTypes>();
		putList(list, ArmorPlate);
		putList(list, DamageShieldPassive);
		putList(list, DamageShieldActive);
		putList(list, BatteryElectric);
		return list;
	}

	public static List<AugmentationTypes> validFeetAugmentations() {
		List<AugmentationTypes> list =
				new ArrayList<AugmentationTypes>();
		putList(list, ArmorPlate);
		putList(list, DamageShieldPassive);
		putList(list, DamageShieldActive);
		putList(list, BatteryElectric);
		return list;
	}

	public static List<AugmentationTypes> validToolAugmentations() {
		List<AugmentationTypes> list =
				new ArrayList<AugmentationTypes>();
		putList(list, BatteryElectric);
		putList(list, SteamGenerator);
		return list;
	}
}
