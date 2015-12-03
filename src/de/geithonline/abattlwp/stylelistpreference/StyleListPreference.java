package de.geithonline.abattlwp.stylelistpreference;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
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
	private final SharedPreferences prefs;
	private final SharedPreferences.Editor prefsEditor;
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
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		prefsEditor = prefs.edit();
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

	// @Override
	// public CharSequence[] getEntryValues() {
	// return DrawerManager.getDrawerNames();
	// }

	@Override
	public String getValue() {
		if (selectedEntry != -1) {
			return entries[selectedEntry].toString();
		}
		return super.getValue();
	}

	@Override
	protected void onPrepareDialogBuilder(final Builder builder) {
		super.onPrepareDialogBuilder(builder);

		entries = getEntries();

		// searchinentriested index
		final String selectedValue = prefs.getString(mKey, "");
		for (int i = 0; i < entries.length; i++) {
			if (selectedValue.compareTo((String) entries[i]) == 0) {
				selectedEntry = i;
				break;
			}
		}

		iconListPreferenceAdapter = new IconListPreferenceScreenAdapter(mContext);
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
				text.setChecked(selectedEntry == position);
				// in
				final Bitmap b = DrawerManager.getIconForDrawer(text.getText().toString(), Settings.getIconSize());
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
				row = mInflater.inflate(R.layout.style_list_preference_row, parent, false);
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
					prefsEditor.putString(mKey, entries[p].toString());
					selectedEntry = p;
					prefsEditor.commit();
				}
			});
			return row;
		}
	}
}
