package de.geithonline.abattlwp.bitmapdrawer.parts;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import de.geithonline.abattlwp.utils.ColorHelper;

public class BubbleDrawer {

	public void drawBubbleGlow(final Canvas canvas, final PointF center, final Paint paint, final float radius, final Path path) {
		// Glossy glow
		paint.setShadowLayer(0, 0, 0, 0);
		setupPaintShaderCenterGlow(center, paint, radius);
		canvas.drawPath(path, paint);

		// Reflection
		final RectF oval = new RectF(center.x - radius * 3 / 4, center.y - radius, center.x + radius * 3 / 4, center.y);
		setupPaintShaderForOval(center, paint, oval);
		canvas.drawOval(oval, paint);
		// aufräumen
		paint.setShader(null);
	}

	private static void setupPaintShaderCenterGlow(final PointF center, final Paint paint, final float radius) {
		final int colorBrighter = ColorHelper.changeAlpha(Color.BLACK, -224);
		final int colorDarker = ColorHelper.changeAlpha(Color.WHITE, -128);
		paint.setShader(new RadialGradient(center.x, center.y, radius, colorBrighter, colorDarker, Shader.TileMode.CLAMP));
	}

	private void setupPaintShaderForOval(final PointF center, final Paint paint, final RectF oval) {
		final int brigntness = 192;
		final int white1 = Color.argb(brigntness, 255, 255, 255);
		final int transparent = Color.argb(0, 255, 255, 255);
		paint.setShader(new LinearGradient(center.x, oval.top, center.x, oval.bottom, //
				white1, transparent, //
				Shader.TileMode.CLAMP));
		paint.setStyle(Style.FILL);
	}

}
