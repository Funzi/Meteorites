package cz.pribula.meteorites;

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

import cz.pribula.meteorites.api.NasaClientImpl;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeteoriteFragment extends android.app.Fragment {

    Button button;
    NasaClientImpl client;
    List<Meteorite> meteorites;
    SimpleAdapter adapter;
    RecyclerView meteoritesView;

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
        adapter = new SimpleAdapter(meteorites);
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        button = (Button) view.findViewById(R.id.retrofitButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<Meteorite>> call = client.getAllMeteorites();
                call.enqueue(new Callback<List<Meteorite>>() {
                    @Override
                    public void onResponse(Call<List<Meteorite>> call, Response<List<Meteorite>> response) {
                        meteorites.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Call<List<Meteorite>> call, Throwable t) {

                    }
                });
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
