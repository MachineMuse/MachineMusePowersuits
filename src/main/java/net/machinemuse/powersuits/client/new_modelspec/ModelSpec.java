package net.machinemuse.powersuits.client.new_modelspec;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelSpec extends Spec {
    Map<ModelPartSpec, Boolean> modelPartSpecMap;


    public ModelSpec(String name, boolean isDefault, EnumSpecType specType) {
        super(name, isDefault, specType);
        modelPartSpecMap = new HashMap<>();
    }

    public void add(Map<ModelPartSpec, Boolean> partSpecMapIn) {
        System.out.println("modelPartSpecMap size before: " + modelPartSpecMap.size());
        System.out.println("specName: " + getName());
        Iterator it= modelPartSpecMap.keySet().iterator();

        Map<ModelPartSpec, Boolean> tempMap  = new HashMap<>();

        for (ModelPartSpec partSpecIn: partSpecMapIn.keySet()) {
            System.out.println("partSpecNameIn: " + partSpecIn.getPartName());

            if ( modelPartSpecMap.isEmpty())
                modelPartSpecMap.put(partSpecIn, partSpecMapIn.get(partSpecIn));
            else
                while (it.hasNext()) {
                    ModelPartSpec partSpecStored = (ModelPartSpec) it.next();
                    if (partSpecIn.samePartSpec(partSpecStored))
                        modelPartSpecMap.remove(partSpecStored);
                    tempMap.put(partSpecIn, partSpecMapIn.get(partSpecIn));
                }
//
//
//                for (ModelPartSpec partSpecStored: modelPartSpecMap.keySet()) {
//                    if (partSpecIn.samePartSpec(partSpecStored))
//                        modelPartSpecMap.remove(partSpecStored);
//                    modelPartSpecMap.put(partSpecIn, partSpecMapIn.get(partSpecIn));
//                }

        }

        if(!tempMap.isEmpty())
            modelPartSpecMap.putAll(tempMap);
        System.out.println("modelPartSpecMap size after: " + modelPartSpecMap.size());
    }

    public List<ModelPartSpec> getVisibleParts() {
        return modelPartSpecMap.entrySet().stream().filter(map-> map.getValue()).map(map->map.getKey()).collect(Collectors.toList());
    }

    public List<ModelPartSpec> getAllParts() {
        return modelPartSpecMap.keySet().stream().collect(Collectors.toList());
    }


    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();

        if (this.getName() != "customModelSpec") {
            nbt.setString("modelSpec", String.valueOf(this.getName().hashCode()));
        } else {
            NBTTagList tagList = new NBTTagList();
            for (ModelPartSpec partSpec: modelPartSpecMap.keySet()) {
                tagList.appendTag(partSpec.toNBT());
            }
            nbt.setTag("customModelSpec", tagList);
        }
        return nbt;
    }
}
