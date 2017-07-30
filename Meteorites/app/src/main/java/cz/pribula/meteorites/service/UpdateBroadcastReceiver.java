package cz.pribula.meteorites.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import cz.pribula.meteorites.MainActivity;
import cz.pribula.meteorites.MeteoriteFragment;

public class UpdateBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        boolean updated = extras.getBoolean(MainActivity.METEORITES_UPDATED_TAG);
        if(updated) {
            ((MeteoriteFragment) ((MainActivity) context).getFragmentManager().findFragmentByTag("fragment_meteorite")).refreshList();
        }
    }
}


