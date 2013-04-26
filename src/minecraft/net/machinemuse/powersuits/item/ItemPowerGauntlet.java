package net.machinemuse.powersuits.item;

import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.arboriculture.IToolGrafter;
import mods.mffs.api.IFieldTeleporter;
import mods.railcraft.api.core.items.IToolCrowbar;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.tool.GrafterModule;
import net.machinemuse.powersuits.powermodule.tool.MFFSFieldTeleporterModule;
import net.machinemuse.powersuits.powermodule.tool.OmniWrenchModule;
import net.machinemuse.powersuits.powermodule.weapon.MeleeAssistModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import powercrystals.minefactoryreloaded.api.IToolHammer;
import universalelectricity.prefab.implement.IToolConfigurator;

/**
 * Describes the modular power tool.
 *
 * @author MachineMuse
 */
public class ItemPowerGauntlet extends ItemElectricTool
        implements
        IModularItem,
        IToolWrench,
        IToolCrowbar,
        IToolGrafter,
        IToolConfigurator,
        IToolHammer,
        IFieldTeleporter {
    public static int assignedItemID;
    String iconpath = MuseIcon.ICON_PREFIX + "handitem";

    /**
     * Constructor. Takes information from the Config.Items enum.
     */
    public ItemPowerGauntlet() {
        super( // ID
                assignedItemID,
                // Damage bonus, added to the material's damage
                0,
                // Tool material, can be changed if necessary
                EnumToolMaterial.EMERALD,
                // not important since it's private and we will override the
                // getter
                new Block[0] // Block[] BlocksEffectiveAgainst
        );
        setMaxStackSize(1);
        setMaxDamage(0);
        this.damageVsEntity = 1;
        setCreativeTab(Config.getCreativeTab());
        setUnlocalizedName("powerGauntlet");
        LanguageRegistry.addName(this, "Power Gauntlet");
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base,
     * (Quality+1)*2 if correct blocktype, 1.5F if sword
     */
    @Override
    public float getStrVsBlock(ItemStack stack, Block block) {
        return getStrVsBlock(stack, block, 0);
    }

    /**
     * FORGE: Overridden to allow custom tool effectiveness
     */
    @Override
    public float getStrVsBlock(ItemStack stack, Block block, int meta) {
        return 1;
    }

    public static boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
        if (block.blockMaterial.isToolNotRequired()) {
            return true;
        }
        for (IBlockBreakingModule module : ModuleManager.getBlockBreakingModules()) {
            if (MuseItemUtils.itemHasActiveModule(stack, module.getName()) && module.canHarvestBlock(stack, block, meta, player)) {
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(iconpath);
    }

    /**
     * Current implementations of this method in child classes do not use the
     * entry argument beside stack. They just raise the damage on the stack.
     */
    @Override
    public boolean hitEntity(ItemStack stack, EntityLiving entityBeingHit, EntityLiving entityDoingHitting) {
        if (entityDoingHitting instanceof EntityPlayer && MuseItemUtils.itemHasActiveModule(stack, MeleeAssistModule.MODULE_MELEE_ASSIST)) {
            EntityPlayer player = (EntityPlayer) entityDoingHitting;
            double drain = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_ENERGY);
            if (ElectricItemUtils.getPlayerEnergy(player) > drain) {
                ElectricItemUtils.drainPlayerEnergy(player, drain);
                double damage = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_DAMAGE);
                double knockback = ModuleManager.computeModularProperty(stack, MeleeAssistModule.PUNCH_KNOCKBACK);
                DamageSource damageSource = DamageSource.causePlayerDamage(player);
                if (entityBeingHit.attackEntityFrom(damageSource, (int) damage)) {
                    Vec3 lookVec = player.getLookVec();
                    entityBeingHit.addVelocity(lookVec.xCoord * knockback, Math.abs(lookVec.yCoord + 0.2f) * knockback, lookVec.zCoord * knockback);
                }
            }
        }
        return true;
    }

    /**
     * Called when a block is destroyed using this tool.
     * <p/>
     * Returns: Whether to increment player use stats with this item
     */
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityLiving entity) {
        if (entity instanceof EntityPlayer) {
            for (IBlockBreakingModule module : ModuleManager.getBlockBreakingModules()) {
                if (MuseItemUtils.itemHasActiveModule(stack, module.getName())) {
                    if (module.onBlockDestroyed(stack, world, blockID, x, y, z, (EntityPlayer) entity)) {
                        return true;
                    }
                }
            }
        }
        return true;

    }

    /**
     * An itemstack sensitive version of getDamageVsEntity - allows items to
     * handle damage based on
     * itemstack data, like tags. Falls back to getDamageVsEntity.
     *
     * @param par1Entity The entity being attacked (or the attacking mob, if it's a mob
     *                   - vanilla bug?)
     * @param itemStack  The itemstack
     * @return the damage
     */
    @Override
    public int getDamageVsEntity(Entity par1Entity, ItemStack itemStack) {
        return (int) ModuleManager.computeModularProperty(itemStack, MeleeAssistModule.PUNCH_DAMAGE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    /**
     * Returns True is the item is renderer in full 3D when hold.
     */
    public boolean isFull3D() {
        return true;
    }

    /**
     * Return the enchantability factor of the item. In this case, 0. Might add
     * an enchantability module later :P
     */
    @Override
    public int getItemEnchantability() {
        return 0;
    }

    /**
     * Return the name for this tool's material.
     */
    @Override
    public String getToolMaterialName() {
        return this.toolMaterial.toString();
    }

    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
        return false;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 72000;
    }

    /**
     * Called when the right click button is pressed
     */
    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        for (IRightClickModule module : ModuleManager.getRightClickModules()) {
            if (module.isValidForItem(itemStack, player) && MuseItemUtils.itemHasActiveModule(itemStack, module.getName())) {
                module.onRightClick(player, world, itemStack);
            }
        }
        return itemStack;
    }

    /**
     * returns the action that specifies what animation to play when the items
     * is being used
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    /**
     * Called when the right click button is released
     */
    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
        String mode = MuseItemUtils.getActiveMode(itemStack);
        IPowerModule module = ModuleManager.getModule(mode);
        if (module instanceof IRightClickModule) {
            ((IRightClickModule) module).onPlayerStoppedUsing(itemStack, world, player, par4);
        }
    }

    @Override
    public boolean shouldPassSneakingClickToBlock(World world, int x, int y, int z) {
        return true;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world,
                                  int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        String mode = MuseItemUtils.getActiveMode(itemStack);
        IPowerModule module = ModuleManager.getModule(mode);
        if (module instanceof IRightClickModule) {
            return ((IRightClickModule) module).onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        String mode = MuseItemUtils.getActiveMode(itemStack);
        IPowerModule module = ModuleManager.getModule(mode);
        if (module instanceof IRightClickModule) {
            ((IRightClickModule) module).onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
            return false;
        }
        return false;
    }

    /**
     * OmniWrench Module stuff
     */
    public boolean canWrench(EntityPlayer player, int x, int y, int z) {
        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof IModularItem) {
            return MuseItemUtils.itemHasActiveModule(player.getCurrentEquippedItem(), OmniWrenchModule.MODULE_OMNI_WRENCH);
        }
        return false;
    }

    public void wrenchUsed(EntityPlayer player, int x, int y, int z) {

    }

    // Railcraft
    @Override
    public boolean canWhack(EntityPlayer player, ItemStack crowbar, int x, int y, int z) {
        return MuseItemUtils.itemHasActiveModule(crowbar, OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    @Override
    public void onWhack(EntityPlayer player, ItemStack crowbar, int x, int y, int z) {
        player.swingItem();
    }

    @Override
    public boolean canLink(EntityPlayer player, ItemStack crowbar, EntityMinecart cart) {
        return MuseItemUtils.itemHasActiveModule(crowbar, OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    @Override
    public void onLink(EntityPlayer player, ItemStack crowbar, EntityMinecart cart) {
        player.swingItem();
    }

    @Override
    public boolean canBoost(EntityPlayer player, ItemStack crowbar, EntityMinecart cart) {
        return MuseItemUtils.itemHasActiveModule(crowbar, OmniWrenchModule.MODULE_OMNI_WRENCH);
    }

    @Override
    public void onBoost(EntityPlayer player, ItemStack crowbar, EntityMinecart cart) {
        player.swingItem();
    }

    // Grafter Module
    @Override
    public float getSaplingModifier(ItemStack stack, World world, EntityPlayer player, int x, int y, int z) {
        if (MuseItemUtils.itemHasActiveModule(stack, GrafterModule.MODULE_GRAFTER)) {
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, GrafterModule.GRAFTER_ENERGY_CONSUMPTION));
            return 100F;
        }
        return 0F;
    }

    /*
    *
    * MFFS Field Teleporter Module
    *
    */
    public boolean canFieldTeleport(EntityPlayer player, ItemStack stack, int teleportCost) {
        if (MuseItemUtils.itemHasModule(stack, MFFSFieldTeleporterModule.MODULE_FIELD_TELEPORTER)) {
            if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, MFFSFieldTeleporterModule.FIELD_TELEPORTER_ENERGY_CONSUMPTION)) {
                return true;
            }
            else if (FMLCommonHandler.instance().getEffectiveSide().isServer()) {
                player.sendChatToPlayer("[Field Security] Could not teleport through forcefield. 20,000J is required to teleport.");
            }
        }
        return false;
    }

    public void onFieldTeleportSuccess(EntityPlayer player, ItemStack stack, int teleportCost) {
        ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, MFFSFieldTeleporterModule.FIELD_TELEPORTER_ENERGY_CONSUMPTION));
    }

    public void onFieldTeleportFailed(EntityPlayer player, ItemStack stack, int teleportCost) {
    }
}
