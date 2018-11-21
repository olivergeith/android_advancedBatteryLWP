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
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.ZeigerShapePath.ZEIGER_TYP;
import de.geithonline.abattlwp.settings.Settings;

public class SuperSimpleDrawer extends AdvancedBitmapDrawer {

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
		fontSizeLevel = maxRadius * 0.6f;
		fontSizeArc = maxRadius * 0.1f;
		// Radiusses

	}

	public SuperSimpleDrawer() {
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
	public boolean supportsPointerColor() {
		return true;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	@Override
	public List<String> getVariants() {
		final List<String> list = new ArrayList<>();
		list.add("Super Slim");
		list.add("Slim");
		list.add("Normal");
		list.add("Wide");
		return list;
	}

	private void drawAll(final int level) {
		float innerRadius = 0.75f;
		switch (Settings.getStyleVariante(this.getClass().getSimpleName())) {
			case "Super Slim":
				innerRadius = 0.88f;
				break;
			case "Slim":
				innerRadius = 0.85f;
				break;
			default:
			case "Normal":
				innerRadius = 0.75f;
				break;
			case "Wide":
				innerRadius = 0.65f;
				break;
		}

		// Hintergrund
		new LevelPart(center, maxRadius * 0.9f, maxRadius * innerRadius, level, -90, 360, EZColoring.LevelColors)//
				// .setColor(Color.WHITE)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		if (Settings.isShowZeiger()) {
			// Zeiger
			new LevelZeigerPart(center, level, maxRadius * innerRadius, maxRadius * (innerRadius - 0.15f), strokeWidth * 3f, -90, 360, EZMode.Einer)//
					.setZeigerType(ZEIGER_TYP.triangle)//
					.draw(bitmapCanvas);
		}
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel, Color.WHITE);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = -90;
		new TextOnCirclePart(center, maxRadius * 0.95f, winkel, fontSizeArc, new Paint())//
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
