package cz.pribula.meteorites.adapter;

import android.content.Context;

import cz.pribula.meteorites.db.MeteoritePojo;
import io.realm.RealmResults;

public class RealmMeteoritesAdapter extends RealmModelAdapter<MeteoritePojo> {

    public RealmMeteoritesAdapter(Context context, RealmResults<MeteoritePojo> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
