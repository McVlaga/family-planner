package com.planner.family.therapist.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtils {

    public static File getCroppedScaledBitmap(Bitmap bitmap) {
        Bitmap squaredBitmap = cropBitmapToSquare(bitmap);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(squaredBitmap, 200, 200, false);
        return getBitmapFile(scaledBitmap);
    }

    public static Bitmap getSquaredBitmapWithColor(String colorStr) {

        Rect rect = new Rect(0, 0, 1, 1);

        Bitmap image = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);

        int color = Color.parseColor(colorStr);

        Paint paint = new Paint();
        paint.setColor(color);

        canvas.drawRect(rect, paint);
        return image;
    }

    public static Bitmap uriToBitmap(Context c, Uri uri) {
        if (c == null && uri == null) {
            return null;
        }
        try {
            InputStream input = c.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(input , null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Bitmap getBitmapFromUri(Context context, Uri imageUri) {
        Bitmap bitmap = null;
        try {
            InputStream input = context.getContentResolver().openInputStream(imageUri);
            bitmap = BitmapFactory.decodeStream(input , null, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap cropBitmapToSquare(Bitmap bitmap) {
        Bitmap newBitmap;

        if (bitmap.getWidth() >= bitmap.getHeight()){

            newBitmap = Bitmap.createBitmap(
                    bitmap,
                    bitmap.getWidth()/2 - bitmap.getHeight()/2,
                    0,
                    bitmap.getHeight(),
                    bitmap.getHeight()
            );

        }else{

            newBitmap = Bitmap.createBitmap(
                    bitmap,
                    0,
                    bitmap.getHeight()/2 - bitmap.getWidth()/2,
                    bitmap.getWidth(),
                    bitmap.getWidth()
            );
        }

        return newBitmap;
    }

    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap bitmapScaled = Bitmap.createScaledBitmap(bitmap, 200, 200, false);

        Bitmap output = Bitmap.createBitmap(bitmapScaled.getWidth(),
                bitmapScaled.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmapScaled.getWidth(), bitmapScaled.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawCircle(bitmapScaled.getWidth() / 2, bitmapScaled.getHeight() / 2,
                bitmapScaled.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmapScaled, rect, rect, paint);
        return output;
    }

    public static File getBitmapFile(Bitmap bitmap) {

        File f = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + "_image.png");
        try {
            f.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }
}
