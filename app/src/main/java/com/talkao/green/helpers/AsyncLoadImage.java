package com.talkao.green.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class AsyncLoadImage extends AsyncTask<String, String, Bitmap> {
    private final static String TAG = "AsyncLoadImage";
    private ImageView imageView;
    private Context mContext;
    private String mName;

    public AsyncLoadImage(ImageView imageView, String nameCache, Context context) {
        this.imageView = imageView;
        this.mName = nameCache;
        this.mContext = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        File imageFile = null;
        if (!mName.equalsIgnoreCase("")){
            imageFile = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), mName);
            if (imageFile.exists()){
                return BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            }
        }


        Bitmap bitmap = null;
        try {
            URL url = new URL(params[0]);
            InputStream is = (InputStream) url.getContent();
            bitmap = BitmapFactory.decodeStream(is);

            if (imageFile != null){
                FileOutputStream fo = new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fo);
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return bitmap;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}