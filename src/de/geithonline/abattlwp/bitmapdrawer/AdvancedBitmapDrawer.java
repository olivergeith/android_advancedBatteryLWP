package de.geithonline.abattlwp.bitmapdrawer;

import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import de.geithonline.abattlwp.PermissionRequester;
import de.geithonline.abattlwp.PremiumBannerDrawer;
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
	private int oldLevel = -99;

	public abstract Bitmap drawBitmap(final int level, Bitmap Bitmap);

	public abstract void drawLevelNumber(final int level);

	public abstract void drawChargeStatusText(final int level);

	public abstract void drawBattStatusText(int level);

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

	private final Bitmap initBitmap(final boolean isDrawIcon) {
		// welche kante ist schmaler?
		// wir orientieren uns an der schmalsten kante
		// das heist, die Batterie ist immer gleich gross
		if (isPortrait()) {
			// hochkant
			setBitmapSize(displayWidth, getBitmapHight(displayWidth), true, isDrawIcon);
		} else {
			// quer
			setBitmapSize(displayHeight, getBitmapHight(displayHeight), false, isDrawIcon);
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

	private final void setBitmapSize(final int w, final int h, final boolean isPortrait, final boolean isDrawIcon) {
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
		// Bitmap neu berechnen wenn Level sich Ändert oder Canvas dimensions
		if (oldLevel != level || w != displayWidth || h != displayHeight || bitmap == null || forcedraw) {
			displayWidth = w;
			displayHeight = h;
			// Memory frei geben für altes bitmap
			if (bitmap != null) {
				bitmap.recycle();
			}
			// Bitnmap neu berechnen
			bitmap = initBitmap(false);
			bitmapCanvas = new Canvas(bitmap);
			bitmap = drawBitmap(level, bitmap);
			if (Settings.isCharging && Settings.isShowChargeState()) {
				drawChargeStatusText(level);
			}
			if (Settings.isShowStatus()) {
				drawBattStatusText(level);
			}
			if (Settings.isShowNumber()) {
				drawLevelNumber(level);
			}
			drawNonPremiumBanner(bitmapCanvas);
		}
		// den aktuellen level merken
		oldLevel = level;
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
		// Bitmap neu berechnen wenn Level sich Ändert oder Canvas dimensions
		displayWidth = w;
		displayHeight = h;
		Bitmap icon = initBitmap(true);
		bitmapCanvas = new Canvas(icon);
		icon = drawBitmap(level, icon);
		if (Settings.isCharging && Settings.isShowChargeState()) {
			drawChargeStatusText(level);
		}
		if (Settings.isShowStatus()) {
			drawBattStatusText(level);
		}
		if (Settings.isShowNumber()) {
			drawLevelNumber(level);
		}
		drawNonPremiumBanner(bitmapCanvas);
		return icon;
	}

	private void drawNonPremiumBanner(final Canvas canvas) {
		// Wenn die APP nicht Premium ist UND dieser Drawer nur für Premium
		if (!Settings.isPremium() && isPremiumDrawer()) {
			new PremiumBannerDrawer(bitmapCanvas).drawTopLeftDiagonalBanner();
		}

	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final float fontSize) {
		drawLevelNumberCentered(canvas, level, fontSize, null, 0);
	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final float fontSize, final DropShadow drop) {
		drawLevelNumberCentered(canvas, level, fontSize, drop, 0);
	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final float fontSize, final int color) {
		drawLevelNumberCentered(canvas, level, fontSize, null, color);
	}

	protected void drawLevelNumberCentered(final Canvas canvas, final int level, final float fontSize, final DropShadow dropShadow, final int color) {
		final String text = "" + level;
		final Paint p = PaintProvider.getLevelNumberPaint(level, fontSize);
		if (color != 0) {
			p.setColor(color);
			p.setAlpha(255);
		}
		p.setTextAlign(Align.CENTER);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		if (dropShadow != null) {
			dropShadow.setUpPaint(p);
		}
		final PointF point = getTextCenterToDraw(new RectF(0, 0, bmpWidth, bmpHeight), p);
		canvas.drawText(text, point.x, point.y, p);
	}

	protected void drawLevelNumberBottom(final Canvas canvas, final int level, final float fontSize) {
		final Paint p = PaintProvider.getLevelNumberPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText("" + level, bmpWidth / 2, bmpHeight - Math.round(bmpWidth * 0.01f), p);
	}

	protected void drawLevelNumber(final Canvas canvas, final int level, final float fontSize, final PointF position) {
		final Paint p = PaintProvider.getLevelNumberPaint(level, fontSize);
		p.setTextAlign(Align.CENTER);
		p.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText("" + level, position.x, position.y, p);
	}

	protected void drawLevelNumberCenteredInRect(final Canvas canvas, final int level, final String txt, final float fontSize, final RectF rect) {
		final Paint p = PaintProvider.getLevelNumberPaint(level, fontSize);
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

	@Override
	public boolean supportsLevelNumberFontSizeAdjustment() {
		return true;
	}

	@Override
	public List<String> getVariants() {
		return null;
	}

	@Override
	public boolean supportsVariants() {
		final List<String> variants = getVariants();
		if (variants == null) {
			return false;
		}
		if (variants.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * Diese Methode kann von Kindklassen überschrieben werden, wenn sie ein anderes Ratio als SQUARE haben wollen!
	 * 
	 * @return the ratio Enum
	 */
	protected BitmapRatio getBitmapRatio() {
		return BitmapRatio.SQUARE;
	}

	@Override
	public boolean isPremiumDrawer() {
		return false;
	}

}
