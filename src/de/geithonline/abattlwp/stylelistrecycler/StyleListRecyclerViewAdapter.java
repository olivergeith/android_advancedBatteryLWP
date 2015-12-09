package de.geithonline.abattlwp.stylelistrecycler;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.geithonline.abattlwp.R;
import de.geithonline.abattlwp.settings.DrawerManager;
import de.geithonline.abattlwp.settings.Settings;

public class StyleListRecyclerViewAdapter extends RecyclerView.Adapter<StyleViewHolder> {

	private final List<String> styleNames;
	private int position;
	private final Context context;
	private final RecyclerViewClickListener itemListener;

	public StyleListRecyclerViewAdapter(final Context context, final List<String> itemList, final RecyclerViewClickListener itemListener) {
		this.itemListener = itemListener;
		DrawerManager.clearIconCache();
		// man könnte sich noch üerlegen, ob man die Liste umsortiert und den gerade selektierten eintrag ganz an den Anfang setzt?!
		styleNames = itemList;
		this.context = context;
	}

	@Override
	public StyleViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
		final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_cardview, null);
		final StyleViewHolder rcv = new StyleViewHolder(layoutView, itemListener);
		return rcv;
	}

	@Override
	public void onBindViewHolder(final StyleViewHolder holder, final int position) {
		final String currentStyle = styleNames.get(position);
		holder.textview.setText(currentStyle);
		holder.imageview.setImageBitmap(DrawerManager.getIconForDrawer(currentStyle, Settings.getIconSize(), 66));
		// highlight some stuff
		highlightItem(holder, currentStyle, R.color.primary, R.color.accent, true);
	}

	private void highlightItem(//
			final StyleViewHolder holder, //
			final String currentStyle, //
			final int normalColor, //
			final int highlightColor, //
			final boolean fullSpanForSelectedItem) {
		// final StaggeredGridLayoutManager.LayoutParams layoutParams = ((StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams());
		// is this the selected style
		if (Settings.getStyle().equals(currentStyle)) {
			holder.textview.setBackgroundResource(highlightColor);
			// if (fullSpanForSelectedItem) {
			// layoutParams.setFullSpan(true);
			// }
		} else {
			holder.textview.setBackgroundResource(normalColor);
			// if (fullSpanForSelectedItem) {
			// layoutParams.setFullSpan(false);
			// }
		}
	}

	@Override
	public int getItemCount() {
		return styleNames.size();
	}

	public int getPosition() {
		return position;
	}

}
