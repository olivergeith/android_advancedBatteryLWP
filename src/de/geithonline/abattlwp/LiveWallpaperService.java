package de.geithonline.abattlwp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.BatteryManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import de.geithonline.abattlwp.bitmapdrawer.IBitmapDrawer;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.R;

public class LiveWallpaperService extends WallpaperService {

	public static SharedPreferences prefs;

	@Override
	public void onCreate() {
		super.onCreate();
		prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		Settings.initPrefs(prefs, getApplicationContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public Engine onCreateEngine() {
		return new MyWallpaperEngine();
	}

	public static String filePath = "aaa";

	// #####################################################################################
	// Wallpaper Engine
	// #####################################################################################
	class MyWallpaperEngine extends Engine implements OnSharedPreferenceChangeListener {
		private boolean initialized = false;
		private int level = 0;
		private int i;
		private final Handler handler = new Handler();
		private boolean visible = true;
		private Bitmap backgroundImage = null;
		private IBitmapDrawer bitmapDrawer = Settings.getBatteryStyle();
		private int cWidth = 0;
		private int cHeight = 0;
		private float dx = 0.0f;
		private int oldWidth = 0;
		private int oldHeight = 0;
		private int millies = 50;
		private boolean forcedraw = false;

		MyWallpaperEngine() {
			registerReceiver(mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			i = 0;
			drawMe();
		}

		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}
		};

		public synchronized void drawMe() {
			handler.removeCallbacks(drawRunner);
			if (visible && initialized) {
				handler.post(drawRunner);
			}
		}

		private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(final Context arg0, final Intent intent) {
				level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
				// final int scale =
				// intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
				final int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
				// Are we charging charged?
				Settings.isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
				// How are we charging?
				final int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
				Settings.isChargeUSB = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
				Settings.isChargeWireless = chargePlug == BatteryManager.BATTERY_PLUGGED_WIRELESS;
				Settings.isChargeAC = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
				Settings.battTemperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
				Settings.battHealth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
				Settings.battVoltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
				drawMe();
			}
		};

		synchronized void draw() {
			final SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				// get the size of canvas
				if (canvas != null) {
					cWidth = canvas.getWidth();
					cHeight = canvas.getHeight();
					// clear the canvas
					if (Settings.isLoadCustomBackground()) {
						// canvas.drawColor(Settings.getPlainWallpaterBackgroundColor());
						drawBackgroundImage(canvas);
					} else {
						canvas.drawPaint(Settings.getWallpaperBackgroundPaint(cWidth, cHeight));
					}
					// schaun, ob der Bitmapdrawer sich geändert hat
					bitmapDrawer = Settings.getBatteryStyle();
					if (!Settings.isCharging || Settings.isAnimationEnabled() == false) {
						bitmapDrawer.draw(level, canvas, forcedraw);
					} else {
						bitmapDrawer.draw(i, canvas, forcedraw);
						// Ret animationlevel
						// Level länger anzeigen
						if (i == level) {
							millies = Settings.getAnimationDelaýOnCurrentLevel();
						} else {
							millies = Settings.getAnimationDelaý();
						}

						i += 1;
						if (i > getAnimationResetLevel()) {
							i = 0;
						}
						handler.removeCallbacks(drawRunner);
						handler.postDelayed(drawRunner, millies);
					}
				}
			} catch (final IllegalArgumentException ex) {
				// do nothing
			} finally {
				if (canvas != null) {
					holder.unlockCanvasAndPost(canvas);
				}
			}
			forcedraw = false;
			// handler.removeCallbacks(drawRunner);
			// if (visible && Settings.isCharging) {
			// handler.postDelayed(drawRunner, millies); // delay mileseconds
			// }
		}

		private int getAnimationResetLevel() {
			switch (Settings.getAnimationStyle()) {
			default:
			case Settings.ANIMATION_STYLE_0_TO_100:
				return 100;
			case Settings.ANIMATION_STYLE_0_TO_LEVEL:
				return level;
			}
		}

		/**
		 * drawBackground
		 * 
		 * @param canvas
		 * @param cWidth
		 * @param cHeight
		 */
		private void drawBackgroundImage(final Canvas canvas) {
			// do we need to create a new backgroundimage?
			if (backgroundImage == null //
					|| forcedraw //
					|| customBackgroundChanged() || cWidth != oldWidth //
					|| cHeight != oldHeight) {
				if (backgroundImage != null) {
					backgroundImage.recycle();
				}
				backgroundImage = getBackgroundImage2();
				oldWidth = cWidth;
				oldHeight = cHeight;
			}
			// draw the background image
			canvas.save();
			canvas.translate(dx, 0);
			final int h = backgroundImage.getHeight();
			if (h > cHeight) {
				canvas.drawBitmap(backgroundImage, 0, cHeight / 2 - h / 2, null);
			} else {
				canvas.drawBitmap(backgroundImage, 0, 0, null);
			}
			canvas.restore();
		}

		private boolean customBackgroundChanged() {
			final String currentPathFromSettings = Settings.getCustomBackgroundFilePath();
			if (!filePath.equals(currentPathFromSettings)) {
				filePath = currentPathFromSettings;
				return true;
			}
			return false;
		}

		/**
		 * initBackgroundImage
		 */
		private Bitmap getBackgroundImage2() {
			Bitmap bgInput;
			Bitmap bgReturn;
			// sollen wir ein custom BG laden ?
			bgInput = Settings.getCustomBackgroundSampled(Math.round(cWidth * 1.4f), cHeight);
			// if it is null...
			if (bgInput == null) {
				bgInput = BitmapFactory.decodeResource(getResources(), R.drawable.background);
			}
			// now we should have a BG
			// lets scale it
			final int w = bgInput.getWidth();
			final int h = bgInput.getHeight();
			final float aspectBG = (float) w / h;

			// Log.i("GEITH", "h=" + h);
			// Log.i("GEITH", "w=" + w);
			// Log.i("GEITH", "as=" + aspectBG);

			// erstmal setzen wir die Zielhöhe auf Canvas heigth
			int dstH = cHeight;
			int dstW = cWidth;
			// ..und berechnen daraufhin die Breite des Bitmaps (damit
			if (Settings.isKeepAspectRatio()) {
				// Aspectratio erhalten bleibt)
				dstW = Math.round(dstH * aspectBG);
				// Log.i("GEITH", "dst_h=" + dstH);
				// Log.i("GEITH", "dst_w=" + dstW);

				// dann schauen wir, ob die dstW breiter ist als das Canvas
				if (dstW < Math.round(cWidth * 1.4f)) {
					// oh schade das Bild ist zu schmal und passt nicht in der
					// Breite
					// dannn machen wir es nun breiter (1.4fach
					// canvasbreite)...dafür wird es leider zu hoch
					// also wird später unten was abgeschnitten
					// 1.4 fach damit es auch was zum sliden gibt ;-)
					dstW = Math.round(cWidth * 1.4f);
					dstH = Math.round(dstW / aspectBG);
					// Log.i("GEITH", "ups dst_h=" + dstH);
					// Log.i("GEITH", "ups dst_w=" + dstW);
				}
			}

			bgReturn = Bitmap.createScaledBitmap(bgInput, dstW, dstH, true);
			if (!bgReturn.equals(bgInput)) {
				bgInput.recycle();
			}
			return bgReturn;
		}

		@Override
		public void onCreate(final SurfaceHolder surfaceHolder) {
			super.onCreate(surfaceHolder);
			Log.i("GEITH", "Register listener 2" + prefs);
			prefs.registerOnSharedPreferenceChangeListener(this);
			drawMe();
		}

		@Override
		public void onSharedPreferenceChanged(final SharedPreferences sharedPreferences, final String key) {
			Log.i("GEITH", "Settings changed: " + key + "  --> redraw!");
			forcedraw = true;
			drawMe();
		}

		@Override
		public void onVisibilityChanged(final boolean visible) {
			this.visible = visible;
			// if screen wallpaper is visible then draw the image otherwise do
			// not draw
			if (visible) {
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}

		@Override
		public void onSurfaceDestroyed(final SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			visible = false;
			handler.removeCallbacks(drawRunner);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
			unregisterReceiver(mBatInfoReceiver);
		}

		@Override
		public void onSurfaceCreated(final SurfaceHolder holder) {
			super.onSurfaceCreated(holder);
			initialized = true;
			drawMe();
		}

		@Override
		public void onSurfaceRedrawNeeded(final SurfaceHolder holder) {
			super.onSurfaceRedrawNeeded(holder);
			drawMe();
		}

		@Override
		public void onTouchEvent(final MotionEvent event) {
			super.onTouchEvent(event);
		}

		@Override
		public void onOffsetsChanged(final float xOffset, final float yOffset, final float xStep, final float yStep, final int xPixels, final int yPixels) {
			if (backgroundImage != null) {
				dx = (cWidth - backgroundImage.getWidth()) * (xOffset);
			}
			drawMe();
		}

		@Override
		public void onSurfaceChanged(final SurfaceHolder holder, final int format, final int width, final int height) {
			super.onSurfaceChanged(holder, format, width, height);
			drawMe();
		}

	}
}