package com.ericwyn.quickopen;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class QuickOpenService extends TileService {

    private String appName = "com.noname81.lmt";
    private PackageManager packageManager;
    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
//        packageManager = getPackageManager();
    }

    @Override
    public void onClick() {
        getQsTile().setState(Tile.STATE_INACTIVE);
        if (packageManager == null){
            if (context != null){
                packageManager = context.getPackageManager();
                Intent launchIntentForPackage = packageManager.getLaunchIntentForPackage(appName);
                startActivity(launchIntentForPackage);
            }
        }

    }

    @Override
    public void onTileAdded() {
        getQsTile().setState(Tile.STATE_INACTIVE);
    }
}
