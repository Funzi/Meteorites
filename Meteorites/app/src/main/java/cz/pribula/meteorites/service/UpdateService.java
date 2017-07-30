package cz.pribula.meteorites.service;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.List;

import cz.pribula.meteorites.MainActivity;
import cz.pribula.meteorites.api.MeteoriteDTO;
import cz.pribula.meteorites.api.NasaClientImpl;
import cz.pribula.meteorites.api.UpdateCallback;
import retrofit2.Call;

public class UpdateService extends GcmTaskService implements UpdateCallback.OnMeteoritesUpdatedListener {

    private static final String TAG = UpdateService.class.getSimpleName();

    public static final String UPDATE_TASK_TAG = "update";

    @Override
    public void onInitializeTasks() {
        //called when app is updated to a new version, reinstalled etc.
        //you have to schedule your repeating tasks again
        scheduleUpdate(UpdateService.this);
        super.onInitializeTasks();
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Call<List<MeteoriteDTO>> call = new NasaClientImpl().getAllMeteoritesFrom2011();
        call.enqueue(new UpdateCallback(getApplication(),this));

        return GcmNetworkManager.RESULT_SUCCESS;
    }

    public static void scheduleUpdate(Context context) {
        final int DAY = 60*60*24;
        final int HOUR = 60*60;
        try {
            PeriodicTask periodic = new PeriodicTask.Builder()
                    .setService(UpdateService.class)
                    .setPeriod(DAY)
                    .setFlex(HOUR)
                    .setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    .setTag(UPDATE_TASK_TAG)
                    .setPersisted(true)
                    .setUpdateCurrent(true)
                    .setRequiresCharging(false)
                    .build();
            GcmNetworkManager.getInstance(context).schedule(periodic);
            Log.v(TAG, "repeating task scheduled");
        } catch (Exception e) {
            Log.e(TAG, "scheduling failed");
            e.printStackTrace();
        }
    }

    public static void cancelUpdate(Context context) {
        GcmNetworkManager
                .getInstance(context)
                .cancelTask(UPDATE_TASK_TAG, UpdateService.class);
    }

    @Override
    public void onMeteoritesUpdated() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.METEORITES_UPDATED_TAG, true);
        intent.setAction(getApplication().getPackageName());
        sendBroadcast(intent);
    }
}
