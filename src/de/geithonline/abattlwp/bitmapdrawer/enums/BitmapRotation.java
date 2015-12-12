package de.geithonline.abattlwp.bitmapdrawer.enums;

public enum BitmapRotation {
	Left, Right, NoRotatation;

	public static BitmapRotation enumForName(final String name) {
		switch (name) {
			case "Left":
				return Left;
			case "Right":
				return Right;
			default:
			case "No Rotation":
				return NoRotatation;
		}
	}

}
