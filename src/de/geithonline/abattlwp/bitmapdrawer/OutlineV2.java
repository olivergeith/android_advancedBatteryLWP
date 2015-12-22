package de.geithonline.abattlwp.bitmapdrawer;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
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
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.parts.AnyPathPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.SkalaPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.CirclePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.RoatingPatternsPath;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class OutlineV2 extends AdvancedBitmapDrawer {

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

	public OutlineV2() {
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

	@Override
	public List<String> getVariants() {
		final List<String> list = new ArrayList<>();
		list.add("Circle");
		list.add("Drop");
		list.add("Heart");
		list.add("Star");
		list.add("10 Circles");
		list.add("Pacman");
		list.add("Pacman-Ghost");
		list.add("Square");
		// list.add("Pacman-Random");
		return list;
	}

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
		new LevelPart(center, maxRadius * 0.99f, maxRadius * 0.80f, level, startWinkel, sweep, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(EZStyle.segmented_onlyactive)//
				.setMode(EZMode.Einer)//
				.draw(bitmapCanvas);
		new LevelPart(center, maxRadius * 0.80f, maxRadius * 0.0f, level, startWinkel, sweep, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);
		final SkalaPart s = Skala.getLevelScalaCircular(center, maxRadius * 0.84f, maxRadius * 0.80f, startWinkel, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontAttributesAllEbenenSameSize()//
				.setFontAttributesEbene3(null)//
				.setFontRadiusEbene1(maxRadius * 0.72f)//
				.setFontRadiusEbene2(maxRadius * 0.72f)//
				.setDicke(strokeWidth * 0.7f);

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.90f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(100), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(outline)//
				.draw(bitmapCanvas);

		new AnyPathPart(center, new Paint(), createPath(level))//
				// .setDropShadow(new DropShadow(strokeWidth * 3, 0, strokeWidth, Color.BLACK))//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(100), GRAD_STYLE.top2bottom), maxRadius * 0.89f, 0)//
				.setOutline(outline)//
				.draw(bitmapCanvas);

		if (Settings.isShowZeiger()) {
			Skala.getZeigerPart(center, level, maxRadius * 0.70f, maxRadius * 0.0f, s.getScala())//
					.setDicke(strokeWidth)//
					.setDropShadow(new DropShadow(strokeWidth * 2, PaintProvider.getGray(32)))//
					.draw(bitmapCanvas);
		}

		// Innen Fläche Ring
		new RingPart(center, maxRadius * 0.35f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(100), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(outline)//
				.draw(bitmapCanvas);
		// Innen Fläche Ring
		new RingPart(center, maxRadius * 0.30f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(100), GRAD_STYLE.top2bottom))//
				.setOutline(outline)//
				.draw(bitmapCanvas);

		s.draw(bitmapCanvas);
		// drawVoltMeter();

	}

	private Path createPath(final int level) {
		final Path p = new Path();

		final Path circle1 = new CirclePath(center, maxRadius * 0.85f, maxRadius * 0.0f, Direction.CCW);
		final int arms = 20;
		final float rotate = 360f / (2 * arms);
		final Path patterns = new RoatingPatternsPath(arms, center, maxRadius * 0.7f, rotate, Settings.getStyleVariante(this.getClass().getSimpleName()));
		p.addPath(circle1);
		p.addPath(patterns);
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
