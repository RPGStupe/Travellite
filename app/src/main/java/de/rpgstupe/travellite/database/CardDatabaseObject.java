package de.rpgstupe.travellite.database;

import de.rpgstupe.travellite.CardDataObject;

/**
 * Created by Fabian on 20.08.2017.
 */

public class CardDatabaseObject {
    public String title;
    public String notes;
    public long id;
    public String date;

    public CardDatabaseObject(CardDataObject cardDataObject) {
        this.title = cardDataObject.getTitle();
        this.notes = cardDataObject.getNotes();
        this.id = cardDataObject.getId();
        this.date = cardDataObject.getDate();
    }

    public CardDatabaseObject() {
    }
}
