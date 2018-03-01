package net.machinemuse.numina.api.capability_ports.inventory;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
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

    int getActiveMode();

    void setActiveMode(int newMode);

    void cycleMode(EntityPlayer player, int dMode);

    int nextMode();

    int prevMode();
}
