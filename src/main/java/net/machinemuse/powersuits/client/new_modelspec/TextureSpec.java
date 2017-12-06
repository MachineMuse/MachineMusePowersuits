package net.machinemuse.powersuits.client.new_modelspec;

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
    Map<TexturedPartSpec, ResourceLocation> textureMap;

    public TextureSpec(String name, boolean isDefault) {
        super(name, isDefault);
        this.textureMap = new HashMap<>();
    }

    public ResourceLocation getTextureLocationForSlot(EntityEquipmentSlot slot, @Nullable String itemState) {
        return this.textureMap.getOrDefault(new TexturedPartSpec(itemState, slot), TextureMap.LOCATION_MISSING_TEXTURE);
    }

    public ResourceLocation getTextureLocationForSlot(EntityEquipmentSlot slot) {
        return this.getTextureLocationForSlot(slot, "all");
    }

    public void add(EntityEquipmentSlot slot, @Nullable String itemState, ResourceLocation textureLocation) {
        this.textureMap.put(new TexturedPartSpec(itemState, slot), textureLocation);
    }

    public void add(EntityEquipmentSlot slot, ResourceLocation textureLocation) {
        this.add(slot, "all", textureLocation);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TextureSpec))
            return false;
        return ((TextureSpec) o).getName().equals(this.getName()) &&
                ((TextureSpec) o).isDefault() == this.isDefault() &&
                ((TextureSpec) o).textureMap.equals(this.textureMap);
    }

    @Override
    public int hashCode() {
        return (isDefault() ? 1 : 0) + getName().hashCode() + textureMap.hashCode();
    }

    static class TexturedPartSpec {
        final String itemState;
        final EntityEquipmentSlot slot;
        TexturedPartSpec(@Nullable String itemState, EntityEquipmentSlot slot) {
            this.itemState = itemState != null ? itemState : "all";
            this.slot = slot;
        }

        public EntityEquipmentSlot getSlot() {
            return slot;
        }

        public String getItemState() {
            return itemState;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof TexturedPartSpec))
                return false;
            return ((TexturedPartSpec) o).getItemState().equals(this.getItemState()) &&
            ((TexturedPartSpec) o).getSlot().equals(this.getSlot());
        }

        @Override
        public int hashCode() {
            return this.getItemState().hashCode() + this.getSlot().hashCode();
        }
    }
}
