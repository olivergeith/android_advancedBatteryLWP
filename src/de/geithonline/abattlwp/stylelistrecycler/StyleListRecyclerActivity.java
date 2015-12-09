package de.geithonline.abattlwp.stylelistrecycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import de.geithonline.abattlwp.R;
import de.geithonline.abattlwp.settings.DrawerManager;
import de.geithonline.abattlwp.settings.Settings;

public class StyleListRecyclerActivity extends AppCompatActivity implements RecyclerViewClickListener {

	private StaggeredGridLayoutManager gaggeredGridLayoutManager;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.style_list_recycler_view);
		final Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
		myToolbar.setBackgroundResource(R.color.primary);
		setSupportActionBar(myToolbar);

		// Get a support ActionBar corresponding to this toolbar
		// Enable the Up button
		// final ActionBar ab = getSupportActionBar();
		// ab.setDisplayHomeAsUpEnabled(true);

		final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
		recyclerView.setHasFixedSize(true);
		recyclerView.setItemAnimator(new DefaultItemAnimator());

		gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(gaggeredGridLayoutManager);

		final StyleListRecyclerViewAdapter rcAdapter = new StyleListRecyclerViewAdapter(StyleListRecyclerActivity.this, DrawerManager.getStyleNames(), this);
		recyclerView.setAdapter(rcAdapter);

		// Scrolling to selected Style
		gaggeredGridLayoutManager.scrollToPosition(DrawerManager.getPostionOfSelectedStyleInList());
	}

	@Override
	public void recyclerViewListClicked(final View v, final int position, final String style) {
		Log.i("Activity", "Clicked AdapterPosition = " + position + " Style=" + style);
		Settings.saveAnyString(Settings.KEY_BATT_STYLE, style);
		finish();
	}

}
