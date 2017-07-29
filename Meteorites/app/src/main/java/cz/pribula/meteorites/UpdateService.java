package cz.pribula.meteorites;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmNetworkManager;
import com.google.android.gms.gcm.GcmTaskService;
import com.google.android.gms.gcm.OneoffTask;
import com.google.android.gms.gcm.PeriodicTask;
import com.google.android.gms.gcm.Task;
import com.google.android.gms.gcm.TaskParams;

import java.util.List;

import cz.pribula.meteorites.api.MeteoriteDTO;
import cz.pribula.meteorites.api.NasaClientImpl;
import retrofit2.Call;

public class UpdateService extends GcmTaskService implements UpdateCallback.OnMeteoritesUpdatedListener {

    private static final String TAG = UpdateService.class.getSimpleName();

    public static final String UPDATE_TASK_TAG = "update";

    @Override
    public void onInitializeTasks() {
        //called when app is updated to a new version, reinstalled etc.
        //you have to schedule your repeating tasks again
        super.onInitializeTasks();
    }

    @Override
    public int onRunTask(TaskParams taskParams) {
        Handler h = new Handler(getMainLooper());
        Log.v(TAG, "onRunTask");
        if(taskParams.getTag().equals(UPDATE_TASK_TAG)) {
            h.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(UpdateService.this, "UPDATE", Toast.LENGTH_SHORT).show();
                }
            });
            Call<List<MeteoriteDTO>> call = new NasaClientImpl().getAllMeteoritesFromDate();
            call.enqueue(new UpdateCallback(getApplication(),this));

        }
        return 1;
    }

    public static void scheduleUpdate(Context context) {
        //in this method, single Repeating task is scheduled (the target service that will be called is MyTaskService.class)
        try {
            PeriodicTask periodic = new PeriodicTask.Builder()
                    //specify target service - must extend GcmTaskService
                    .setService(UpdateService.class)
                    //repeat every day
                    .setPeriod(60*60*24)
                    //specify how much earlier the task can be executed (in seconds)
                    .setFlex(100)
                    . setRequiredNetwork(Task.NETWORK_STATE_CONNECTED)
                    //tag that is unique to this task (can be used to cancel task)
                    .setTag(UPDATE_TASK_TAG)
                    //whether the task persists after device reboot
                    .setPersisted(true)
                    //if another task with same tag is already scheduled, replace it with this task
                    .setUpdateCurrent(true)
                    //request that charging must be connected
                    //.setExtras(n)
                    .setRequiresCharging(false)
                    .build();
            GcmNetworkManager.getInstance(context).schedule(periodic);
            Log.v(TAG, "repeating task scheduled");
        } catch (Exception e) {
            Log.e(TAG, "scheduling failed");
            e.printStackTrace();
        }
    }

    public static void scheduleOneOff(Context context) {
        //in this method, single OneOff task is scheduled (the target service that will be called is MyTaskService.class)
        try {
            OneoffTask oneoff = new OneoffTask.Builder()
                    //specify target service - must extend GcmTaskService
                    .setService(UpdateService.class)
                    //tag that is unique to this task (can be used to cancel task)
                    .setTag(UPDATE_TASK_TAG)
                    //executed between 0 - 10s from now
                    .setExecutionWindow(5, 10)
                    //set required network state, this line is optional
                    //.setRequiredNetwork(Task.NETWORK_STATE_ANY)
                    //request that charging must be connected, this line is optional
                    .setRequiresCharging(false)
                    //set some data we want to pass to our task
                    //if another task with same tag is already scheduled, replace it with this task
                    .setUpdateCurrent(true)
                    .build();
            GcmNetworkManager.getInstance(context).schedule(oneoff);
            Log.v(TAG, "oneoff task scheduled");
        } catch (Exception e) {
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
        intent.setAction("cz.pribula.meteorites");
        sendBroadcast(intent);
    }
}
