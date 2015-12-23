package de.geithonline.abattlwp.bitmapdrawer.enums;

public enum EZMode {
	Einer, EinerOnly9Segmente, EinerOnly10Segmente, Fuenfer, Zehner, FuenfUndZwanziger, Zwanziger, Zweier, Vierer;

	public static EZMode enumForName(final String name) {
		switch (name) {
			default:
			case "1":
				return EZMode.Einer;
			case "2":
				return EZMode.Zweier;
			case "4":
				return EZMode.Vierer;
			case "5":
				return EZMode.Fuenfer;
			case "10":
				return EZMode.Zehner;
			case "20":
				return EZMode.Zwanziger;
			case "25":
				return EZMode.FuenfUndZwanziger;
		}
	}

}
