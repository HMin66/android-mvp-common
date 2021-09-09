package com.mvp.commonlibrary.permission;

import android.app.Activity;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import java.util.List;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/13.
 */

public class PermissionHelper {

    private PermissionHelper(){}

    public static void request(final Activity activity, final GrantedPermissionListener listener, String... permissions){
        AndPermission.with(activity)
                .permission(permissions)
                .rationale(DefaultRationale.getInstance())
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        listener.onGrantedPermission(permissions);
                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                if (AndPermission.hasAlwaysDeniedPermission(activity, permissions)) {
                    PermissionSetting
                            .create(activity)
                            .showSetting(permissions, null);
                }
            }
        }).start();
    }

    public static void request(final Activity activity, final GrantedPermissionListener grantedListener, final DeniedPermissionListener deniedListener, String... permissions){
        AndPermission.with(activity)
                .permission(permissions)
                .rationale(DefaultRationale.getInstance())
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        grantedListener.onGrantedPermission(permissions);
                    }
                }).onDenied(new Action() {
            @Override
            public void onAction(List<String> permissions) {
                if (AndPermission.hasAlwaysDeniedPermission(activity, permissions)) {
                    deniedListener.onDeniedPermission(permissions);
                }
            }
        }).start();
    }
}
