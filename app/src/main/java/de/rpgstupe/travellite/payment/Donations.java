package de.rpgstupe.travellite.payment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.text.TextUtils;

import de.rpgstupe.travellite.R;
import de.rpgstupe.travellite.payment.util.IabBroadcastReceiver;
import de.rpgstupe.travellite.payment.util.IabHelper;
import de.rpgstupe.travellite.payment.util.IabResult;
import de.rpgstupe.travellite.payment.util.Purchase;

/**
 * Created by Fabian on 05.08.2017.
 */

public class Donations implements IabBroadcastReceiver.IabBroadcastListener, DialogInterface.OnClickListener {
    private static final int RC_REQUEST = 10001;
    private static final String SKU_1 = "donation_1";
    private static final String SKU_2 = "donation_2";
    private static final String SKU_3 = "donation_3";
    public IabBroadcastReceiver mBroadcastReceiver;
    public IabHelper mHelper;
    String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtipBPugWTCOuIwapgtvP1UAu0wmKuXQSegZyg+1lTloNpV9NqToQyoWh0AI+w5SWt59yPzOxHc5yErbZhz2Wtk/pPcwGR1hJjTNDmMpBbM9uPe5dr3K+IqHYiEpMggiSKmi4KxPhrKOB+eQjOVNjRa3sr2q00rT9oMUE4TFJvShTQegcg/1WNwYQh/pQoWUDIPZWP6L8c3UW7MR6j/m8WCXOY92TkaKTccJ4TI48l6dptyGhbkwwdiR+1t9k5rlo2w9GGa1bgKs+0E2gex7yyrzqQD33/JxZBKPYhBPnJRSCxGGceyGDFO00oHllSEhm/cJZ2cX487+ssxBODiWr0QIDAQAB";
    Context context;
    private String mSelectedDonation;
    public IntentFilter broadcastFilter;

    public Donations(final Context context) {
        this.context = context;
        mHelper = new IabHelper(context, base64EncodedPublicKey);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
                mBroadcastReceiver = new IabBroadcastReceiver(Donations.this);
                broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                context.registerReceiver(mBroadcastReceiver,broadcastFilter);
            }
        });
    }


    public void onDonationButtonClicked() {

        CharSequence[] options;
        options = new CharSequence[3];
        options[0] = context.getString(R.string.donate_small);
        options[1] = context.getString(R.string.donate_medium);
        options[2] = context.getString(R.string.donate_large);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.donate))
                .setSingleChoiceItems(options, 0 /* checkedItem */, this)
                .setPositiveButton(android.R.string.ok, this)
                .setNegativeButton(android.R.string.cancel, this);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int id) {
        if (id == 0 /* First choice item */) {
            mSelectedDonation = SKU_1;
        } else if (id == 1 /* Second choice item */) {
            mSelectedDonation = SKU_2;
        } else if (id == 2 /* Third choice item */) {
            mSelectedDonation = SKU_3;
        } else if (id == DialogInterface.BUTTON_POSITIVE /* continue button */) {
            if (TextUtils.isEmpty(mSelectedDonation)) {
                // The user has not changed from the default selection
                mSelectedDonation = SKU_1;
            }
            try {
                mHelper.launchPurchaseFlow((Activity) context, mSelectedDonation, RC_REQUEST, mPurchaseFinishedListener);
            } catch (IabHelper.IabAsyncInProgressException e) {
                e.printStackTrace();
            }
            // Reset the dialog options
            mSelectedDonation = "";
        } else if (id != DialogInterface.BUTTON_NEGATIVE) {

        }
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(context);
        bld.setMessage(message);
        bld.setNeutralButton(android.R.string.ok, null);
        bld.create().show();
    }

    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                return;
            }
                // bought the premium upgrade!
                alert(context.getString(R.string.donate_thanks));
        }
    };

    @Override
    public void receivedBroadcast() {

    }
}
