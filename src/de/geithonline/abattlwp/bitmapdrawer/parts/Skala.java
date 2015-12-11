package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.PointF;
import de.geithonline.abattlwp.bitmapdrawer.data.LevelLines;
import de.geithonline.abattlwp.bitmapdrawer.data.LevelLines.LevelLinesStyle;

public class Skala {

	public static SkalaPart getLevelScalaCircular(//
			final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final LevelLinesStyle style) {

		final LevelLines levelLines = LevelLines.getLevelLines(0, 99, style); // 99!!!! (100 soll bei einem Kreis nicht gezeichnet werden!)
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, startWinkel, 360, 0, 100, levelLines);
	}

	public static SkalaPart getLevelScalaArch(//
			final PointF center, //
			final float radiusLineStart, final float radiusLineEnd, //
			final float startWinkel, final float sweep, final LevelLinesStyle style) {

		final LevelLines levelLines = LevelLines.getLevelLines(0, 100, style);
		return new SkalaPart(center, radiusLineStart, radiusLineEnd, startWinkel, sweep, 0, 100, levelLines);
	}

}
