package net.machinemuse.general.gui.frame;

import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.general.gui.clickable.ClickableButton;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.general.gui.clickable.ClickableModule;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.MusePacketInstallModuleRequest;
import net.machinemuse.powersuits.network.MusePacketSalvageModuleRequest;
import net.machinemuse.powersuits.powermodule.PowerModule;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.network.Player;

public class InstallSalvageFrame extends ScrollableFrame {
	protected ItemSelectionFrame targetItem;
	protected ModuleSelectionFrame targetModule;
	protected ClickableButton installButton;
	protected ClickableButton salvageButton;
	protected EntityClientPlayerMP player;
	
	public InstallSalvageFrame(EntityClientPlayerMP player, Point2D topleft,
			Point2D bottomright,
			Colour borderColour, Colour insideColour,
			ItemSelectionFrame targetItem, ModuleSelectionFrame targetModule) {
		super(topleft, bottomright, borderColour, insideColour);
		this.player = player;
		this.targetItem = targetItem;
		this.targetModule = targetModule;
		double sizex = bottomright.x() - topleft.x();
		double sizey = bottomright.y() - topleft.y();
		
		this.installButton = new ClickableButton("Install", new Point2D(
				bottomright.x() - sizex / 2.0, bottomright.y() - sizey
						/ 4.0),
				new Point2D(20, 6), true);
		this.salvageButton = new ClickableButton("Salvage", new Point2D(
				topleft.x() + sizex / 2.0, topleft.y() + sizey / 4.0),
				new Point2D(24, 6), true);
		
	}
	
	@Override public void update(double mousex, double mousey) {
		// TODO Auto-generated method stub
		
	}
	
	@Override public void draw() {
		if (targetItem.getSelectedItem() != null
				&& targetModule.getSelectedModule() != null) {
			drawBackground();
			drawItems();
			drawButtons();
		}
	}
	
	private void drawBackground() {
		MuseRenderer.drawFrameRect(topleft, bottomright, borderColour,
				insideColour, 0, 4);
	}
	
	private void drawItems() {
		ItemStack stack = targetItem.getSelectedItem().getItem();
		PowerModule module = targetModule.getSelectedModule().getModule();
		List<ItemStack> itemsToDraw = targetModule.getSelectedModule()
				.getModule().getInstallCost();
		double yoffset;
		if (!ItemUtils.itemHasModule(stack, module.getName())) {
			yoffset = topleft.y() + 4;
		} else {
			yoffset = bottomright.y() - 20;
		}
		double xoffset = -8.0 * itemsToDraw.size()
				+ (topleft.x() + bottomright.x()) / 2;
		int i = 0;
		for (ItemStack costItem : itemsToDraw) {
			MuseRenderer.drawItemAt(
					16 * i++ + xoffset,
					yoffset,
					costItem);
		}
	}
	
	private void drawButtons() {
		ItemStack stack = targetItem.getSelectedItem().getItem();
		PowerModule module = targetModule.getSelectedModule().getModule();
		if (!ItemUtils.itemHasModule(stack, module.getName())) {
			
			installButton.setEnabled(ItemUtils.hasInInventory(
					module.getInstallCost(), player.inventory));
			installButton.draw();
		} else {
			salvageButton.draw();
		}
	}
	
	@Override public void onMouseDown(double x, double y, int button) {
		ClickableItem selItem = targetItem.getSelectedItem();
		ClickableModule selModule = targetModule.getSelectedModule();
		if (selItem != null && selModule != null) {
			ItemStack stack = selItem.getItem();
			PowerModule module = selModule.getModule();
			
			if (!ItemUtils.itemHasModule(stack, module.getName())) {
				if (installButton.hitBox(x, y)) {
					doInstall();
				}
			} else {
				if (salvageButton.hitBox(x, y)) {
					doSalvage();
				}
			}
		}
	}
	
	private void doSalvage() {
		PowerModule module = targetModule.getSelectedModule().getModule();
		MusePacket newpacket = new MusePacketSalvageModuleRequest(
				(Player) player,
				targetItem.getSelectedItem().inventorySlot,
				module.getName());
		player.sendQueue.addToSendQueue(newpacket.getPacket250());
	}
	
	/**
	 * Performs all the functions associated with the install button. This
	 * requires communicating with the server.
	 */
	private void doInstall() {
		ItemStack stack = targetItem.getSelectedItem().getItem();
		PowerModule module = targetModule.getSelectedModule().getModule();
		if (ItemUtils.hasInInventory(module.getInstallCost(), player.inventory)) {
			// Doing it client-side first in case of lag
			ItemUtils.deleteFromInventory(module.getInstallCost(),
					player.inventory);
			ItemUtils.itemAddModule(stack, module);
			// Now send request to server to make it legit
			MusePacket newpacket = new MusePacketInstallModuleRequest(
					(Player) player,
					targetItem.getSelectedItem().inventorySlot,
					module.getName());
			player.sendQueue.addToSendQueue(newpacket.getPacket250());
		}
	}
}
