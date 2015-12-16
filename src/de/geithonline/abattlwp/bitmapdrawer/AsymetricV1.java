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
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.parts.ArchPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.Skala;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class AsymetricV1 extends AdvancedBitmapDrawer {

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
		center2.y = bmpHeight * 0.57f;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.07f;
		fontSizeScala = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.2f;
		// Radiusses

	}

	public AsymetricV1() {
	}

	@Override
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public boolean supportsLevelStyle() {
		return true;
	}

	@Override
	public boolean supportsGlowScala() {
		return true;
	}

	@Override
	public boolean supportsLevelNumberFontSizeAdjustment() {
		return false;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.95f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(224), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(0), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.94f, maxRadius * 0.0f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(128), PaintProvider.getGray(16), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
		// SkalaBackground
		new RingPart(center2, maxRadius * 0.65f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(Color.WHITE, strokeWidth / 2))//
				.draw(bitmapCanvas);
		// Skala
		Skala.getLevelScalaCircular(center2, maxRadius * 0.65f, maxRadius * 0.70f, -90, LevelLinesStyle.ZehnerFuenferEiner)//
				.setFontAttributesEbene1(new FontAttributes(fontSizeScala))//
				.setFontAttributesEbene2Default()//
				.setDicke(strokeWidth * 0.75f)//
				.draw(bitmapCanvas);

		// phase
		new RingPart(center2, maxRadius * 0.64f, maxRadius * 0.59f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(224), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth / 3))//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center2, maxRadius * 0.58f, maxRadius * 0.48f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);
		// Zeiger
		new LevelZeigerPart(center2, level, maxRadius * 0.64f, maxRadius * 0.0f, strokeWidth, -90, 360, EZMode.Einer)//
				.setDropShadow(new DropShadow(1.5f * strokeWidth, 0, 1.5f * strokeWidth, Color.BLACK))//
				.draw(bitmapCanvas);

		// Innen Phase
		if (Settings.isShowGlowScala()) {
			new RingPart(center2, maxRadius * 0.2f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center2, maxRadius * 0.2f, maxRadius * 0.15f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 5, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		new RingPart(center2, maxRadius * 0.2f, maxRadius * 0.15f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(224), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth / 3))//
				.draw(bitmapCanvas);
		// Innen Fläche
		new RingPart(center2, maxRadius * 0.14f, maxRadius * 0.00f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(192), PaintProvider.getGray(32), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(32), strokeWidth))//
				.draw(bitmapCanvas);

		// Timer
		new ArchPart(center, maxRadius * 0.93f, maxRadius * 0.81f, -201, 92, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new LevelPart(center, maxRadius * 0.91f, maxRadius * 0.83f, level, -200, 90, EZColoring.ColorOf100)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(EZStyle.segmented_all)//
				.setMode(EZMode.Zehner)//
				.draw(bitmapCanvas);
		new ArchPart(center, maxRadius * 0.93f, maxRadius * 0.81f, 21, -92, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new LevelPart(center, maxRadius * 0.91f, maxRadius * 0.83f, level, 20, -90, EZColoring.ColorOf100)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(EZStyle.segmented_all)//
				.setMode(EZMode.EinerOnly10Segmente)//
				.draw(bitmapCanvas);

		new ArchPart(center, maxRadius * 0.93f, maxRadius * 0.70f, -108, 36, PaintProvider.getBackgroundPaint())//
				.setEraseBeforDraw()//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
	}

	@Override
	public void drawLevelNumber(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.74f, -90, fontSizeLevel, PaintProvider.getLevelNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, "" + level);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.74f, -70, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.LEFT)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.74f, -110, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.RIGHT)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
