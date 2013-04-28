package net.machinemuse.powersuits.item;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;

import java.util.ArrayList;
import java.util.List;

public class ItemComponent extends Item {
    public static int assignedItemID;

    public static List<Icon> icons;
    public static List<String> iconNames;
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

    public ItemComponent() {
        super(assignedItemID);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(Config.getCreativeTab());
        icons = new ArrayList<Icon>();
        iconNames = new ArrayList<String>();
        names = new ArrayList<String>();
        descriptions = new ArrayList<String>();
    }

    public ItemStack addComponent(String oredictName, String englishName, String description, String iconName) {
        names.add(oredictName);
        iconNames.add(iconName);
        descriptions.add(description);
        ItemStack stack = new ItemStack(this, 1, names.size() - 1);
        LanguageRegistry.addName(stack, englishName);
        return stack;
    }

    public ItemStack addComponent(int id, String oredictName, String englishName, String description, String iconName) {
        names.add(oredictName);
        iconNames.add(iconName);
        descriptions.add(description);
        ItemStack stack = new ItemStack(this, 1, names.size() - 1);
        LanguageRegistry.addName(stack, englishName);
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        if (Config.doAdditionalInfo()) {
            String message = "For use in Tinker Table.";
            message = MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
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
                "wiring");
        solenoid = addComponent("componentSolenoid", "Solenoid", "Wires wound around a ferromagnetic core produces a basic electromagnet.",
                "solenoid");
        servoMotor = addComponent("componentServo", "Servo Motor",
                "A special type of motor which uses a pulse-modulated signal to enact very precise movements.", "servo");
        gliderWing = addComponent("componentGliderWing", "Glider Wing",
                "A lightweight aerodynamic wing with an electromagnet for quick deployment and retraction.", "gliderwing");
        ionThruster = addComponent("componentIonThruster", "Ion Thruster",
                "Essentially a miniature particle accelerator. Accelerates ions to near-light speed to produce thrust.", "ionthruster");
        lvcapacitor = addComponent("componentLVCapacitor", "LV Capacitor",
                "A simple capacitor can store and discharge small amounts of energy rapidly.", "lvcapacitor");
        mvcapacitor = addComponent("componentMVCapacitor", "MV Capacitor",
                "A more advanced capacitor which can store more energy at higher voltages.", "mvcapacitor");
        hvcapacitor = addComponent("componentHVCapacitor", "HV Capacitor",
                "A synthetic crystal device which can store and release massive amounts of energy.", "hvcapacitor");
        parachute = addComponent("componentParachute", "Parachute", "A simple reusable parachute which can be deployed and recovered in midair.",
                "parachuteitem");
        basicPlating = addComponent("componentPlatingBasic", "Basic Plating", "Some carefully-arranged metal armor plates.", "basicplating1");
        advancedPlating = addComponent("componentPlatingAdvanced", "Advanced Plating",
                "Some carefully-arranged armor plates of a rare and stronger material", "advancedplating1");
        fieldEmitter = addComponent("componentFieldEmitter", "Force Field Emitter",
                "An advanced device which directly manipulates electromagnetic and gravitational fields in an area.", "fieldemitter");
        laserHologram = addComponent("componentLaserEmitter", "Hologram Emitter",
                "A multicoloured laser array which can cheaply alter the appearance of something.", "hologramemitter");
        carbonMyofiber = addComponent("componentCarbonMyofiber", "Carbon Myofiber",
                "A small bundle of carbon fibers, refined for use in artificial muscles.", "myofiber");
        controlCircuit = addComponent("componentControlCircuit", "Control Circuit",
                "A simple networkable microcontroller for coordinating an individual component.", "controlcircuit");
        myofiberGel = addComponent("componentMyofiberGel", "Myofiber Gel",
                "A thick, conductive paste, perfect for fitting between myofibers in an artificial muscle.", "paste");
        artificialMuscle = addComponent("componentArtificialMuscle", "Artificial Muscle",
                "An electrical, artificial muscle, with less range of movement than human muscle but orders of magnitude more strength.",
                "artificialmuscle");
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @Override
    public Icon getIconFromDamage(int index) {
        return icons.get(index);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        for (String iconName : iconNames) {
            icons.add(iconRegister.registerIcon(MuseIcon.ICON_PREFIX + iconName));
        }

        for (IPowerModule module : ModuleManager.getAllModules()) {
            module.registerIcon(iconRegister);
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int index = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, names.size() - 1);
        return "powerArmorComponent." + names.get(index).replaceAll("\\s", "");
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye
     * returns 16 items). For creative tab.
     */
    @Override
    public void getSubItems(int itemID, CreativeTabs tab, List listToAddTo) {
        for (int i = 0; i < names.size(); ++i) {
            listToAddTo.add(new ItemStack(itemID, 1, i));
        }
    }
}
