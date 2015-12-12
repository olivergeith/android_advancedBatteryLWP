package de.geithonline.abattlwp.bitmapdrawer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import de.geithonline.abattlwp.PermissionRequester;
import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
import de.geithonline.abattlwp.bitmapdrawer.enums.BitmapRatio;
import de.geithonline.abattlwp.settings.PaintProvider;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.BitmapHelper;

/**
 * @author Oliver
 *
 */
/**
 * @author Oliver
 *
 */
public abstract class AdvancedBitmapDrawer implements IBitmapDrawer {
	private int displayHeight = 0;
	private int displayWidth = 0;
	private Bitmap bitmap;
	protected Canvas bitmapCanvas;
	protected int bmpHeight = 0;
	protected int bmpWidth = 0;
	protected int level = -99;
	private boolean isDrawIcon = false;

	public abstract Bitmap drawBitmap(final int level, Bitmap Bitmap);

	public abstract void drawLevelNumber(final int level);

	public abstract void drawChargeStatusText(final int level);

	public abstract void drawBattStatusText();

	private final void drawOnCanvas(Bitmap bitmap, final Canvas canvas) {
		switch (Settings.getBitmapRatation()) {
			default:
			case NoRotatation:
				break;
			case Left:
				bitmap = BitmapHelper.rotate(bitmap, -90);
				break;
			case Right:
				bitmap = BitmapHelper.rotate(bitmap, 90);
				break;
		}

		final int bh = bitmap.getHeight();
		final int bw = bitmap.getWidth();
		int y = 0;
		switch (Settings.getVerticalPosition()) {
			default:
			case Center:
				y = displayHeight / 2 - bh / 2;
				break;
			case Top:
				y = 0;
				break;
			case Bottom:
				y = displayHeight - bh;
				break;
			case Custom: // offset von unten
				y = displayHeight - bh - Settings.getVerticalPositionOffset(isPortrait());
				break;
		}
		int x = 0;
		switch (Settings.getHorizontalPosition()) {
			default:
			case Center:
				x = displayWidth / 2 - bw / 2;
				break;
			case Left:
				x = 0;
				break;
			case Right:
				x = displayWidth - bw;
				break;
		}
		canvas.drawBitmap(bitmap, x, y, null);
	}

	private final Bitmap initBitmap() {
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		// das heist, die Batterie ist immer gleich gross
		if (isPortrait()) {
			// hochkant
			setBitmapSize(displayWidth, getBitmapHight(displayWidth), true);
		} else {
			// quer
			setBitmapSize(displayHeight, getBitmapHight(displayHeight), false);
		}
		final Bitmap bitmap = Bitmap.createBitmap(bmpWidth, bmpHeight, Bitmap.Config.ARGB_8888);
		return bitmap;
	}

	private final int getBitmapHight(final int width) {
		switch (getBitmapRatio()) {
			default:
			case SQUARE:
				return width;
			case RECTANGULAR:
				return getBitmapHightRectangular(width);
		}
	}

	/**
	 * this method should be overwritten if you want anoher Hight for a rectangular bitmap, other then width/2...that's default!
	 * 
	 * @param width
	 * @return
	 */
	protected int getBitmapHightRectangular(final int width) {
		return width / 2;
	}

	private final void setBitmapSize(final int w, final int h, final boolean isPortrait) {
		// kein resizen wenn ein icon gemalt wird!
		if (isDrawIcon) {
			bmpHeight = h;
			bmpWidth = w;
			return;
		}

		if (isPortrait) {
			// hochkant
			bmpHeight = Math.round(h * Settings.getPortraitResizeFactor());
			bmpWidth = Math.round(w * Settings.getPortraitResizeFactor());
		} else {
			// landscape mode
			bmpHeight = Math.round(h * Settings.getLandscapeResizeFactor());
			bmpWidth = Math.round(w * Settings.getLandscapeResizeFactor());
		}
	}

	@Override
	public void draw(final int level, final Canvas canvas, final boolean forcedraw) {
		final int h = canvas.getHeight();
		final int w = canvas.getWidth();
		// Bitmap neu berechnen wenn Level sich �ndert oder Canvas dimensions
		if (this.level != level || w != displayWidth || h != displayHeight || bitmap == null || forcedraw) {
			displayWidth = w;
			displayHeight = h;
			// Memory frei geben f�r altes bitmap
			if (bitmap != null) {
				bitmap.recycle();
			}
			// Bitnmap neu berechnen
			bitmap = initBitmap();
			bitmapCanvas = new Canvas(bitmap);
			bitmap = drawBitmap(level, bitmap);
			if (Settings.isShowNumber()) {
				drawLevelNumber(level);
			}
			if (Settings.isCharging && Settings.isShowChargeState()) {
				drawChargeStatusText(level);
			}
			if (Settings.isShowStatus()) {
				drawBattStatusText();
			}
		}
		// den aktuellen level merken
		this.level = level;
		if (Settings.isDebugging()) {
			if (PermissionRequester.isReadWritePermission()) {
				BitmapHelper.saveBitmap(bitmap, getClass().getSimpleName(), level);
			}
		}
		drawOnCanvas(bitmap, canvas);
	}

	@Override
	public Bitmap drawIcon(final int level, final int size) {
		final int h = size;
		final int w = size;
		// Bitmap neu berechnen wenn Level sich �ndert oder Canvas dimensions
		displayWidth = w;
		displayHeight = h;
		isDrawIcon = true;
		Bitmap icon = initBitmap();
		bitmapCanvas = new Canvas(icon);
		icon = drawBitmap(level, icon);
		isDrawIcon = false;
		if (Settings.isShowNumber()) {
			drawLevelNumber(level);
		}
		if (Settings.isCharging && Settings.isShowChargeState()) {
			drawChargeStatusText(level);
		}
		if (Settings.isShowStatus()) {
			drawBattStatusText();
		}
		return icon;
	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final float fontSize) {
		drawLevelNumberCentered(canvas, level, fontSize, null);
	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final float fontSize, final DropShadow dropShadow) {
		final String text = "" + level;
		final Paint p = PaintProvider.getNumberPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		if (dropShadow != null) {
			dropShadow.setUpPaint(p);
		}
		final PointF point = getTextCenterToDraw(new RectF(0, 0, bmpWidth, bmpHeight), p);
		canvas.drawText(text, point.x, point.y, p);
	}

	protected void drawLevelNumberBottom(final Canvas canvas, final int level, final float fontSize) {
		final Paint p = PaintProvider.getNumberPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText("" + level, bmpWidth / 2, bmpHeight - Math.round(bmpWidth * 0.01f), p);
	}

	protected void drawLevelNumber(final Canvas canvas, final int level, final float fontSize, final PointF position) {
		final Paint p = PaintProvider.getNumberPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText("" + level, position.x, position.y, p);
	}

	protected void drawLevelNumberCenteredInRect(final Canvas canvas, final int level, final String txt, final float fontSize, final RectF rect) {
		final Paint p = PaintProvider.getNumberPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		final PointF position = getTextCenterToDraw(rect, p);
		canvas.drawText(txt, position.x, position.y, p);
	}

	private static PointF getTextCenterToDraw(final RectF region, final Paint paint) {
		final Rect textBounds = new Rect();
		paint.getTextBounds("69", 0, 2, textBounds);
		final float x = region.centerX();
		final float y = region.centerY() + textBounds.height() * 0.5f;
		return new PointF(x, y);
	}

	private boolean isPortrait() {
		return displayHeight > displayWidth;
	}

	@Override
	public boolean supportsShowPointer() {
		return false;
	}

	@Override
	public boolean supportsPointerColor() {
		return false;
	}

	@Override
	public boolean supportsShowRand() {
		return false;
	}

	@Override
	public boolean supportsLevelStyle() {
		return false;
	}

	@Override
	public boolean supportsGlowScala() {
		return false;
	}

	@Override
	public boolean supportsExtraLevelBars() {
		return false;
	}

	@Override
	public boolean supportsThermometer() {
		return false;
	}

	@Override
	public boolean supportsVoltmeter() {
		return false;
	}

	/**
	 * Diese Methode kann von Kindklassen �berschrieben werden, wenn sie ein anderes Ratio als SQUARE haben wollen!
	 * 
	 * @return the ratio Enum
	 */
	protected BitmapRatio getBitmapRatio() {
		return BitmapRatio.SQUARE;
	}

}
