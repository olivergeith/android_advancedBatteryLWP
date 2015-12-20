package de.geithonline.abattlwp.bitmapdrawer.enums;

public enum EZStyle {
	sweep, segmented_onlyactive, segmented_all, sweep_withOutline, segmented_all_alpha, sweep_withAplpah, sweep_withBackgroundPaint, segmented_all_backgroundPaint;

	public static EZStyle enumForName(final String name) {
		switch (name) {
			default:
			case "Normal":
				return EZStyle.sweep;
			case "Normal (alpha)":
				return EZStyle.sweep_withAplpah;
			case "Normal (backgroundpaint)":
				return EZStyle.sweep_withBackgroundPaint;
			case "Normal (outline)":
				return EZStyle.sweep_withOutline;
			case "Only activ segments":
				return EZStyle.segmented_onlyactive;
			case "All segments (outline)":
				return EZStyle.segmented_all;
			case "All segments (alpha)":
				return EZStyle.segmented_all_alpha;
			case "All segments (backgroundpaint)":
				return EZStyle.segmented_all_backgroundPaint;

		}
	}

}
