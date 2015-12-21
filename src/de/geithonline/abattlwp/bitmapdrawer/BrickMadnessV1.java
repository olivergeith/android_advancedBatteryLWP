package de.geithonline.abattlwp.bitmapdrawer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.parts.AnyPathPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart.RingType;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.StarPath;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;
import de.geithonline.abattlwp.utils.PathHelper;
import de.geithonline.abattlwp.utils.Randomizer;

public class BrickMadnessV1 extends AdvancedBitmapDrawer {

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
		fontSizeLevel = maxRadius * 0.1f;
	}

	private final Map<Integer, Point> positionMap = new HashMap<>();

	private final List<Point> positions = new ArrayList<>();

	public enum BrickStyle {
		Normal, Random;
	}

	protected Point drawRandomPoint() {
		final int size = positions.size();
		if (size == 0) {
			return new Point(0, 0);
		}
		final int location = Randomizer.getRandomInt(-1, size - 1);
		final Point p = positions.remove(location);
		return p;
	}

	protected Point drawFirstPoint() {
		final int size = positions.size();
		if (size == 0) {
			return new Point(0, 0);
		}
		final int location = 0;
		final Point p = positions.remove(location);
		return p;
	}

	protected Point drawLastPoint() {
		final int size = positions.size();
		if (size == 0) {
			return new Point(0, 0);
		}
		final int location = size - 1;
		final Point p = positions.remove(location);
		return p;
	}

	public BrickMadnessV1(final BrickStyle style) {
		// Liste mit Positionen bauen
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				positions.add(new Point(x, y));
			}
		}
		// alle level durchgehen und ein zufälligen Punkt ziehen
		for (int i = 1; i <= 100; i++) {
			switch (style) {
				default:
				case Normal:
					positionMap.put(i, drawFirstPoint());
					break;
				case Random:
					positionMap.put(i, drawRandomPoint());
					break;
			}
		}
	}

	@Override
	public boolean supportsShowRand() {
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
		final float randOffset = maxRadius * 0.15f;
		// SkalaBackground
		new RingPart(center, maxRadius * 0.99f, maxRadius * 0.0f, PaintProvider.getBackgroundPaint(), RingType.Square)//
				.draw(bitmapCanvas);

		new RingPart(center, maxRadius * 0.99f, maxRadius - randOffset, new Paint(), RingType.Square)//
				.setGradient(new Gradient(PaintProvider.getGray(32), PaintProvider.getGray(96), GRAD_STYLE.top2bottom))//
				.setOutline(new Outline(PaintProvider.getGray(128), strokeWidth / 2))//
				.draw(bitmapCanvas);

		// Kästchen malen
		final float raster = 2 * maxRadius * 0.85f / 10;
		final float abstand = maxRadius * 0.01f;
		// EasterEgg... On December we use stars!
		final Calendar date = new GregorianCalendar();

		for (int i = 1; i <= 100; i++) {
			final Point p = positionMap.get(i);
			final PointF centerSquare = new PointF();
			centerSquare.x = randOffset + raster / 2 + p.x * raster;
			centerSquare.y = randOffset + raster / 2 + p.y * raster;
			if (i <= level) {
				if (date.get(Calendar.MONTH) == Calendar.DECEMBER) {
					// Easteregg : Rotating Stars in December
					final Path star = new StarPath(5, centerSquare, raster / 2, raster / 4);
					PathHelper.rotatePath(centerSquare.x, centerSquare.y, star, level * 7.2f);
					new AnyPathPart(centerSquare, PaintProvider.getBatteryPaint(level), star)//
							.draw(bitmapCanvas);
				} else {
					new RingPart(centerSquare, raster / 2 - abstand, 0.0f, PaintProvider.getBatteryPaint(level), RingType.Square)//
							.draw(bitmapCanvas);
				}
				// Zahlenzeichnenne
				if (Settings.isShowRand()) {
					final RectF rect = GeometrieHelper.getCircle(centerSquare, raster / 2);
					drawLevelNumberCenteredInRect(bitmapCanvas, i, "" + i, fontSizeArc, rect);
				}

			}
		}

	}

	@Override
	public void drawLevelNumber(final int level) {
		new TextOnLinePart(center, maxRadius * 0.88f, -90, fontSizeLevel, PaintProvider.getLevelNumberPaint(level, fontSizeLevel))//
				.setAlign(Align.CENTER)//
				.draw(bitmapCanvas, level + " %");
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
