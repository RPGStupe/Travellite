package de.rpgstupe.travellite.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import de.rpgstupe.travellite.Configuration;
import de.rpgstupe.travellite.R;
import de.rpgstupe.travellite.SharedPreferencesLoader;
import de.rpgstupe.travellite.WorldMap;
import de.rpgstupe.travellite.database.UserDatabaseObject;
import de.rpgstupe.travellite.payment.Donations;
import de.rpgstupe.travellite.utils.AdUtil;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final int RC_SIGN_IN = 9001;


    public static boolean displayReviewCard = false;
    private Donations donations;
    public static GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rework);
        Configuration.instance.initDatabase();
        Arrays.sort(Configuration.instance.countryCodesList, new Comparator<String>() {
            public int compare(String code1, String code2) {
                return getResources().getString(getResources().getIdentifier(code1, "string", "de.rpgstupe.travellite"))
                        .toUpperCase().compareTo(getResources().getString(getResources().getIdentifier(code2, "string", "de.rpgstupe.travellite"))
                                .toUpperCase());
            }
        });
        AdUtil.initializeAds((AdView) findViewById(R.id.admob_adview));
        setOnClickListeners();
        if (displayReviewCard) {
            showReviewDialog();
        }
        donations = new Donations(this);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Configuration.instance.database.getReference("users").child(Configuration.instance.mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Configuration.instance.dataSnapshot = dataSnapshot.getValue(UserDatabaseObject.class);
                if (Configuration.instance.dataSnapshot != null && Configuration.instance.dataSnapshot.countries != null) {
                    Configuration.instance.selectedCountryCodesList = new ArrayList<>(Configuration.instance.dataSnapshot.countries);
                }
                if (Configuration.instance.worldMap == null) {
                    Configuration.instance.worldMap = new WorldMap(MainActivity.this);
                } else {
                    if (MainActivity.this != Configuration.instance.worldMap.getActivity()) {
                        Configuration.instance.worldMap = new WorldMap(MainActivity.this);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void init() {
        Configuration.instance.database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(Configuration.instance.mAuth.getCurrentUser().getUid())) {
                    new SharedPreferencesLoader(MainActivity.this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Configuration.instance.database.getReference("users").child(Configuration.instance.mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Configuration.instance.dataSnapshot = dataSnapshot.getValue(UserDatabaseObject.class);
                if (Configuration.instance.dataSnapshot != null && Configuration.instance.dataSnapshot.countries != null) {
                    Configuration.instance.selectedCountryCodesList = new ArrayList<>(Configuration.instance.dataSnapshot.countries);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void writeNewEntry(String username) {
        Configuration.instance.database.getReference("users").child(username).child("username").setValue(username);
        Configuration.instance.database.getReference("users").child(username).child("countries").setValue(Configuration.instance.selectedCountryCodesList);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        // very important:
        if (donations.mBroadcastReceiver != null) {
            unregisterReceiver(donations.mBroadcastReceiver);
        }

        // very important:
        if (donations.mHelper != null) {
            donations.mHelper.disposeWhenFinished();
            donations.mHelper = null;
        }
    }


    private void showReviewDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_review, (ViewGroup) findViewById(android.R.id.content), false);
        final Button btnReview = (Button) viewInflated.findViewById(R.id.btn_review);
        btnReview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String packageName = getApplicationContext().getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                }
            }
        });
        builder.setView(viewInflated);
        builder.setNegativeButton(R.string.never, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.later, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }


    private void setOnClickListeners() {
        ImageButton btn_menu_countries = (ImageButton) findViewById(R.id.btn_menu_countries);
        ImageButton btn_menu_statistics = (ImageButton) findViewById(R.id.btn_menu_statistics);
        ImageButton btn_menu_journal = (ImageButton) findViewById(R.id.btn_menu_journal);
        btn_menu_countries.setOnClickListener(this);
        btn_menu_statistics.setOnClickListener(this);
        btn_menu_journal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (findViewById(v.getId()) != null && findViewById(v.getId()).isEnabled()) {
            switch (v.getId()) {
                case R.id.btn_menu_countries:
                    Intent intentCountries = new Intent(MainActivity.this, CountriesListActivity.class);
                    startActivity(intentCountries);
                    break;
                case R.id.btn_menu_statistics:
                    Intent intentStatistics = new Intent(MainActivity.this, StatisticsActivity.class);
                    startActivity(intentStatistics);
                    break;
                case R.id.btn_menu_journal:
                    Intent intentJournal = new Intent(MainActivity.this, CardViewActivity.class);
                    startActivity(intentJournal);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_info:
                AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                alertDialog.setTitle(getResources().getString(R.string.information_title));
                alertDialog.setMessage(getResources().getString(R.string.information_message));
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // here you can add functions
                    }
                });
                alertDialog.show();
                break;
            case R.id.action_donate:
                donations.onDonationButtonClicked();
                break;
            default:
                break;
        }

        return true;
    }
}
