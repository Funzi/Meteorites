package cz.pribula.meteorites.api;


import java.util.List;

import cz.pribula.meteorites.MeteoriteDTO;
import retrofit2.Call;
import retrofit2.http.GET;

public interface NasaClient {

    @GET("resource/y77d-th95.json/")
    Call<List<MeteoriteDTO>> getAllMeteorites();

    @GET("resource/y77d-th95.json/" + "?$where=year>='2011-01-10T0:00:00'")
    Call<List<MeteoriteDTO>> getAllMeteoritesFromDate();
}
