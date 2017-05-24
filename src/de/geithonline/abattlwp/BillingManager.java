package de.geithonline.abattlwp;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.geithonline.abattlwp.billinghelper.IabHelper;
import de.geithonline.abattlwp.billinghelper.IabResult;
import de.geithonline.abattlwp.billinghelper.Inventory;
import de.geithonline.abattlwp.billinghelper.Purchase;
import de.geithonline.abattlwp.settings.Settings;
import de.geithonline.abattlwp.utils.StorageHelper;
import de.geithonline.abattlwp.utils.Toaster;

/**
 * @author geith
 * 
 */
public class BillingManager {
	/*
	 * base64EncodedPublicKey should be YOUR APPLICATION'S PUBLIC KEY (that you got from the Google Play developer console). This is not your developer public
	 * key, it's the *app-specific* public key.
	 * 
	 * Instead of just storing the entire literal string here embedded in the program, construct the key at runtime from pieces or use bit manipulation (for
	 * example, XOR with some other string) to hide the actual key. The key itself is not secret information, but we don't want to make it easy for an attacker
	 * to replace the public key with one of their own and then fake messages from the server.
	 */
	private final String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAo57E5qlVRLGXXnPR3ZQu1Fy2FWl+d/WTLiFXmhAtqmsP0DaHk4ViP8RrNySNI9xJ6tZY+udnIdiaUrV00fKsuDfhuO81TDwTUmqoDYMk9bEOCwJF8/FLODKB3i2qurNKZiBk7K5QriJPvXDwcoYfa8CUhkJ80LG3i1JBiy8NozPsFOcaJtCQdTTRAt1P3xT1PA2Vhxw8V44HhVNiqQsO/FZv6+IGiZwUTVMtgFbDOMhJfDQ92usHbgRFQ6QclSAUCNIs8yYy59brdyg5Fn5u57ohs2GXP9kxxdIB1dx/K1CBiwY+lMO4Bm/Zgov2d2iJB1G37AAArseE2gheQ8m7EQIDAQAB";
	// The helper object
	private IabHelper mHelper;
	private final String TAG = "Billing";
	private boolean mIsPremium = false;
	static final String SKU_PREMIUM = "premium";
	static final int RC_REQUEST = 10001;
	private final Activity activity;
	private final Button button;
	private static boolean setupBillingHasError = false;

	public BillingManager(final Activity activity) {
		this.activity = activity;
		// Button erzeugen und unsichtbar machen
		button = new Button(activity);
		button.setText("Upgrade to Premium-Version");
		button.setVisibility(View.GONE);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View v) {
				onUpgradeAppButtonClicked(v);
			}
		});

		setupBilling();
	}

	public boolean isPremium() {
		return mIsPremium;
	}

	public Button getButton() {
		return button;
	}

	protected void onDestroy() {
		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

	private void setupBilling() {
		// schauen wir erstmal local, ob wir premium sind....
		mIsPremium = readProStatus();
		setupBillingHasError = readBillingErrrorStatus();
		if (mIsPremium) {
			Log.i(TAG, "Is Premium Connection zum Billing wird gar nicht erst aufgebaut!");
			if (Settings.isDebuggingMessages()) {
				Toaster.showInfoToast(activity, "This is the Premium Version");
			}
			Settings.saveProStatusToPrefs(true);
			return;
		} else {
			if (Settings.isDebuggingMessages()) {
				Toaster.showInfoToast(activity, "This is the Free Version");
			}
		}
		// haben wir schon mal versucht das Billing setup aufzurufen (efolglos)
		if (setupBillingHasError == true) {
			Log.e(TAG, "Es gab schon mal eine Problem mit dem Setup des IN-App Billings --> Connection zum Billing wird gar nicht erst aufgebaut!");
			return;
		}
		mHelper = new IabHelper(activity.getApplicationContext(), base64EncodedPublicKey);
		// enable debug logging (for a production application, you should set
		// this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			@Override
			public void onIabSetupFinished(final IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					complain("Problem setting up in-app billing: " + result);
					setupBillingHasError = true;
					saveBillingError(setupBillingHasError);
					handleButton();
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null) {
					return;
				}

				// IAB is fully set up. Now, let's get an inventory of stuff we
				// own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	// Listener that's called when we finish querying the items and
	// subscriptions we own
	private final IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		@Override
		public void onQueryInventoryFinished(final IabResult result, final Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null) {
				return;
			}

			// Is it a failure?
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check the developer payload to see if it's correct! See verifyDeveloperPayload().
			 */

			// Do we have the premium upgrade?
			final Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
			mIsPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
			saveProStatus(mIsPremium);
			handleButton();
			Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	// User clicked the "Upgrade to Premium" button.
	private void onUpgradeAppButtonClicked(final View arg0) {
		Log.d(TAG, "Upgrade button clicked; launching purchase flow for upgrade.");
		final String payload = "Premiumz";
		mHelper.launchPurchaseFlow(activity, SKU_PREMIUM, RC_REQUEST, mPurchaseFinishedListener, payload);
	}

	// Callback for when a purchase is finished
	private final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		@Override
		public void onIabPurchaseFinished(final IabResult result, final Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null) {
				return;
			}

			if (result.isFailure()) {
				complain("Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				complain("Error purchasing. Authenticity verification failed.");
				return;
			}

			Log.d(TAG, "Purchase successful.");

			if (purchase.getSku().equals(SKU_PREMIUM)) {
				// bought the premium upgrade!
				Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
				alert("Thank you for upgrading to premium!");
				mIsPremium = true;
				handleButton();
				saveProStatus(mIsPremium);
			}
		}
	};

	/** Verifies the developer payload of a purchase. */
	private boolean verifyDeveloperPayload(final Purchase p) {
		final String payload = p.getDeveloperPayload();
		if (payload.equals("Premiumz")) {
			return true;
		}
		return false;
	}

	private void complain(final String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
	}

	private void alert(final String message) {
		final AlertDialog.Builder bld = new AlertDialog.Builder(activity);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	// ##########################################################################
	// Saving and reading the Premiumstatus on device
	// ##########################################################################
	private void handleButton() {
		if (mIsPremium || setupBillingHasError) {
			button.setVisibility(View.GONE);
		} else {
			button.setVisibility(View.VISIBLE);
		}
	}

	private void saveProStatus(final boolean isPre) {
		final File file = new File(activity.getFilesDir(), "muimerp.txt");
		if (isPre) {
			try {
				file.createNewFile();
				// Toaster.showInfoToast(activity, file.getAbsolutePath());
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else {
			file.delete();
		}
		// Wir speichern das auch noch mal in den preferences,,,dann kann man
		// später schöner drauf zugreifen
		Settings.saveProStatusToPrefs(isPre);
	}

	private boolean readProStatus() {
		final File file = new File(activity.getFilesDir(), "muimerp.txt");
		return file.exists() || StorageHelper.muimerpExists();
	}

	private void saveBillingError(final boolean isError) {
		final File file = new File(activity.getFilesDir(), "billingerror.txt");
		if (isError) {
			try {
				file.createNewFile();
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else {
			file.delete();
		}
	}

	private boolean readBillingErrrorStatus() {
		final File file = new File(activity.getFilesDir(), "billingerror.txt");
		return file.exists();
	}

}
