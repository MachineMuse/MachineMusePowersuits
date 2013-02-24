package net.machinemuse.powersuits.event;

import java.lang.reflect.Method;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.common.MuseLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ThaumRenderEventHandler {
	private Method thaumGogglesHUDRendererEvent;
	private Method thaumGogglesHUDRendererGUI;
	private Method thaumAuraWorldRenderer;
	private Object thaumcraftRenderEventHandler;
	private Object thaumcraftGUITicker;

	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void renderLast(RenderWorldLastEvent event) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if (player != null && player.inventory.armorItemInSlot(3) != null && player.inventory.armorItemInSlot(3).getItem() instanceof IModularItem) {
			ItemStack stack = player.inventory.armorItemInSlot(3);
			if (MuseItemUtils.itemHasModule(stack, "Aurameter")) {
				renderThaumGui(event, event.partialTicks, player, Minecraft.getMinecraft().theWorld.getWorldTime());
			}
		}
	}

	public void renderThaumGui(RenderWorldLastEvent event, float partialTicks, EntityPlayer player, long time) {
		try {
			if (thaumcraftRenderEventHandler == null || thaumGogglesHUDRendererEvent == null || thaumAuraWorldRenderer == null
					|| thaumGogglesHUDRendererGUI == null
					&& ModCompatability.isThaumCraftLoaded()) {
				Class eventHandler = Class.forName("thaumcraft.client.RenderEventHandler");
				Class guiTicker = Class.forName("thaumcraft.client.GUITicker");
				thaumcraftRenderEventHandler = eventHandler.getConstructor().newInstance();
				thaumcraftGUITicker = guiTicker.getConstructor().newInstance();
				thaumGogglesHUDRendererEvent = eventHandler.getMethod("renderGogglesHUD", RenderWorldLastEvent.class, float.class,
						EntityPlayer.class,
						long.class);
				thaumGogglesHUDRendererGUI = guiTicker.getMethod("renderGogglesHUD", float.class, EntityPlayer.class, long.class);
				thaumAuraWorldRenderer = eventHandler.getMethod("renderAuraNodes", RenderWorldLastEvent.class, float.class, EntityPlayer.class,
						long.class);
			}
			GL11.glPushMatrix();
			GL11.glDepthMask(false);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 1);
			GL11.glDisable(2884);
			GL11.glDisable(2896);
			thaumGogglesHUDRendererGUI.invoke(thaumcraftGUITicker, partialTicks, player, time);
			thaumGogglesHUDRendererEvent.invoke(thaumcraftRenderEventHandler, event, partialTicks, player, time);
			thaumAuraWorldRenderer.invoke(thaumcraftRenderEventHandler, event, partialTicks, player, time);
			GL11.glDisable(3042);
			GL11.glEnable(2896);
			GL11.glDepthMask(true);

			GL11.glPopMatrix();
		} catch (Exception e) {
			MuseLogger.logError("Problem loading Thaumcraft classes to display HUD");
			e.printStackTrace();
		}
	}
}
