package com.manoranjan.applecart.Api;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveImageHelper implements Target {

    private Context context;
    private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference ;
    private String name;
    private String desc;

    public SaveImageHelper(Context context, AlertDialog alertDialog, ContentResolver contentResolver, String name, String desc) {

        this.context=context;
        this.alertDialogWeakReference = new WeakReference<AlertDialog>(alertDialog);
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
    }



    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        ContentResolver r = contentResolverWeakReference.get();

        AlertDialog dialog = alertDialogWeakReference.get();

        if (r != null)
            MediaStore.Images.Media.insertImage(r,bitmap,name,desc);
        dialog.dismiss();
        Toast.makeText(context,"Sucessfully Downloaded ", Toast.LENGTH_SHORT).show();
        /*Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);*/
        //open galerry after download
        /*Intent intent =new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/*");
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //startActivity(intent);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivity(Intent.createChooser(intent,"VIEW PICTURE"));*/

    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {
        AlertDialog dialog = alertDialogWeakReference.get();
        dialog.dismiss();
        Toast.makeText(context,"faild", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
