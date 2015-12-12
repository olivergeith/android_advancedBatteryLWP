package de.geithonline.abattlwp.bitmapdrawer.enums;

public enum EZMode {
	Einer, EinerOnly9Segmente, EinerOnly10Segmente, Fuenfer, Zehner;

	public static EZMode enumForName(final String name) {
		switch (name) {
			default:
			case "1":
				return EZMode.Einer;
			case "5":
				return EZMode.Fuenfer;
			case "10":
				return EZMode.Zehner;
		}
	}

}
