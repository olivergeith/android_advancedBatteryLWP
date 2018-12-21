package de.geithonline.abattlwp.bitmapdrawer;

import java.util.ArrayList;
import java.util.List;

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
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart.RingType;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;

public class BrickV3 extends AdvancedBitmapDrawer {

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

	public BrickV3() {
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
	public boolean supportsGlowScala() {
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
		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint(), RingType.Square)//
				.draw(bitmapCanvas);

		new LevelPart(center, maxRadius * 1.5f, maxRadius * 0.0f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Ring aussen
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.99f, maxRadius - randOffset, new Paint(), RingType.Square)//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 30, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.99f, maxRadius - randOffset, new Paint(), RingType.Square)//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.99f, maxRadius - randOffset, new Paint(), RingType.Square)//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}

		new RingPart(center, maxRadius * 0.99f, maxRadius - randOffset, new Paint(), RingType.Square)//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// innere Fläche
		if (Settings.isShowGlowScala()) {
			new RingPart(center, maxRadius * 0.4f, maxRadius * 0f, new Paint(), RingType.SquareRoundet)//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 30, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.4f, maxRadius * 0f, new Paint(), RingType.SquareRoundet)//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.4f, maxRadius * 0f, new Paint(), RingType.SquareRoundet)//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		new RingPart(center, maxRadius * 0.4f, maxRadius * 0f, new Paint(), RingType.SquareRoundet)//
				.setGradient(new Gradient(PaintProvider.getGray(96), PaintProvider.getGray(32), GRAD_STYLE.diagonal))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * 0.35f, maxRadius * 0f, new Paint(), RingType.SquareRoundet)//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.diagonal))//
				// .setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
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
