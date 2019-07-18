package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.fluid.BlockFluidLiquidNitrogen;
import net.machinemuse.powersuits.fluid.LiquidNitrogen;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

/**
 * Created by Claire Semple on 9/9/2014.
 * <p>
 * Ported to Java by lehjr on 10/22/16.
 */
@Mod.EventBusSubscriber(modid = MODID)
public enum MPSItems {
    INSTANCE;

    // Armor --------------------------------------------------------------------------------------
    public static final String powerArmorHelmetRegName = MODID + ":powerarmor_head";
    public static final String powerArmorChestPlateRegName = MODID + ":powerarmor_torso";
    public static final String powerArmorLeggingsRegName = MODID + ":powerarmor_legs";
    public static final String powerArmorBootsRegName = MODID + ":powerarmor_feet";

    @GameRegistry.ObjectHolder(powerArmorHelmetRegName)
    public static final ItemPowerArmorHelmet powerArmorHead = null;
    @GameRegistry.ObjectHolder(powerArmorChestPlateRegName)
    public static final ItemPowerArmorChestplate powerArmorTorso = null;
    @GameRegistry.ObjectHolder(powerArmorLeggingsRegName)
    public static final ItemPowerArmorLeggings powerArmorLegs = null;
    @GameRegistry.ObjectHolder(powerArmorBootsRegName)
    public static final ItemPowerArmorBoots powerArmorFeet = null;

    // HandHeld -----------------------------------------------------------------------------------
    public static final String powerFistRegName = MODID + ":power_fist";

    @GameRegistry.ObjectHolder(powerFistRegName)
    public static final ItemPowerFist powerFist = null;

    // Components ---------------------------------------------------------------------------------
    public static final String componentsRegname = MODID + ":powerarmorcomponent";

    @GameRegistry.ObjectHolder(componentsRegname)
    public static final ItemComponent components = null;

    // Blocks -------------------------------------------------------------------------------------
    public static final String tinkerTableRegName = MODID + ":tinkertable";
    public static final String luxCapaRegName = MODID + ":luxcapacitor";

    @GameRegistry.ObjectHolder(tinkerTableRegName)
    public static final BlockTinkerTable tinkerTable = null;

    @GameRegistry.ObjectHolder(luxCapaRegName)
    public static final BlockLuxCapacitor luxCapacitor = null;

    // Fluid --------------------------------------------------------------------------------------
    public static final LiquidNitrogen liquidNitrogen = new LiquidNitrogen();

    public static final String blockLiquidNitrogenName = MODID + ":liquid_nitrogen";
    @GameRegistry.ObjectHolder(blockLiquidNitrogenName)
    public static final BlockFluidLiquidNitrogen blockLiquidNitrogen = null;

    @SubscribeEvent
    public static void regigisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                // Armor --------------------------------------------------------------------------------------
                new ItemPowerArmorHelmet(powerArmorHelmetRegName),
                new ItemPowerArmorChestplate(powerArmorChestPlateRegName),
                new ItemPowerArmorLeggings(powerArmorLeggingsRegName),
                new ItemPowerArmorBoots(powerArmorBootsRegName),

                // HandHeld -----------------------------------------------------------------------------------
                new ItemPowerFist(powerFistRegName),

                // Components ---------------------------------------------------------------------------------
                new ItemComponent(componentsRegname),

//                // ItemBlocks ---------------------------------------------------------------------------------
                new ItemBlock(tinkerTable).setRegistryName(new ResourceLocation(tinkerTableRegName)),
                new ItemBlock(luxCapacitor).setRegistryName(new ResourceLocation(luxCapaRegName))
        );

        ItemComponent temp = (ItemComponent) event.getRegistry().getValue(new ResourceLocation(componentsRegname));
        if (temp != null)
            temp.registerOres();
    }

    @SubscribeEvent
    public static void initBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new BlockTinkerTable( new ResourceLocation(tinkerTableRegName)));
        event.getRegistry().register(new BlockLuxCapacitor(new ResourceLocation(luxCapaRegName)));

        event.getRegistry().register(new BlockFluidLiquidNitrogen(new ResourceLocation(blockLiquidNitrogenName)));
    }

    static boolean alreadyRegistered = true;
    public static void initFluids() {
        if (!FluidRegistry.isFluidRegistered("liquidnitrogen") && !FluidRegistry.isFluidRegistered("liquid_nitrogen")) {
            FluidRegistry.registerFluid(liquidNitrogen);
            FluidRegistry.addBucketForFluid(liquidNitrogen);
            alreadyRegistered = false;
        }
    }
}
