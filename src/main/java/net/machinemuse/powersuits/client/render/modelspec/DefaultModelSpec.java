package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.model.MPSOBJLoader;
import net.machinemuse.powersuits.item.ItemPowerArmor;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static net.machinemuse.powersuits.client.render.modelspec.MorphTarget.*;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:11 AM, 29/04/13
 *
 * Ported to Java by lehjr on 11/8/16.
 */
public class DefaultModelSpec {
    public static Colour normalcolour = Colour.WHITE;
    public static Colour glowcolour = new Colour(17.0 / 255, 78.0 / 255, 1, 1);

    public static ModelSpec[] loadDefaultModel() {
        List<ModelSpec> defaultSpecList = new ArrayList<>();

        /* Head ----------------------- */
        ModelSpec headModel = loadModel(new ResourceLocation("powersuits:models/mps_helm.obj"));
        makeEntries(Head, EntityEquipmentSlot.HEAD, 0, false, "helm_main;helm_tube_entry1;helm_tubes;helm_tube_entry2".split(";"), headModel);
        makeEntries(Head, EntityEquipmentSlot.HEAD, 1, true, "visor".split(";"), headModel);
        defaultSpecList.add(headModel);

        /* Arms ----------------------- */
        ModelSpec armsModel = loadModel(new ResourceLocation("powersuits:models/mps_arms.obj"));
        makeEntries(RightArm, EntityEquipmentSlot.CHEST, 0, false, "arms3".split(";"),armsModel);
        makeEntries(RightArm, EntityEquipmentSlot.CHEST, 1, true, "crystal_shoulder_2".split(";"), armsModel);
        makeEntries(LeftArm, EntityEquipmentSlot.CHEST, 0, false, "arms2".split(";"), armsModel);
        makeEntries(LeftArm, EntityEquipmentSlot.CHEST, 1, true, "crystal_shoulder_1".split(";"), armsModel);
        defaultSpecList.add(armsModel);

        /* Body ----------------------- */
        ModelSpec bodyModel = loadModel(new ResourceLocation("powersuits:models/mps_chest.obj"));
        makeEntries(Body, EntityEquipmentSlot.CHEST, 0, false, "belt;chest_main;polySurface36;backpack;chest_padding".split(";"), bodyModel);
        makeEntries(Body, EntityEquipmentSlot.CHEST, 1, true, "crystal_belt".split(";"), bodyModel);
        defaultSpecList.add(bodyModel);

        /* Legs ----------------------- */
        ModelSpec legsModel = loadModel(new ResourceLocation("powersuits:models/mps_pantaloons.obj"));
        makeEntries(RightLeg, EntityEquipmentSlot.LEGS, 0, false, "leg1".split(";"), legsModel);
        makeEntries(LeftLeg, EntityEquipmentSlot.LEGS, 0, false, "leg2".split(";"), legsModel);
        defaultSpecList.add(legsModel);

        /* Feet ----------------------- */
        ModelSpec feetModel = loadModel(new ResourceLocation("powersuits:models/mps_boots.obj"));
        makeEntries(RightLeg, EntityEquipmentSlot.FEET, 0, false, "boots1".split(";"), feetModel);
        makeEntries(LeftLeg, EntityEquipmentSlot.FEET, 0, false, "boots2".split(";"), feetModel);
        defaultSpecList.add(feetModel);

        return (ModelSpec[]) defaultSpecList.toArray();
    }

    public static ModelSpec loadModel(ResourceLocation file)  {
        // TODO: this may or may not fail. Not sure how late textures can be registered.
        try {
            MPSOBJLoader.INSTANCE.registerModelSprites(file); //<-- this registers the textures without caching the model
            IBakedModel model = ModelRegistry.getInstance().loadBakedModel(file);
            if (model != null && model instanceof OBJModel.OBJBakedModel) {
                return (ModelRegistry.getInstance().put(MuseStringUtils.extractName(file), new ModelSpec(model, null, null, file.toString())));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void makeEntries(MorphTarget target, EntityEquipmentSlot slot, int colourIndex, Boolean glow, String[] names, ModelSpec model) {
        for (String name: names)
            model.put(name, new ModelPartSpec(model, target, name, slot, colourIndex, glow, name));
    }

    public static NBTTagCompound makeModelPrefs(ItemStack stack, EntityEquipmentSlot slot) {
        ItemPowerArmor item = (ItemPowerArmor) stack.getItem();
        Colour normalcolour = item.getColorFromItemStack(stack);
        Colour glowcolour = item.getGlowFromItemStack(stack);
        List<NBTTagCompound> list = new ArrayList<>();

        switch (slot) {
            case HEAD:
                list.addAll(makePrefs("mps_helm", "helm_main;helm_tube_entry1;helm_tubes;helm_tube_entry2".split(";"), 0, false));
                list.addAll(makePrefs("mps_helm", "visor".split(";"), 1, true));
                break;

            case CHEST:
                list.addAll(makePrefs("mps_arms", "arms2;arms3".split(";"), 0, false));
                list.addAll(makePrefs("mps_arms", "crystal_shoulder_2;crystal_shoulder_1".split(";"), 1, true));
                list.addAll(makePrefs("mps_chest", "belt;chest_main;polySurface36;backpack;chest_padding".split(";"), 0, false));
                list.addAll(makePrefs("mps_chest", "crystal_belt".split(";"), 1, true));
                break;

            case LEGS:
                list.addAll(makePrefs("mps_pantaloons", "leg1;leg2".split(";"), 0, false));
                break;

            case FEET:
                list.addAll(makePrefs("mps_boots", "boots1;boots2".split(";"), 0, false));
                break;
        }
        NBTTagCompound nbt = new NBTTagCompound();
        for (NBTTagCompound elem: list) {
            nbt.setTag(elem.getString("model") + "." + elem.getString("part"), elem);
        }
        return nbt;
    }

    public static List<NBTTagCompound> makePrefs(String modelname, String[] partnames, int colour, boolean glow) {
        List<NBTTagCompound> prefArray = new ArrayList<>();
        ModelSpec model = ModelRegistry.getInstance().get(modelname);
        for (String name: partnames) {
            prefArray.add(makePref(model.get(name), colour, glow));
        }
        return prefArray;
    }

    public static NBTTagCompound makePref(ModelPartSpec partSpec, Integer colourindex, Boolean glow) {
        return partSpec.multiSet(new NBTTagCompound(), null, glow, colourindex);
    }
}