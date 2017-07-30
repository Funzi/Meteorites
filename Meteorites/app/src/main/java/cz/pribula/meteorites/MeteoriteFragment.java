package cz.pribula.meteorites;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import cz.pribula.meteorites.adapter.MeteoritesAdapter;
import cz.pribula.meteorites.adapter.RealmMeteoritesAdapter;
import cz.pribula.meteorites.api.MeteoriteDTO;
import cz.pribula.meteorites.api.NasaClientImpl;
import cz.pribula.meteorites.api.UpdateCallback;
import cz.pribula.meteorites.db.Meteorite;
import cz.pribula.meteorites.db.RealmController;
import cz.pribula.meteorites.map.CustomMapFragment;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;


public class MeteoriteFragment extends android.app.Fragment implements MeteoritesAdapter.OnAdapterItemClickListener, OnMapReadyCallback, UpdateCallback.OnMeteoritesUpdatedListener {

    private NasaClientImpl client;
    private List<Meteorite> meteorites;
    private MeteoritesAdapter adapter;
    private RecyclerView meteoritesView;
    private GoogleMap map;
    private Meteorite currentMeteorite;

    public MeteoriteFragment() {
        // Required empty public constructor
    }

    public static MeteoriteFragment newInstance() {
        MeteoriteFragment fragment = new MeteoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new NasaClientImpl();
        meteorites = new ArrayList<>();
        adapter = new MeteoritesAdapter(this);
        setRetainInstance(true);
        SharedPreferences sharedPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);

        if(!sharedPref.getBoolean(getString(R.string.app_name), false)) {
            updateMeteorites();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meteorite, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        meteoritesView = (RecyclerView) view.findViewById(R.id.meteorites_list_view);
        meteoritesView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        meteoritesView.addItemDecoration(itemDecoration);
        meteoritesView.setAdapter(adapter);

        /// //realm
        RealmController.with(getActivity()).refresh();
        // get all persisted objects
        // create the helper adapter and notify data set changes
        // changes will be reflected automatically
        setRealmAdapter(RealmController.with(getActivity()).getMeteorites());

        setToolbarTitle();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAdapterItemClick(Meteorite item) {
        addMap(item);
    }

    private void addMap(Meteorite item) {
        CustomMapFragment mapFragment = CustomMapFragment.newInstance(item.getName());
        currentMeteorite = item;
        mapFragment.getMapAsync(this);
        ((MainActivity) getActivity()).addFragment(MainActivity.FragmentType.MAP_FRAGMENT, mapFragment);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        placeMarker(currentMeteorite.getName(), Double.parseDouble(currentMeteorite.getLatitude()), Double.parseDouble(currentMeteorite.getLongitude()));
    }

    private void placeMarker(String title, double lat, double lon) {
        final int ZOOM_WORLD = 1;
        if (map != null) {
            LatLng marker = new LatLng(lat, lon);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, ZOOM_WORLD));
            map.addMarker(new MarkerOptions().title(title).position(marker));
        }
    }

    private void setRealmAdapter(RealmResults<Meteorite> meteoritesResult) {
        RealmController.sortMeteoritesByParameter(meteoritesResult, "mass");
        RealmMeteoritesAdapter realmAdapter = new RealmMeteoritesAdapter(getActivity().getApplicationContext(), meteoritesResult, true);

        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    public void updateMeteorites() {
        Call<List<MeteoriteDTO>> call = client.getAllMeteoritesFrom2011();
        call.enqueue(new UpdateCallback(getActivity().getApplication(),this));
    }

    @Override
    public void onMeteoritesUpdated() {
        setToolbarTitle();
        SharedPreferences sharedPref = getActivity().getPreferences(getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.app_name), true);
        editor.commit();
        adapter.notifyDataSetChanged();
    }

    private void setToolbarTitle() {
        getActivity().setTitle(MainActivity.APP_TITLE + " (" + adapter.getItemCount()+")");
    }

    public void refreshList() {
        adapter.notifyDataSetChanged();
    }
}
