package com.mvp.commonlibrary.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.util.Util;

import java.security.MessageDigest;

/**
 * Created by hmin66 on 2019/12/4.
 */

public class RotateTransformation extends BitmapTransformation {

    private final String ID = getClass().getName();
    private float rotateRotationAngle = 0f;

    public RotateTransformation(Context context, float rotateRotationAngle) {
        this.rotateRotationAngle = rotateRotationAngle;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        Matrix matrix = new Matrix();

        matrix.postRotate(rotateRotationAngle);

        return Bitmap.createBitmap(toTransform, 0, 0, toTransform.getWidth(), toTransform.getHeight(), matrix, true);
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + "rotate" + rotateRotationAngle).getBytes(CHARSET));
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RotateTransformation) {
            RotateTransformation other = (RotateTransformation) obj;
            return rotateRotationAngle == other.rotateRotationAngle;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Util.hashCode(ID.hashCode(), Util.hashCode(rotateRotationAngle));
    }
}
