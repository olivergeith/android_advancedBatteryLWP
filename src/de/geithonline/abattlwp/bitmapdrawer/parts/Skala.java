package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.PointF;
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
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, startWinkel, 360, 0, 100, levelLines);
	}

	public static SkalaPart getLevelScalaArch(//
			final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final float sweep, final LevelLinesStyle style) {

		final SkalaLines levelLines = SkalaLines.getLevelLines(0, 100, style);
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, startWinkel, sweep, 0, 100, levelLines);
	}

	// #########################################################################
	// Volt-Scalas
	// #########################################################################
	public static SkalaPart getDefaultVoltmeterPart(final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final float sweep, final VoltLinesStyle style) {
		final SkalaLines skalaLines = SkalaLines.getVoltLines(3.5f, style); // Todo startVoltage configurierbar machen
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, startWinkel, sweep, 0, 100, skalaLines)//
				.setNumberFormatNachkomma1();
	}

}
