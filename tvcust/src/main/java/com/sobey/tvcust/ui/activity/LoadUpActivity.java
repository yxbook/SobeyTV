package com.sobey.tvcust.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.sobey.tvcust.common.CommonNet;
import com.sobey.tvcust.R;
import com.sobey.tvcust.common.AppData;
import com.sobey.tvcust.entity.User;
import com.sobey.tvcust.utils.AppHelper;

import org.xutils.http.RequestParams;


public class LoadUpActivity extends BaseAppCompatActivity implements CommonNet.NetHander{

    private Handler mHandler = new Handler();

    private String token;
    private boolean startup;
    private User user;

    private long lasttime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadup);

        lasttime = System.currentTimeMillis();

        //测试获取token成功
//        AppData.App.saveToken("xxxx");
        //测试获取token失败
//        AppData.App.removeToken();
        //测试移除startup
//        AppData.App.removeStartUp();

        //获取token
        token = AppData.App.getToken();
        startup = AppHelper.getStartUp();

        if (token == null || "".equals(token)) {
            //无token 等待2秒 去登录页
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goLoginActivity();
                }
            }, 2000);
        } else {
            //有token 执行登录
            login();
        }
    }

    private void login() {
        RequestParams params = new RequestParams(AppData.Url.getInfo);
        params.addHeader("token", token);
        CommonNet.post(this, params, 1, User.class, null);
    }

    private void goLoginActivity() {
        if (startup) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, StartUpActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void goHomeActivity() {
        if (startup) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, StartUpActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void goNext() {
        final Intent intent = new Intent();
        if (AppHelper.getStartUp()) {
            intent.setClass(LoadUpActivity.this, HomeActivity.class);
        } else {
            intent.setClass(LoadUpActivity.this, StartUpActivity.class);
        }
        startActivity(intent);
        finish();
    }

    @Override
    public void netGo(int code, Object pojo, String text, Object obj) {
        User user = (User) pojo;
        AppData.App.removeUser();
        AppData.App.saveUser(user);

        long time = System.currentTimeMillis() - lasttime;
        if (time<2000){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goHomeActivity();
                }
            },2000-time);
        }else {
            goHomeActivity();
        }
    }

    @Override
    public void netStart(int code) {

    }

    @Override
    public void netEnd(int code) {

    }

    @Override
    public void netSetFalse(int code, int status, String text) {

    }

    @Override
    public void netSetFail(int code, int errorCode, String text) {

    }

    @Override
    public void netSetError(int code, String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();

        long time = System.currentTimeMillis() - lasttime;
        if (time<2000){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    goLoginActivity();
                }
            },2000-time);
        }else {
            goLoginActivity();
        }
    }

    @Override
    public void netException(int code, String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}
