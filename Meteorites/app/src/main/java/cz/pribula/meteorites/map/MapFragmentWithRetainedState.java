package cz.pribula.meteorites.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;


public class MapFragmentWithRetainedState extends MapFragment {

    private static final String TOOLBAR_TITLE_TAG = "title";

    public static MapFragmentWithRetainedState newInstance(String fragmentTitle) {
        Bundle bundle = new Bundle();
        bundle.putString(TOOLBAR_TITLE_TAG, fragmentTitle);
        MapFragmentWithRetainedState fragment = new MapFragmentWithRetainedState();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle bundle) {
        setRetainInstance(true);
        super.onCreate(bundle);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        String title = getArguments().getString(TOOLBAR_TITLE_TAG);
        getActivity().setTitle(title);
        return super.onCreateView(layoutInflater, viewGroup, bundle);
    }
}
