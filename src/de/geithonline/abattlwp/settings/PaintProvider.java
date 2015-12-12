package de.geithonline.abattlwp.settings;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import de.geithonline.abattlwp.utils.ColorHelper;

public class PaintProvider {

	private static Paint initBattStatusPaint() {
		final Paint battStatusPaint = new Paint();
		battStatusPaint.setAntiAlias(true);
		battStatusPaint.setAlpha(255);
		battStatusPaint.setFakeBoldText(true);
		battStatusPaint.setStyle(Style.FILL);
		return battStatusPaint;
	}

	private static Paint initErasurePaint() {
		final Paint erasurePaint = new Paint();
		erasurePaint.setAntiAlias(true);
		erasurePaint.setColor(Color.TRANSPARENT);
		final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
		erasurePaint.setXfermode(xfermode);
		erasurePaint.setStyle(Style.FILL);
		return erasurePaint;
	}

	private static Paint initNumberPaint() {
		final Paint numberPaint = new Paint();
		numberPaint.setAntiAlias(true);
		numberPaint.setFakeBoldText(true);
		return numberPaint;
	}

	private static Paint initTextPaint() {
		final Paint textPaint = new Paint();
		textPaint.setAntiAlias(true);
		textPaint.setAntiAlias(true);
		textPaint.setFakeBoldText(true);
		return textPaint;
	}

	private static Paint initBattPaint() {
		final Paint battPaint = new Paint();
		battPaint.setAntiAlias(true);
		battPaint.setStyle(Style.FILL);
		return battPaint;
	}

	// #####################################################################################
	// LevelColors
	// #####################################################################################
	public static int getColorForLevel(final int level) {
		if (Settings.isCharging && Settings.isUseChargeColors()) {
			return Settings.getChargeColor();
		}
		if (level > Settings.getMidThreshold()) {
			if (Settings.isGradientColors()) {
				return getGradientColorForLevel(level);
			} else {
				return Settings.getBattColor();
			}
		} else {
			if (level < Settings.getLowThreshold()) {
				return Settings.getBattColorLow();
			} else {
				if (Settings.isGradientColorsMid()) {
					return getGradientColorForLevel(level);
				} else {
					return Settings.getBattColorMid();
				}
			}
		}
	}

	public static int getGradientColorForLevel(final int level) {

		if (level > Settings.getMidThreshold()) {
			return ColorHelper.getRadiantColor(Settings.getBattColor(), Settings.getBattColorMid(), level, 100, Settings.getMidThreshold());
		} else {
			if (level < Settings.getLowThreshold()) {
				return Settings.getBattColorLow();
			} else {
				return ColorHelper.getRadiantColor(Settings.getBattColorLow(), Settings.getBattColorMid(), level, Settings.getLowThreshold(),
						Settings.getMidThreshold());
			}
		}
	}

	// #####################################################################################
	// Different Paints
	// #####################################################################################
	public static Paint getErasurePaint() {
		return initErasurePaint();
	}

	public static Paint getLevelNumberPaint(final int level, final float fontSize) {
		final Paint numberPaint = initNumberPaint();
		if (Settings.isColoredNumber()) {
			numberPaint.setColor(getColorForLevel(level));
		} else {
			numberPaint.setColor(Settings.getNumberColor());
		}
		final float fSize = adjustFontSize(level, fontSize);
		numberPaint.setTextSize(fSize);
		return numberPaint;
	}

	public static Paint getTextPaint(final int level, final float fontSizeArc) {
		return getTextPaint(level, fontSizeArc, Align.CENTER, true, false);
	}

	private static Paint getTextPaint(final int level, final float fontSize, final Align align, final boolean bold, final boolean erase) {
		final Paint textPaint = initTextPaint();
		if (Settings.isColoredNumber()) {
			textPaint.setColor(getColorForLevel(level));
		} else {
			textPaint.setColor(Settings.getBattColor());
		}
		textPaint.setTextSize(fontSize);
		if (bold) {
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			textPaint.setTypeface(Typeface.DEFAULT);
		}
		textPaint.setTextAlign(align);
		if (erase) {
			final PorterDuffXfermode xfermode = new PorterDuffXfermode(Mode.CLEAR);
			textPaint.setXfermode(xfermode);
		}
		return textPaint;
	}

	private static float adjustFontSize(final int level, final float fontSize) {
		float fSize = fontSize;
		if (level == 100) {
			fSize = Math.round(fontSize * Settings.getFontSize100() / 100f);
		}
		// generelle fontadjust einbeziehen
		fSize = Math.round(fSize * Settings.getFontSize() / 100f);
		return fSize;
	}

	public static Paint getBatteryPaint(final int level) {
		final Paint battPaint = initBattPaint();
		battPaint.setColor(getColorForLevel(level));
		return battPaint;
	}

	public static Paint getZeigerPaint() {
		final Paint zeigerPaint1 = new Paint();
		zeigerPaint1.setAntiAlias(true);
		zeigerPaint1.setAlpha(255);
		zeigerPaint1.setStyle(Style.FILL);
		final Paint zeigerPaint = zeigerPaint1;
		zeigerPaint.setColor(Settings.getZeigerColor());
		return zeigerPaint;
	}

	public static Paint getBackgroundPaint() {
		final Paint backgroundPaint = new Paint();
		backgroundPaint.setAntiAlias(true);
		backgroundPaint.setStyle(Style.FILL);
		final Paint backgrdPaint = backgroundPaint;
		backgrdPaint.setColor(Settings.getBackgroundColor());
		backgrdPaint.setAlpha(Settings.getBackgroundOpacity());
		return backgrdPaint;
	}

	public static Paint getTextBattStatusPaint(final float fontSizeArc, final Align align, final boolean bold) {
		final Paint battStatusPaint = initBattStatusPaint();
		battStatusPaint.setColor(Settings.getBattStatusColor());
		battStatusPaint.setTextSize(fontSizeArc);
		if (bold) {
			battStatusPaint.setTypeface(Typeface.DEFAULT_BOLD);
		} else {
			battStatusPaint.setTypeface(Typeface.DEFAULT);
		}
		battStatusPaint.setTextAlign(align);
		return battStatusPaint;
	}

	public static int getGray(final int level) {
		return getGray(level, 255);
	}

	public static int getGray(final int level, final int alpha) {
		return Color.argb(alpha, level, level, level);
	}

}
