package net.machinemuse.powersuits.client.new_modelspec;

import com.google.common.base.Objects;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * This is just a way of mapping a possible texture combinations for a piece of PowerArmor using the default vanilla model
 *
 */

public class TextureSpec extends Spec {
    //TODO: come up with a datatype that ModelPartSpec can extend that would replace the map here.








    Map<Binding, ResourceLocation> textureMap;

    public TextureSpec(String name, boolean isDefault) {
        super(name, isDefault, EnumSpecType.ARMOR_SKIN);
        this.textureMap = new HashMap<>();
    }

    public ResourceLocation getTextureLocationForSlot(EntityEquipmentSlot slot, @Nullable String itemState) {
        return this.textureMap.getOrDefault(new Binding(slot, itemState), TextureMap.LOCATION_MISSING_TEXTURE);
    }

    public ResourceLocation getTextureLocationForSlot(EntityEquipmentSlot slot) {
        return this.getTextureLocationForSlot(slot, "all");
    }

    public void add(EntityEquipmentSlot slot, @Nullable String itemState, ResourceLocation textureLocation) {
        this.textureMap.put(new Binding(slot, itemState), textureLocation);
    }

    public void add(EntityEquipmentSlot slot, ResourceLocation textureLocation) {
        this.add(slot, "all", textureLocation);
    }

    public void add(Binding binding, ResourceLocation textureLocation) {
        this.textureMap.put(binding, textureLocation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        TextureSpec that = (TextureSpec) o;
        return Objects.equal(textureMap, that.textureMap);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), textureMap);
    }
}