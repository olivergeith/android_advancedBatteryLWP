package de.geithonline.abattlwp.bitmapdrawer;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.parts.AnyPathPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart.RingType;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;

public class BinaryBarsV1 extends AdvancedBitmapDrawer {

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

	public BinaryBarsV1() {
	}

	@Override
	public boolean isPremiumDrawer() {
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

		new RingPart(center, maxRadius * 0.99f, maxRadius - randOffset, new Paint(), RingType.Square)//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		String binary = Integer.toBinaryString(level);
		while (binary.length() < 7) {
			binary = "0" + binary;
		}
		// Log.i("BrickBinaryV1", "Binary Code: " + binary);

		// Kästchen malen
		final float abstand = maxRadius * 0.02f;
		final float rectBreite = (2 * (maxRadius - randOffset) - 8 * abstand) / 7;

		for (int i = 0; i < 7; i++) {
			final RectF rect = new RectF();
			rect.bottom = bmpHeight - randOffset - abstand;
			rect.left = randOffset + abstand + i * (rectBreite + abstand);
			rect.right = rect.left + rectBreite;
			rect.top = randOffset + abstand + i * (rectBreite + abstand); // todo ?
			final Path rectPath = new Path();
			rectPath.addRect(rect, Direction.CW);
			// Rechtecke malen
			final char bin = binary.charAt(i);
			// Log.i("BrickBinaryV1", "Bin(" + i + "): " + bin);
			if (bin == '1') {
				new AnyPathPart(center, PaintProvider.getBatteryPaint(level), rectPath)//
						.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
						.draw(bitmapCanvas);
			} else {
				new AnyPathPart(center, PaintProvider.getBackgroundPaint(), rectPath)//
						.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
						.draw(bitmapCanvas);
			}
			// Zahlen zeichnenne
			final PointF centerSquare = new PointF();
			centerSquare.x = rect.left + (rect.right - rect.left) / 2;
			centerSquare.y = rect.bottom - rectBreite / 2;
			final RectF rectNumber = GeometrieHelper.getCircle(centerSquare, rectBreite / 2);
			drawLevelNumberCenteredInRect(bitmapCanvas, i, "" + (int) Math.pow(2, (6 - i)), fontSizeArc, rectNumber);
		}

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
