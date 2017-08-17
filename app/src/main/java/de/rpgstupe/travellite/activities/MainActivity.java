package de.rpgstupe.travellite.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.caverock.androidsvg.SVGImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import de.rpgstupe.travellite.DatabaseObject;
import de.rpgstupe.travellite.Donations;
import de.rpgstupe.travellite.R;
import de.rpgstupe.travellite.WorldMap;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final int RC_SIGN_IN = 9001;
    public static FirebaseAuth mAuth;
    public static SharedPreferences prefs;
    public static String[] countryCodesList = {
            "AFG", "AGO", "ALB", "ARE", "ARG", "ARM", "ASM", "AUS", "AUT", "AZE", "BDI",
            "BEN", "BEL", "BFA", "BGD", "BGR", "BHR", "BHS", "BIH", "BLR", "BLZ", "BOL", "BRA",
            "BTN", "BWA", "CAF", "CAN", "CHE", "CHL", "CHN", "CIV", "CMR", "COD", "COG", "COL",
            "CRI", "CUB", "CYP", "CZE", "DEU", "DJI", "DNK", "DOM", "DZA", "ECU", "EGY", "ERI",
            "ESH", "ESP", "EST", "ETH", "FIN", "FJI", "FLK", "FRA", "GAB", "GBR", "GEO", "GHA",
            "GIN", "GMB", "GNB", "GNQ", "GRC", "GRL", "GTM", "GUF", "GUY", "HND", "HRV", "HTI",
            "HUN", "IDN", "IND", "IRL", "IRN", "IRQ", "ISL", "ISR", "ITA", "JAM", "JOR", "JPN",
            "KAZ", "KEN", "KGZ", "KHM", "KOR", "KWT", "LAO", "LBN", "LBR", "LBY", "LKA", "LSO",
            "LTU", "LUX", "LVA", "MAR", "MDA", "MDG", "MEX", "MKD", "MLI", "MMR", "MNE", "MNG",
            "MOZ", "MRT", "MWI", "MYS", "NAM", "NCL", "NER", "NGA", "NIC", "NLD", "NOR", "NPL",
            "NZL", "OMN", "PAK", "PAN", "PER", "PHL", "PNG", "POL", "PRI", "PRK", "PRT", "PRY",
            "QAT", "ROU", "RUS", "RWA", "SAU", "SDN", "SEN", "SGS", "SLB", "SLE", "SLV", "SOM",
            "SRB", "SSD", "SUR", "SVK", "SVN", "SWE", "SWZ", "SYR", "TCD", "TGO", "THA", "TJK",
            "TKM", "TLS", "TUN", "TUR", "TWN", "TZA", "UGA", "UKR", "URY", "USA", "UZB", "VEN",
            "VNM", "YEM", "ZAF", "ZMB", "ZWE"
    };

    public static String[] countryCodesListAsia = {
            "AFG", "ARM", "AZE", "BHR", "BGD", "BTN", "KHM", "CHN", "GEO", "IND", "IDN", "IRN",
            "IRQ", "ISR", "JPN", "JOR", "KAZ", "KWT", "KGZ", "LAO", "LBN", "MYS", "MNG", "MMR",
            "NPL", "PRK", "OMN", "PAK", "PHL", "QAT", "SAU", "KOR", "LKA", "SYR", "TWN", "TJK",
            "THA", "TUR", "TKM", "ARE", "UZB", "VNM", "YEM"
    };

    public static String[] countryCodesListAustralia = {
            "ASM", "AUS", "NZL", "TLS", "FJI", "NCL", "PNG", "SLB"
    };

    public static String[] countryCodesListNA = {
            "BHS", "BLZ", "CAN", "CRI", "CUB", "DOM", "SLV", "GRL", "GTM", "HTI", "HND", "JAM",
            "MEX", "NIC", "PAN", "PRI", "USA"
    };

    public static String[] countryCodesListLatinAmerica = {
            "ARG", "BOL", "BRA", "CHL", "COL", "ECU", "FLK", "GUF", "GUY", "PRY", "PER", "SUR",
            "URY", "VEN"
    };

    public static String[] countryCodesListEurope = {
            "ALB", "AUT", "BLR", "BEL", "BIH", "BGR", "HRV", "CYP", "CZE", "DNK", "EST", "FIN",
            "FRA", "DEU", "GRC", "HUN", "ISL", "IRL", "ITA", "LVA", "LTU", "LUX", "MKD", "MDA",
            "MNE", "NLD", "NOR", "POL", "PRT", "ROU", "RUS", "SRB", "SVK", "SVN", "ESP", "SWE",
            "CHE", "UKR", "GBR"
    };

    public static String[] countryCodesListAfrica = {
            "DZA", "AGO", "BEN", "BWA", "BFA", "BDI", "CMR", "CAF", "TCD", "COG", "COD", "DJI",
            "EGY", "GNQ", "ERI", "ETH", "GAB", "GMB", "GHA", "GIN", "GNB", "CIV", "KEN", "LSO",
            "LBR", "LBY", "MDG", "MWI", "MLI", "MRT", "MAR", "MOZ", "NAM", "NER", "NGA", "RWA",
            "SEN", "SLE", "SOM", "ZAF", "SSD", "SDN", "SWZ", "TZA", "TGO", "TUN", "UGA", "ZMB",
            "ZWE"
    };

    public static boolean displayReviewCard = false;

    public static FirebaseDatabase database;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    public static List<String> selectedCountryCodesList = new ArrayList<>();
    public static int selectedCountries;
    public static int allCountries;
    private Donations donations;
    private boolean bDisplay;
    private boolean bDeleted;
    private int openedCount;
    private int cardCount;
    private GoogleApiClient mGoogleApiClient;
    static FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rework);
        initializeSharedPrefs();
        WorldMap worldmap = new WorldMap(new SVGImageView(this), null, this);
        Arrays.sort(countryCodesList, new Comparator<String>() {
            public int compare(String code1, String code2) {
                return getResources().getString(getResources().getIdentifier(code1, "string", "de.rpgstupe.travellite"))
                        .toUpperCase().compareTo(getResources().getString(getResources().getIdentifier(code2, "string", "de.rpgstupe.travellite"))
                                .toUpperCase());
            }
        });
        initializeAds();
        setOnClickListeners();
        //loadSharedPrefsCountries();
        displayReviewCard = isDisplayReviewCard();
        if (displayReviewCard) {
            showReviewDialog();
        }
        donations = new Donations(this);
        mAuth = FirebaseAuth.getInstance();
        signInwithGoogle();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        database.setPersistenceEnabled(true);
    }

    public static void writeNewEntry(String username, List<String> countries) {
        DatabaseObject databaseObject = new DatabaseObject(username, countries);

        database.getReference("users").child(username).child("username").setValue(username);
        database.getReference("users").child(username).child("countries").setValue(selectedCountryCodesList);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @SuppressWarnings("VisibleForTests")
    public static void uploadFile(Bitmap bitmap, String name) {
        if (bitmap != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference mountainImagesRef = storage.getReference("images/journal/" + mAuth.getCurrentUser().getUid() + "/" + name);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = mountainImagesRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                }
            });
        }
    }


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            database.getReference("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DatabaseObject s = dataSnapshot.getValue(DatabaseObject.class);
                    if (s.countries != null) {
                        selectedCountryCodesList = new ArrayList<>(s.countries);
                        updateMap();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
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
                prefs.edit().putBoolean("de.rpgstupe.travellite.reviewcarddeleted", true).apply();
            }
        });
        builder.setView(viewInflated);
        builder.setNegativeButton(R.string.never, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prefs.edit().putBoolean("de.rpgstupe.travellite.reviewcarddeleted", true).apply();
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

    protected void signInwithGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


    private void setOnClickListeners() {
        ImageButton btn_menu_countries = (ImageButton) findViewById(R.id.btn_menu_countries);
        ImageButton btn_menu_statistics = (ImageButton) findViewById(R.id.btn_menu_statistics);
        ImageButton btn_menu_journal = (ImageButton) findViewById(R.id.btn_menu_journal);
        btn_menu_countries.setOnClickListener(this);
        btn_menu_statistics.setOnClickListener(this);
        btn_menu_journal.setOnClickListener(this);
    }

    private void initializeAds() {
        AdView mAdMobAdView = (AdView) findViewById(R.id.admob_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdMobAdView.loadAd(adRequest);
    }

    private void initializeSharedPrefs() {
        prefs = this.getSharedPreferences(
                "de.rpgstupe.travellite", Context.MODE_PRIVATE);
        prefs.edit().putInt("de.rpgstupe.travellite.countopened", prefs.getInt("de.rpgstupe.travellite.countopened", 0) + 1).apply();
    }

    private void loadSharedPrefsCountries() {
        String s = prefs.getString("de.rpgstupe.travellite.countrykeys", "");
        bDisplay = prefs.getBoolean("de.rpgstupe.travellite.reviewcarddisplay", false);
        bDeleted = prefs.getBoolean("de.rpgstupe.travellite.reviewcarddeleted", false);
        cardCount = prefs.getInt("de.rpgstupe.travellite.countcards", 0);
        openedCount = prefs.getInt("de.rpgstupe.travellite.countopened", 0);
        String[] countryArray = s.split(",");
        List<String> countryList = Arrays.asList(countryArray);

        for (String key : countryCodesList) {
            if (countryList.contains(key)) {
                selectedCountryCodesList.add(key);
                WorldMap.colorizeCountry(key, true);
            }
        }
        selectedCountries = selectedCountryCodesList.size();
        allCountries = countryCodesList.length;
    }

    private void updateMap() {

        for (String key : selectedCountryCodesList) {
                WorldMap.colorizeCountry(key, true);
            }
        selectedCountries = selectedCountryCodesList.size();
        WorldMap.update();
    }

    private boolean isDisplayReviewCard() {

        if (bDisplay == false) {
            if (cardCount >= 3 && openedCount >= 10) {
                bDisplay = true;
                prefs.edit().putBoolean("de.rpgstupe.travellite.reviewcarddisplay", true).apply();
            }
        }
        return bDisplay && !bDeleted;
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
