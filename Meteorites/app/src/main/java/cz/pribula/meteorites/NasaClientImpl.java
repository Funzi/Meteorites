package cz.pribula.meteorites;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by david on 26-Jul-17.
 */

public class NasaClientImpl {

    String API_BASE_URL = "https://data.nasa.gov/";
    NasaClient client;

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

        client =  retrofit.create(NasaClient.class);
    }

    public Call<List<Meteorite>> getAllMeteorites() {
        return client.getAllMeteorites();
    }
}
