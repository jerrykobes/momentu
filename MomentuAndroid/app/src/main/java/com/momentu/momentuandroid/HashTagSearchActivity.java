package com.momentu.momentuandroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.momentu.momentuandroid.Adapter.CardPagerAdapter;
import com.momentu.momentuandroid.Animation.ShadowTransformer;
import com.momentu.momentuandroid.Fragment.BaseFragment;
import com.momentu.momentuandroid.Fragment.CommandFromActivity;
import com.momentu.momentuandroid.Fragment.SlidingSearchResultsFragment;
import com.momentu.momentuandroid.Model.TrendHashTagCard;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by akara on 11/4/2017.
 */

public class HashTagSearchActivity extends AppCompatActivity implements BaseFragment.BaseExampleFragmentCallbacks, NavigationView.OnNavigationItemSelectedListener {

    /* TrendHashTag ViewPager */
    private ViewPager mViewPager;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private String[] cityWideHashTags = new String[6];
    private String[] stateWideHashTags = new String[6];
    private String[] nationWideHashTags = new String[6];

    /* Search Fragment */
    private Fragment currentFragment;

    /* Location */
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private LocationManager mLocationManager = null;
    private List<Address> mAddresses;
    private String mCityName;
    private String mStateName;
    private String mCountryName;
    private Location mLocation;

    /* Camera */
    public static final int CAMERA_REQUEST = 1888;
    private static final int LOCATION_INTERVAL = 1000;
    private static final int LOCATION_DISTANCE = 10;
    private final String TAG = "HashTagSearchActivity";
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private DrawerLayout mDrawerLayout;
    private TextView mWhereAmI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hashtag_search);
//        Intent intent = getIntent();
//        String token = intent.getStringExtra("token");
//        token = token.split(":")[1].split(",")[0];
//        Log.d("SearchPage", "" +token);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mWhereAmI = (TextView) findViewById(R.id.where_am_i);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        showTrendHashtagPager();

        showSearchFragment(new SlidingSearchResultsFragment());

        initializeLocationManager();
//        checkLocation();

        //Take picture/video
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA},
                HashTagSearchActivity.CAMERA_REQUEST);

        ImageButton cameraButton = (ImageButton) this.findViewById(R.id.bCamera);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });
    }

    ;

    @Override
    public void onAttachSearchViewToDrawer(FloatingSearchView searchView) {
        searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.log_out:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.ask_log_out)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Toast.makeText(HashTagSearchActivity.this, "You have been successfully logged out",
                                        Toast.LENGTH_LONG).show();
                                Intent logout = new Intent(HashTagSearchActivity.this, WelcomeActivity.class);
                                finish();
                                startActivity(logout);
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

                return true;
            default:
                return true;
        }
    }

    @Override
    public void onBackPressed() {
        @SuppressLint("RestrictedApi") List fragments = getSupportFragmentManager().getFragments();
        BaseFragment currentFragment = (BaseFragment) fragments.get(fragments.size() - 1);

        if (!currentFragment.onActivityBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            Log.i(TAG, "Received response for Location permission request.");
            // Check if the only required permission has been granted
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission has been granted, preview can be displayed
                Log.i(TAG, "Location permission has now been granted.");
                try {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                            mLocationListeners[0]);
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                } catch (java.lang.SecurityException ex) {
                    Log.i(TAG, "fail to request location update, ignore", ex);
                } catch (IllegalArgumentException ex) {
                    Log.d(TAG, "gps provider does not exist " + ex.getMessage());
                }
                showLocation(mLocation);
            } else {
                Log.i(TAG, "Location permission was NOT granted.");
            }
        }
    }

    //Trend Hashtag pager
    private void showTrendHashtagPager() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);

        mCardAdapter = new CardPagerAdapter();

        //TODO: Hard Code!
        cityWideHashTags[0] = "#Sixers";
        cityWideHashTags[1] = "#anniversary";
        cityWideHashTags[2] = "#Supernatural";
        cityWideHashTags[3] = "#scnews";
        cityWideHashTags[4] = "#AOMG";
        cityWideHashTags[5] = "#Scandal";

        stateWideHashTags[0] = "#AOMG";
        stateWideHashTags[1] = "#anniversary";
        stateWideHashTags[2] = "#AnOpenSecret";
        stateWideHashTags[3] = "#Scandal";
        stateWideHashTags[4] = "#Sixers";
        stateWideHashTags[5] = "#scnews";

        nationWideHashTags[0] = "#AllStars3";
        nationWideHashTags[1] = "#Scandal";
        nationWideHashTags[2] = "#Supernatural";
        nationWideHashTags[3] = "#AppleMichiganAve";
        nationWideHashTags[4] = "#AOMG";
        nationWideHashTags[5] = "#anniversary";

        mCardAdapter.addCardItem(new TrendHashTagCard(cityWideHashTags));
        mCardAdapter.addCardItem(new TrendHashTagCard(stateWideHashTags));
        mCardAdapter.addCardItem(new TrendHashTagCard(nationWideHashTags));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
        mCardShadowTransformer.enableScaling(true);
    }

    // Create Fragment
    private void showSearchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment).commit();
        currentFragment = fragment;
    }

    // Location
    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void checkLocation() {
        // Check location permission
        if (ActivityCompat.checkSelfPermission(HashTagSearchActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("Permission_Check", "Bad!");

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(HashTagSearchActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d("Permission_Check", "Rationale");
                new AlertDialog.Builder(this)
                        .setTitle(R.string.title_location_permission)
                        .setMessage(R.string.text_location_permission)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(HashTagSearchActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                Log.d("Permission_Check", "No explanation");
                ActivityCompat.requestPermissions(HashTagSearchActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            Log.d("Permission_Check", "Good!");
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                        mLocationListeners[0]);
                mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            } catch (java.lang.SecurityException ex) {
                Log.i(TAG, "fail to request location update, ignore", ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
            showLocation(mLocation);
        }
    }

    private void showLocation(Location location) {
        String mLocationName;
        if (location == null) {
            mLocationName = "Unknown";
        } else {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            try {
                mAddresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (mAddresses != null & mAddresses.size() > 0) {
                mCityName = mAddresses.get(0).getLocality();
                mStateName = mAddresses.get(0).getAdminArea();
                mCountryName = mAddresses.get(0).getCountryName();
                mLocationName = ((mCityName == null) ? "" : mCityName) + " " +
                        ((mStateName == null) ? "" : mStateName) + " " +
                        ((mCountryName == null) ? "" : mCountryName);
            } else {
                mLocationName = "Unknown";
            }
        }

        mWhereAmI.setText(((mCityName == null) ? "Where am I" : mCityName));
        Toast.makeText(getBaseContext(), "Current location:" + mLocationName,
                Toast.LENGTH_LONG).show();
    }

    public void clickToSearch(View view) {
        String mTrendingHastTag = ((Button) view).getText().toString();
//        Toast.makeText(getBaseContext(), "Clicked button " + mTrendingHastTag,
//                Toast.LENGTH_SHORT).show();
        //TODO: should pass the data to the feed preview page.
        SlidingSearchResultsFragment fragment = (SlidingSearchResultsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        fragment.injectHashTag(mTrendingHastTag);
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            checkLocation();
        }

        @Override
        public void onProviderDisabled(String provider) {
            //if the provider is disabled, then open setting.
            Log.e(TAG, "onProviderDisabled: " + provider);
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
}
