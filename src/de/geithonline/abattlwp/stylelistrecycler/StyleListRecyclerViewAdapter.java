package de.geithonline.abattlwp.stylelistrecycler;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.geithonline.abattlwp.R;
import de.geithonline.abattlwp.settings.DrawerManager;

public class StyleListRecyclerViewAdapter extends RecyclerView.Adapter<StyleViewHolder> {

	private final List<String> styleNames;
	private final Context context;

	public StyleListRecyclerViewAdapter(final Context context, final List<String> itemList) {
		styleNames = itemList;
		this.context = context;
	}

	@Override
	public StyleViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

		final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.style_cardview, null);
		final StyleViewHolder rcv = new StyleViewHolder(layoutView);
		return rcv;
	}

	@Override
	public void onBindViewHolder(final StyleViewHolder holder, final int position) {
		holder.textview.setText(styleNames.get(position));
		holder.imageview.setImageBitmap(DrawerManager.getIconForDrawerForceDrawNew(styleNames.get(position), 500, 66));
	}

	@Override
	public int getItemCount() {
		return styleNames.size();
	}
}
