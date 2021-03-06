package de.geithonline.abattlwp.bitmapdrawer;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPartRectangular;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class LevelBar extends AdvancedBitmapDrawer {

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
		fontSizeLevel = maxRadius * 0.35f;
	}

	public LevelBar() {
	}

	@Override
	public boolean isPremiumDrawer() {
		return true;
	}

	@Override
	public boolean supportsLevelStyle() {
		return true;
	}

	@Override
	public List<String> getVariants() {
		final List<String> list = new ArrayList<>();
		return list;
	}

	@Override
	public Bitmap drawBitmap(final int level, final Bitmap bitmap) {
		initPrivateMembers();
		drawAll(level);
		return bitmap;
	}

	private void drawAll(final int level) {
		final float randOffset = maxRadius * 0.15f;

		final RectF levelrect = GeometrieHelper.getOval(new PointF(center.x - maxRadius * 0.95f, center.y), maxRadius * 0.1f, maxRadius);
		new LevelPartRectangular(levelrect, level, EZColoring.LevelColors)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

	}

	@Override
	public void drawLevelNumber(final int level) {
		final float randOffset = maxRadius * 0.15f;
		final RectF rect = new RectF();
		rect.bottom = bmpHeight / 2;
		rect.left = bmpWidth / 2;
		rect.right = bmpWidth - randOffset;
		rect.top = randOffset; // todo ?
		drawLevelNumberCenteredInRect(bitmapCanvas, level, "" + level, fontSizeLevel, rect);

		final PointF c = new PointF(rect.centerX(), rect.centerY());

	}

	@Override
	public void drawChargeStatusText(final int level) {
		new TextOnLinePart(center, maxRadius * 0.90f, 0, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		new TextOnLinePart(center, maxRadius * 0.95f, 90, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.CENTER)//
				.invert(true).draw(bitmapCanvas, Settings.getBattStatusCompleteShort());
	}

}
