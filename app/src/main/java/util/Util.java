package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileOutputStream;

public class Util {

    static DatabaseReference dbRef;

    static StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    public enum nodeValues {

        Users,
        Dog,
        DogOwner,
        DogWalker,
        Requests,
        Notifications

    }
    public enum imageFolders {

        DogOwnersImages,
        DogWalkersImages,
        DogImages,

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

    public static StorageReference setStorageReference(String folderImages, String photoId){

        StorageReference photoReference= storageReference.child(folderImages  + "/" +
                photoId);
        return photoReference;
    }

}
