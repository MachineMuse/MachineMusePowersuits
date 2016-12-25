package net.machinemuse.numina.network;

import net.machinemuse.numina.recipe.JSONRecipeList;
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
            JSONRecipeList.loadRecipesFromString(recipe);
        } catch (Exception e) {

        }
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeString(recipe);
    }

    private static MusePacketRecipeUpdatePackager PACKAGERINSTANCE;
    public static MusePacketRecipeUpdatePackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketRecipeUpdatePackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketRecipeUpdatePackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            String recipe = readString(datain);
            return new MusePacketRecipeUpdate(player, recipe);
        }
    }
}