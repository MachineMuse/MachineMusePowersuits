package net.machinemuse.powersuits.network;

import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.item.IModule;
import net.machinemuse.numina.api.nbt.IPropertyModifier;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.powersuits.common.items.modules.PropertyModifierFlatAdditive;
import net.machinemuse.powersuits.common.items.modules.PropertyModifierLinearAdditive;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.DataInputStream;
import java.util.List;
import java.util.Map;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:23 PM, 12/05/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketPropertyModifierConfig extends MusePacket {
    EntityPlayer player;
    DataInputStream data;

    public MusePacketPropertyModifierConfig(EntityPlayer player, DataInputStream data) {
        this.player= player;
        this.data = data;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeInt(ModuleManager.getAllModules().size());
        for (IModule module : ModuleManager.getAllModules()) {
            writeString(module.getDataName());
            writeBoolean(module.isAllowed());
            writeInt(module.getPropertyModifiers().size());
            for (Map.Entry<String, List<IPropertyModifier>> entry: module.getPropertyModifiers().entrySet()) {
                writeString(entry.getKey()); // propertyName
                List<IPropertyModifier> propmodlist = entry.getValue();
                writeInt(propmodlist.size());
                for (IPropertyModifier propmod : propmodlist) {
                    if (propmod instanceof PropertyModifierFlatAdditive)
                        writeDouble(((PropertyModifierFlatAdditive) propmod).valueAdded);
                    else if (propmod instanceof PropertyModifierLinearAdditive)
                        writeDouble(((PropertyModifierLinearAdditive) propmod).multiplier);
                    else writeDouble(0);
                }
            }
        }
    }

    /**
     * Called by the network manager since it does all the packet mapping
     *
     * @param player
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void handleClient(EntityPlayer player) {
        MusePackager d = MusePacketPropertyModifierConfigPackager.getInstance();
        int numModules = d.readInt(data);
        for (int i = 0; i < numModules; i++) {
            String moduleName = d.readString(data);
            boolean allowed = d.readBoolean(data);
            IModule module = ModuleManager.getModule(moduleName);
            if (module instanceof PowerModuleBase)
                ((PowerModuleBase) module).setIsAllowed(allowed);
            int numProps = d.readInt(data);
            for (int j = 0; j < numProps; j++ ) {
                String propName = d.readString(data);
                int numModifiers = d.readInt(data);
                List<IPropertyModifier> proplist = module.getPropertyModifiers().get(propName);
                for (int m = 0; m < numModifiers; m++) {
                    IPropertyModifier propMod = proplist.get(m);
                    if (propMod instanceof PropertyModifierFlatAdditive)
                        ((PropertyModifierFlatAdditive) propMod).valueAdded = d.readDouble(data);
                    else if (propMod instanceof PropertyModifierLinearAdditive)
                        ((PropertyModifierLinearAdditive) propMod).multiplier = d.readDouble(data);
                    else d.readDouble(data);
                }
            }
        }
    }

    private static MusePacketPropertyModifierConfigPackager PACKAGERINSTANCE;
    public static MusePacketPropertyModifierConfigPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null) {
            synchronized (MusePacketPropertyModifierConfigPackager.class) {
                if (PACKAGERINSTANCE == null) {
                    PACKAGERINSTANCE = new MusePacketPropertyModifierConfigPackager();
                }
            }
        }
        return PACKAGERINSTANCE;
    }

    public static class MusePacketPropertyModifierConfigPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            return new MusePacketPropertyModifierConfig(player, datain);
        }
    }
}
