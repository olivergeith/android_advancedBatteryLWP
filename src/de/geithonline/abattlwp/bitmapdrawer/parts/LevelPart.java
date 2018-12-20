package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.shapes.LevelArcPath;
import de.geithonline.abattlwp.settings.PaintProvider;

public class LevelPart {

	private EZStyle style = EZStyle.sweep;
	private EZMode modus = EZMode.Einer;
	private final PointF c;
	private final float ra;
	private final float ri;
	private Paint paint;
	private int levelIntern;
	private final float maxWinkel;
	private final float startWinkel;
	private float abstandZwischenSegemten = 1.5f;
	private float anzahlSegmente = 10;

	private float strokeWidthSegmente = 1f;
	private final int level;
	private final EZColoring coloring;
	private boolean inverted = false;

	public LevelPart(final PointF center, final float radAussen, final float radInnen, //
			final int level, final float startWinkel, final float maxWinkel, final EZColoring coloring) {
		c = center;
		ra = radAussen;
		ri = radInnen;
		this.level = level;
		this.maxWinkel = maxWinkel;
		this.startWinkel = startWinkel;
		this.coloring = coloring;
		setMode(modus);
		switch (coloring) {
			default:
			case LevelColors:
				paint = PaintProvider.getBatteryPaint(this.level);
				break;
			case Colorfull:
			case Custom:
			case ColorOf100:
				paint = PaintProvider.getBatteryPaint(100);
				break;
		}
		initPaint();
	}

	private void initPaint() {
		paint.setStrokeWidth(strokeWidthSegmente);
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public LevelPart setColor(final int color) {
		paint.setColor(color);
		return this;
	}

	public LevelPart setDropShadow(final DropShadow dropShadow) {
		if (dropShadow != null) {
			dropShadow.setUpPaint(paint);
		}
		return this;
	}

	public LevelPart setMode(final EZMode modus) {
		this.modus = modus;
		switch (this.modus) {
			default:
			case Einer:
				levelIntern = level;
				anzahlSegmente = 100;
				break;
			case EinerOnly9Segmente:
				levelIntern = level % 10;
				anzahlSegmente = 9;
				break;
			case EinerOnly10Segmente:
				levelIntern = level % 10;
				anzahlSegmente = 10;
				break;
			case Zweier:
				levelIntern = level / 2;
				anzahlSegmente = 50;
				break;
			case Vierer:
				levelIntern = level / 4;
				anzahlSegmente = 25;
				break;
			case Fuenfer:
				levelIntern = level / 5;
				anzahlSegmente = 20;
				break;
			case FuenfUndZwanziger:
				levelIntern = level / 25;
				anzahlSegmente = 4;
				break;
			case Zwanziger:
				levelIntern = level / 20;
				anzahlSegmente = 5;
				break;
			case Zehner:
				levelIntern = level / 10;
				anzahlSegmente = 10;
				break;
		}
		return this;
	}

	public LevelPart setStyle(final EZStyle style) {
		this.style = style;
		return this;
	}

	/**
	 * Draws the inverted level arc everything above the actual level up to 100%
	 * 
	 * @param inverted
	 * @return
	 */
	public LevelPart setInverted(final boolean inverted) {
		this.inverted = inverted;
		return this;
	}

	/**
	 * @param abstandsWinkel
	 *            in grad (default ist 1.5 grad)
	 * @return
	 */
	public LevelPart setSegemteAbstand(final float abstandsWinkel) {
		abstandZwischenSegemten = abstandsWinkel;
		return this;
	}

	public LevelPart setStrokeWidth(final float strokWidth) {
		strokeWidthSegmente = strokWidth;
		paint.setStrokeWidth(strokeWidthSegmente);
		return this;
	}

	public void draw(final Canvas canvas) {
		switch (style) {
			default:
			case sweep:
				drawSweep(canvas);
				break;
			case sweep_withAplpah:
				drawSweepWithAlpha(canvas);
				break;
			case sweep_withBackgroundPaint:
				drawSweepWithBackground(canvas);
				break;
			case sweep_withOutline:
				drawSweepWithOutline(canvas);
				break;
			case segmented_onlyactive:
				drawSegemtedOnlyAct(canvas);
				break;
			case segmented_all:
				drawSegemtedAll(canvas);
				break;
			case segmented_all_alpha:
				drawSegemtedAllHalfAlpha(canvas);
				break;
			case segmented_all_backgroundPaint:
				drawSegemtedAllBackgroundPaint(canvas);
				break;
		}
	}

	private void drawSweepWithOutline(final Canvas canvas) {
		final Path outline = new LevelArcPath(c, ra, ri, startWinkel, maxWinkel);
		paint.setStyle(Style.STROKE);
		canvas.drawPath(outline, paint);
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		final float sweep = winkelProSegment * levelIntern;
		final Path path = new LevelArcPath(c, ra, ri, startWinkel, sweep);
		final Path pathInverted = new LevelArcPath(c, ra, ri, startWinkel + sweep, maxWinkel - sweep);
		paint.setStyle(Style.FILL);
		if (!inverted) {
			canvas.drawPath(path, paint);
		} else {
			canvas.drawPath(pathInverted, paint);
		}
	}

	private void drawSweepWithAlpha(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		final float sweep = winkelProSegment * levelIntern;

		final Path path = new LevelArcPath(c, ra, ri, startWinkel, sweep);
		final Path path2 = new LevelArcPath(c, ra, ri, startWinkel + sweep, maxWinkel - sweep);
		paint.setStyle(Style.FILL);
		final int alpha = paint.getAlpha();
		if (!inverted) {
			canvas.drawPath(path, paint);
		} else {
			canvas.drawPath(path2, paint);
		}
		// rest in ein drittel alpha malen
		paint.setColor(PaintProvider.getColorForLevel(100));
		paint.setAlpha(alpha / 3);
		if (!inverted) {
			canvas.drawPath(path2, paint);
		} else {
			canvas.drawPath(path, paint);
		}

	}

	private void drawSweepWithBackground(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		final float sweep = winkelProSegment * levelIntern;
		final Path path = new LevelArcPath(c, ra, ri, startWinkel, sweep);
		final Path path2 = new LevelArcPath(c, ra, ri, startWinkel + sweep, maxWinkel - sweep);
		paint.setStyle(Style.FILL);
		if (!inverted) {
			canvas.drawPath(path, paint);
		} else {
			canvas.drawPath(path2, paint);
		}
		// rest in ein drittel alpha malen
		paint.setColor(PaintProvider.getBackgroundPaint().getColor());
		if (!inverted) {
			canvas.drawPath(path2, paint);
		} else {
			canvas.drawPath(path, paint);
		}

	}

	private void drawSweep(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		final float sweep = winkelProSegment * levelIntern;
		if (!inverted) {
			final Path path = new LevelArcPath(c, ra, ri, startWinkel, sweep);
			paint.setStyle(Style.FILL);
			canvas.drawPath(path, paint);
		} else {
			final Path path = new LevelArcPath(c, ra, ri, startWinkel + sweep, maxWinkel - sweep);
			paint.setStyle(Style.FILL);
			canvas.drawPath(path, paint);
		}
	}

	private void drawSegemtedOnlyAct(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		float sweepProSeg;
		if (winkelProSegment < 0) {
			sweepProSeg = winkelProSegment + abstandZwischenSegemten;
		} else {
			sweepProSeg = winkelProSegment - abstandZwischenSegemten;
		}
		// alpha merken
		final int alpha = paint.getAlpha();
		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			float winkel;
			if ((i < levelIntern && !inverted) || (i >= levelIntern && inverted)) {
				if (winkelProSegment < 0) {
					winkel = startWinkel + i * winkelProSegment - abstandZwischenSegemten / 2;
				} else {
					winkel = startWinkel + i * winkelProSegment + abstandZwischenSegemten / 2;
				}
				if (coloring.equals(EZColoring.Colorfull)) {
					final int faktor = (int) (100 / anzahlSegmente);
					paint.setColor(PaintProvider.getColorForLevel(i * faktor));
				}
				// f�r den Colorfull Fall wieder setzen von Alpha!
				paint.setAlpha(alpha);
				final Path path = new LevelArcPath(c, ra, ri, winkel, sweepProSeg);
				canvas.drawPath(path, paint);
			} else {
				// do nothing
			}
		}

	}

	private void drawSegemtedAll(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		float sweepProSeg;
		if (winkelProSegment < 0) {
			sweepProSeg = winkelProSegment + abstandZwischenSegemten;
		} else {
			sweepProSeg = winkelProSegment - abstandZwischenSegemten;
		}
		final int alpha = paint.getAlpha();
		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			paint.setStrokeWidth(strokeWidthSegmente);
			if ((i < levelIntern && !inverted) || (i >= levelIntern && inverted)) {
				paint.setStyle(Style.FILL_AND_STROKE);
			} else {
				paint.setStyle(Style.STROKE);
			}
			if (coloring.equals(EZColoring.Colorfull)) {
				final int faktor = (int) (100 / anzahlSegmente);
				paint.setColor(PaintProvider.getColorForLevel(i * faktor));
			}
			paint.setAlpha(alpha);
			float winkel;
			if (winkelProSegment < 0) {
				winkel = startWinkel + i * winkelProSegment - abstandZwischenSegemten / 2;
			} else {
				winkel = startWinkel + i * winkelProSegment + abstandZwischenSegemten / 2;
			}
			final Path path = new LevelArcPath(c, ra, ri, winkel, sweepProSeg);
			canvas.drawPath(path, paint);
		}
	}

	private void drawSegemtedAllHalfAlpha(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		float sweepProSeg;
		if (winkelProSegment < 0) {
			sweepProSeg = winkelProSegment + abstandZwischenSegemten;
		} else {
			sweepProSeg = winkelProSegment - abstandZwischenSegemten;
		}
		final int alpha = paint.getAlpha();
		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			if (coloring.equals(EZColoring.Colorfull)) {
				final int faktor = (int) (100 / anzahlSegmente);
				paint.setColor(PaintProvider.getColorForLevel(i * faktor));
			}
			paint.setStrokeWidth(strokeWidthSegmente);
			if ((i < levelIntern && !inverted) || (i >= levelIntern && inverted)) {
				paint.setAlpha(alpha);
			} else {
				paint.setAlpha(alpha / 3);
			}

			float winkel;
			if (winkelProSegment < 0) {
				winkel = startWinkel + i * winkelProSegment - abstandZwischenSegemten / 2;
			} else {
				winkel = startWinkel + i * winkelProSegment + abstandZwischenSegemten / 2;
			}
			final Path path = new LevelArcPath(c, ra, ri, winkel, sweepProSeg);
			canvas.drawPath(path, paint);
		}
	}

	private void drawSegemtedAllBackgroundPaint(final Canvas canvas) {
		final float winkelProSegment = maxWinkel / anzahlSegmente;
		float sweepProSeg;
		if (winkelProSegment < 0) {
			sweepProSeg = winkelProSegment + abstandZwischenSegemten;
		} else {
			sweepProSeg = winkelProSegment - abstandZwischenSegemten;
		}
		for (int i = 0; i < anzahlSegmente; i = i + 1) {
			paint.setStrokeWidth(strokeWidthSegmente);
			if ((i < levelIntern && !inverted) || (i >= levelIntern && inverted)) {
				if (coloring.equals(EZColoring.Colorfull)) {
					final int faktor = (int) (100 / anzahlSegmente);
					paint.setColor(PaintProvider.getColorForLevel(i * faktor));
				} else {
					paint.setColor(PaintProvider.getColorForLevel(level));
				}
			} else {
				paint.setColor(PaintProvider.getBackgroundPaint().getColor());
			}

			float winkel;
			if (winkelProSegment < 0) {
				winkel = startWinkel + i * winkelProSegment - abstandZwischenSegemten / 2;
			} else {
				winkel = startWinkel + i * winkelProSegment + abstandZwischenSegemten / 2;
			}
			final Path path = new LevelArcPath(c, ra, ri, winkel, sweepProSeg);
			canvas.drawPath(path, paint);
		}
	}

}
