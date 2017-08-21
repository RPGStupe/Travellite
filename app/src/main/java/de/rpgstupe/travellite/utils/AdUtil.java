package de.rpgstupe.travellite.utils;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Fabian on 18.08.2017.
 */

public class AdUtil {
    public static void initializeAds(AdView mAdView) {
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
}
