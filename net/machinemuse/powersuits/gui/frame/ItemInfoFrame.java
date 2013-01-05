package net.machinemuse.powersuits.gui.frame;

import java.util.Arrays;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;
import net.machinemuse.powersuits.gui.clickable.ClickableItem;
import net.machinemuse.powersuits.item.IModularItem;
import net.machinemuse.powersuits.tinker.TinkerAction;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

public class ItemInfoFrame implements IGuiFrame {
	public static final double SCALEFACTOR = 0.5;
	protected ItemSelectionFrame target;
	protected TinkerAction previewAction;
	protected Point2D topleft;
	protected Point2D bottomright;
	protected Colour borderColour;
	protected Colour insideColour;
	protected EntityPlayer player;

	public ItemInfoFrame(EntityPlayer player, Point2D topleft,
			Point2D bottomright,
			Colour borderColour, Colour insideColour, ItemSelectionFrame target) {
		this.topleft = topleft.times(1.0 / SCALEFACTOR);
		this.bottomright = bottomright.times(1.0 / SCALEFACTOR);
		this.borderColour = borderColour;
		this.insideColour = insideColour;
		this.target = target;
		this.player = player;
	}

	@Override
	public void draw() {
		ClickableItem selected = target.getSelectedItem();
		if (selected != null) {
			GL11.glPushMatrix();
			GL11.glScaled(SCALEFACTOR, SCALEFACTOR, SCALEFACTOR);
			MuseRenderer.drawFrameRect(topleft, bottomright, borderColour,
					insideColour, 0, 8);
			int xoffset = 8;
			int yoffset = 8;
			int i = 0;
			List<String> info = ((IModularItem) selected.getItem().getItem())
					.getLongInfo(player, selected.getItem());
			for (String infostring : info) {
				String[] str = infostring.split("\t");
				MuseRenderer.drawStringsJustified(Arrays.asList(str),
						topleft.x()
								+ xoffset, bottomright.x() - xoffset,
						topleft.y() + yoffset
								+ i * 10);

				i++;
			}
			GL11.glPopMatrix();
		}

	}

	@Override
	public void onMouseDown(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseMove(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseUp(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getToolTip(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

}
