package ankit.applespace.carbuddies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import ankit.applespace.carbuddies.JavaPackage.SpClass;
import ankit.applespace.carbuddies.R;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "Ankit";
//    LocationManager locationManager;
    SpClass spClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        spClass = new SpClass(this);
        if(spClass.getValue("CODE")!=null){
            startActivity(new Intent(SplashActivity.this,MapsActivity.class));
            finish();
        }else {
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent=new Intent(SplashActivity.this,CreateActivity.class);
                    //intent.putExtra("NAME", userName);
                    startActivity(intent);
                    finish();
                }
            }, 500);

        }



    }
/*

    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: ");
    }
*/

    /*public void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(SplashActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location!=null){
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            Log.i(TAG, "Latitude: "+lat);
            Log.i(TAG, "Longtitude: "+lng);

        }
    }*/
}
