package util;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.example.myapplication.OwnerProfileActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;

public class Util {

    static DatabaseReference dbRef;

    static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    static boolean isLoaded = false;

    public enum nodeValues {

        Users,
        Dog,
        DogOwner,
        DogWalker,
        Requests,
        Notifications,
        Reviews

    }
    public enum imageFolders {

        DogOwnersImages,
        DogWalkersImages,
        DogImages,
        StarsImages

    }

    public enum requestStatus {

        Accepted,
        Rejected,
        Requested,

    }

    public enum notificationStatus {

        Read,
        Unread,


    }

    //Local image storage handling
    public static void saveImage(Bitmap finalBitmap, String photoStr) {

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path + "/" , photoStr + ".jpg");
        if (file.exists ()) file.delete ();
        try {
            path.mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public static Bitmap getSavedImage(String photoStr){
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path + "/" , photoStr + ".jpg");

        if(file.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

            return myBitmap;


        }
        return null;
    }

    public static void deleteFile(String photoStr) {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File file = new File(path + "/" , photoStr + ".jpg");
        if (file.exists ()) file.delete ();
    }



    //Static methods to instantiate the firebase database access and set the reference to access the nodes and children
    //requested by different activities classes

    public static DatabaseReference setNodeDatabaseReference(String nodeValue){
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(nodeValue));
        return dbRef;
    }

    public static DatabaseReference setNodeAndChildDatabaseReference(String nodeValue, String childNodeValue){
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(nodeValue))
                .child(childNodeValue);
        return dbRef;
    }

    public static DatabaseReference setNodeAndChildrenDatabaseReference(String nodeValue, String childNodeValue1,
                                                                        String childNodeValue2){
        dbRef = FirebaseDatabase
                .getInstance()
                .getReference(String.valueOf(nodeValue))
                .child(childNodeValue1)
                .child(childNodeValue2);
        return dbRef;
    }

    //Reference the firebase storage access
    public static StorageReference setStorageReference(String folderImages, String photoId){

        StorageReference photoReference= storageReference.child(folderImages  + "/" +
                photoId);
        return photoReference;
    }

    /**
     * Validates if a string is empty or only contains whitespace characters
     * @param str the string to validate
     * @return true if the string is empty or only contains whitespace characters, false otherwise
     */
    public static boolean isNullOrWhiteSpace(String str) {
        return str == null || str.trim().isEmpty();
    }

    // Load an image from the firebase reference into an imageview widget
    public static boolean loadImageFromDB(String imageFolder, String imageId, ImageView imageViewObj){
        final long ONE_MEGABYTE = 1024 * 1024;

        Util.setStorageReference(imageFolder,imageId)
                .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {@Override
                public void onSuccess(byte[] bytes) {
                    Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    imageViewObj.setImageBitmap(bmp);
                    isLoaded = true;
                }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        isLoaded = false;
                    }
                });
        return isLoaded;
    }


}
