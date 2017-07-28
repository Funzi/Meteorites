package cz.pribula.meteorites;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity  {

    private static final String FRAGMENT_METEORITE_TAG = "fragment_meteorite";
    private static final String FRAGMENT_MAP_TAG = "fragment_map";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment frag = new MeteoriteFragment().newInstance("", "");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, frag, FRAGMENT_METEORITE_TAG).commit();
        setContentView(R.layout.activity_main);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name(Realm.DEFAULT_REALM_NAME)
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_update: ((MeteoriteFragment) getFragmentManager().findFragmentByTag(FRAGMENT_METEORITE_TAG)).updateMeteorites();
                                        return true;
            case R.id.menu_item_about: return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void addMapFragment(Fragment mapFragment) {
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, mapFragment,FRAGMENT_MAP_TAG);
        fragmentTransaction.addToBackStack(null).commit();
    }

    public void removeFragment() {
        FragmentManager fm = getFragmentManager();
        Fragment newFragment = fm.findFragmentByTag(FRAGMENT_MAP_TAG);

        if(newFragment != null) {
            fm.beginTransaction().remove(newFragment).commit();
            fm.popBackStack();
        }
        fm.executePendingTransactions();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().findFragmentByTag(FRAGMENT_MAP_TAG)!=null) {
            removeFragment();
        } else {
            super.onBackPressed();
        }
    }
}
