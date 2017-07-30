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
        if(updated && context instanceof MainActivity) {
            MeteoriteFragment fragment = ((MeteoriteFragment) ((MainActivity) context).getFragmentManager().findFragmentByTag(MainActivity.FRAGMENT_METEORITE_TAG));
            if(fragment != null) { fragment.refreshList();}
        }
    }
}


