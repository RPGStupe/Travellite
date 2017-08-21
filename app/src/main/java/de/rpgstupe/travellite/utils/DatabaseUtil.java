package de.rpgstupe.travellite.utils;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.rpgstupe.travellite.Configuration;


/**
 * Created by Fabian on 18.08.2017.
 */

public class DatabaseUtil {
    @SuppressWarnings("VisibleForTests")
    public static void uploadFile(byte[] bitmap, final String name) {
        if (bitmap != null) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference mountainImagesRef = storage.getReference("images/journal/" + Configuration.instance.mAuth.getCurrentUser().getUid() + "/" + name);
            mountainImagesRef.putBytes(bitmap);
        }
    }
}
