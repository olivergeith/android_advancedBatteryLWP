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
		final String style = styleNames.get(position);
		holder.textview.setText(style);
		holder.imageview.setImageBitmap(DrawerManager.getIconForDrawer(style, Settings.getIconSize(), 66));

		if (Settings.getStyle().equals(style)) {
			// wir haben das gerade selektierte element
			holder.textview.setBackgroundResource(R.color.accent);
		} else {
			holder.textview.setBackgroundResource(R.color.primary);
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
