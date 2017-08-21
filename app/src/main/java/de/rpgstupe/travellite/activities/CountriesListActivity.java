package de.rpgstupe.travellite.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.rpgstupe.travellite.utils.AdUtil;
import de.rpgstupe.travellite.Configuration;
import de.rpgstupe.travellite.adapters.CountryListAdapter;
import de.rpgstupe.travellite.ListItemCountry;
import de.rpgstupe.travellite.R;
import de.rpgstupe.travellite.payment.Donations;

/**
 * Created by Fabian on 25.06.2017.
 */

public class CountriesListActivity extends AppCompatActivity {


    private static String LOG_TAG = "CountriesListActivity";

    List<ListItemCountry> listItemCountryList = new ArrayList<>();
    private Donations donations;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private CountryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countries);
        Configuration.instance.initializeCountryListItemsList(getResources());
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_countrylist);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new CountryListAdapter(getResources(), Configuration.instance.countryListItemsList);
        mAdapter.setOnItemClickListener(new CountryListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                mAdapter.getmDataset().get(position).setActivated(!mAdapter.getmDataset().get(position).isActivated());
                if (mAdapter.getmDataset().get(position).isActivated()) {
                    Configuration.instance.selectedCountryCodesList.add(mAdapter.getmDataset().get(position).getCountryCode());
                } else {
                    Configuration.instance.selectedCountryCodesList.remove(mAdapter.getmDataset().get(position).getCountryCode());
                }
                mAdapter.notifyItemChanged(position);
                Set<String> tempSet = new HashSet<>(Configuration.instance.selectedCountryCodesList);
                Configuration.instance.selectedCountryCodesList = new ArrayList<>();
                Configuration.instance.selectedCountryCodesList.addAll(tempSet);
            }
        });
        mRecyclerView.setAdapter(mAdapter);



        AdUtil.initializeAds((AdView) findViewById(R.id.admob_adview));
        donations = new Donations(this);
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
                MainActivity.writeNewEntry(Configuration.instance.mAuth.getCurrentUser().getUid());
                new LongOperation().execute("");
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

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Configuration.instance.selectedCountries = Configuration.instance.selectedCountryCodesList.size();
            Configuration.instance.worldMap.colorizeCountry(Configuration.instance.selectedCountryCodesList);
            List<String> decolorizeList = new ArrayList<>(Arrays.asList(Configuration.instance.countryCodesList));
            decolorizeList.removeAll(Configuration.instance.selectedCountryCodesList);
            Configuration.instance.worldMap.decolorizeCountry(decolorizeList);
            Configuration.instance.worldMap.updateMap();
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println(result);
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}