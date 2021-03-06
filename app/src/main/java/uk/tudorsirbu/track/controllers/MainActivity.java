package uk.tudorsirbu.track.controllers;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import uk.tudorsirbu.track.LocationManager;
import uk.tudorsirbu.track.R;

import static uk.tudorsirbu.track.util.Constants.PREFS_NAME;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, PermissionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(this)
                .check();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (item.getItemId()) {
            case R.id.navigation_home:
                fragmentManager.beginTransaction()
                        .remove(fragmentManager.findFragmentById(R.id.fragment_container))
                        .replace(R.id.fragment_container, new UserMapFragment())
                        .commit();
                return true;
            case R.id.navigation_dashboard:
                fragmentManager.beginTransaction()
                        .remove(fragmentManager.findFragmentById(R.id.fragment_container))
                        .replace(R.id.fragment_container, new JourneysListFragment())
                        .commit();
                return true;
            case R.id.navigation_settings:
                fragmentManager.beginTransaction()
                        .remove(fragmentManager.findFragmentById(R.id.fragment_container))
                        .replace(R.id.fragment_container, new SettingsFragment())
                        .commit();
                return true;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // switch off location tracking when app is not in foreground if background tracking is
        // disabled
        SharedPreferences mSharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean backgroundEnabled = mSharedPrefs.getBoolean(getString(R.string.pref_key_tracking), false);
        if(!backgroundEnabled)
            LocationManager.toggleLocation(this, false);
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {

    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse response) {

    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.permission_required);
        builder.setMessage(R.string.permission_desc);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Dexter.withActivity(MainActivity.this)
                        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .withListener(MainActivity.this)
                        .check();
            }
        });
        builder.show();
    }
}
