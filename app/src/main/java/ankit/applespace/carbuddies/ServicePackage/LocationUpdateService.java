package ankit.applespace.carbuddies.ServicePackage;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;

/**
 * Created by ankit on 10/12/17.
 */

public class LocationUpdateService extends IntentService implements LocationListener {


    private static final String TAG = "Ankit";
    LocationManager locationManager;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    public LocationUpdateService(String name) {
        super(name);
        Log.i(TAG, "LocationUpdateService: ");
    }


    public LocationUpdateService() {
        super("LocationUpdateService");
        Log.i(TAG, "LocationUpdateService1: ");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        Log.i(TAG, "onHandleIntent: ");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getLocation();
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.i(TAG, "onLocationChanged: ");
        Toast.makeText(this, "onLocationChanged", Toast.LENGTH_LONG).show();
    }

    public void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location!=null){
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            Log.i(TAG, "Latitude: "+lat);
            Log.i(TAG, "Longtitude: "+lng);
//            Toast.makeText(this, "dfd"+lat, Toast.LENGTH_SHORT).show();

        }



    }

}
