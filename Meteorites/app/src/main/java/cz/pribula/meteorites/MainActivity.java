package cz.pribula.meteorites;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button;
import io.realm.Realm;
import io.realm.RealmConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment frag = new MeteoriteFragment().newInstance("", "");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, frag, "").commit();
        setContentView(R.layout.activity_main);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
