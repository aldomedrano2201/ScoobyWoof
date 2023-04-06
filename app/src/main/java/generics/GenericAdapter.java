package generics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.myapplication.PropertyExtractor;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import util.Util;


public class GenericAdapter extends BaseAdapter {

    private Context context;
    private GenericClass genericlistView;
    private ArrayList<? extends Object> listOfObj;
    private String storagePath, customDetail;
    int idViewItem;

    public GenericAdapter(Context context, GenericClass genericlistView, int idViewItem, String storagePath, String customDetail) {

        this.context = context;
        this.genericlistView = genericlistView;
        this.idViewItem = idViewItem;
        this.storagePath = storagePath;
        this.listOfObj = (ArrayList<? extends Object>) genericlistView.getValue();
        this.customDetail = customDetail;
    }

    @Override
    public int getCount() {
        return listOfObj.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfObj.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        final int position = 1;
        View viewItem = null;
        ImageView imPhoto;
        TextView tvText1,tvText2;
        try {
        // We need to convert one_item.xml to the corresponding
        // Java view hierarchy  ==> this conversion is
        // called Layout inflation
        LayoutInflater inflater= LayoutInflater.from(context);
        viewItem = inflater.inflate(idViewItem,viewGroup,false);
        imPhoto = viewItem.findViewById(R.id.imgPic);
        tvText1 = viewItem.findViewById(R.id.text1);
        tvText2 = viewItem.findViewById(R.id.text2);
        GenericClass obj = new GenericClass<>(listOfObj.get(i));
        Map<String, Object> result = new HashMap<>();

            result = PropertyExtractor.extractProperties(obj.getValue());
            tvText1.setText(result.get("name").toString());
            tvText2.setText(result.get(customDetail).toString());
            String photoStr= result.get("id").toString();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference photoReference= storageReference.child(storagePath  + "/" +
                    photoStr);
            if (photoReference != null){
                final long ONE_MEGABYTE = 1024 * 1024;
                photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        imPhoto.setImageBitmap(bmp);
                        Util.saveImage(bmp,photoStr);


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });


            }

            return viewItem;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }



}
