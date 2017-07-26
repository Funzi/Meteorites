package cz.pribula.meteorites;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NasaClient {

    @GET("resource/y77d-th95.json/")
    Call<List<Meteorite>> getAllMeteorites();
}
