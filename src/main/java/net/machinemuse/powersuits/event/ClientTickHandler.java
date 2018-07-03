package net.machinemuse.powersuits.event;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.client.gui.hud.EnergyMeter;
import net.machinemuse.powersuits.client.gui.hud.HeatMeter;
import net.machinemuse.powersuits.client.gui.hud.PlasmaChargeMeter;
import net.machinemuse.powersuits.client.gui.hud.WaterMeter;
import net.machinemuse.powersuits.client.gui.tinker.clickable.ClickableKeybinding;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate;
import net.machinemuse.powersuits.powermodule.armor.WaterTankModule;
import net.machinemuse.powersuits.powermodule.misc.AutoFeederModule;
import net.machinemuse.powersuits.powermodule.misc.ClockModule;
import net.machinemuse.powersuits.powermodule.misc.CompassModule;
import net.machinemuse.powersuits.powermodule.weapon.PlasmaCannonModule;
import net.machinemuse.utils.*;
import net.machinemuse.utils.render.MuseRenderer;
import net.machinemuse.utils.render.PlasmaUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Ported to Java by lehjr on 10/24/16.
 */
public class ClientTickHandler {
    /**
     * This handler is called before/after the game processes input events and
     * updates the gui state mainly. *independent of rendering, so don't do rendering here
     * -is also the parent class of KeyBindingHandleryBaseIcon
     *
     * @author MachineMuse
     */

    public ArrayList<String> modules;
    private boolean drawWaterMeter = false;
    private boolean drawPlasmaMeter = false;

    @SubscribeEvent
    public void onPreClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (ClickableKeybinding kb : KeybindManager.getKeybindings()) {
                kb.doToggleTick();
            }
        } else {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player != null && MuseItemUtils.getModularItemsInInventory(player).size() > 0) {
                PlayerInputMap inputmap = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
                inputmap.forwardKey = Math.signum(player.movementInput.moveForward);
                inputmap.strafeKey = Math.signum(player.movementInput.moveStrafe);
                inputmap.jumpKey = player.movementInput.jump;
                inputmap.sneakKey = player.movementInput.sneak;
                inputmap.motionX = player.motionX;
                inputmap.motionY = player.motionY;
                inputmap.motionZ = player.motionZ;
                if (inputmap.hasChanged()) {
                    inputmap.refresh();
                    MusePacket inputPacket = new MusePacketPlayerUpdate(player, inputmap);
                    PacketSender.sendToServer(inputPacket);
                }
            }
        }
    }

    public void findInstalledModules(EntityPlayer player) {
        if (player != null) {
            ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (helmet != null && helmet.getItem() instanceof ItemPowerArmorHelmet) {
                if (ModuleManager.itemHasActiveModule(helmet, AutoFeederModule.MODULE_AUTO_FEEDER)) {
                    modules.add(AutoFeederModule.MODULE_AUTO_FEEDER);
                }
                if (ModuleManager.itemHasActiveModule(helmet, ClockModule.MODULE_CLOCK)) {
                    modules.add(ClockModule.MODULE_CLOCK);
                }
                if (ModuleManager.itemHasActiveModule(helmet, CompassModule.MODULE_COMPASS)) {
                    modules.add(CompassModule.MODULE_COMPASS);
                }
            }

            ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (chest != null && chest.getItem() instanceof ItemPowerArmorChestplate) {
                if (ModuleManager.itemHasActiveModule(chest, WaterTankModule.MODULE_WATER_TANK)) {
                    modules.add(WaterTankModule.MODULE_WATER_TANK);
                }
            }

            ItemStack powerfist = player.getHeldItemMainhand();
            if (powerfist != null && powerfist.getItem() instanceof ItemPowerFist) {
                if (ModuleManager.itemHasActiveModule(powerfist, PlasmaCannonModule.MODULE_PLASMA_CANNON))
                    modules.add(PlasmaCannonModule.MODULE_PLASMA_CANNON);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        ItemStack food = new ItemStack(Items.COOKED_BEEF);
        ItemStack clock = new ItemStack(Items.CLOCK);
        ItemStack compass = new ItemStack(Items.COMPASS);

        int yOffsetString = 18;
        double yOffsetIcon = 16.0;
        String ampm;

        double yBaseIcon;
        int yBaseString;
        if (Config.useGraphicalMeters()) {
            yBaseIcon = 150.0;
            yBaseString = 155;
        } else {
            yBaseIcon = 26.0;
            yBaseString = 32;
        }

        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            modules = new ArrayList<String>();
            findInstalledModules(player);
            if (player != null && Minecraft.getMinecraft().isGuiEnabled() && MuseItemUtils.modularItemsEquipped(player).size() > 0 && Minecraft.getMinecraft().currentScreen == null) {
                Minecraft mc = Minecraft.getMinecraft();
                ScaledResolution screen = new ScaledResolution(mc);
                for (int i = 0; i < modules.size(); i++) {
                    if (Objects.equals(modules.get(i), AutoFeederModule.MODULE_AUTO_FEEDER)) {
                        int foodLevel = (int) MuseItemUtils.getFoodLevel(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
                        String num = MuseStringUtils.formatNumberShort(foodLevel);
                        if (i == 0) {
                            MuseRenderer.drawString(num, 17, yBaseString);
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon, food);
                        } else {
                            MuseRenderer.drawString(num, 17, yBaseString + (yOffsetString * i));
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), food);
                        }
                    } else if (Objects.equals(modules.get(i), ClockModule.MODULE_CLOCK)) {
                        long time = player.world.provider.getWorldTime();
                        long hour = ((time % 24000) / 1000);
                        if (Config.use24hClock()) {
                            if (hour < 19) {
                                hour += 6;
                            } else {
                                hour -= 18;
                            }
                            ampm = "h";
                        } else {
                            if (hour < 6) {
                                hour += 6;
                                ampm = " AM";
                            } else if (hour == 6) {
                                hour = 12;
                                ampm = " PM";
                            } else if (hour > 6 && hour < 18) {
                                hour -= 6;
                                ampm = " PM";
                            } else if (hour == 18) {
                                hour = 12;
                                ampm = " AM";
                            } else {
                                hour -= 18;
                                ampm = " AM";
                            }
                        }
                        if (i == 0) {
                            MuseRenderer.drawString(hour + ampm, 17, yBaseString);
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon, clock);
                        } else {
                            MuseRenderer.drawString(hour + ampm, 17, yBaseString + (yOffsetString * i));
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), clock);
                        }
                    } else if (Objects.equals(modules.get(i), CompassModule.MODULE_COMPASS)) {
                        if (i == 0) {
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon, compass);
                        } else {
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), compass);
                        }
                    } else if (Objects.equals(modules.get(i), WaterTankModule.MODULE_WATER_TANK)) {
                        drawWaterMeter = true;
                    }
                    else if (Objects.equals(modules.get(i), PlasmaCannonModule.MODULE_PLASMA_CANNON)) {
                        drawPlasmaMeter = true;
                    }
                }
                drawMeters(player, screen);
            }
        }
    }

    protected HeatMeter heat = null;
    protected HeatMeter energy = null;
    protected WaterMeter water = null;
    protected PlasmaChargeMeter plasma = null;

    private void drawMeters(EntityPlayer player, ScaledResolution screen) {
        double top = (double)screen.getScaledHeight() / 2.0 - (double)16;
//    	double left = screen.getScaledWidth() - 2;
    	double left = screen.getScaledWidth() - 34;

        // energy
        double maxEnergy = ElectricItemUtils.getMaxEnergy(player);
        double currEnergy = ElectricItemUtils.getPlayerEnergy(player);
        String currEnergyStr = MuseStringUtils.formatNumberShort(currEnergy) + "J";
        String maxEnergyStr = MuseStringUtils.formatNumberShort(maxEnergy);

        // heat
        double maxHeat = MuseHeatUtils.getMaxHeat(player);
        double currHeat = MuseHeatUtils.getPlayerHeat(player);
        String currHeatStr = MuseStringUtils.formatNumberShort(currHeat);
        String maxHeatStr = MuseStringUtils.formatNumberShort(maxHeat);

        // water
        double maxWater = WaterUtils.getMaxWater(player);
        double currWater = WaterUtils.getPlayerWater(player);
        String currWaterStr = MuseStringUtils.formatNumberShort(currWater);
        String maxWaterStr = MuseStringUtils.formatNumberShort(maxWater);

        // plasma
        double maxPlasma = PlasmaUtils.getMaxPlasma(player);
        double currPlasma = PlasmaUtils.getPlayerPlasma(player);
        String currPlasmaStr = MuseStringUtils.formatNumberShort(currPlasma);
        String maxPlasmaStr = MuseStringUtils.formatNumberShort(maxPlasma);

        if (Config.useGraphicalMeters()) {
            int numMeters = 1;

            if (maxEnergy > 0) {
                numMeters ++;
                if (energy == null) {
                    energy = new EnergyMeter();
                }
            } else energy = null;

            if (heat == null) heat = new HeatMeter();

            if (maxWater > 0 && drawWaterMeter ) {
                numMeters ++;
                if (water == null) {
                    water = new WaterMeter();
                }
            } else water = null;

            if (maxPlasma > 0 && drawPlasmaMeter ){
                numMeters ++;
                if (plasma == null) {
                    plasma = new PlasmaChargeMeter();
                }
            } else plasma = null;

//          double stringX = left - (double)(numMeters * 8) - 2;
            double stringX = left - 2;
            final int totalMeters = numMeters;
            //"(totalMeters-numMeters) * 8" = 0 for whichever of these is first,
            //but including it won't hurt and this makes it easier to swap them around.

            if (energy != null) {
//        	    energy.draw(left - (numMeters * 8), top, currEnergy / maxEnergy);
                energy.draw(left, top + (totalMeters-numMeters) * 8, currEnergy / maxEnergy);
                MuseRenderer.drawRightAlignedString(currEnergyStr, stringX  , top);
                numMeters --;
            }

// 	    	heat.draw(left - (numMeters * 8), top, currHeat / maxHeat);
            heat.draw(left, top + (totalMeters-numMeters) * 8, MuseMathUtils.clampDouble(currHeat, 0, maxHeat) / maxHeat);
            MuseRenderer.drawRightAlignedString(currHeatStr, stringX, top + (totalMeters-numMeters) * 8);
            numMeters --;

            if (water != null) {
//                water.draw(left - (numMeters * 8), top, currWater / maxWater);
                water.draw(left, top + (totalMeters-numMeters) * 8, currWater / maxWater);
                MuseRenderer.drawRightAlignedString(currWaterStr, stringX, top + (totalMeters-numMeters) * 8);
                numMeters --;
            }

            if (plasma != null) {
//    	    	plasma.draw(left - (numMeters * 8), top, currPlasma / maxPlasma);
                plasma.draw(left, top + (totalMeters-numMeters) * 8, currPlasma / maxPlasma);
                MuseRenderer.drawRightAlignedString(currPlasmaStr, stringX, top + (totalMeters-numMeters) * 8);
            }

        } else {
            int numReadouts = 0;
            if (maxEnergy > 0) {
                MuseRenderer.drawString(currEnergyStr + '/' + maxEnergyStr + " \u1D60", 2, 2 );
                numReadouts += 1;
            }

            MuseRenderer.drawString(currHeatStr + '/' + maxHeatStr + " C", 2, 2 + (numReadouts * 9));
            numReadouts += 1;

            if (maxWater > 0 && drawWaterMeter ) {
                MuseRenderer.drawString(currWaterStr + '/' + maxWaterStr + " buckets", 2, 2 + (numReadouts * 9));
                numReadouts += 1;
            }

            if (maxPlasma > 0 && drawPlasmaMeter ) {
                MuseRenderer.drawString(currPlasmaStr + '/' + maxPlasmaStr + "%", 2, 2 + (numReadouts * 9));
            }
        }
    }
}