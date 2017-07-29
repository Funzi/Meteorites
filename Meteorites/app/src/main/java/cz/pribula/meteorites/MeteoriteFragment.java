package cz.pribula.meteorites;

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
import cz.pribula.meteorites.api.NasaClientImpl;
import cz.pribula.meteorites.db.MeteoritePojo;
import cz.pribula.meteorites.db.RealmController;
import cz.pribula.meteorites.map.MapFragmentWithRetainedState;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeteoriteFragment extends android.app.Fragment implements MeteoritesAdapter.OnAdapterItemClickListener, OnMapReadyCallback {

    NasaClientImpl client;
    List<MeteoritePojo> meteorites;
    MeteoritesAdapter adapter;
    RecyclerView meteoritesView;
    GoogleMap map;
    private Realm realm;
    private MeteoritePojo currentMeteorite;

    public MeteoriteFragment() {
        // Required empty public constructor
    }

    public static MeteoriteFragment newInstance(String param1, String param2) {
        MeteoriteFragment fragment = new MeteoriteFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new NasaClientImpl();
        meteorites = new ArrayList<>();
        adapter = new MeteoritesAdapter(getActivity(), this);
        this.realm = RealmController.with(getActivity()).getRealm();
        setRetainInstance(true);
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void persistData(List<Meteorite> meteorites) {
        for (Meteorite m : meteorites) {

            MeteoritePojo newMeteorite = new MeteoritePojo();
            newMeteorite.setId(m.getId());
            newMeteorite.setLatitude(m.getLatitude());
            newMeteorite.setLongitude(m.getLongitude());
            newMeteorite.setMass(m.getMass());
            newMeteorite.setNameType(m.getNameType());
            newMeteorite.setTimestamp(m.getTimestamp());
            newMeteorite.setName(m.getName());
            realm.beginTransaction();
            realm.copyToRealm(newMeteorite);
            realm.commitTransaction();
        }

        // getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE).with(this).setPreLoad(true);
    }

    @Override
    public void onAdapterItemClick(MeteoritePojo item) {
        addMap(item);
    }

    private void addMap(MeteoritePojo item) {
        MapFragmentWithRetainedState mapFragment = MapFragmentWithRetainedState.newInstance();
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

    public void setRealmAdapter(RealmResults<MeteoritePojo> meteoritesResult) {
        RealmController.sortMeteoritesByParameter(meteoritesResult, "mass");
        RealmMeteoritesAdapter realmAdapter = new RealmMeteoritesAdapter(getActivity().getApplicationContext(), meteoritesResult, true);

        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    public void updateMeteorites() {
        Call<List<Meteorite>> call = client.getAllMeteoritesFromDate();
        call.enqueue(new Callback<List<Meteorite>>() {
            @Override
            public void onResponse(Call<List<Meteorite>> call, Response<List<Meteorite>> response) {
                persistData(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Meteorite>> call, Throwable t) {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
