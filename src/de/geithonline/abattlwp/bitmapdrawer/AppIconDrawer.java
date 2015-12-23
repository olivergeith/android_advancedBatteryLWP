package de.geithonline.abattlwp.bitmapdrawer;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZStyle;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class AppIconDrawer extends AdvancedBitmapDrawer {

	private float strokeWidth;

	private float fontSizeLevel;

	private float maxRadius;

	private final PointF center = new PointF();

	private float fontSizeArc;

	private void initPrivateMembers() {
		center.x = bmpWidth / 2;
		center.y = bmpHeight / 2;
		maxRadius = bmpWidth / 2;
		// Strokes
		strokeWidth = maxRadius * 0.02f;
		// fontsizes
		fontSizeLevel = maxRadius * 0.7f;
		fontSizeArc = maxRadius * 0.08f;
		// Radiusses

	}

	public AppIconDrawer() {
	}

	@Override
	public List<String> getVariants() {
		final List<String> list = new ArrayList<>();
		list.add("Normal");
		list.add("Without background");
		return list;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {
		// Hintergrund
		switch (Settings.getStyleVariante(this.getClass().getSimpleName())) {
			default:
			case "Normal":
				new RingPart(center, maxRadius * 0.99f, 0, PaintProvider.getBackgroundPaint())//
						.setColor(Color.argb(255, 0, 88, 133)) // Primary Color
						.draw(bitmapCanvas);
				break;
			case "Without background":
				break;
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
