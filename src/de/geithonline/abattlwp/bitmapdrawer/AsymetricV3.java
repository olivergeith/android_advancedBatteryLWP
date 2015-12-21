package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.FontAttributes;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.data.SkalaLines.LevelLinesStyle;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.parts.EraserPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.CirclePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class AsymetricV3 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;
	private float fontSizeScala;

	private float maxRadius;

	private final PointF center = new PointF();
	private final PointF center2 = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		center2.x = bmpWidth / 2;
		center2.y = bmpHeight * 2f;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.07f;
		fontSizeScala = maxRadius * 0.15f;
		fontSizeLevel = maxRadius * 0.23f;
		// Radiusses

	}

	public AsymetricV3() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsLevelNumberFontSizeAdjustment() {
		return false;
	}

	@Override
	public boolean supportsLevelStyle() {
		return true;
	}

	@Override
	public boolean isPremiumDrawer() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {
		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(Color.WHITE, strokeWidth / 2))//
				.draw(bitmapCanvas);
		// Skala
		final float winkel = -90 - level * 3.6f;
		Skala.getLevelScalaCircular(center2, maxRadius * 2.95f, maxRadius * 3.02f, winkel, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontAttributesEbene2Default()//
				.setFontAttributesEbene3Default()//
				.setupDefaultBaseLineRadius()//
				// .setupLineRadienAllTheSame()//
				.setDickeBaseLine(strokeWidth)//
				.setDicke(strokeWidth)//
				.draw(bitmapCanvas);
		new LevelZeigerPart(center2, level, maxRadius * 2.93f, 0, strokeWidth * 2, winkel, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(strokeWidth * 3, Color.BLACK))//
				.setZeigerType(ZEIGER_TYP.rect)//
				.draw(bitmapCanvas);

		// aussen alles weg radieren
		new EraserPart(new CirclePath(center, maxRadius * 1.99f, maxRadius * 0.98f)).draw(bitmapCanvas);

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.89f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(80), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.89f, maxRadius * 0.50f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(80), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.50f, maxRadius * 0.40f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(80), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Level
		Skala.getLevelScalaArch(center, maxRadius * 0.72f, maxRadius * 0.76f, 135, 270, LevelLinesStyle.ZehnerFuenfer)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala / 2))//
				.setDicke(strokeWidth)//
				.setDickeBaseLine(strokeWidth / 2)//
				.setupDefaultBaseLineRadius()//
				.draw(bitmapCanvas);
		new LevelPart(center, maxRadius * 0.70f, maxRadius * 0.51f, level, 135, 270, EZColoring.LevelColors)//
				.setStyle(Settings.getLevelStyle())//
				.setSegemteAbstand(1f)//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

	}

	@Override
	public void drawLevelNumber(final int level) {
		new TextOnLinePart(center, maxRadius * 0.78f, 90, fontSizeLevel, PaintProvider.getLevelNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.invert(true)//
				.setDropShadow(new DropShadow(strokeWidth * 2, Color.BLACK))//
				.draw(bitmapCanvas, "" + level + "%");
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 270 + Math.round(level * 3.6f);
		new TextOnCirclePart(center, maxRadius * 0.43f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.LEFT)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.96f, 90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.invert(true)//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
