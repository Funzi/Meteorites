package cz.pribula.meteorites.api;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NasaClientImpl implements NasaClient{

    private NasaClient client;

    public NasaClientImpl() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        client = retrofit.create(NasaClient.class);
    }
    @Override
    public Call<List<MeteoriteDTO>> getAllMeteorites() {
        return client.getAllMeteorites();
    }

    @Override
    public Call<List<MeteoriteDTO>> getAllMeteoritesFrom2011() {
        return client.getAllMeteoritesFrom2011();
    }
}
