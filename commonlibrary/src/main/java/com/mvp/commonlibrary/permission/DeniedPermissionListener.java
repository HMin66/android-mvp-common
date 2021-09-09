package com.mvp.commonlibrary.permission;

import java.util.List;

/**
 * 描述： 拒绝权限监听
 * 作者： 天天童话丶
 * 时间： 2018/12/13.
 */

public interface DeniedPermissionListener {

    void onDeniedPermission(List<String> permissions);
}
