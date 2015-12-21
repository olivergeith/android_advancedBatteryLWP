package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.LevelLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.parts.AnyPathPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.CirclePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.StarPath;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class OutlineV1 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;
	private float raster;
	private final PointF center = new PointF();
	private Outline outline;

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;
		maxRadius = bmpWidth / 2;
		raster = maxRadius / 100;

		// Strokes
		strokeWidth = 2 * raster;
		// fontsizes
		fontSizeArc = 8 * raster;
		fontSizeScala = 8 * raster;
		fontSizeLevel = 30 * raster;

		outline = new Outline(PaintProvider.getGray(64), strokeWidth / 2);
	}

	public OutlineV1() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsShowPointer() {
		return true;
	}

	@Override
	public boolean supportsLevelStyle() {
		return true;
	}

	// @Override
	// public boolean supportsGlowScala() {
	// return true;
	// }

	@Override
	public boolean supportsVoltmeter() {
		return true;
	}

	// @Override
	// public boolean supportsThermometer() {
	// return true;
	// }

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private final float startWinkel = -90;
	private final float sweep = 360;

	private void drawAll(final int level) {
		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.setOutline(outline)//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.99f, maxRadius * 0.0f, level, startWinkel, sweep, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);
		final SkalaPart s = Skala.getLevelScalaCircular(center, maxRadius * 0.75f, maxRadius * 0.78f, startWinkel, LevelLinesStyle.Zehner)//
				// final SkalaPart s = Skala.getLevelScalaCircular(center, maxRadius * 0.41f, maxRadius * 0.43f, startWinkel, LevelLinesStyle.ZehnerFuenfer)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setDicke(strokeWidth * 0.5f);
		if (Settings.isShowZeiger()) {
			Skala.getZeigerPart(center, level, maxRadius * 0.99f, maxRadius * 0.0f, s.getScala())//
					.setDicke(strokeWidth)//
					.setDropShadow(new DropShadow(strokeWidth * 2, PaintProvider.getGray(32)))//
					.draw(bitmapCanvas);
		}

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.90f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(100), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(outline)//
				.draw(bitmapCanvas);

		new AnyPathPart(center, new Paint(), createPath(level))//
				.setDropShadow(new DropShadow(strokeWidth * 2, Color.BLACK))//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(100), GRAD_STYLE.top2bottom), maxRadius * 0.89f, 0)//
				.setOutline(outline)//
				.draw(bitmapCanvas);
		// Innen Fläche Ring
		new RingPart(center, maxRadius * 0.40f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(100), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(outline)//
				.draw(bitmapCanvas);
		// Innen Fläche Ring
		new RingPart(center, maxRadius * 0.35f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(100), GRAD_STYLE.top2bottom))//
				.setOutline(outline)//
				.draw(bitmapCanvas);

		s.draw(bitmapCanvas);
		// drawVoltMeter();

	}

	private Path createPath(final int level) {
		final Path p = new Path();

		final Path circle1 = new CirclePath(center, maxRadius * 0.90f, maxRadius * 0.0f, Direction.CCW);
		final Path star = new StarPath(20, center, maxRadius * 0.74f, maxRadius * 0.60f); // Direction.CW
		final Path circle2 = new CirclePath(center, maxRadius * 0.40f, maxRadius * 0.0f, Direction.CCW);
		p.addPath(circle1);
		p.addPath(star);
		p.addPath(circle2);
		return p;
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel, new DropShadow(strokeWidth * 2, PaintProvider.getGray(32)));
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = -90;
		new TextOnCirclePart(center, maxRadius * 0.91f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.96f, 90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.invert(true)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
