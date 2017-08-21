package de.rpgstupe.travellite;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.opencsv.CSVParser;
import com.snatik.storage.Storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.rpgstupe.travellite.activities.MainActivity;
import de.rpgstupe.travellite.adapters.JournalAdapter;

/**
 * Created by Fabian on 20.08.2017.
 */

public class SharedPreferencesLoader {

    public SharedPreferences prefs;

    public SharedPreferencesLoader(Activity activity) {
        initializeSharedPrefs(activity);
        loadSharedPrefs(activity);
    }

    private void initializeSharedPrefs(Activity activity) {
        prefs = activity.getSharedPreferences(
                "de.rpgstupe.travellite", Context.MODE_PRIVATE);
    }

    private void loadSharedPrefs(Activity activity) {
        String s = prefs.getString("de.rpgstupe.travellite.countrykeys", "");
        if (!"".equals(s)) {
            String[] countryArray = s.split(",");
            List<String> countryList = Arrays.asList(countryArray);
            Configuration.instance.selectedCountryCodesList = countryList;
        }
        MainActivity.writeNewEntry(Configuration.instance.mAuth.getCurrentUser().getUid());
        Storage storage = new Storage(activity);

        String path = storage.getInternalFilesDirectory();
        String journalFilePath = path + "/Journal.csv";
        List results = new ArrayList<>();
        if (storage.isFileExist(journalFilePath)) {
            CSVParser parser = new CSVParser(',');
            String content = storage.readTextFile(journalFilePath);
            storage.deleteFile(journalFilePath);
            if (!"".equals(content)) {
                String[] contentArray = content.split("\n");
                for (final String item : contentArray) {
                    final String[] items;
                    try {
                        items = parser.parseLine(item);
                        results.add(new CardDataObject(Long.parseLong(items[0]), items[1], items[2], items[3], path + "/journal_images", storage));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Collections.reverse(results);
            }
        }
        Configuration.instance.database.getReference("users").child(Configuration.instance.mAuth.getCurrentUser().getUid()).child("journal").setValue(JournalAdapter.getmDatasetDatabase(results));
    }
}
