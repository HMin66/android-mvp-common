package com.hmin66.commonlibrary.permission;

import java.util.List;

/**
 * 描述： 授予权限监听
 * 作者： 天天童话丶
 * 时间： 2018/12/13.
 */

public interface GrantedPermissionListener {

    void onGrantedPermission(List<String> permissions);
}
