package de.geithonline.abattlwp.bitmapdrawer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import de.geithonline.abattlwp.EasterEgg;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient.GRAD_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.parts.AnyPathPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart;
import de.geithonline.abattlwp.bitmapdrawer.parts.RingPart.RingType;
import de.geithonline.abattlwp.bitmapdrawer.parts.TextOnLinePart;
import de.geithonline.abattlwp.bitmapdrawer.shapes.HeartPath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.PacmanPath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.PacmanPath.PACMAN_STYLE;
import de.geithonline.abattlwp.bitmapdrawer.shapes.StarPath;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.GeometrieHelper;
import de.geithonline.abattlwp.utils.PathHelper;
import de.geithonline.abattlwp.utils.Randomizer;

public class BrickV1 extends AdvancedBitmapDrawer {

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
	private final Map<Integer, Point> positionMapRandom = new HashMap<>();

	private Point drawRandomPoint(final List<Point> positions) {
		final int size = positions.size();
		if (size == 0) {
			return new Point(0, 0);
		}
		final int location = Randomizer.getRandomInt(-1, size - 1);
		final Point p = positions.remove(location);
		return p;
	}

	private Point drawFirstPoint(final List<Point> positions) {
		final int size = positions.size();
		if (size == 0) {
			return new Point(0, 0);
		}
		final int location = 0;
		final Point p = positions.remove(location);
		return p;
	}

	@Override
	public boolean supportsGlowScala() {
		return true;
	}

	public BrickV1() {
		final List<Point> positions = new ArrayList<>();
		final List<Point> positionsRandom = new ArrayList<>();
		// Liste mit Positionen bauen
		for (int y = 0; y < 10; y++) {
			for (int x = 0; x < 10; x++) {
				positions.add(new Point(x, y));
				positionsRandom.add(new Point(x, y));
			}
		}
		// alle level durchgehen und ein zuf�lligen Punkt ziehen
		for (int i = 1; i <= 100; i++) {
			positionMap.put(i, drawFirstPoint(positions));
			positionMapRandom.put(i, drawRandomPoint(positionsRandom));
		}
	}

	@Override
	public boolean isPremiumDrawer() {
		return true;
	}

	@Override
	public List<String> getVariants() {
		final List<String> list = new ArrayList<>();
		list.add("Normal");
		list.add("Random");
		list.add("Normal with numbers");
		list.add("Random with numbers");
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
		// K�stchen malen
		final float raster = 2 * maxRadius * 0.85f / 10;
		final float abstand = maxRadius * 0.01f;
		// EasterEgg... On December we use stars!
		final EasterEgg easterEgg = new EasterEgg();

		for (int i = 1; i <= 100; i++) {
			final Point p;
			switch (Settings.getStyleVariante(this.getClass().getSimpleName())) {
				default:
				case "Normal":
				case "Normal with numbers":
					p = positionMap.get(i);
					break;
				case "Random":
				case "Random with numbers":
					p = positionMapRandom.get(i);
					break;
			}
			final PointF centerSquare = new PointF();
			centerSquare.x = randOffset + raster / 2 + p.x * raster;
			centerSquare.y = randOffset + raster / 2 + p.y * raster;
			if (i <= level) {
				if (easterEgg.isSomebodiesBirthday()) {
					final float size = raster / 4 + Randomizer.getRandomFloat(0, raster / 4);
					final Path heart = new HeartPath(centerSquare, size);
					new AnyPathPart(centerSquare, PaintProvider.getBatteryPaint(level), heart)//
							.draw(bitmapCanvas);
				} else if (easterEgg.isDecember()) {
					// Easteregg : Rotating Stars in December
					final Path star = new StarPath(5, centerSquare, raster / 2, raster / 4);
					PathHelper.rotatePath(centerSquare.x, centerSquare.y, star, level * 7.2f);
					new AnyPathPart(centerSquare, PaintProvider.getBatteryPaint(level), star)//
							.draw(bitmapCanvas);
				} else if (easterEgg.isHalloween()) {
					// Easteregg : Pacmans in Oktober (Halloween)
					final Path star = new PacmanPath(centerSquare, raster / 2, PACMAN_STYLE.GHOST_RANDOM_EYES);
					new AnyPathPart(centerSquare, PaintProvider.getBatteryPaint(level), star)//
							.draw(bitmapCanvas);
				} else {
					new RingPart(centerSquare, raster / 2 - abstand, 0.0f, PaintProvider.getBatteryPaint(level), RingType.Square)//
							.draw(bitmapCanvas);
				}
				// Zahlenzeichnenne
				switch (Settings.getStyleVariante(this.getClass().getSimpleName())) {
					default:
						break;
					case "Normal with numbers":
					case "Random with numbers":
						final RectF rect = GeometrieHelper.getCircle(centerSquare, raster / 2);
						drawLevelNumberCenteredInRect(bitmapCanvas, i, "" + i, fontSizeArc, rect);
						break;
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
