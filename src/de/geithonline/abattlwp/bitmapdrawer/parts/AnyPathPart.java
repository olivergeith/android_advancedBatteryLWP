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
import de.geithonline.abattlwp.settings.PaintProvider;

public class AnyPathPart {

	private final PointF c;
	private float ra;
	private float ri;
	private final Paint paint;
	private final Path path;
	private Outline outline = null;
	private Gradient gradient;
	private boolean erase = false;

	public AnyPathPart(final PointF center, final Paint paint, final Path path) {
		c = center;
		this.path = path;
		this.paint = paint;
		initPaint();
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public AnyPathPart setOutline(final Outline outline) {
		this.outline = outline;
		return this;
	}

	public AnyPathPart setAlpha(final int alpha) {
		paint.setAlpha(alpha);
		return this;
	}

	public AnyPathPart setEraseBeforDraw() {
		erase = true;
		return this;
	}

	public AnyPathPart setDropShadow(final DropShadow dropShadow) {
		if (dropShadow != null) {
			dropShadow.setUpPaint(paint);
		}
		return this;
	}

	public AnyPathPart setColor(final int color) {
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		return this;
	}

	public AnyPathPart setGradient(final Gradient gradient, final float radAussen, final float radInnen) {
		ra = radAussen;
		ri = radInnen;
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

	public AnyPathPart draw(final Canvas canvas) {
		if (erase) {
			canvas.drawPath(path, PaintProvider.getErasurePaint());
		}
		canvas.drawPath(path, paint);
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
