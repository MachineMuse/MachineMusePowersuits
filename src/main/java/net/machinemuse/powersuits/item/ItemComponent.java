package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemComponent extends Item {
    public static Map<ItemStack, String> descriptions = new HashMap<>();
    public static Map<ItemStack, String> names = new HashMap<>();

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
        this.populate();
    }

    public ItemStack addComponent(int index, String oredictName, String description) {
        ItemStack stack = new ItemStack(this, 1, index);
        names.put(stack, oredictName);
        descriptions.put(stack, description);


        //oredict compares itemIDs and damage values only
//        OreDictionary.registerOre(oredictName, stack);
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        if (Config.doAdditionalInfo()) {
            String message =  I18n.format("tooltip.componentTooltip");
            message = MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
            currentTipList.add(message);
            String description = descriptions.get(stack);
            currentTipList.addAll(MuseStringUtils.wrapStringToLength(description, 30));
        } else {
            currentTipList.add(Config.additionalInfoInstructions());
        }
    }

    // changed this to static values for meta just in case we need to change or add things later
    public void populate() {
        // NOTE: Only add to end otherwise people's IDs will get screwed up n.n'
        wiring = addComponent(0, "componentWiring", "A special type of wiring with high voltaic capacity and precision, necessary for the sensitive electronics in power armor.");
        solenoid = addComponent(1, "componentSolenoid", "Wires wound around a ferromagnetic core produces a basic electromagnet.");
        servoMotor = addComponent(2, "componentServo", "A special type of motor which uses a pulse-modulated signal to enact very precise movements.");
        gliderWing = addComponent(3, "componentGliderWing", "A lightweight aerodynamic wing with an electromagnet for quick deployment and retraction.");
        ionThruster = addComponent(4, "componentIonThruster", "Essentially a miniature particle accelerator. Accelerates ions to near-light speed to produce thrust.");
        lvcapacitor = addComponent(5, "componentLVCapacitor", "A simple capacitor can store and discharge small amounts of energy rapidly.");
        mvcapacitor = addComponent(6, "componentMVCapacitor", "A more advanced capacitor which can store more energy at higher voltages.");
        hvcapacitor = addComponent(7, "componentHVCapacitor", "A synthetic crystal device which can store and release massive amounts of energy.");
        evcapacitor = addComponent(8, "componentEVCapacitor", "The most advanced energy storage device ever created. Now 15% less likely to randomly explode!");
        parachute = addComponent(9, "componentParachute", "A simple reusable parachute which can be deployed and recovered in midair.");
        basicPlating = addComponent(10, "componentPlatingBasic", "Some carefully-arranged metal armor plates.");
        advancedPlating = addComponent(11, "componentPlatingAdvanced", "Some carefully-arranged armor plates of a rare and stronger material");
        fieldEmitter = addComponent(12, "componentFieldEmitter", "An advanced device which directly manipulates electromagnetic and gravitational fields in an area.");
        laserHologram = addComponent(13, "componentLaserEmitter", "A multicoloured laser array which can cheaply alter the appearance of something.");
        carbonMyofiber = addComponent(14,"componentCarbonMyofiber", "A small bundle of carbon fibers, refined for use in artificial muscles.");
        controlCircuit = addComponent(15, "componentControlCircuit", "A simple networkable microcontroller for coordinating an individual component.");
        myofiberGel = addComponent(16, "componentMyofiberGel", "A thick, conductive paste, perfect for fitting between myofibers in an artificial muscle.");
        artificialMuscle = addComponent(17, "componentArtificialMuscle", "An electrical, artificial muscle, with less range of movement than human muscle but orders of magnitude more strength.");
        solarPanel = addComponent(18, "componentSolarPanel", "A light sensitive device that will generate electricity from the sun.");
        magnet = addComponent(19, "componentMagnet", "A metallic device that generates a magnetic field which pulls items towards the player.");
        computerChip = addComponent(20, "componentComputerChip", "An upgraded control circuit that contains a CPU which is capable of more advanced calculations.");
        rubberHose = addComponent(21,"componentRubberHose", "A heavily insulated rubber hose capable of withstanding extreme heat or cold");
        liquidNitrogen = addComponent(22, "componentLiquidNitrogen", "A bucket of Liquid Nitrogen");
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String unlocalizedName = names.get(itemStack);
        if (unlocalizedName != null){
            unlocalizedName = unlocalizedName.replaceAll("\\s", "");
        } else
            unlocalizedName = "";

        return "item.powerArmorComponent." + unlocalizedName;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye
     * returns 16 items). For creative tab.
     */
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List listToAddTo) {
        for (int i = 0; i < names.keySet().size(); ++i) {
            listToAddTo.add(new ItemStack(this, 1, i));
        }
    }
}