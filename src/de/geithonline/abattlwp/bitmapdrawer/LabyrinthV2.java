package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class LabyrinthV2 extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;
	private float fontSizeArc;

	private float maxRadius;

	private final PointF center = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;

		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeArc = maxRadius * 0.08f;
		fontSizeLevel = maxRadius * 0.3f;
	}

	public LabyrinthV2() {
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
	public boolean supportsShowPointer() {
		return true;
	}

	@Override
	public boolean supportsGlowScala() {
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

	private final float startWinkel = -90;
	private final float sweep = 360f;

	private void drawAll(final int level) {

		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint())//
				.draw(bitmapCanvas);

		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.89f, maxRadius * 0.87f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.89f, maxRadius * 0.87f, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}

		// Ausen Ring
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.87f, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		for (int i = 0; i < 8; i++) {
			final float aussen = maxRadius * 0.85f;
			final float dicke = maxRadius * 0.07f;
			final float spacing = maxRadius * 0.01f;
			final float ra = aussen - i * dicke;
			final float ri = ra - dicke + spacing;
			final float sw = startWinkel + i * -15;
			final float swp = sweep - i * 15;
			// Level
			new LevelPart(center, ra, ri, level, sw, swp, EZColoring.LevelColors)//
					.setSegemteAbstand(1.0f)//
					.setStrokeWidth(strokeWidth / 3)//
					.setStyle(Settings.getLevelStyle())//
					.setMode(Settings.getLevelMode())//
					.draw(bitmapCanvas);
			if (Settings.isShowZeiger()) {
				// Zeiger
				new LevelZeigerPart(center, level, ra, ri, strokeWidth, sw, swp, EZMode.Einer)//
						.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
						.draw(bitmapCanvas);
			}
		}

	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 90 + Math.round(level * 3.6f);
		new TextOnCirclePart(center, maxRadius * 0.9f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnCirclePart(center, maxRadius * 0.9f, -90, fontSizeArc, new Paint())//
				.setColor(Settings.getBattStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
