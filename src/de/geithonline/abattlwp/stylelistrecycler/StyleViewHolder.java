package de.geithonline.abattlwp.stylelistrecycler;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import de.geithonline.abattlwp.R;

public class StyleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
	private static String LOG = "StyleViewHolder";
	public TextView textview;
	public ImageView imageview;
	private final RecyclerViewClickListener itemListener;

	public StyleViewHolder(final View layoutView, final RecyclerViewClickListener itemListener) {
		super(layoutView);
		this.itemListener = itemListener;
		layoutView.setOnClickListener(this);
		textview = (TextView) layoutView.findViewById(R.id.style_textview);
		imageview = (ImageView) layoutView.findViewById(R.id.style_imageview);
	}

	@Override
	public void onClick(final View view) {
		Log.i(LOG, "Clicked AdapterPosition = " + getAdapterPosition() + " Style=" + textview.getText());
		itemListener.recyclerViewListClicked(view, getAdapterPosition(), textview.getText().toString());
	}

}
