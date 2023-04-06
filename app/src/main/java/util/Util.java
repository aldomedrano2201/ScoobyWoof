package util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class Util {



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



}
