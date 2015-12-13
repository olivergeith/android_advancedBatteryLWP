package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.PointF;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaData;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.LevelLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.VoltLinesStyle;

public class Skala {

	// #########################################################################
	// Level-Scalas
	// #########################################################################
	public static SkalaPart getLevelScalaCircular(//
			final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final LevelLinesStyle style) {

		final SkalaLines levelLines = SkalaLines.getLevelLines(0, 99, style); // 99!!!! (100 soll bei einem Kreis nicht gezeichnet werden!)
		final SkalaData scale = new SkalaData(startWinkel, 360, 0, 100);
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, scale, levelLines);
	}

	public static SkalaPart getLevelScalaArch(//
			final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final float sweep, final LevelLinesStyle style) {

		final SkalaLines levelLines = SkalaLines.getLevelLines(0, 100, style);
		final SkalaData scale = new SkalaData(startWinkel, sweep, 0, 100);
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, scale, levelLines);
	}

	// #########################################################################
	// Volt-Scalas
	// #########################################################################
	public static SkalaPart getDefaultVoltmeterPart(final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final float sweep, final VoltLinesStyle style) {
		final SkalaLines skalaLines = SkalaLines.getVoltLines(3.5f, style); // Todo startVoltage configurierbar machen
		final SkalaData scale = new SkalaData(startWinkel, sweep, 3.5f, 4.5f);
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, scale, skalaLines)//
				.setNumberFormatNachkomma1();
	}

	// #########################################################################
	// Temp-Scalas
	// #########################################################################
	public static SkalaPart getDefaultThermometerPart(final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final float sweep, final LevelLinesStyle style) {
		final SkalaLines levelLines = SkalaLines.getLevelLines(10, 60, style);
		final SkalaData scale = new SkalaData(startWinkel, sweep, 10, 60);
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, scale, levelLines)//
				.setNumberFormatNachkommaOhne();
	}

	// #########################################################################
	// Zeiger
	// #########################################################################
	// public static SkalaZeigerPart getDefaultVoltmeterZeigerPart(//
	// final PointF center, final float value, //
	// final float radAussen, final float radInnen, //
	// final SkalaData scale) {
	// return new SkalaZeigerPart(center, value, radAussen, radInnen, scale);
	// }
	//
	// public static SkalaZeigerPart getDefaultThermometerZeigerPart(//
	// final PointF center, final float value, //
	// final float radAussen, final float radInnen, //
	// final SkalaData scale) {
	// return new SkalaZeigerPart(center, value, radAussen, radInnen, scale);
	// }

	public static SkalaZeigerPart getZeigerPart(//
			final PointF center, final float value, //
			final float radAussen, final float radInnen, //
			final SkalaData scale) {
		return new SkalaZeigerPart(center, value, radAussen, radInnen, scale);
	}

}
