package de.geithonline.abattlwp.stylelistrecycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import de.geithonline.abattlwp.R;
import de.geithonline.abattlwp.settings.DrawerManager;

public class StyleListRecyclerActivity extends AppCompatActivity {

	private StaggeredGridLayoutManager gaggeredGridLayoutManager;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.style_list_recycler_view);

		final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
		recyclerView.setHasFixedSize(true);
		recyclerView.setItemAnimator(new DefaultItemAnimator());

		gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(gaggeredGridLayoutManager);

		final StyleListRecyclerViewAdapter rcAdapter = new StyleListRecyclerViewAdapter(StyleListRecyclerActivity.this, DrawerManager.getStyleNames());
		recyclerView.setAdapter(rcAdapter);
	}

}
