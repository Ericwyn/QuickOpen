package com.ericwyn.quickopen;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class QuickOpenService extends TileService {

//    private String appName = "com.noname81.lmt";
    private PackageManager packageManager;
//    private Context context;

    @Override
    public void onCreate() {
//        super.onCreate();
//        this.context = getApplicationContext();
        packageManager = getPackageManager();
    }

    @Override
    public void onClick() {
        if (packageManager == null){
            return;
        }
        getQsTile().setState(Tile.STATE_UNAVAILABLE);

        SharedPreferences sharedPreferences= getSharedPreferences("QuickOpen", Context .MODE_PRIVATE);
        String appName = sharedPreferences.getString("appName", "null");
        if (appName.equals("null")){
            Toast.makeText(getApplicationContext(), "尚未设置启动应用，请长按进入设置页面", Toast.LENGTH_LONG).show();
            return;
        }

        // 新的启动方法
        Intent intent = new Intent(Intent.ACTION_MAIN);//入口Main
        intent.addCategory(Intent.CATEGORY_LAUNCHER);// 启动LAUNCHER,跟MainActivity里面的配置类似
        intent.setPackage(appName);//包名
        //查询要启动的Activity
        List<ResolveInfo> apps = packageManager.queryIntentActivities(intent, 0);
        if (apps.size() > 0) {//如果包名存在
            ResolveInfo ri = apps.get(0);
            // //获取包名
            String packageName = ri.activityInfo.packageName;
            //获取app启动类型
            String className = ri.activityInfo.name;
            //组装包名和类名
            ComponentName cn = new ComponentName(packageName, className);
            //设置给Intent
            intent.setComponent(cn);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //根据包名类型打开Activity
            startActivity(intent);
        } else {
            Toast.makeText(this, "找不到包名;" + appName, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onTileAdded() {
        getQsTile().setState(Tile.STATE_UNAVAILABLE);
    }


}
