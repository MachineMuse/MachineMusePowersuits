package net.machinemuse.numina.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Objects;

public class NBTTagListUtils {
    public static void replaceOrAddModelModelSpecNBT(NBTTagList tagList, NBTTagCompound nbt) {
        String modelName = nbt.getString("model");
        if (tagList.hasNoTags())
            tagList.appendTag(nbt);
        else {
            removeModelSpecNBT(tagList, modelName);
            tagList.appendTag(nbt);
        }
    }

    public static void removeModelSpecNBT(NBTTagList tagList, String modelName) {
        while(true) {
            boolean found = false;
            for (int i = 0; i < tagList.tagCount(); i++) {
                String specName = tagList.getCompoundTagAt(i).getString("model");
                if (Objects.equals(tagList.getCompoundTagAt(i).getString("model"), modelName)) {
                    tagList.removeTag(i);
                    found = true;
                    break;
                }
            }
            if (!found)
                break;
        }
    }

    public static NBTTagCompound getSpecNBTorNull(NBTTagList tagList, String modelName) {
        NBTTagCompound nbt;
        for (int i = 0; i < tagList.tagCount(); i++) {
            nbt = tagList.getCompoundTagAt(i);
            if (Objects.equals(modelName, nbt.getString("model")))
                return nbt;
        }
        return null;
    }
}