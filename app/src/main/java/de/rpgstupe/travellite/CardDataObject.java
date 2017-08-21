package de.rpgstupe.travellite;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.snatik.storage.Storage;

import de.rpgstupe.travellite.activities.CardViewActivity;

import static de.rpgstupe.travellite.activities.CardViewActivity.imageFilePath;
import static de.rpgstupe.travellite.activities.CardViewActivity.storage;

/**
 * Created by Fabian on 25.06.2017.
 */

public class CardDataObject implements OnSuccessListener<byte[]>, OnFailureListener {
    private String title;
    private String notes;
    private byte[] cardImage;
    private long id;
    private String date;
    private boolean uploaded;

    public CardDataObject(long id, String title, String notes, String date) {
        this.title = title;
        this.notes = notes;
        this.id = id;
        this.date = date;
        this.cardImage = getCardImageFromId();
    }

    public CardDataObject(long id, String title, String notes, String date, String path, Storage storage) {
        this.title = title;
        this.notes = notes;
        this.id = id;
        this.date = date;
        this.cardImage = getCardImageFromId(storage, path);
    }


    public byte[] getCardImage() {
        return cardImage;
    }

    public void setCardImage(byte[] cardImage) {
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

    public byte[] getCardImageFromId() {
        byte[] imageBytes = null;
        if (storage.isFileExist(imageFilePath + id)) {
            imageBytes = storage.readFile(imageFilePath + id);
        } else {
            StorageReference storageRef = Configuration.instance.firebaseStorage.getReference("images/journal/" + Configuration.instance.mAuth.getCurrentUser().getUid() + "/" + id);
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(this).addOnFailureListener(this);
        }
        return imageBytes;
    }

    public byte[] getCardImageFromId(Storage storage, String path) {
        byte[] imageBytes = null;
        if (storage.isFileExist(path + id)) {
            imageBytes = storage.readFile(path + id);
        } else {
            StorageReference storageRef = Configuration.instance.firebaseStorage.getReference("images/journal/" + Configuration.instance.mAuth.getCurrentUser().getUid() + "/" + id);
            final long ONE_MEGABYTE = 1024 * 1024;
            storageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(this).addOnFailureListener(this);
        }
        return imageBytes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public void onSuccess(byte[] bytes) {
        cardImage = bytes;
        storage.createFile(imageFilePath + id, cardImage);
        CardViewActivity.reload();
    }

    @Override
    public void onFailure(@NonNull Exception e) {
        //TODO: show placeholder image if not in storage nor firebasestorage
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }
}