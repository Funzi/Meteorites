package cz.pribula.meteorites.api;

import android.app.Application;
import android.widget.Toast;

import java.util.List;

import cz.pribula.meteorites.R;
import cz.pribula.meteorites.db.RealmController;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class UpdateCallback implements Callback<List<MeteoriteDTO>> {

    private OnMeteoritesUpdatedListener listener;
    private Application app;

    public UpdateCallback(Application app, OnMeteoritesUpdatedListener listener) {
        this.listener = listener;
        this.app = app;
    }

    @Override
    public void onResponse(Call<List<MeteoriteDTO>> call, Response<List<MeteoriteDTO>> response) {
        try {
            //checking if default configuration has been set
            Realm.getDefaultInstance();
        } catch(NullPointerException e) {
            //setting default configuration in case app is not turned on
            RealmController.setRealmConfiguration(app);
        }
        RealmController.with(app).persistData(response.body());
        listener.onMeteoritesUpdated();
    }

    @Override
    public void onFailure(Call<List<MeteoriteDTO>> call, Throwable t) {
        Toast.makeText(app, R.string.connection_failed_toast, Toast.LENGTH_LONG).show();
    }

    public interface OnMeteoritesUpdatedListener {
        void onMeteoritesUpdated();
    }
}
