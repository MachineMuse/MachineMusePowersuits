package net.machinemuse.powersuits.event;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.IModularItem;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.event.ForgeSubscribe;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.IAspectSource;
import thaumcraft.api.ObjectTags;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ThaumRenderEventHandler {
    private static Class c_eventHandler;
    private static Class c_guiTicker;
    private static Object o_eventHandler;
    private static Object o_guiTicker;
    private static Method m_drawTagOnJar;
    private static Method m_renderAuraNodes;
    private static Method m_renderGogglesHUD_eventHandler;
    private static Method m_renderGogglesHUD_guiTicker;
    private static Field f_tagscale;

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void renderLast(RenderWorldLastEvent event) {
        float tagscale = getTagScale();
        if (tagscale > 0.0F) setTagScale(tagscale - 0.005F);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player != null && player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() instanceof IModularItem) {
            ItemStack stack = player.inventory.armorItemInSlot(3);
            if (MuseItemUtils.itemHasModule(stack, "Aurameter")) {
                renderAuraNodes(event, event.partialTicks, player, Minecraft.getMinecraft().theWorld.getWorldTime());
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void blockHighlight(DrawBlockHighlightEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        if (player != null && player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() instanceof IModularItem) {
            ItemStack stack = player.inventory.armorItemInSlot(3);
            if (MuseItemUtils.itemHasModule(stack, "Aurameter")) {
                drawTags(event.target, player.worldObj, event.partialTicks);
            }
        }
    }

    public static void drawTags(MovingObjectPosition target, World world, float partialTicks) {
        int x = target.blockX;
        int y = target.blockY;
        int z = target.blockZ;
        float tagscale = getTagScale();

        TileEntity entity = world.getBlockTileEntity(x, y, z);
        if (entity instanceof IAspectSource) {
            ObjectTags tags = ((IAspectSource) entity).getSourceTags();
            if (tags.size() > 0) {
                if (tagscale < 0.3F) setTagScale(tagscale * 0.9f + 0.031F);
                drawTagOnJar(x, y + 0.4F, z, tags, 220, ForgeDirection.UP, partialTicks);
            }

        }
    }

    public static void populateReflections() {
        try {
            if (c_eventHandler == null) {
                c_eventHandler = Class.forName("thaumcraft.client.lib.RenderEventHandler");
                o_eventHandler = c_eventHandler.newInstance();
            }
            if (m_drawTagOnJar == null) {
                m_drawTagOnJar = c_eventHandler.getDeclaredMethod("drawTagOnJar", Double.TYPE, Double.TYPE, Double.TYPE, ObjectTags.class, Integer.TYPE, ForgeDirection.class, Float.TYPE);
            }
            if (m_renderAuraNodes == null) {
                m_renderAuraNodes = c_eventHandler.getDeclaredMethod("renderAuraNodes", RenderWorldLastEvent.class, Float.TYPE, EntityPlayer.class, Long.TYPE);
            }
            if (m_renderGogglesHUD_eventHandler == null) {
                m_renderGogglesHUD_eventHandler = c_eventHandler.getDeclaredMethod("renderGogglesHUD", RenderWorldLastEvent.class, Float.TYPE, EntityPlayer.class, Long.TYPE);
            }
            if (f_tagscale == null) {
                f_tagscale = c_eventHandler.getField("tagscale");
            }
            if (c_guiTicker == null) {
                c_guiTicker = Class.forName("thaumcraft.client.lib.GUITicker");
            }
            if (m_renderGogglesHUD_guiTicker == null) {
                m_renderGogglesHUD_guiTicker = c_guiTicker.getDeclaredMethod("renderGogglesHUD", Float.TYPE, EntityPlayer.class, Long.TYPE);
                o_guiTicker = c_guiTicker.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void drawTagOnJar(double x, double y, double z, ObjectTags tags, int bright, ForgeDirection dir, float partialTicks) {
        populateReflections();
        try {
            m_drawTagOnJar.invoke(o_eventHandler, x, y, z, tags, bright, dir, partialTicks);
        } catch (Exception e) {
        }
    }

    public static void renderGogglesHUD(RenderWorldLastEvent event, float partialTicks, EntityPlayer player, long time) {
        populateReflections();
        try {
            m_renderGogglesHUD_eventHandler.invoke(o_eventHandler, event, partialTicks, player, time);
        } catch (Exception e) {
        }
    }

    public static void renderGogglesHUD(float partialTicks, EntityPlayer player, long time) {
        populateReflections();
        try {
            m_renderGogglesHUD_guiTicker.invoke(o_guiTicker, partialTicks, player, time);
        } catch (Exception e) {
        }
    }

    public static void renderAuraNodes(RenderWorldLastEvent event, float partialTicks, EntityPlayer player, long time) {
        populateReflections();
        GL11.glPushMatrix();
        GL11.glDepthMask(false);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        GL11.glDisable(2884);
        GL11.glDisable(2896);
        try {
            m_renderAuraNodes.invoke(o_eventHandler, event, partialTicks, player, time);
        } catch (Exception e) {
        }
        GL11.glDisable(3042);
        GL11.glEnable(2896);
        GL11.glDepthMask(true);

        GL11.glPopMatrix();
    }

    public static void setTagScale(float f) {
        populateReflections();
        try {
            f_tagscale.set(o_eventHandler, f);
        } catch (Exception e) {
        }
    }

    public static float getTagScale() {
        populateReflections();
        try {
            return (Float) (f_tagscale.get(o_eventHandler));
        } catch (Exception e) {
        }
        return 0;
    }
}
