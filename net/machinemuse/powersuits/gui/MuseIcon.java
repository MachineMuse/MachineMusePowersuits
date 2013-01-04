package net.machinemuse.powersuits.gui;

public class MuseIcon {
	public static final String ICON_PATH = "/icons.png";
	public static final MuseIcon ORB1 = new MuseIcon(ICON_PATH, 0);

	String texturefile;
	int index;

	public MuseIcon(String texturefile, int index) {
		super();
		this.texturefile = texturefile;
		this.index = index;
	}

	public String getTexturefile() {
		return texturefile;
	}

	public void setTexturefile(String texturefile) {
		this.texturefile = texturefile;
	}

	public int getIconIndex() {
		return index;
	}

	public void setIconIndex(int index) {
		this.index = index;
	}

}
