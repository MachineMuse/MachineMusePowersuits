package net.machinemuse.powersuits.gui.tinker.frame;

import net.machinemuse.numina.client.gui.scrollable.ScrollableFrame;
import net.machinemuse.numina.client.render.MuseRenderer;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.FlyFromPointToPoint2D;
import net.machinemuse.numina.math.geometry.GradientAndArcCalculator;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.gui.tinker.clickable.ClickableItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ItemSelectionFrame extends ScrollableFrame {
    protected List<ClickableItem> itemButtons;
    protected int selectedItemStack = -1;
    protected EntityPlayer player;
    protected List<MusePoint2D> itemPoints;
    protected int lastItemSlot = -1;

    public ItemSelectionFrame(MusePoint2D topleft, MusePoint2D bottomright, Colour borderColour, Colour insideColour, EntityPlayer player) {
        super(topleft, bottomright, borderColour, insideColour);
        this.player = player;
        List<Integer> slots = MuseItemUtils.getModularItemSlotsInInventory(player);
        loadPoints(slots.size());
        loadItems();
    }

    public int getLastItemSlot() {
        return lastItemSlot;
    }

    public int getSelectedItemSlot() {
        return selectedItemStack;
    }

    private void loadPoints(int num) {
        double centerx = (border.left() + border.right()) / 2;
        double centery = (border.top() + border.bottom()) / 2;
        itemPoints = new ArrayList();
        List<MusePoint2D> targetPoints = GradientAndArcCalculator.pointsInLine(num,
                new MusePoint2D(centerx, border.bottom()),
                new MusePoint2D(centerx, border.top()));
        for (MusePoint2D point : targetPoints) {
            // Fly from middle over 200 ms
            itemPoints.add(new FlyFromPointToPoint2D(
                    new MusePoint2D(centerx, centery),
                    point, 200));
        }
    }

    private void loadItems() {
        if (player != null) {
            itemButtons = new ArrayList<>();
//            double centerx = (border.left() + border.right()) / 2;
//            double centery = (border.top() + border.bottom()) / 2;
//            List<Integer> slots = MuseItemUtils.getModularItemSlotsEquiped(player);
            List<Integer> slots = MuseItemUtils.getModularItemSlotsInInventory(player);
            if (slots.size() > itemPoints.size()) {
                loadPoints(slots.size());
            }
            if (slots.size() > 0) {
                Iterator<MusePoint2D> pointiterator = itemPoints.iterator();

                for (int slot : slots) {
                    ClickableItem clickie = new ClickableItem(
                            player.inventory.getStackInSlot(slot),
                            pointiterator.next(), slot);
                    itemButtons.add(clickie);
                }
            }
        }
    }

    @Override
    public void update(double mousex, double mousey) {
        loadItems();
    }

    @Override
    public void draw() {
        drawBackground();
        drawItems();
        drawSelection();
    }

    private void drawBackground() {
        super.draw();
    }

    private void drawItems() {
        for (ClickableItem item : itemButtons) {
            item.draw();
        }
    }

    private void drawSelection() {
        if (selectedItemStack != -1) {
            MuseRenderer.drawCircleAround(
                    Math.floor(itemButtons.get(selectedItemStack).getPosition().getX()),
                    Math.floor(itemButtons.get(selectedItemStack).getPosition().getY()),
                    10);
        }
    }

    public boolean hasNoItems() {
        return itemButtons.size() == 0;
    }

    public ClickableItem getPreviousSelectedItem() {
        if (itemButtons.size() > lastItemSlot && lastItemSlot != -1) {
            return itemButtons.get(lastItemSlot);
        } else {
            return null;
        }
    }

    public ClickableItem getSelectedItem() {
        if (itemButtons.size() > selectedItemStack && selectedItemStack != -1) {
            return itemButtons.get(selectedItemStack);
        } else {
            return null;
        }
    }

    @Override
    public void onMouseDown(double x, double y, int button) {
        int i = 0;
        for (ClickableItem item : itemButtons) {
            if (item.hitBox(x, y)) {
                lastItemSlot = selectedItemStack;
                Musique.playClientSound(SoundDictionary.SOUND_EVENT_GUI_SELECT, SoundCategory.BLOCKS, 1, null);
                selectedItemStack = i;
                break;
            } else {
                i++;
            }
        }
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        int itemHover = -1;
        int i = 0;
        for (ClickableItem item : itemButtons) {
            if (item.hitBox(x, y)) {
                itemHover = i;
                break;
            } else {
                i++;
            }
        }
        if (itemHover > -1) {
            return itemButtons.get(itemHover).getToolTip();
        } else {
            return null;
        }
    }
}
