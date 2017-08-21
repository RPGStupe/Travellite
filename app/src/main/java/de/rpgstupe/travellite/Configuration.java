package de.rpgstupe.travellite;

import android.content.res.Resources;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import de.rpgstupe.travellite.database.UserDatabaseObject;

/**
 * Created by Fabian on 18.08.2017.
 */

public enum Configuration {
    instance;

    public String[] countryCodesList = {
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


    public String[] countryCodesListAsia = {
            "AFG", "ARM", "AZE", "BHR", "BGD", "BTN", "KHM", "CHN", "GEO", "IND", "IDN", "IRN",
            "IRQ", "ISR", "JPN", "JOR", "KAZ", "KWT", "KGZ", "LAO", "LBN", "MYS", "MNG", "MMR",
            "NPL", "PRK", "OMN", "PAK", "PHL", "QAT", "SAU", "KOR", "LKA", "SYR", "TWN", "TJK",
            "THA", "TUR", "TKM", "ARE", "UZB", "VNM", "YEM"
    };





    public String[] countryCodesListAll = {
            "AFG", "ARM", "AZE", "BHR", "BGD", "BTN", "KHM", "CHN", "GEO", "IND", "IDN", "IRN",
            "IRQ", "ISR", "JPN", "JOR", "KAZ", "KWT", "KGZ", "LAO", "LBN", "MYS", "MNG", "MMR",
            "NPL", "PRK", "OMN", "PAK", "PHL", "QAT", "SAU", "KOR", "LKA", "SYR", "TWN", "TJK",
            "THA", "TUR", "TKM", "ARE", "UZB", "VNM", "YEM",
            "ASM", "AUS", "NZL", "TLS", "FJI", "NCL", "PNG", "SLB",
            "BHS", "BLZ", "CAN", "CRI", "CUB", "DOM", "SLV", "GRL", "GTM", "HTI", "HND", "JAM",
            "MEX", "NIC", "PAN", "PRI", "USA",
            "ARG", "BOL", "BRA", "CHL", "COL", "ECU", "FLK", "GUF", "GUY", "PRY", "PER", "SUR",
            "URY", "VEN",
            "ALB", "AUT", "BLR", "BEL", "BIH", "BGR", "HRV", "CYP", "CZE", "DNK", "EST", "FIN",
            "FRA", "DEU", "GRC", "HUN", "ISL", "IRL", "ITA", "LVA", "LTU", "LUX", "MKD", "MDA",
            "MNE", "NLD", "NOR", "POL", "PRT", "ROU", "RUS", "SRB", "SVK", "SVN", "ESP", "SWE",
            "CHE", "UKR", "GBR",
            "DZA", "AGO", "BEN", "BWA", "BFA", "BDI", "CMR", "CAF", "TCD", "COG", "COD", "DJI",
            "EGY", "GNQ", "ERI", "ETH", "GAB", "GMB", "GHA", "GIN", "GNB", "CIV", "KEN", "LSO",
            "LBR", "LBY", "MDG", "MWI", "MLI", "MRT", "MAR", "MOZ", "NAM", "NER", "NGA", "RWA",
            "SEN", "SLE", "SOM", "ZAF", "SSD", "SDN", "SWZ", "TZA", "TGO", "TUN", "UGA", "ZMB",
            "ZWE"
    };

    public String[] countryCodesListAustralia = {
            "ASM", "AUS", "NZL", "TLS", "FJI", "NCL", "PNG", "SLB"
    };

    public String[] countryCodesListNA = {
            "BHS", "BLZ", "CAN", "CRI", "CUB", "DOM", "SLV", "GRL", "GTM", "HTI", "HND", "JAM",
            "MEX", "NIC", "PAN", "PRI", "USA"
    };

    public String[] countryCodesListLatinAmerica = {
            "ARG", "BOL", "BRA", "CHL", "COL", "ECU", "FLK", "GUF", "GUY", "PRY", "PER", "SUR",
            "URY", "VEN"
    };

    public String[] countryCodesListEurope = {
            "ALB", "AUT", "BLR", "BEL", "BIH", "BGR", "HRV", "CYP", "CZE", "DNK", "EST", "FIN",
            "FRA", "DEU", "GRC", "HUN", "ISL", "IRL", "ITA", "LVA", "LTU", "LUX", "MKD", "MDA",
            "MNE", "NLD", "NOR", "POL", "PRT", "ROU", "RUS", "SRB", "SVK", "SVN", "ESP", "SWE",
            "CHE", "UKR", "GBR"
    };

    public String[] countryCodesListAfrica = {
            "DZA", "AGO", "BEN", "BWA", "BFA", "BDI", "CMR", "CAF", "TCD", "COG", "COD", "DJI",
            "EGY", "GNQ", "ERI", "ETH", "GAB", "GMB", "GHA", "GIN", "GNB", "CIV", "KEN", "LSO",
            "LBR", "LBY", "MDG", "MWI", "MLI", "MRT", "MAR", "MOZ", "NAM", "NER", "NGA", "RWA",
            "SEN", "SLE", "SOM", "ZAF", "SSD", "SDN", "SWZ", "TZA", "TGO", "TUN", "UGA", "ZMB",
            "ZWE"
    };

    public List<String> selectedCountryCodesList = new ArrayList<>();
    public int selectedCountries;
    public WorldMap worldMap;

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FirebaseDatabase database;
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public List<CountryListDataObject> countryListItemsList;
    public UserDatabaseObject dataSnapshot;

    public void initDatabase(){
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true);
        }
    }
    public void initializeCountryListItemsList(Resources resources) {
        if (countryListItemsList == null) {
            countryListItemsList = new ArrayList<>();
            for (int i = 0; i < countryCodesList.length; i++) {
                int identifier = resources.getIdentifier(countryCodesList[i], "string", "de.rpgstupe.travellite");
                if (identifier != 0) {
                    String countryName;
                    countryName = resources.getString(identifier);
                    countryListItemsList.add(new CountryListDataObject(countryName, countryCodesList[i], selectedCountryCodesList.contains(countryCodesList[i])));
                }
            }
        }
    }
}
