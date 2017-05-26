package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemComponent extends Item {
    public static Map<Integer, String> descriptions = new HashMap<>();
    public static Map<Integer, String> names = new HashMap<>();

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
    public static ItemStack magnet;
    public static ItemStack solarPanel;
    public static ItemStack computerChip;
    public static ItemStack liquidNitrogen;
    public static ItemStack rubberHose;

    public ItemComponent() {
        super();
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(Config.getCreativeTab());
        this.setUnlocalizedName("item.power_armor_component.");
    }

    public ItemStack addComponent(int meta, String oredictName, String description) {
        ItemStack stack = new ItemStack(this, 1, meta);
        names.put(meta, oredictName);
        OreDictionary.registerOre(oredictName, stack);
        descriptions.put(meta, description);
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        if (Config.doAdditionalInfo()) {
            String message =  I18n.format("tooltip.componentTooltip");
            message = MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
            currentTipList.add(message);
            String description = (descriptions.get(stack.getMetadata()) != null) ? descriptions.get(stack.getMetadata()) : "";
            currentTipList.addAll(MuseStringUtils.wrapStringToLength(description, 30));
        } else {
            currentTipList.add(Config.additionalInfoInstructions());
        }
    }

    // changed this to static values for meta just in case we need to change or add things later
    public void populate() {
        // NOTE: Only add to end otherwise people's IDs will get screwed up n.n'
        wiring = addComponent(0, "component_wiring", "A special type of wiring with high voltaic capacity and precision, necessary for the sensitive electronics in power armor.");
        solenoid = addComponent(1, "component_solenoid", "Wires wound around a ferromagnetic core produces a basic electromagnet.");
        servoMotor = addComponent(2, "component_servo", "A special type of motor which uses a pulse-modulated signal to enact very precise movements.");
        gliderWing = addComponent(3, "component_glider_wing", "A lightweight aerodynamic wing with an electromagnet for quick deployment and retraction.");
        ionThruster = addComponent(4, "component_ion_thruster", "Essentially a miniature particle accelerator. Accelerates ions to near-light speed to produce thrust.");
        lvcapacitor = addComponent(5, "component_lv_capacitor", "A simple capacitor can store and discharge small amounts of energy rapidly.");
        mvcapacitor = addComponent(6, "component_mv_capacitor", "A more advanced capacitor which can store more energy at higher voltages.");
        hvcapacitor = addComponent(7, "component_hv_capacitor", "A synthetic crystal device which can store and release massive amounts of energy.");
        evcapacitor = addComponent(8, "component_ev_capacitor", "The most advanced energy storage device ever created. Now 15% less likely to randomly explode!");
        parachute = addComponent(9, "component_parachute", "A simple reusable parachute which can be deployed and recovered in midair.");
        basicPlating = addComponent(10, "component_plating_basic", "Some carefully-arranged metal armor plates.");
        advancedPlating = addComponent(11, "component_plating_advanced", "Some carefully-arranged armor plates of a rare and stronger material");
        fieldEmitter = addComponent(12, "component_field_emitter", "An advanced device which directly manipulates electromagnetic and gravitational fields in an area.");
        laserHologram = addComponent(13, "component_laser_emitter", "A multicoloured laser array which can cheaply alter the appearance of something.");
        carbonMyofiber = addComponent(14,"component_carbon_myofiber", "A small bundle of carbon fibers, refined for use in artificial muscles.");
        controlCircuit = addComponent(15, "component_control_circuit", "A simple networkable microcontroller for coordinating an individual component.");
        myofiberGel = addComponent(16, "component_myofiber_gel", "A thick, conductive paste, perfect for fitting between myofibers in an artificial muscle.");
        artificialMuscle = addComponent(17, "component_artificial_muscle", "An electrical, artificial muscle, with less range of movement than human muscle but orders of magnitude more strength.");
        solarPanel = addComponent(18, "component_solar_panel", "A light sensitive device that will generate electricity from the sun.");
        magnet = addComponent(19, "component_magnet", "A metallic device that generates a magnetic field which pulls items towards the player.");
        computerChip = addComponent(20, "component_computer_chip", "An upgraded control circuit that contains a CPU which is capable of more advanced calculations.");
        rubberHose = addComponent(21,"component_rubber_hose", "A heavily insulated rubber hose capable of withstanding extreme heat or cold");
        liquidNitrogen = addComponent(22, "component_liquid_nitrogen", "A bucket of Liquid Nitrogen");
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String unlocalizedName = names.get(itemStack.getMetadata());
        if (unlocalizedName != null){
            unlocalizedName = unlocalizedName.replaceAll("\\s", "");
        } else
            unlocalizedName = "";

        return "item.power_armor_component." + unlocalizedName;
   }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     */
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (Integer meta : names.keySet()) {
            subItems.add(new ItemStack(this, 1, meta));
        }
    }
}