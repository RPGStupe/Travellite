package de.rpgstupe.travellite.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.rpgstupe.travellite.Donations;
import de.rpgstupe.travellite.ListItemCountry;
import de.rpgstupe.travellite.R;
import de.rpgstupe.travellite.WorldMap;

/**
 * Created by Fabian on 25.06.2017.
 */

public class CountriesListActivity extends AppCompatActivity {


    private static String LOG_TAG = "CountriesListActivity";

    List<ListItemCountry> listItemCountryList = new ArrayList<>();
    private Donations donations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        addCountryListItems();
        initializeAds();
        donations = new Donations(this);
    }

    private void initializeAds() {
        AdView mAdMobAdView = (AdView) findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdMobAdView.loadAd(adRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_countrieslist_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_countrieslist_confirm:
                MainActivity.selectedCountryCodesList = Arrays.asList(getCountryKeys().split(","));
                if (MainActivity.selectedCountryCodesList.size() == 1 && MainActivity.selectedCountryCodesList.get(0).equals("")) {
                    MainActivity.selectedCountryCodesList = new ArrayList<>();
                }
                MainActivity.selectedCountries = MainActivity.selectedCountryCodesList.size();
                WorldMap.update();
                MainActivity.prefs.edit().putString("de.rpgstupe.travellite.countrykeys", getCountryKeys()).apply();
                showToast(getResources().getString(R.string.saved), Toast.LENGTH_SHORT);
                MainActivity.writeNewEntry(MainActivity.mAuth.getCurrentUser().getUid(), MainActivity.selectedCountryCodesList);
                this.finish();
                break;
            case R.id.action_countrieslist_donate:
                donations.onDonationButtonClicked();
                break;
            default:
                break;
        }
        return true;
    }

    private void addCountryListItems() {
        LinearLayout countryListLayout = (LinearLayout) findViewById(R.id.country_list);
        for (String country : MainActivity.countryCodesList) {
            ListItemCountry listItemCountry = new ListItemCountry(this);
            listItemCountryList.add(listItemCountry);
            int identifier = getResources().getIdentifier(country, "string", "de.rpgstupe.travellite");
            if (identifier != 0) {
                listItemCountry.setTitleText(getResources().getString(identifier));
            }
            listItemCountry.setKey(country);
            if (MainActivity.selectedCountryCodesList.contains(country)) {
                listItemCountry.setSelected(true);
            }
            countryListLayout.addView(listItemCountry);
        }
        // add an empty list item cause of ads space
        ListItemCountry listItemCountry = new ListItemCountry(this);
        listItemCountryList.add(listItemCountry);
        listItemCountry.setTitleText("");
        listItemCountry.setKey("");
        countryListLayout.addView(listItemCountry);
    }

    private String getCountryKeys() {
        List<String> keyList = new ArrayList<>();
        for (ListItemCountry country : listItemCountryList) {
            if (country.isCountrySelected()) {
                keyList.add(country.getKey());
            }
        }
        String keys = android.text.TextUtils.join(",", keyList);
        System.out.println(keys);
        return keys;
    }

    private void showToast(String text, int duration) {
        Toast toast = Toast.makeText(this, text, duration);
        toast.show();
    }
}