package net.machinemuse.powersuits.client.new_modelspec;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArmorModelSpec extends Spec {
    Map<ResourceLocation, List<ModelPartSpec>> defautSpecMap;

    public ArmorModelSpec(String name, boolean isDefault) {
        super(name, isDefault);
        defautSpecMap = new HashMap<>();
    }
}
