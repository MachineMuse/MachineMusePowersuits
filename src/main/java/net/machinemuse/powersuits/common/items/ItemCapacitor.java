package net.machinemuse.powersuits.common.items;

import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCapacitor extends Item {
    private static ItemCapacitor ourInstance;

    public static ItemCapacitor getInstance() {
        if (ourInstance == null)
            ourInstance = new ItemCapacitor();
        return ourInstance;
    }

    public static Map<Integer, String> descriptions = new HashMap<>();
    public static Map<Integer, String> names = new HashMap<>();

    private ItemCapacitor() {
        this.maxStackSize = 64;
        this.setRegistryName("item.powerArmorCapacitor");
        this.setUnlocalizedName("item.powerArmorCapacitor.");
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(MPSSettings.getCreativeTab());
        this.populate();
    }

    public static ItemStack lvcapacitor;
    public static ItemStack mvcapacitor;
    public static ItemStack hvcapacitor;
    public static ItemStack evcapacitor;


    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> currentTipList, ITooltipFlag flagIn) {
        if (MPSSettings.doAdditionalInfo()) {
            String message =  I18n.format("tooltip.componentTooltip");
            message = MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic);
            currentTipList.add(message);
            String description = (descriptions.get(stack.getMetadata()) != null) ? descriptions.get(stack.getMetadata()) : "";
            currentTipList.addAll(MuseStringUtils.wrapStringToLength(description, 30));
        } else {
            currentTipList.add(MPSSettings.additionalInfoInstructions());
        }
    }

    public ItemStack addComponent(int meta, String oredictName, String description) {
        ItemStack stack = new ItemStack(this, 1, meta);
        names.put(meta, oredictName);
        OreDictionary.registerOre(oredictName, stack);
        descriptions.put(meta, description);
        return stack;
    }

    private void populate() {
        lvcapacitor = addComponent(1, "LVCapacitor", "A simple capacitor can store and discharge small amounts of energy rapidly.");
        mvcapacitor = addComponent(2, "MVCapacitor", "A more advanced capacitor which can store more energy at higher voltages.");
        hvcapacitor = addComponent(3, "HVCapacitor", "A synthetic crystal device which can store and release massive amounts of energy.");
        evcapacitor = addComponent(4, "EVCapacitor", "The most advanced energy storage device ever created. Now 15% less likely to randomly explode!");
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack) {
        String unlocalizedName = names.get(itemStack.getMetadata());
        if (unlocalizedName != null){
            unlocalizedName = unlocalizedName.replaceAll("\\s", "");
        } else
            unlocalizedName = "";

        return "item.powerArmorCapacitor." + unlocalizedName;
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye
     * returns 16 items). For creative tab.
     */
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> listToAddTo) {
        for (Integer meta : names.keySet()) {
            listToAddTo.add(new ItemStack(this, 1, meta));
        }
    }

//    /**
//     * @deprecated Will be removed as soon as possible, hopefully 1.9.
//     */
//    @Deprecated
//    public static void registerTESRItemStack(Item item, int metadata, Class<? extends TileEntity> TileClass)
//    {
//        tileItemMap.put(Pair.of(item, metadata), TileClass);
//    }



}