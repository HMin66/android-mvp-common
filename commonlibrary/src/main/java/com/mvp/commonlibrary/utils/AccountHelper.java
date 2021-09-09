package com.mvp.commonlibrary.utils;

import android.app.Application;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.mvp.commonlibrary.router.path.ARouterPath;

/**
 * 描述：
 * 作者： 天天童话丶
 * 时间： 2018/12/15.
 */

public class AccountHelper {

    private static AccountHelper mAccountHelper;
    private static final String TOKEN = "Token";
    private static final String XGTOKEN = "XgToken";

    private ACache mACache;
    private String mToken;
    private String mXgToken;
    private boolean isTokenInvalid = false;
    private boolean isRefresh = false;
    private User mUser;

    private AccountHelper(Application application){
        this.mACache = ACache.get(application);
        this.mToken = mACache.getAsString(TOKEN);
        this.mXgToken = mACache.getAsString(XGTOKEN);
        if (isLogin()){
            mUser = new User(mACache.getAsString(User.username),
                    mACache.getAsString(User.userheadimg),
                    mACache.getAsString(User.userphone),
                    mACache.getAsString(User.userstate));
        }
    }

    public static AccountHelper init(Application application){
        if (mAccountHelper == null){
            synchronized (AccountHelper.class){
                if (mAccountHelper == null){
                    mAccountHelper = new AccountHelper(application);
                }
            }
        }

        return mAccountHelper;
    }

    public static AccountHelper getInstance(){
        return mAccountHelper;
    }

    public boolean isLogin(){
        return !TextUtils.isEmpty(mToken);
    }

    public String getXgToken() {
        return mXgToken;
    }

    public void setXgToken(String xgToken) {
        if (TextUtils.isEmpty(xgToken)){
            return;
        }
        this.mXgToken = xgToken;
        this.mACache.put(XGTOKEN, xgToken);
    }

    /**
     * 设置一次性token
     * @param token
     */
    public void setOneToken(String token){
        if (TextUtils.isEmpty(token)){
            return;
        }
        this.isTokenInvalid = false;
        this.mToken = token;
        this.mUser = new User();
    }

    public void setOneUserId(String userId){
        if (!TextUtils.isEmpty(userId) && mUser != null){
            mUser.setUserId(userId);
        }
    }

    public void setOneUserState(int userState){
        if (mUser != null){
            mUser.setUserState(userState + "");
        }
    }

    public void setToken(String token){
        if (TextUtils.isEmpty(token)){
            return;
        }
        this.isTokenInvalid = false;
        this.mToken = token;
        this.mACache.put(TOKEN, token);
        if (this.mUser == null) this.mUser = new User();
    }

    public String getToken(){
        return mToken;
    }

    public void setTokenInvalid(){
        this.isTokenInvalid = true;
        this.isRefresh = true;
        ARouter.getInstance().build(ARouterPath.LaunchActivity).navigation();
    }

    public void exitLogin(){
        this.mToken = "";
        this.mUser = null;
        this.mACache.put(TOKEN, mToken);
        this.mACache.put(User.username, "");
        this.mACache.put(User.userheadimg, "");
        this.mACache.put(User.userphone, "");
        this.mACache.clear();
    }

    public boolean isTokenInvalid() {
        return isTokenInvalid;
    }

    public boolean isRefresh(){ return isRefresh;}

    private class User{
        public static final String username = "username";
        public static final String userheadimg = "userheadimg";
        public static final String userphone = "userphone";
        public static final String userid = "userid";
        public static final String userstate = "userstate";

        private String userName;
        private String userHeadImg;
        private String userPhone;
        private String userId;
        private String userState; //0、未激活 1、已激活未认证 2、已认证（空闲中，未接单）3、已分配运单（未接单） 4、接单中 10、认证未通过 12、重新申请 20、黑名单

        public User(){}

        public User(String userName, String userHeadImg, String userPhone, String userState) {
            this.userName = userName;
            this.userHeadImg = userHeadImg;
            this.userPhone = userPhone;
            this.userState = userState;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserHeadImg() {
            return userHeadImg;
        }

        public void setUserHeadImg(String userHeadImg) {
            this.userHeadImg = userHeadImg;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserState() {
            return userState;
        }

        public void setUserState(String userState) {
            this.userState = userState;
        }
    }

    public String getUserId(){
        if (mUser == null) return "";
        return mUser.getUserId() == null ? "" : mUser.getUserId();
    }

    public String getUserName(){
        if (mUser == null) return "";
        return mUser.getUserName() == null ? "" : mUser.getUserName();
    }

    public String getUserHeadImg(){
        if (mUser == null) return "";
        return mUser.getUserHeadImg() == null ? "" : mUser.getUserHeadImg();
    }

    public String getUserPhone(){
        if (mUser == null) return "";
        return mUser.getUserPhone() == null ? "" : mUser.getUserPhone();
    }

    public String getUserState(){
        if (mUser == null) return "";
        return mUser.getUserState();
    }

    public void setUserData(String userName, String userHeadImg, String userPhone){
        if (!TextUtils.isEmpty(userName)){
            mUser.setUserName(userName);
            mACache.put(User.username, userName);
        }

        if (!TextUtils.isEmpty(userHeadImg)){
            mUser.setUserHeadImg(userHeadImg);
            mACache.put(User.userheadimg, userHeadImg);
        }

        if (!TextUtils.isEmpty(userPhone)){
            mUser.setUserPhone(userPhone);
            mACache.put(User.userphone, userPhone);
        }
    }

    public void setUserName(String userName){
        if (!TextUtils.isEmpty(userName) && mUser != null){
            mUser.setUserName(userName);
            mACache.put(User.username, userName);
        }
    }

    public void setUserId(String userId){
        if (!TextUtils.isEmpty(userId) && mUser != null){
            mUser.setUserId(userId);
            mACache.put(User.userid, userId);
        }
    }

    public void setUserState(int userState){
        if (mUser != null){
            mUser.setUserState(userState + "");
            mACache.put(User.userstate, userState + "");
        }
    }

    public void setUserHeadImg(String avatar){
        if (mUser != null){
            mUser.setUserHeadImg(TextUtils.isEmpty(avatar) ? "" : avatar);
            mACache.put(User.userheadimg, TextUtils.isEmpty(avatar) ? "" : avatar);
        }
    }

}
