package net.machinemuse.powersuits.client.event;

import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.heat.MuseHeatUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.string.MuseStringUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.gui.hud.*;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableKeybinding;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.machinemuse.powersuits.utils.modulehelpers.AutoFeederHelper;
import net.machinemuse.powersuits.utils.modulehelpers.FluidUtils;
import net.machinemuse.powersuits.utils.modulehelpers.PlasmaCannonHelper;
import net.minecraft.client.Minecraft;
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
    protected HeatMeter heat = null;
    protected HeatMeter energy = null;
    protected WaterMeter water = null;
    protected FluidMeter fluidMeter = null;
    protected PlasmaChargeMeter plasma = null;
    private FluidUtils waterUtils;
    private FluidUtils fluidUtils;

    @SubscribeEvent
    public void onPreClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            for (ClickableKeybinding kb : KeybindManager.getKeybindings()) {
                kb.doToggleTick();
            }
        }
    }

    public void findInstalledModules(EntityPlayer player) {
        if (player != null) {
            ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
            if (!helmet.isEmpty() && helmet.getItem() instanceof ItemPowerArmorHelmet) {
                if (ModuleManager.INSTANCE.itemHasActiveModule(helmet, MPSModuleConstants.MODULE_AUTO_FEEDER__DATANAME)) {
                    modules.add(MPSModuleConstants.MODULE_AUTO_FEEDER__DATANAME);
                }
                if (ModuleManager.INSTANCE.itemHasActiveModule(helmet, MPSModuleConstants.MODULE_CLOCK__DATANAME)) {
                    modules.add(MPSModuleConstants.MODULE_CLOCK__DATANAME);
                }
                if (ModuleManager.INSTANCE.itemHasActiveModule(helmet, MPSModuleConstants.MODULE_COMPASS__DATANAME)) {
                    modules.add(MPSModuleConstants.MODULE_COMPASS__DATANAME);
                }
            }

            ItemStack chest = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (!chest.isEmpty() && chest.getItem() instanceof ItemPowerArmorChestplate) {
                if (ModuleManager.INSTANCE.itemHasActiveModule(chest, MPSModuleConstants.MODULE_BASIC_COOLING_SYSTEM__DATANAME)) {
                    modules.add(MPSModuleConstants.MODULE_BASIC_COOLING_SYSTEM__DATANAME);
                }

                if (ModuleManager.INSTANCE.itemHasActiveModule(chest, MPSModuleConstants.MODULE_ADVANCED_COOLING_SYSTEM__DATANAME)) {
                    modules.add(MPSModuleConstants.MODULE_ADVANCED_COOLING_SYSTEM__DATANAME);
                }
            }

            ItemStack powerfist = player.getHeldItemMainhand();
            if (!powerfist.isEmpty() && powerfist.getItem() instanceof ItemPowerFist) {
                if (ModuleManager.INSTANCE.itemHasActiveModule(powerfist, MPSModuleConstants.MODULE_PLASMA_CANNON__DATANAME))
                    modules.add(MPSModuleConstants.MODULE_PLASMA_CANNON__DATANAME);
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
        if (MPSConfig.INSTANCE.useGraphicalMeters()) {
            yBaseIcon = 150.0;
            yBaseString = 155;
        } else {
            yBaseIcon = 26.0;
            yBaseString = 32;
        }

        if (event.phase == TickEvent.Phase.END) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            modules = new ArrayList<>();
            findInstalledModules(player);
            if (player != null && Minecraft.getMinecraft().isGuiEnabled() && MuseItemUtils.getModularItemsEquipped(player).size() > 0 && Minecraft.getMinecraft().currentScreen == null) {
                Minecraft mc = Minecraft.getMinecraft();
                ScaledResolution screen = new ScaledResolution(mc);
                for (int i = 0; i < modules.size(); i++) {
                    if (Objects.equals(modules.get(i), MPSModuleConstants.MODULE_AUTO_FEEDER__DATANAME)) {
                        int foodLevel = (int) AutoFeederHelper.getFoodLevel(player.getItemStackFromSlot(EntityEquipmentSlot.HEAD));
                        String num = MuseStringUtils.formatNumberShort(foodLevel);
                        if (i == 0) {
                            MuseRenderer.drawString(num, 17, yBaseString);
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon, food);
                        } else {
                            MuseRenderer.drawString(num, 17, yBaseString + (yOffsetString * i));
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), food);
                        }
                    } else if (Objects.equals(modules.get(i), MPSModuleConstants.MODULE_CLOCK__DATANAME)) {
                        long time = player.world.provider.getWorldTime();
                        long hour = ((time % 24000) / 1000);
                        if (MPSConfig.INSTANCE.use24hClock()) {
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
                    } else if (Objects.equals(modules.get(i), MPSModuleConstants.MODULE_COMPASS__DATANAME)) {
                        if (i == 0) {
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon, compass);
                        } else {
                            MuseRenderer.drawItemAt(-1.0, yBaseIcon + (yOffsetIcon * i), compass);
                        }
                    } else if (Objects.equals(modules.get(i), MPSModuleConstants.MODULE_BASIC_COOLING_SYSTEM__DATANAME)) {
                        waterUtils = new FluidUtils(player, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), MPSModuleConstants.MODULE_BASIC_COOLING_SYSTEM__DATANAME);
                    } else if (Objects.equals(modules.get(i), MPSModuleConstants.MODULE_ADVANCED_COOLING_SYSTEM__DATANAME)) {
                        fluidUtils = new FluidUtils(player, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), MPSModuleConstants.MODULE_ADVANCED_COOLING_SYSTEM__DATANAME);
                    }
                }
                drawMeters(player, screen);
            }
        }
    }

    private void drawMeters(EntityPlayer player, ScaledResolution screen) {
        double top = (double) screen.getScaledHeight() / 2.0 - (double) 16;
//    	double left = screen.getScaledWidth() - 2;
        double left = screen.getScaledWidth() - 34;

        // energy
        double maxEnergy = ElectricItemUtils.getMaxPlayerEnergy(player);
        double currEnergy = ElectricItemUtils.getPlayerEnergy(player);
        String currEnergyStr = MuseStringUtils.formatNumberShort(currEnergy) + "RF";
        String maxEnergyStr = MuseStringUtils.formatNumberShort(maxEnergy);

        // heat
        double maxHeat = MuseHeatUtils.getPlayerMaxHeat(player);
        double currHeat = MuseHeatUtils.getPlayerHeat(player);
        String currHeatStr = MuseStringUtils.formatNumberShort(currHeat);
        String maxHeatStr = MuseStringUtils.formatNumberShort(maxHeat);

        // Water
        double maxWater = 0;
        double currWater = 0;
        String currWaterStr = "";
        String maxWaterStr = "";

        if (waterUtils != null) {
            maxWater = waterUtils.getMaxFluidLevel();
            currWater = waterUtils.getFluidLevel();
            currWaterStr = MuseStringUtils.formatNumberShort(currWater);
            maxWaterStr = MuseStringUtils.formatNumberShort(maxWater);
        }

        // Fluid
        double maxFluid = 0;
        double currFluid = 0;
        String currFluidStr = "";
        String maxFluidStr = "";

        if (ModuleManager.INSTANCE.itemHasModule(player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), MPSModuleConstants.MODULE_ADVANCED_COOLING_SYSTEM__DATANAME)) {
            fluidUtils = new FluidUtils(player, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST), MPSModuleConstants.MODULE_ADVANCED_COOLING_SYSTEM__DATANAME);
            maxFluid = fluidUtils.getMaxFluidLevel();
            currFluid = fluidUtils.getFluidLevel();
            currFluidStr = MuseStringUtils.formatNumberShort(currWater);
            maxFluidStr = MuseStringUtils.formatNumberShort(maxWater);
        }

        // plasma
        double maxPlasma = PlasmaCannonHelper.getMaxPlasma(player);
        double currPlasma = PlasmaCannonHelper.getPlayerPlasma(player);
        String currPlasmaStr = MuseStringUtils.formatNumberShort(currPlasma);
        String maxPlasmaStr = MuseStringUtils.formatNumberShort(maxPlasma);

        if (MPSConfig.INSTANCE.useGraphicalMeters()) {
            int numMeters = 0;

            if (maxEnergy > 0) {
                numMeters++;
                if (energy == null) {
                    energy = new EnergyMeter();
                }
            } else energy = null;

            if (maxHeat > 0) {
                numMeters++;
                if (heat == null)
                    heat = new HeatMeter();
            } else heat = null;

            if (maxWater > 0) {
                numMeters++;
                if (water == null) {
                    water = new WaterMeter();
                }
            } else water = null;

            if (maxFluid > 0) {
                numMeters++;
                if (fluidMeter == null) {
                    fluidMeter = fluidUtils.getFluidMeter();
                }
            }

            if (maxPlasma > 0 /* && drawPlasmaMeter */) {
                numMeters++;
                if (plasma == null) {
                    plasma = new PlasmaChargeMeter();
                }
            } else plasma = null;

            double stringX = left - 2;
            final int totalMeters = numMeters;
            //"(totalMeters-numMeters) * 8" = 0 for whichever of these is first,
            //but including it won't hurt and this makes it easier to swap them around.

            if (energy != null) {
                energy.draw(left, top + (totalMeters - numMeters) * 8, currEnergy / maxEnergy);
                MuseRenderer.drawRightAlignedString(currEnergyStr, stringX, top);
                numMeters--;
            }

            heat.draw(left, top + (totalMeters - numMeters) * 8, MuseMathUtils.clampDouble(currHeat, 0, maxHeat) / maxHeat);
            MuseRenderer.drawRightAlignedString(currHeatStr, stringX, top + (totalMeters - numMeters) * 8);
            numMeters--;

            if (water != null) {
                water.draw(left, top + (totalMeters - numMeters) * 8, currWater / maxWater);
                MuseRenderer.drawRightAlignedString(currWaterStr, stringX, top + (totalMeters - numMeters) * 8);
                numMeters--;
            }

            if (fluidMeter != null) {
                fluidMeter.draw(left, top + (totalMeters - numMeters) * 8, currFluid / maxFluid);
                MuseRenderer.drawRightAlignedString(currFluidStr, stringX, top + (totalMeters - numMeters) * 8);
                numMeters--;
            }

            if (plasma != null) {
                plasma.draw(left, top + (totalMeters - numMeters) * 8, currPlasma / maxPlasma);
                MuseRenderer.drawRightAlignedString(currPlasmaStr, stringX, top + (totalMeters - numMeters) * 8);
            }

        } else {
            int numReadouts = 0;
            if (maxEnergy > 0) {
                MuseRenderer.drawString(currEnergyStr + '/' + maxEnergyStr + " \u1D60", 2, 2);
                numReadouts += 1;
            }

            MuseRenderer.drawString(currHeatStr + '/' + maxHeatStr + " C", 2, 2 + (numReadouts * 9));
            numReadouts += 1;

            if (maxWater > 0) {
                MuseRenderer.drawString(currWaterStr + '/' + maxWaterStr + " buckets", 2, 2 + (numReadouts * 9));
                numReadouts += 1;
            }

            if (maxFluid > 0) {
                MuseRenderer.drawString(currFluidStr + '/' + maxFluidStr + " buckets", 2, 2 + (numReadouts * 9));
                numReadouts += 1;
            }

            if (maxPlasma > 0 /* && drawPlasmaMeter */) {
                MuseRenderer.drawString(currPlasmaStr + '/' + maxPlasmaStr + "%", 2, 2 + (numReadouts * 9));
            }
        }
    }
}