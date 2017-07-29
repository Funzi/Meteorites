package cz.pribula.meteorites.map;

import android.os.Bundle;

import com.google.android.gms.maps.MapFragment;


public class MapFragmentWithRetainedState extends MapFragment {

    public static MapFragmentWithRetainedState newInstance() {
        return new MapFragmentWithRetainedState();
    }

    @Override
    public void onCreate(Bundle bundle) {
        setRetainInstance(true);
        super.onCreate(bundle);
    }
}
