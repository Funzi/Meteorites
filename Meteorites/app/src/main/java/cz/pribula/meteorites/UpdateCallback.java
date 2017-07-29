package cz.pribula.meteorites;

import java.util.List;

import cz.pribula.meteorites.api.MeteoriteDTO;
import cz.pribula.meteorites.db.RealmController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class UpdateCallback implements Callback<List<MeteoriteDTO>> {

    OnMeteoritesUpdatedListener listener;

    public UpdateCallback(OnMeteoritesUpdatedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onResponse(Call<List<MeteoriteDTO>> call, Response<List<MeteoriteDTO>> response) {
        RealmController.getInstance().persistData(response.body());
        listener.onMeteoritesUpdated();
    }

    @Override
    public void onFailure(Call<List<MeteoriteDTO>> call, Throwable t) {
    }

    public interface OnMeteoritesUpdatedListener {
        void onMeteoritesUpdated();
    };
}
