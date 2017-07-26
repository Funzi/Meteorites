package cz.pribula.meteorites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeteoriteFragment extends android.app.Fragment {

    Button mButton;
    NasaClientImpl client;

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
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meteorite, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mButton = (Button) view.findViewById(R.id.retrofitButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<List<Meteorite>> call = client.getAllMeteorites();
                call.enqueue(new Callback<List<Meteorite>>() {
                    @Override
                    public void onResponse(Call<List<Meteorite>> call, Response<List<Meteorite>> response) {

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
