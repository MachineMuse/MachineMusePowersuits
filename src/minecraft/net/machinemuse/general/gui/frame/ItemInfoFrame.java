package net.machinemuse.general.gui.frame;

import java.util.Arrays;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.MusePoint2D;
import net.machinemuse.general.gui.clickable.ClickableItem;
import net.machinemuse.powersuits.item.IModularItem;
import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

public class ItemInfoFrame extends ScrollableFrame {
	public static final double SCALEFACTOR = 0.5;
	protected ItemSelectionFrame target;
	protected EntityPlayer player;
	protected List<String> info;
	
	public ItemInfoFrame(EntityPlayer player, MusePoint2D topleft,
			MusePoint2D bottomright,
			Colour borderColour, Colour insideColour, ItemSelectionFrame target) {
		super(topleft, bottomright, borderColour, insideColour);
		this.topleft = topleft.times(1.0 / SCALEFACTOR);
		this.bottomright = bottomright.times(1.0 / SCALEFACTOR);
		this.target = target;
		this.player = player;
	}
	
	@Override public void update(double mousex, double mousey) {
		ClickableItem selected = target.getSelectedItem();
		if (selected != null) {
			IModularItem item = ItemUtils.getAsModular(selected.getItem().getItem());
			info = item.getLongInfo(player, selected.getItem());
		} else {
			info = null;
		}
	}
	
	@Override public void draw() {
		if (info != null) {
			GL11.glPushMatrix();
			GL11.glScaled(SCALEFACTOR, SCALEFACTOR, SCALEFACTOR);
			MuseRenderer.drawFrameRect(topleft, bottomright, borderColour,
					insideColour, 0, 8);
			int xoffset = 8;
			int yoffset = 8;
			int i = 0;
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
	
	@Override public void onMouseDown(double x, double y, int button) {
		// TODO Auto-generated method stub
		
	}
	
	@Override public void onMouseUp(double x, double y, int button) {
		// TODO Auto-generated method stub
		
	}
	
	@Override public List<String> getToolTip(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}
}
