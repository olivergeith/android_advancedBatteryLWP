// package de.geithonline.abattlwp.bitmapdrawer.parts.deprecated;
//
// import android.graphics.Canvas;
// import android.graphics.Paint;
// import android.graphics.Paint.Align;
// import android.graphics.Path;
// import android.graphics.PointF;
// import android.graphics.RectF;
// import de.geithonline.abattlwp.bitmapdrawer.data.DropShadow;
// import de.geithonline.abattlwp.settings.PaintProvider;
// import de.geithonline.abattlwp.utils.GeometrieHelper;
//
// public class SkalaTextPart {
//
// private final PointF c;
// private final float radius;
// private final float maxWinkel;
// private final float startWinkel;
// private final float fontSize10er;
// private float fontsize5er;
// private boolean draw100 = false;
// private boolean draw0 = true;
// private final Paint paint;
//
// @Deprecated
// public SkalaTextPart(final PointF center, final float radius, final float fontSize10er, final float startWinkel, final float maxWinkel) {
// c = center;
// this.radius = radius;
// this.maxWinkel = maxWinkel;
// this.startWinkel = startWinkel;
// this.fontSize10er = fontSize10er;
// paint = PaintProvider.getTextScalePaint(fontSize10er, Align.CENTER, true);
// paint.setTextAlign(Align.CENTER);
// }
//
// public SkalaTextPart setDropShadow(final DropShadow dropShadow) {
// if (dropShadow != null) {
// dropShadow.setUpPaint(paint);
// }
// return this;
// }
//
// public SkalaTextPart overrideColor(final int color) {
// paint.setColor(color);
// return this;
// }
//
// public SkalaTextPart setFontsize5er(final float fontsize5er) {
// this.fontsize5er = fontsize5er;
// return this;
// }
//
// public SkalaTextPart setDraw100(final boolean draw100) {
// this.draw100 = draw100;
// return this;
// }
//
// public SkalaTextPart setDraw0(final boolean draw0) {
// this.draw0 = draw0;
// return this;
// }
//
// public void draw(final Canvas canvas) {
// float fontSize = 0f;
// int forloopEnd = 100;
// if (draw100) {
// forloopEnd = 101;
// }
// int forloopStart = 1;
// if (draw0) {
// forloopStart = 0;
// }
// for (int i = forloopStart; i < forloopEnd; i = i + 1) {
//
// if (i % 10 == 0) {// 10 er
// fontSize = fontSize10er;
// } else if (i % 5 == 0) { // 5er
// fontSize = fontsize5er;
// } else { // einer
// fontSize = 0f;
// }
// if (fontSize > 0f) {
// final float winkelProProzent = maxWinkel / 100;
// final float winkel = startWinkel + i * winkelProProzent;
// final Path mArc = new Path();
// final RectF oval = GeometrieHelper.getCircle(c, radius);
// mArc.addArc(oval, winkel - 18, 36);
// paint.setTextSize(fontSize);
// canvas.drawTextOnPath("" + i, mArc, 0, 0, paint);
// }
// }
// }
//
// }
