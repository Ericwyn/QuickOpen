package com.ericwyn.quickopen;


import android.content.Intent;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class HomeService extends TileService {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onClick() {
        getQsTile().setState(Tile.STATE_INACTIVE);
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }

    @Override
    public void onTileAdded() {
        getQsTile().setState(Tile.STATE_INACTIVE);
    }
}
