package cz.pribula.meteorites.api;


import java.util.List;

import cz.pribula.meteorites.Meteorite;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NasaClient {

    @GET("resource/y77d-th95.json/")
    Call<List<Meteorite>> getAllMeteorites();
}
