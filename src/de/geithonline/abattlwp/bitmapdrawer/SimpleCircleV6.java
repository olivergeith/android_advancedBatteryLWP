package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZColoring;
import de.geithonline.abattlwp.bitmapdrawer.enums.EZMode;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.LevelZeigerPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart.RingType;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnCirclePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class SimpleCircleV6 extends AdvancedBitmapDrawer {

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
		fontSizeLevel = maxRadius * 0.4f;
	}

	public SimpleCircleV6() {
	}

	public SimpleCircleV6(final float bgRa, final float bgRi, final float levelRa, final float levelRi) {
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
	public boolean supportsShowRand() {
		return false;
	}

	@Override
	public boolean supportsGlowScala() {
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
		new RingPart(center, maxRadius, 0, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32, 200), PaintProvider.getGray(192, 200), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Level
		new LevelPart(center, maxRadius * 0.99f, maxRadius * 0f, level, -90, 360, EZColoring.LevelColors)//
				.setSegemteAbstand(1.25f)//
				.setStrokeWidth(strokeWidth / 3)//
				.setStyle(Settings.getLevelStyle())//
				.setMode(Settings.getLevelMode())//
				.draw(bitmapCanvas);

		// Ring aussen
		if (Settings.isShowGlowScala()) {
			// new RingPart(center, maxRadius * 0.99f, maxRadius * 0.85f, new Paint(), RingType.SquareInnerCircle)//
			// .setColor(Color.BLACK)//
			// .setDropShadow(new DropShadow(strokeWidth * 30, Settings.getGlowScalaColor()))//
			// .draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.99f, maxRadius * 0.85f, new Paint(), RingType.SquareInnerCircle)//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * 0.99f, maxRadius * 0.85f, new Paint(), RingType.SquareInnerCircle)//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		new RingPart(center, maxRadius * 1f, maxRadius * 0.82f, new Paint(), RingType.SquareInnerCircle)//
				.setGradient(new Gradient(PaintProvider.getGray(192), PaintProvider.getGray(32), GRAD_STYLE.diagonal))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		new RingPart(center, maxRadius * 0.92f, maxRadius * 0.82f, new Paint(), RingType.SquareInnerCircle)//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(128), GRAD_STYLE.diagonal))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// new LevelPart(center, maxRadius * 0.99f, maxRadius * levelRa, level, -90, 360, EZColoring.LevelColors)//
		// .setSegemteAbstand(1.25f)//
		// .setStrokeWidth(strokeWidth / 3)//
		// .setStyle(Settings.getLevelStyle())//
		// .setMode(Settings.getLevelMode())//
		// .setInverted(true)//
		// .draw(bitmapCanvas);
		//
		// SkalaGlow
		final float ra = 0.5f;
		final float ri = 0.4f;
		if (Settings.isShowGlowScala()) {
			// new RingPart(center, maxRadius * ra, maxRadius * ri, new Paint())//
			// .setColor(Color.BLACK)//
			// .setDropShadow(new DropShadow(strokeWidth * 30, Settings.getGlowScalaColor()))//
			// .draw(bitmapCanvas);
			new RingPart(center, maxRadius * ra, maxRadius * ri, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 10, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
			new RingPart(center, maxRadius * ra, maxRadius * ri, new Paint())//
					.setColor(Color.BLACK)//
					.setDropShadow(new DropShadow(strokeWidth * 3, Settings.getGlowScalaColor()))//
					.draw(bitmapCanvas);
		}
		if (Settings.isShowZeiger()) {
			// Zeiger
			new LevelZeigerPart(center, level, maxRadius * 1f, maxRadius * 0.65f, strokeWidth, -90, 360, EZMode.Einer)//
					.setDropShadow(new DropShadow(2 * strokeWidth, Color.BLACK))//
					.draw(bitmapCanvas);
		}
		// Innen Ring
		new RingPart(center, maxRadius * ra, maxRadius * ri, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(192), PaintProvider.getGray(32), GRAD_STYLE.diagonal))//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
		new RingPart(center, maxRadius * ri, maxRadius * 0, new Paint())//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(192), GRAD_STYLE.diagonal))//
				.setOutline(new Outline(PaintProvider.getGray(192), strokeWidth / 2))//
				.draw(bitmapCanvas);
	}

	@Override
	public void drawLevelNumber(final int level) {
		drawLevelNumberCentered(bitmapCanvas, level, fontSizeLevel);
	}

	@Override
	public void drawChargeStatusText(final int level) {
		final long winkel = 270 + Math.round(level * 3.6f);
		new TextOnCirclePart(center, maxRadius * 0.84f, winkel, fontSizeArc, new Paint())//
				.setColor(Settings.getChargeStatusColor())//
				.setAlign(Align.LEFT)//
				.draw(bitmapCanvas, Settings.getChargingText());
	}

	@Override
	public void drawBattStatusText(final int level) {
		final Path mArc = new Path();
		final RectF oval = GeometrieHelper.getCircle(center, maxRadius * 0.40f);
		mArc.addArc(oval, 180, 180);
		final String text = Settings.getBattStatusCompleteShort();
		final Paint p = PaintProvider.getTextBattStatusPaint(fontSizeArc, Align.CENTER, true);
		bitmapCanvas.drawTextOnPath(text, mArc, 0, 0, p);
	}

}
