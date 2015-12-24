package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.shapes.CirclePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.SquarePath;
import de.geithonline.abattlwp.bitmapdrawer.shapes.SquarePath.SQUARE_STYLE;
import de.geithonline.abattlwp.settings.PaintProvider;

public class RingPart {

	public enum RingType {
		Circle, SquareRoundet, Square
	};

	private PointF c;
	private float ra;
	private float ri;
	private Paint paint;
	private Path path;
	private Outline outline = null;
	private Gradient gradient;
	private boolean erase = false;
	private BubbleDrawer bubble = null;

	public RingPart(final PointF center, final float radAussen, final float radInnen, final Paint paint) {
		init(center, radAussen, radInnen, paint, RingType.Circle);
	}

	public RingPart(final PointF center, final float radAussen, final float radInnen, final Paint paint, final RingType type) {
		init(center, radAussen, radInnen, paint, type);
	}

	private void init(final PointF center, final float radAussen, final float radInnen, final Paint paint, final RingType type) {
		c = center;
		ra = radAussen;
		ri = radInnen;
		this.paint = paint;
		initPaint();
		switch (type) {
			default:
			case Circle:
				path = new CirclePath(c, ra, ri);
				break;
			case SquareRoundet:
				path = new SquarePath(c, ra, ri, SQUARE_STYLE.ROUNDED);
				break;
			case Square:
				path = new SquarePath(c, ra, ri, SQUARE_STYLE.NORMAL);
				break;
		}
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public RingPart setOutline(final Outline outline) {
		this.outline = outline;
		return this;
	}

	public RingPart setGlossyBubble() {
		bubble = new BubbleDrawer();
		return this;
	}

	public RingPart setAlpha(final int alpha) {
		paint.setAlpha(alpha);
		return this;
	}

	public RingPart setEraseBeforDraw() {
		erase = true;
		return this;
	}

	public RingPart setDropShadow(final DropShadow dropShadow) {
		if (dropShadow != null) {
			dropShadow.setUpPaint(paint);
		}
		return this;
	}

	public RingPart setColor(final int color) {
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		return this;
	}

	public RingPart setGradient(final Gradient gradient) {
		this.gradient = gradient;
		setupGradient();
		return this;
	}

	private float[] getDistancesRadial() {
		final float di = ri / ra;
		final float da = 1.0f;
		final float distances[] = new float[] { di, da };
		return distances;
	}

	private void setupGradient() {
		if (gradient != null) {
			paint.setStyle(Style.FILL);
			switch (gradient.getStyle()) {
				default:
				case top2bottom:
					paint.setShader(new LinearGradient(c.x, c.y - ra, c.x, c.y + ra, gradient.getColor1(), gradient.getColor2(), Shader.TileMode.MIRROR));
					break;
				case radial:
					final int[] colors = new int[] { gradient.getColor1(), gradient.getColor2() };
					paint.setShader(new RadialGradient(c.x, c.y, ra, colors, getDistancesRadial(), Shader.TileMode.CLAMP));
					break;
			}
		}
	}

	public RingPart draw(final Canvas canvas) {
		if (erase) {
			canvas.drawPath(path, PaintProvider.getErasurePaint());
		}
		if (bubble != null) {
			bubble.drawBubbleGlow(canvas, c, paint, ra, path);
		} else {
			canvas.drawPath(path, paint);
		}
		// Outline?
		if (outline != null) {
			// aufräumen für Outline
			paint.setShader(null);
			paint.setShadowLayer(0, 0, 0, Color.BLACK);
			// stroke einstellen
			paint.setColor(outline.getColor());
			paint.setStrokeWidth(outline.getStrokeWidth());
			paint.setStyle(Style.STROKE);
			// und nochmal zeichnen
			canvas.drawPath(path, paint);
		}
		return this;
	}
}
