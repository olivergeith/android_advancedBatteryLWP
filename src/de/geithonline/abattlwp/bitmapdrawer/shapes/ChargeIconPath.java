package de.geithonline.abattlwp.bitmapdrawer.shapes;

import android.graphics.Path;
import android.graphics.RectF;

public class ChargeIconPath extends Path {

	public ChargeIconPath(final RectF bounds) {
		super();
		moveTo(bounds.right, bounds.top);
		lineTo(bounds.left, bounds.top + bounds.height() * 3 / 5);
		lineTo(bounds.left + bounds.width() * 0.5f, bounds.top + bounds.height() * 3 / 5);
		lineTo(bounds.left, bounds.bottom);
		lineTo(bounds.right, bounds.top + bounds.height() * 2 / 5);
		lineTo(bounds.left + bounds.width() * 0.5f, bounds.top + bounds.height() * 2 / 5);
		lineTo(bounds.right, bounds.top);
	}

}
