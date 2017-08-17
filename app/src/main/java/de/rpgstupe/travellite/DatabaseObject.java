package de.rpgstupe.travellite;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Fabian on 17.08.2017.
 */
@IgnoreExtraProperties
public class DatabaseObject {
    public String username;
    public List<String> countries;

    public DatabaseObject(String username, List<String> countries) {
        this.username = username;
        this.countries = countries;
    }

    public DatabaseObject() {}
}
