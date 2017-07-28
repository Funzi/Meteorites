package cz.pribula.meteorites;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import cz.pribula.meteorites.adapter.MeteoritesAdapter;
import cz.pribula.meteorites.adapter.RealmMeteoritesAdapter;
import cz.pribula.meteorites.api.NasaClientImpl;
import cz.pribula.meteorites.db.MeteoritePojo;
import cz.pribula.meteorites.db.RealmController;
import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeteoriteFragment extends android.app.Fragment implements MeteoritesAdapter.OnAdapterItemClickListener{

    Button button;
    NasaClientImpl client;
    List<MeteoritePojo> meteorites;
    MeteoritesAdapter adapter;
    RecyclerView meteoritesView;
    private Realm realm;

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
        adapter = new MeteoritesAdapter(meteorites,this,getActivity());
        //get realm instance
        this.realm = RealmController.with(getActivity()).getRealm();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meteorite, container, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        meteoritesView = (RecyclerView) view.findViewById(R.id.meteorites_list_view);
        meteoritesView.setLayoutManager(layoutManager);
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        meteoritesView.addItemDecoration(itemDecoration);
        meteoritesView.setAdapter(adapter);
        // refresh the realm instance
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

    private void persistData(List<Meteorite> met) {
        for (Meteorite b : met) {
            // Persist your data easily
            MeteoritePojo newMeteorite = new MeteoritePojo();
            newMeteorite.setId(b.getId());
            newMeteorite.setLatitude(b.getLatitude());
            newMeteorite.setLongitude(b.getLongitude());
            newMeteorite.setMass(b.getMass());
            newMeteorite.setNameType(b.getNameType());
            newMeteorite.setTimestamp(b.getTimestamp());
            newMeteorite.setName(b.getName());
            realm.beginTransaction();
            realm.copyToRealm(newMeteorite);
            realm.commitTransaction();
        }

       // getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE).with(this).setPreLoad(true);
    }
    @Override
    public void onAdapterItemClick(MeteoritePojo item) {
        MapFragment mMapFragment = MapFragment.newInstance();
        GoogleMapOptions options = new GoogleMapOptions();
        options.mapType(GoogleMap.MAP_TYPE_SATELLITE)
                .compassEnabled(false)
                .rotateGesturesEnabled(false)
                .tiltGesturesEnabled(false)
                ;
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mMapFragment);
        fragmentTransaction.commit();
    }

    public void setRealmAdapter(RealmResults<MeteoritePojo> meteoritesResult) {
        RealmMeteoritesAdapter realmAdapter = new RealmMeteoritesAdapter(getActivity().getApplicationContext(), meteoritesResult, true);
        // Set the data and tell the RecyclerView to draw
        adapter.setRealmAdapter(realmAdapter);
        adapter.notifyDataSetChanged();
    }

    public void updateMeteorites() {
        Call<List<Meteorite>> call = client.getAllMeteorites();
        call.enqueue(new Callback<List<Meteorite>>() {
            @Override
            public void onResponse(Call<List<Meteorite>> call, Response<List<Meteorite>> response) {
                persistData(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Meteorite>> call, Throwable t) {
                doSomething();
                adapter.notifyDataSetChanged();
            }

            private void doSomething() {
            }
        });
    }
}
