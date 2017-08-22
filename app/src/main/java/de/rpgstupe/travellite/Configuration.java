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
            "AFG", "EGY", "ALA", "ALB", "DZA", "ASM", "VIR", "AND", "AGO", "AIA", "ATA", "ATG",
            "GNQ", "ARG", "ARM", "ABW", "ASC", "AZE", "ETH", "AUS", "BHS", "BHR", "BGD", "BRB",
            "BEL", "BLZ", "BEN", "BMU", "BTN", "BOL", "BIH", "BWA", "BVT", "BRA", "VGB", "IOT",
            "BRN", "BGR", "BFA", "BDI", "CHL", "CHN", "COK", "CRI", "DNK", "DEU", "SHN", "DGA",
            "DMA", "DOM", "DJI", "ECU", "SLV", "ERI", "EST", "FLK", "FRO", "FJI", "FIN", "FRA",
            "GUF", "PYF", "ATF", "GAB", "GMB", "GEO", "GHA", "GIB", "GRD", "GRC", "GRL", "GLP",
            "GUM", "GTM", "GGY", "GIN", "GNB", "GUY", "HTI", "HMD", "HND", "HKG", "IND", "IDN",
            "IRQ", "IRN", "IRL", "ISL", "IMN", "ISR", "ITA", "JAM", "JPN", "YEM", "JEY", "JOR",
            "CYM", "KHM", "CMR", "CAN", "CPV", "KAZ", "QAT", "KEN", "KGZ", "KIR", "CCK", "COL",
            "COM", "COD", "HRV", "CUB", "KWT", "LAO", "LSO", "LVA", "LBN", "LBR", "LBY", "LIE",
            "LTU", "LUX", "MAC", "MDG", "MWI", "MYS", "MDV", "MLI", "MLT", "MAR", "MHL", "MTQ",
            "MRT", "MUS", "MYT", "MKD", "MEX", "FSM", "MDA", "MCO", "MNG", "MNE", "MSR", "MOZ",
            "MMR", "NAM", "NRU", "NPL", "NCL", "NZL", "NTZ", "NIC", "NLD", "ANT", "NER", "NGA",
            "NIU", "PRK", "MNP", "NFK", "NOR", "OMN", "AUT", "TLS", "PAK", "PSE", "PLW", "PAN",
            "PNG", "PRY", "PER", "PHL", "PCN", "POL", "PRT", "PRI", "CIV", "COG", "REU", "RWA",
            "ROU", "RUS", "SLB", "ZMB", "WSM", "SMR", "STP", "SAU", "SWE", "CHE", "SEN", "SRB",
            "SCG", "SYC", "SLE", "ZWE", "SGP", "SVK", "SVN", "SOM", "SUN", "ESP", "LKA", "KNA",
            "LCA", "SPM", "VCT", "ZAF", "SDN", "SGS", "KOR", "SUR", "SJM", "SWZ", "SYR", "TJK",
            "TWN", "TZA", "THA", "TGO", "TKL", "TON", "TTO", "TAA", "TCD", "CZE", "TUN", "TUR",
            "TKM", "TCA", "TUV", "UGA", "UKR", "HUN", "URY", "UZB", "VUT", "VAT", "VEN", "ARE",
            "USA", "GBR", "VNM", "WLF", "CXR", "BLR", "ESH", "CAF", "CYP"
    };


    public String[] countryCodesListAsia = {
            "AFG", "ARM", "AZE", "BHR", "BGD", "BTN", "IOT", "BRN", "CHN", "GUM", "HKG", "IND",
            "IDN", "IRQ", "IRN", "ISR", "JPN", "YEM", "JOR", "KHM", "KAZ", "QAT", "KGZ", "CCK",
            "KWT", "LAO", "LBN", "MAC", "MYS", "MDV", "MNG", "MMR", "NPL", "NTZ", "PRK", "OMN",
            "PAK", "PSE", "PHL", "RUS", "SAU", "SGP", "LKA", "KOR", "SYR", "TJK", "TWN", "THA",
            "TUR", "TKM", "UZB", "ARE", "VNM", "CXR", "CYP"
    };

    public String[] countryCodesListAustralia = {
            "ASM", "AUS", "COK", "FJI", "PYF", "HMD", "KIR", "MHL", "FSM", "NRU", "NCL", "NZL",
            "NIU", "MNP", "NFK", "TLS", "PLW", "PNG", "PCN", "SLB", "WSM", "TKL", "TON", "TUV",
            "VUT", "WLF"
    };

    public String[] countryCodesListNA = {
            "AIA", "ATG", "ABW", "BHS", "BRB", "BLZ", "BMU", "VGB", "CRI", "DMA", "SLV", "GRD",
            "GRL", "GLP", "GTM", "HTI", "HND", "JAM", "CYM", "CAN", "CUB", "MTQ", "MEX", "MSR",
            "NIC", "ANT", "PRI", "KNA", "SPM", "TCA", "USA"
    };

    public String[] countryCodesListLatinAmerica = {
            "VIR", "ARG", "BOL", "BRA", "CHL", "DOM", "ECU", "GUF", "GUY", "COL", "PAN", "PRY",
            "PER", "LCA", "VCT", "SGS", "SUR", "TTO", "URY", "VEN"
    };

    public String[] countryCodesListEurope = {
            "ALA", "ALB", "AND", "BEL", "BIH", "BGR", "DNK", "DEU", "EST", "FRO", "FIN", "FRA",
            "GEO", "GRC", "GGY", "IRL", "ISL", "IMN", "ITA", "JEY", "HRV", "LVA", "LIE", "LTU",
            "LUX", "MLT", "MKD", "MDA", "MCO", "MNE", "NLD", "NOR", "AUT", "POL", "PRT", "ROU",
            "SMR", "SWE", "CHE", "SRB", "SCG", "SVK", "SVN", "SUN", "ESP", "SJM", "CZE", "UKR",
            "HUN", "VAT", "GBR", "BLR"
    };

    public String[] countryCodesListAfrica = {
            "EGY", "DZA", "AGO", "GNQ", "ASC", "ETH", "BEN", "BWA", "BFA", "BDI", "SHN", "DGA",
            "DJI", "ERI", "FLK", "GAB", "GMB", "GHA", "GIB", "GIN", "GNB", "CMR", "CPV", "KEN",
            "COM", "COD", "LSO", "LBR", "LBY", "MDG", "MWI", "MLI", "MAR", "MRT", "MUS", "MYT",
            "MOZ", "NAM", "NER", "NGA", "CIV", "COG", "REU", "RWA", "ZMB", "STP", "SEN", "SYC",
            "SLE", "ZWE", "SOM", "ZAF", "SDN", "SWZ", "TZA", "TGO", "TAA", "TCD", "TUN", "UGA",
            "ESH", "CAF"
    };

    public List<String> selectedCountryCodesList = new ArrayList<>();
    public int selectedCountries;
    public WorldMap worldMap;

    public FirebaseAuth mAuth = FirebaseAuth.getInstance();

    public FirebaseDatabase database;
    public FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    public List<CountryListDataObject> countryListItemsList;
    public UserDatabaseObject dataSnapshot;

    public void initDatabase() {
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
