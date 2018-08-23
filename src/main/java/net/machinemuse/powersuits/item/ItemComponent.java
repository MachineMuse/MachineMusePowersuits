package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.utils.MuseCommonStrings;
import net.machinemuse.powersuits.utils.MuseStringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemComponent extends Item {
    private volatile static ItemComponent INSTANCE;

    public static ItemComponent getInstance() {
        if (INSTANCE == null) {
            synchronized (ItemComponent.class) {
                if (INSTANCE == null) INSTANCE = new ItemComponent();
            }
        }
        return INSTANCE;
    }

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
    public static ItemStack ironPlating;
    public static ItemStack diamonddPlating;
    public static ItemStack fieldEmitter;
    public static ItemStack laserHologram;
    public static ItemStack carbonMyofiber;
    public static ItemStack controlCircuit;
    public static ItemStack myofiberGel;
    public static ItemStack artificialMuscle;
    public static ItemStack magnet;
    public static ItemStack solarPanel;
    public static ItemStack computerChip;
    public static ItemStack liquidNitrogen; // FIXME
    public static ItemStack rubberHose;

    private ItemComponent() {
        this.maxStackSize = 64;
        this.setRegistryName("powerarmorcomponent");
        this.setUnlocalizedName("item.powerArmorComponent.");
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(MPSConfig.INSTANCE.getCreativeTab());
        this.populate();
    }

    public ItemStack addComponent(int meta, String oredictName, String description) {
        ItemStack stack = new ItemStack(this, 1, meta);
        names.put(meta, oredictName);
        descriptions.put(meta, description);
        return stack;
    }

    public void registerOres() {
        for (int meta : names.keySet()) {
            OreDictionary.registerOre(names.get(meta), new ItemStack(this, 1, meta));
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> currentTipList, ITooltipFlag flagIn) {
        if (MPSConfig.INSTANCE.doAdditionalInfo()) {
            String message =  I18n.format("tooltip.componentTooltip");
            message = MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
            currentTipList.add(message);
            String description = (descriptions.get(stack.getMetadata()) != null) ? descriptions.get(stack.getMetadata()) : "";
            currentTipList.addAll(MuseStringUtils.wrapStringToLength(description, 30));
        } else {
            currentTipList.add(MuseCommonStrings.additionalInfoInstructions());
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
        ironPlating = addComponent(10, "componentPlatingIron", "Some carefully-arranged metal armor plates.");
        diamonddPlating = addComponent(11, "componentPlatingDiamond", "Some carefully-arranged armor plates of a rare and stronger material");
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
        String unlocalizedName = names.get(itemStack.getMetadata());
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
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (Integer meta : names.keySet()) {
            items.add(new ItemStack(this, 1, meta));
        }
    }
}
