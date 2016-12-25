package net.machinemuse.numina.render;

/**
 * Ported to Java by lehjr on 10/25/16.
 */
public class ParticleDictionary {
    static {
        new ParticleDictionary();
    }

    public static final String hugeexplosion = "hugeexplosion";
    public static final String largeexplode = "largeexplode";
    public static final String fireworksSpark = "fireworksSpark";
    public static final String bubble = "bubble";
    public static final String suspended = "suspended";
    public static final String depthsuspend = "depthsuspend";
    public static final String townaura = "townaura";
    public static final String crit = "crit";
    public static final String magicCrit = "magicCrit";
    public static final String smoke = "smoke";
    public static final String mobSpell = "mobSpell";
    public static final String mobSpellAmbient = "mobSpellAmbient";
    public static final String spell = "spell";
    public static final String instantSpell = "instantSpell";
    public static final String witchMagic = "witchMagic";
    public static final String note = "note";
    public static final String enchantmenttable = "enchantmenttable";
    public static final String explode = "explode";
    public static final String flame = "flame";
    public static final String lava = "lava";
    public static final String footstep = "footstep";
    public static final String splash = "splash";
    public static final String largesmoke = "largesmoke";
    public static final String portal = "portal";
    public static final String reddust = "reddust";
    public static final String cloud = "cloud";
    public static final String snowballpoof = "snowballpoof";
    public static final String dripWater = "dripWater";
    public static final String dripLava = "dripLava";
    public static final String snowshovel = "snowshovel";
    public static final String slime = "slime";
    public static final String heart = "heart";
    public static final String angryVillager = "angryVillager";
    public static final String happyVillager = "happyVillager";

    public String iconcrack_(int itemID) {
        return "iconcrack_" + itemID;
    }

    public String tilecrack_(final int blockID) {
        return "tilecrack_" + blockID;
    }
}