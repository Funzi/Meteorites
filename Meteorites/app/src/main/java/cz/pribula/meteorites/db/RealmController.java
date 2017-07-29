package cz.pribula.meteorites.db;


import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController(Application application) {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController with(Fragment fragment) {

        if (instance == null) {
            instance = new RealmController(fragment.getActivity().getApplication());
        }
        return instance;
    }

    public static RealmController with(Activity activity) {

        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {

        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }

    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

    public void refresh() {
        realm.refresh();
    }

    public void clearAll() {
        realm.beginTransaction();
        realm.clear(Meteorite.class);
        realm.commitTransaction();
    }

    public RealmResults<Meteorite> getMeteorites() {
        return realm.where(Meteorite.class).findAll();
    }

    //query a single item with the given id
    public Meteorite getMeteorite(String id) {
        return realm.where(Meteorite.class).equalTo("id", id).findFirst();
    }

    public static void sortMeteoritesByParameter(RealmResults<Meteorite> meteoritesResult, String parameter) {
        meteoritesResult.sort(parameter, RealmResults.SORT_ORDER_DESCENDING);
    }
}
