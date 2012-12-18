package machinemuse.powersuits.common.augmentation;

public abstract class Augmentation {
	public abstract String getName();

	public abstract float getWeight();

	public abstract boolean isValidForSlot(int slot);
}
