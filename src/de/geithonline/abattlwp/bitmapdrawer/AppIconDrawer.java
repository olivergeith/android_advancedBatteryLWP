package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class AppIconDrawer extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;

	private float maxRadius;

	private final PointF center = new PointF();

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;
		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeLevel = maxRadius * 0.7f;
		// Radiusses

	}

	public AppIconDrawer() {
	}

	@Override
	public boolean supportsShowRand() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {
		// Hintergrund
		if (Settings.isShowRand()) {
			new RingPart(center, maxRadius * 0.99f, 0, PaintProvider.getBackgroundPaint())//
					.setColor(Color.argb(255, 0, 88, 133)) // Primary Color
					.draw(bitmapCanvas);
		}
		// level ring
		new LevelPart(center, maxRadius * 0.80f, maxRadius * 0.60f, level, -90, 360, EZColoring.LevelColors)//
				.setColor(Color.WHITE)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setMode(EZMode.Einer)//
				.setStyle(EZStyle.sweep_withAplpah)//
				.draw(bitmapCanvas);

		// Zeiger
		new LevelZeigerPart(center, level, maxRadius * 0.83f, maxRadius * 0.57f, strokeWidth * 2f, -90, 360, EZMode.Einer)//
				.overrideColor(Color.WHITE)//
				.setZeigerType(ZEIGER_TYP.rect)//
				// .overrideColor(Color.RED)//
				.draw(bitmapCanvas);
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel, Color.WHITE);
	}

	@Override
	public void drawChargeStatusText(final int level) {
	}

	@Override
	public void drawBattStatusText(final int level) {
	}

}
