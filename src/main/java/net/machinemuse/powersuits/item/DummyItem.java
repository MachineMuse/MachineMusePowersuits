package net.machinemuse.powersuits.item;

import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 2/26/17.
 *
 * This is a temporary hackish workaround to get models to render with the texture
 *
 */
public class DummyItem extends Item {
    public static ItemStack dummyHead;
    public static ItemStack dummyArms;
    public static ItemStack dummyChest;
    public static ItemStack dummyJetPack;
    public static ItemStack dummyLegs;
    public static ItemStack dummyFeet;
    public static ItemStack dummyArmor2;

    public Map<Integer, String> names = new HashMap<>();
    public Map<Integer, ModelResourceLocation> modelLocations = new HashMap<>();
    Map<ResourceLocation, IBakedModel> modelMap = new HashMap<>();

    public DummyItem(){
        super();
        setHasSubtypes(true);
        setMaxDamage(0);
        setUnlocalizedName("item.mpsArmorDummyItem.");
    }

    public ItemStack addDummy(int meta, String name) {
        ModelResourceLocation location = new ModelResourceLocation(Config.RESOURCE_PREFIX + "mpsArmorDummyItem." + name);
        ItemStack stack = new ItemStack(this, 1, meta);
        names.put(meta, name);
        modelLocations.put(meta, location);
        return stack;
    }

    public void setup() {
        dummyHead = addDummy(0,"Helmet");
        dummyArms = addDummy(1,"Arms");
        dummyChest = addDummy(2, "Chest");
        dummyJetPack = addDummy(3,"JetPack");
        dummyLegs = addDummy(4,"Legs");
        dummyFeet = addDummy(5,"Feet");
        dummyArmor2 = addDummy(6,"Armor2");
    }

    public void setModel(IBakedModel model, String resourcePath) {
        ResourceLocation modelpath = resourcePathToModelLocation(resourcePath);
        modelMap.put(modelpath, model);
    }

    public IBakedModel getModel(ResourceLocation modelpath) {
        return modelMap.get(modelpath);
    }

    private ResourceLocation resourcePathToModelLocation(String resourcepath) {
        switch(resourcepath) {
            case "mpsArmorDummyItem.Helmet":
                return new ResourceLocation("powersuits:item/armor/mps_helm.obj");
            case "mpsArmorDummyItem.Arms":
                return new ResourceLocation("powersuits:item/armor/mps_arms.obj");
            case "mpsArmorDummyItem.Chest":
                return new ResourceLocation("powersuits:item/armor/mps_chest.obj");
            case "mpsArmorDummyItem.JetPack":
                return new ResourceLocation("powersuits:item/armor/jetpack.obj");
            case "mpsArmorDummyItem.Legs":
                return new ResourceLocation("powersuits:item/armor/mps_pantaloons.obj");
            case "mpsArmorDummyItem.Feet":
                return new ResourceLocation("powersuits:item/armor/mps_boots.obj");
            case "mpsArmorDummyItem.Armor2":
                return new ResourceLocation("powersuits:item/armor/armor2.obj");
            default:
                return new ResourceLocation("powersuits:item/armor/armor2.obj");
        }
    }
}
