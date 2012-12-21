package machinemuse.powersuits.trash;

import java.util.ArrayList;
import java.util.List;


public class AugSlot {
	private List<AugSlot> connectedSlots;
	private SlotType type;
	private ItemAugmentation installedAug;

	public AugSlot(SlotType type) {
		connectedSlots = new ArrayList<AugSlot>();
		this.type = type;
	}

	public List<AugSlot> getConnectedSlots() {
		return connectedSlots;
	}

	public void connectToSlot(AugSlot other) {
		if (!this.connectedSlots.contains(other)) {
			this.connectedSlots.add(other);
		}
		if (!other.connectedSlots.contains(this)) {
			other.connectedSlots.add(this);
		}
	}

	// Returns true if successful, false otherwise
	public boolean installAugmentation(ItemAugmentation aug) {
		if (aug.canGoInSlot(type)) {
			installedAug = aug;
			return true;
		} else {
			return false;
		}
	}

	public void disconnectFromSlot(AugSlot other) {
		if (!this.connectedSlots.contains(other)) {
			this.connectedSlots.add(other);
		}
		if (!other.connectedSlots.contains(this)) {
			other.connectedSlots.add(this);
		}
	}

	public SlotType getType() {
		return type;
	}

	public static enum SlotType {
		Vision,
		Hearing,
		Movement,
		Generator,
		PowerConduit,
		Weapon,
		Tool,

	}
}
