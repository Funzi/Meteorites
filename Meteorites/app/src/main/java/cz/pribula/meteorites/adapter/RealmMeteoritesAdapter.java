package cz.pribula.meteorites.adapter;

import android.content.Context;

import cz.pribula.meteorites.db.Meteorite;
import io.realm.RealmResults;

public class RealmMeteoritesAdapter extends RealmModelAdapter<Meteorite> {

    public RealmMeteoritesAdapter(Context context, RealmResults<Meteorite> realmResults, boolean automaticUpdate) {

        super(context, realmResults, automaticUpdate);
    }
}
