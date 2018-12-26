package net.machinemuse.numina.capabilities.inventory;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:11 PM, 9/3/13
 *
 * Ported to Java by lehjr on 11/1/16.
 */
public interface IModeChangingItemCapability extends IModularItemCapability {
    @SideOnly(Side.CLIENT)
    @Nullable
    TextureAtlasSprite getModeIcon(int modeIndex);

    List<Integer> getValidModes();

    boolean isValidMode(String mode);

    ItemStack getActiveModule();

    int getActiveMode();

    void setActiveMode(String unLocalizedName);

    void setActiveMode(int newMode);

    void cycleMode(EntityPlayer player, int dMode);

    int nextMode();

    int prevMode();
}
