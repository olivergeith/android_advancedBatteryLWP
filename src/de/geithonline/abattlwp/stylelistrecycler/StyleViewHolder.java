package de.geithonline.abattlwp.stylelistrecycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.geithonline.abattlwp.R;

public class StyleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

	public TextView textview;
	public ImageView imageview;

	public StyleViewHolder(final View itemView) {
		super(itemView);
		itemView.setOnClickListener(this);
		textview = (TextView) itemView.findViewById(R.id.style_textview);
		imageview = (ImageView) itemView.findViewById(R.id.style_imageview);
	}

	@Override
	public void onClick(final View view) {
		Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
	}
}
