package net.machinemuse.powersuits.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

public class ItemComponent extends Item {
    public static int assignedItemID;

    public static List<IIcon> icons;
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
        super();
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(Config.getCreativeTab());
        icons = new ArrayList<IIcon>();
        iconNames = new ArrayList<String>();
        names = new ArrayList<String>();
        descriptions = new ArrayList<String>();
    }

    public ItemStack addComponent(String oredictName, String description, String iconName) {
        names.add(oredictName);
        iconNames.add(iconName);
        descriptions.add(description);
        ItemStack stack = new ItemStack(this, 1, names.size() - 1);

        //oredict compares itemIDs and damage values only
        OreDictionary.registerOre(oredictName, stack);
        return stack;
    }

    public ItemStack addComponent(int id, String oredictName, String description, String iconName) {
        names.add(oredictName);
        iconNames.add(iconName);
        descriptions.add(description);
        ItemStack stack = new ItemStack(this, 1, names.size() - 1);
        //oredict compares itemIDs and damage values only
        OreDictionary.registerOre(oredictName, stack);
        return stack;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        if (Config.doAdditionalInfo()) {
            String message = StatCollector.translateToLocal("item.powerArmorComponent.common.usage");
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
        wiring = addComponent("componentWiring", StatCollector.translateToLocal("item.powerArmorComponent.componentWiring.desc"), "wiring");
        solenoid = addComponent("componentSolenoid", StatCollector.translateToLocal("item.powerArmorComponent.componentSolenoid.desc"), "solenoid");
        servoMotor = addComponent("componentServo", StatCollector.translateToLocal("item.powerArmorComponent.componentServo.desc"), "servo");
        gliderWing = addComponent("componentGliderWing", StatCollector.translateToLocal("item.powerArmorComponent.componentGliderWing.desc"), "gliderwing");
        ionThruster = addComponent("componentIonThruster", StatCollector.translateToLocal("item.powerArmorComponent.componentIonThruster.desc"), "ionthruster");
        lvcapacitor = addComponent("componentLVCapacitor", StatCollector.translateToLocal("item.powerArmorComponent.componentLVCapacitor.desc"), "lvcapacitor");
        mvcapacitor = addComponent("componentMVCapacitor", StatCollector.translateToLocal("item.powerArmorComponent.componentMVCapacitor.desc"), "mvcapacitor");
        hvcapacitor = addComponent("componentHVCapacitor", StatCollector.translateToLocal("item.powerArmorComponent.componentHVCapacitor.desc"), "hvcapacitor");
        parachute = addComponent("componentParachute", StatCollector.translateToLocal("item.powerArmorComponent.componentParachute.desc"), "parachuteitem");
        basicPlating = addComponent("componentPlatingBasic", StatCollector.translateToLocal("item.powerArmorComponent.componentPlatingBasic.desc"), "basicplating1");
        advancedPlating = addComponent("componentPlatingAdvanced", StatCollector.translateToLocal("item.powerArmorComponent.componentPlatingAdvanced.desc"), "advancedplating1");
        fieldEmitter = addComponent("componentFieldEmitter", StatCollector.translateToLocal("item.powerArmorComponent.componentFieldEmitter.desc"), "fieldemitter");
        laserHologram = addComponent("componentLaserEmitter", StatCollector.translateToLocal("item.powerArmorComponent.componentLaserEmitter.desc"), "hologramemitter");
        carbonMyofiber = addComponent("componentCarbonMyofiber", StatCollector.translateToLocal("item.powerArmorComponent.componentCarbonMyofiber.desc"), "myofiber");
        controlCircuit = addComponent("componentControlCircuit", StatCollector.translateToLocal("item.powerArmorComponent.componentControlCircuit.desc"), "controlcircuit");
        myofiberGel = addComponent("componentMyofiberGel", StatCollector.translateToLocal("item.powerArmorComponent.componentMyofiberGel.desc"), "paste");
        artificialMuscle = addComponent("componentArtificialMuscle", StatCollector.translateToLocal("item.powerArmorComponent.componentArtificialMuscle.desc"), "artificialmuscle");
    }

    /**
     * Gets an icon index based on an item's damage value
     */
    @Override
    public IIcon getIconFromDamage(int index) {
        return icons.get(index);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        icons.clear();
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
        return "item.powerArmorComponent." + names.get(index).replaceAll("\\s", "");
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye
     * returns 16 items). For creative tab.
     */
    @Override
    public void getSubItems(Item item, CreativeTabs tab, List listToAddTo) {
        for (int i = 0; i < names.size(); ++i) {
            listToAddTo.add(new ItemStack(this, 1, i));
        }
    }
}
