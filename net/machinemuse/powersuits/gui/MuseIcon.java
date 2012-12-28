package net.machinemuse.powersuits.gui;

public class MuseIcon {
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
