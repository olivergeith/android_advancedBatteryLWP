package de.geithonline.abattlwp.bitmapdrawer.enums;

public enum PositionHorizontal {
	Left, Center, Right;

	public static PositionHorizontal enumForName(final String name) {
		switch (name) {
			case "Left":
				return PositionHorizontal.Left;
			default:
			case "Center":
				return PositionHorizontal.Center;
			case "Right":
				return PositionHorizontal.Right;
		}
	}

}
