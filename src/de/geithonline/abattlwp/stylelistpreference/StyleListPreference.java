package de.geithonline.abattlwp.stylelistpreference;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import de.geithonline.abattlwp.R;
import de.geithonline.abattlwp.settings.DrawerManager;
import de.geithonline.abattlwp.settings.Settings;

/**
 * 
 * @author oliver
 * 
 */
public class StyleListPreference extends ListPreference {

	private IconListPreferenceScreenAdapter iconListPreferenceAdapter = null;
	private final Context mContext;
	private final LayoutInflater mInflater;
	private CharSequence[] entries;
	private final String mKey;
	private int selectedEntry = -1;

	public StyleListPreference(final Context context, final AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public StyleListPreference(final Context context, final AttributeSet attrs, final int defStyle) {
		super(context, attrs);
		mContext = context;

		final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StyleListPreference, defStyle, 0);

		mInflater = LayoutInflater.from(context);
		mKey = getKey();
		a.recycle();
	}

	@Override
	public CharSequence getEntry() {
		if (selectedEntry != -1) {
			return entries[selectedEntry];
		}
		if (super.getEntry() == null) {
			return "";
		} else {
			return super.getEntry().toString();
		}
	}

	@Override
	public CharSequence[] getEntries() {
		return DrawerManager.getDrawerNames();
	}

	@Override
	public CharSequence[] getEntryValues() {
		return DrawerManager.getDrawerNames();
	}

	@Override
	public String getValue() {
		if (selectedEntry != -1) {
			return entries[selectedEntry].toString();
		}
		return super.getValue();
	}

	@Override
	protected void onPrepareDialogBuilder(final Builder builder) {
		entries = getEntries();
		// hier befriedigen wir die Super klasse
		setEntries(entries);
		setEntryValues(entries);
		super.onPrepareDialogBuilder(builder);

		// searchinentriested index
		final String selectedValue = Settings.getAnyString(mKey, "");
		for (int i = 0; i < entries.length; i++) {
			if (selectedValue.compareTo((String) entries[i]) == 0) {
				selectedEntry = i;
				break;
			}
		}

		iconListPreferenceAdapter = new IconListPreferenceScreenAdapter(mContext);
		builder.setIcon(R.drawable.icon);
		// final ImageView title = new ImageView(builder.getContext());
		// title.setBackgroundResource(R.drawable.choosebatterystyle);
		// title.setScaleType(ScaleType.CENTER_CROP);
		// builder.setCustomTitle(title);
		builder.setSingleChoiceItems(iconListPreferenceAdapter, selectedEntry, null);
	}

	private class IconListPreferenceScreenAdapter extends BaseAdapter {
		public IconListPreferenceScreenAdapter(final Context context) {
		}

		@Override
		public int getCount() {
			return entries.length;
		}

		class CustomHolder {
			private CheckedTextView text = null;

			// @SuppressLint("NewApi")
			CustomHolder(final View row, final int position) {

				final ImageView imageView = (ImageView) row.findViewById(R.id.image);

				text = (CheckedTextView) row.findViewById(R.id.image_list_view_row_text_view);
				text.setText(entries[position]);
				if (selectedEntry == position) {
					text.setBackgroundResource(R.color.accent);
					text.setChecked(selectedEntry == position);
				} else {
					text.setBackgroundResource(R.color.primary);
				}
				// in
				final Bitmap b = DrawerManager.getIconForDrawer(text.getText().toString(), Settings.getIconSize(), 66);
				if (b != null) {
					imageView.setImageBitmap(b);
					text.setText(" " + text.getText());
				}

			}
		}

		@Override
		public Object getItem(final int position) {
			return null;
		}

		@Override
		public long getItemId(final int position) {
			return position;
		}

		@Override
		public View getView(final int position, final View convertView, final ViewGroup parent) {
			View row = convertView;
			CustomHolder holder = null;
			final int p = position;
			if (row == null) {
				row = mInflater.inflate(R.layout.style_list_preference_row_material, parent, false);
			}
			holder = new CustomHolder(row, position);

			row.setTag(holder);
			row.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(final View v) {
					v.requestFocus();

					final Dialog mDialog = getDialog();
					mDialog.dismiss();

					callChangeListener(entries[p]);
					Settings.saveAnyString(mKey, entries[p].toString());
					selectedEntry = p;
				}
			});
			return row;
		}
	}
}
