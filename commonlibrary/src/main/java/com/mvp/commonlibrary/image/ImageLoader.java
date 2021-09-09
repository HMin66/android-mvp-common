package com.mvp.commonlibrary.image;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.mvp.commonlibrary.utils.FileUtil;

import java.io.File;

/**
 * Created by aojiaoqiang on 2018/2/7.
 */

public class ImageLoader {

    public static void load(Context context, String url, int defImg, ImageView imgView, Transformation<Bitmap> transformation){
        if (checkUI(context))
            GlideApp.with(context)
                    .load(url)
                    .error(defImg)
                    .placeholder(defImg)
                    .transform(transformation)
                    .into(imgView);
    }

    public static void loadBitmap(Context context, String url, int defImg, ImageView imgView, Transformation<Bitmap> transformation, RequestListener<Bitmap> listener){
        if (checkUI(context))
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
//                    .error(defImg)
//                    .placeholder(defImg)
                    .transform(transformation)
                    .listener(listener)
                    .into(imgView);
    }

    public static void loadBitmap(Context context, String url, int defImg, ImageView imgView, RequestListener<Bitmap> listener){
        if (checkUI(context))
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
//                    .error(defImg)
//                    .placeholder(defImg)
                    .listener(listener)
                    .into(imgView);
    }

    public static void loadGif(Context context, String url, int defImg, ImageView imgView, RequestListener<GifDrawable> listener){
        if (checkUI(context))
            GlideApp.with(context)
                    .asGif()
                    .load(url)
//                    .error(defImg)
//                    .placeholder(defImg)
                    .listener(listener)
                    .into(imgView);
    }

    public static void loadGif(Context context, String url, int defImg, ImageView imgView, Transformation<Bitmap> transformation, RequestListener<GifDrawable> listener){
        if (checkUI(context))
            GlideApp.with(context)
                    .asGif()
                    .load(url)
//                    .error(defImg)
//                    .placeholder(defImg)
                    .transform(transformation)
                    .listener(listener)
                    .into(imgView);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param url
     * @param imgView
     */
    public static void loadCircleBtimap(Context context, String url, ImageView imgView) {
        if (checkUI(context))
            GlideApp.with(context)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imgView);
    }

    /**
     * 加载网络图片
     *
     * @param context
     * @param url
     * @param imgView
     */
    public static void load(Context context, String url, ImageView imgView) {
        if (checkUI(context))
            GlideApp.with(context)
                    .load(url)
                    .into(imgView);
    }

    /**
     *
     *
     * @param context
     * @param url
     * @param angle   旋转角度
     * @param radius  圆角半径
     * @param imgView
     */
    public static void load(Context context, String url, float angle, int radius, ImageView imgView) {
        if (checkUI(context))
            GlideApp.with(context)
                    .load(url)
                    .transform( new MultiTransformation(new RotateTransformation(context, angle),
                            new RoundTransformation(context, radius)))
                    .into(imgView);
    }

    /**
     * 加载本地图片
     *
     * @param context
     * @param obj
     * @param imgView
     */
    public static void load(Context context, File obj, ImageView imgView) {
        if (checkUI(context))
            GlideApp.with(context)
                    .load(obj)
                    .into(imgView);
    }

    /**
     * 加载本地图片
     *
     * @param context
     * @param obj
     * @param imgView
     */
    public static void load(Context context, File obj, int defImg, ImageView imgView) {
        if (checkUI(context))
            GlideApp.with(context)
                    .load(obj)
                    .error(defImg)
                    .placeholder(defImg)
                    .into(imgView);
    }

    public static void load(Context context, Object path, ImageView imgView) {
        if (checkUI(context))
            GlideApp.with(context)
                    .load(path)
                    .into(imgView);
    }


    public static void load(Fragment fragment, int rid, ImageView imgView) {
        if (checkUI(fragment.getActivity()))
            GlideApp.with(fragment)
                    .load(rid)
                    .centerCrop()
                    .into(imgView);
    }

    /**
     * @param context
     * @param url
     * @param defImg  加载中和加载失败的图片
     * @param imgView
     */
    public static void load(Context context, String url, int defImg, ImageView imgView) {
        if (checkUI(context))
            GlideApp.with(context)
                    .load(url)
                    .centerCrop()
                    .error(defImg)
                    .placeholder(defImg)
                    .into(imgView);
    }

    /**
     * @param context
     * @param defImg  加载中和加载失败的图片
     * @param imgView
     */
    public static void load(Context context, int resourceId, int defImg, ImageView imgView) {
        if (checkUI(context))
            GlideApp.with(context)
                    .load(resourceId)
                    .centerCrop()
                    .error(defImg)
                    .placeholder(defImg)
                    .into(imgView);
    }

    /**
     * @param context
     * @param url
     * @param defImg  加载中和加载失败的图片
     * @param imgView
     */
    public static void load(Context context, String url, int defImg, DiskCacheStrategy strategy, ImageView imgView) {
        if (checkUI(context))
            GlideApp.with(context)
                    .load(url)//
                    .placeholder(defImg)//
                    .error(defImg)//
                    .diskCacheStrategy(strategy)//
                    .into(imgView);
    }

    /**
     * 清除ImageView 的失败图片 否则 setImageBitmap 无效
     *
     * @param context
     * @param view
     */
    public static void clear(Context context, View view) {
        if (checkUI(context))
            GlideApp.with(context).clear(view);
    }

    /**
     * 解决glide异常 You cannot start a load for a destroyed activity
     * @param context
     * @return
     */
    private static boolean checkUI(Context context){
        if (context == null) return false;
        if (context instanceof Activity)
            return !((Activity)context).isDestroyed();
        return true;
    }

    /**
     * 下载到本地
     * @param context 上下文
     * @param url 网络图
     */
    public static void saveImgToLocal(Context context, String url) {
        //如果是网络图片，抠图的结果，需要先保存到本地
        GlideApp.with(context)
                .downloadOnly()
                .load(url)
                .listener(new RequestListener<File>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
                        Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
                        Toast.makeText(context, "下载成功", Toast.LENGTH_SHORT).show();
                        saveToAlbum(context, resource.getAbsolutePath());
                        return false;
                    }
                })
                .preload();
    }

    /**
     * 保存到相册中
     * @param context 上下文
     * @param srcPath 网络图保存到本地的缓存文件路径
     */
    public static void saveToAlbum(Context context, String srcPath) {
        String dcimPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "content";
        FileUtil.createPath(dcimPath);
        File file = new File(dcimPath, "content_" + System.currentTimeMillis() + ".png");
        boolean isCopySuccess = FileUtil.copyFile(srcPath, file.getAbsolutePath());
        if (isCopySuccess) {
            //发送广播通知
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getAbsolutePath())));
            Toast.makeText(context, "图片保存到相册成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "图片保存到相册失败", Toast.LENGTH_SHORT).show();
        }
    }
}
