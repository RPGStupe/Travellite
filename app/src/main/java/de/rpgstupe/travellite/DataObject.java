package de.rpgstupe.travellite;

import android.graphics.Bitmap;

/**
 * Created by Fabian on 25.06.2017.
 */

public class DataObject {
    private String mText1;
    private String mText2;
    private Bitmap cardImage;
    private long id;
    private String date;

    public DataObject(String text1, String text2, String date, Bitmap cardImage, long id) {
        mText1 = text1;
        mText2 = text2;
        this.cardImage = cardImage;
        this.id = id;
        this.date = date;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public String getmText2() {
        return mText2;
    }

    public void setmText2(String mText2) {
        this.mText2 = mText2;
    }

    public Bitmap getCardImage() {
        return cardImage;
    }

    public void setCardImage(Bitmap cardImage) {
        this.cardImage = cardImage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}