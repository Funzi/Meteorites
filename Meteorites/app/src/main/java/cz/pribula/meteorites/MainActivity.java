package cz.pribula.meteorites;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.maps.MapFragment;

import cz.pribula.meteorites.db.RealmController;

public class MainActivity extends AppCompatActivity {

    public enum FragmentType {NO_FRAGMENT, MAP_FRAGMENT, ABOUT_FRAGMENT}

    private static final String FRAGMENT_METEORITE_TAG = "fragment_meteorite";
    private static final String FRAGMENT_MAP_TAG = "fragment_map";
    private static final String FRAGMENT_TYPE_TAG = "fragment_type";

    private FragmentType currentFragmentType;

    public static final String APP_TITLE = "Meteorites";
    public static final String METEORITES_UPDATED_TAG = "updated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fragment frag = new MeteoriteFragment().newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, frag, FRAGMENT_METEORITE_TAG).commit();
        currentFragmentType = FragmentType.NO_FRAGMENT;

        RealmController.setRealmConfiguration(getApplication());

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cz.pribula.meteorites");
        UpdateBroadcastReceiver receiver = new UpdateBroadcastReceiver();
        registerReceiver(receiver, intentFilter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(FRAGMENT_TYPE_TAG, currentFragmentType);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currentFragmentType = (FragmentType) savedInstanceState.getSerializable(FRAGMENT_TYPE_TAG);
        if (currentFragmentType != FragmentType.NO_FRAGMENT) {
            Fragment addedFragment = getFragmentManager().findFragmentByTag(FRAGMENT_MAP_TAG);
            if (addedFragment == null) {
                addFragment(currentFragmentType);
            } else {
                addFragment(currentFragmentType, addedFragment);
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void addFragment(FragmentType fragmentType) {
        addFragment(fragmentType, getFragmentByType(fragmentType));
    }

    public void addFragment(FragmentType fragmentType, Fragment fragment) {
        if (fragmentType == FragmentType.NO_FRAGMENT) {
            return;
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment, FRAGMENT_MAP_TAG);
        ft.addToBackStack(null).commit();
        this.currentFragmentType = fragmentType;
    }

    private Fragment getFragmentByType(FragmentType type) {
        switch (type) {
            case NO_FRAGMENT:
                return null;
            case MAP_FRAGMENT:
                return MapFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_update:
                ((MeteoriteFragment) getFragmentManager().findFragmentByTag(FRAGMENT_METEORITE_TAG)).updateMeteorites();
                return true;
            case R.id.menu_item_about:
                UpdateService.scheduleOneOff(this);
                return true;
            case R.id.menu_item_clear:
                RealmController.with(getApplication()).clearAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void removeFragment() {
        FragmentManager fm = getFragmentManager();
        Fragment newFragment = fm.findFragmentByTag(FRAGMENT_MAP_TAG);

        if (newFragment != null) {
            fm.beginTransaction().remove(newFragment).commit();
            fm.popBackStack();
        }
        fm.executePendingTransactions();
        currentFragmentType = FragmentType.NO_FRAGMENT;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().findFragmentByTag(FRAGMENT_MAP_TAG) != null) {
            removeFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
