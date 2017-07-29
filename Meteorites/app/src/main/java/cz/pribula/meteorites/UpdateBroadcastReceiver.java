package cz.pribula.meteorites;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class UpdateBroadcastReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        boolean updated = extras.getBoolean(MainActivity.METEORITES_UPDATED_TAG);
        if(updated) {
            ((MeteoriteFragment) ((MainActivity) context).getFragmentManager().findFragmentByTag("fragment_meteorite")).adapter.notifyDataSetChanged();
        }
    }
}


