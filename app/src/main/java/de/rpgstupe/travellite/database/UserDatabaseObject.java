package de.rpgstupe.travellite.database;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by Fabian on 17.08.2017.
 */
@IgnoreExtraProperties
public class UserDatabaseObject {
    public String username;
    public List<String> countries;
    public List<CardDatabaseObject> journal;

    public UserDatabaseObject(String username, List<String> countries, List<CardDatabaseObject> cardDatabaseObjectList) {
        this.username = username;
        this.countries = countries;
        this.journal = cardDatabaseObjectList;
    }

    public UserDatabaseObject() {}
}
