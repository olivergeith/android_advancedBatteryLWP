package de.geithonline.abattlwp.bitmapdrawer.enums;

public enum PositionVertical {
	Center, Top, Bottom, Custom;

	public static PositionVertical enumForName(final String name) {
		switch (name) {
			case "Top":
				return PositionVertical.Top;
			default:
			case "Center":
				return PositionVertical.Center;
			case "Bottom":
				return PositionVertical.Bottom;
			case "Custom":
				return PositionVertical.Custom;
		}
	}

}
