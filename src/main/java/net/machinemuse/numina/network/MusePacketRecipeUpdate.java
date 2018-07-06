package net.machinemuse.numina.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.DataInputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:40 PM, 12/16/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketRecipeUpdate extends MusePacket {
    private String recipe;

    public MusePacketRecipeUpdate(EntityPlayer player, String recipe) {
        this.recipe = recipe;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClient(EntityPlayer player) {
        try {
//            JSONRecipeList.loadRecipesFromString(recipe);
        } catch (Exception ignored) {

        }
    }

    @Override
    public IMusePackager packager() {
        return MusePacketRecipeUpdatePackager.INSTANCE;
    }

    @Override
    public void write() {
        writeString(recipe);
    }

    public static MusePacketRecipeUpdatePackager getPackagerInstance() {
        return MusePacketRecipeUpdatePackager.INSTANCE;
    }

    public enum MusePacketRecipeUpdatePackager implements IMusePackager {
        INSTANCE;

        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            String recipe = readString(datain);
            return new MusePacketRecipeUpdate(player, recipe);
        }
    }
}
