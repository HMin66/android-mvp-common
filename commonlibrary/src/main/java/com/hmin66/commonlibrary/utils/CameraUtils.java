package com.hmin66.commonlibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/18.
 */

public class CameraUtils {

    public static final int REQUEST_CODE_TAKE_PICTURE = 1000;

    /**
     * 调用系统相机
     */
    public static void openSystemCamera(Activity activity){
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String userHeadImg = "userheadimg.jpg";
        File file = new File(DirectoryUtils
                .getCacheDirectory(activity, "pictures")
                .getAbsolutePath() + File.separator + userHeadImg);
        Uri photoUri = FileProvider.getUriForFile(
                activity, activity.getPackageName() + ".fileprovider",
                file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        activity.startActivityForResult(openCameraIntent, REQUEST_CODE_TAKE_PICTURE);
    }

}
