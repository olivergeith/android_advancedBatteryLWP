package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.data.Gradient;
import de.geithonline.abattlwp.bitmapdrawer.data.Outline;
import de.geithonline.abattlwp.bitmapdrawer.shapes.HalfArcPath;
import de.geithonline.abattlwp.settings.PaintProvider;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public class HalfArchPart {

	private final PointF c;
	private final float ra;
	private final float ri;
	private final Paint paint;
	private Path path;
	private Outline outline = null;
	// private DropShadow dropShadow;
	private Gradient gradient;
	private float undercut = 0;
	private boolean erase = false;

	public HalfArchPart(final PointF center, final float radAussen, final float radInnen, final Paint paint) {
		c = center;
		ra = radAussen;
		ri = radInnen;

		this.paint = paint;
		initPaint();
	}

	private void initPaint() {
		paint.setAntiAlias(true);
		paint.setStyle(Style.FILL);
	}

	public HalfArchPart setEraseBeforDraw() {
		erase = true;
		return this;
	}

	public HalfArchPart setOutline(final Outline outline) {
		this.outline = outline;
		return this;
	}

	public HalfArchPart setUndercut(final float undercut) {
		this.undercut = undercut;
		return this;
	}

	public HalfArchPart setAlpha(final int alpha) {
		paint.setAlpha(alpha);
		return this;
	}

	public HalfArchPart setDropShadow(final DropShadow dropShadow) {
		if (dropShadow != null) {
			dropShadow.setUpPaint(paint);
		}
		return this;
	}

	public HalfArchPart setColor(final int color) {
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		return this;
	}

	public HalfArchPart setGradient(final Gradient gradient) {
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

	public HalfArchPart draw(final Canvas canvas) {
		path = new HalfArcPath(c, ra, ri, undercut);

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
