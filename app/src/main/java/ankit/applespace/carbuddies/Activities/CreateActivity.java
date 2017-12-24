package ankit.applespace.carbuddies.Activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import ankit.applespace.carbuddies.JavaPackage.SpClass;
import ankit.applespace.carbuddies.Model.User;
import ankit.applespace.carbuddies.R;

public class CreateActivity extends AppCompatActivity {

    private Button createBtn;
    private Button joinBtn;
    private TextView usernameTV;
    private String TAG="Ankit";

    SpClass spClass;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");

    LocationManager locationManager;
    private double lat;
    private double lng;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ankit.applespace.carbuddies.R.layout.activity_create);

        spClass = new SpClass(this);

        usernameTV = findViewById(R.id.usernameTV);
        usernameTV.setText("Hello, "+spClass.getValue("NAME")+"!");

        //it will be used only one time to get the user's Name
        final String userName = spClass.getValue("NAME");
        if (userName == null) {
            startActivity(new Intent(CreateActivity.this, NameActivity.class));
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        createBtn = findViewById(R.id.createBtn);
        joinBtn = findViewById(R.id.joinBtn);


        final TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        Log.d("Ankit", "Android ID: " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(CreateActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    1);
//            Toast.makeText(this, "permission issue", Toast.LENGTH_SHORT).show();


            return;
        }
        Log.d("Ankit", "Device ID : " + tm.getDeviceId());
        spClass.setValue("ID", tm.getDeviceId());

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(CreateActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(CreateActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            1);
//                    Toast.makeText(CreateActivity.this, "Permission issue", Toast.LENGTH_SHORT).show();
                    return;
                }



                Toast.makeText(CreateActivity.this, "ID: " + tm.getDeviceId(), Toast.LENGTH_SHORT).show();

                Random random = new Random();

                String code = String.format("%04d", random.nextInt(10000));

                Log.i(TAG, "Code: "+code);

                spClass.setValue("CODE", code);

                getLocation();
//                myRef.child(spClass.getValue("CODE")).child(spClass.getValue("NAME")).setValue(new User(spClass.getValue("NAME"),lat,lng));
                myRef.child(spClass.getValue("CODE")).child(spClass.getValue("ID")).setValue(new User(spClass.getValue("NAME"),lat,lng,spClass.getValue("ID")));

//                myRef.setValue("hello");
                Log.i(TAG, "onClick: " + spClass.getValue("CODE") + "," + spClass.getValue("NAME") + " " + spClass.getValue("NAME") + ", " + lat + lng);

                /*Intent intent = new Intent(CreateActivity.this, CodeActivity.class);
                intent.putExtra("CODE",spClass.getValue("CODE"));

                startActivity(intent);

                finish();
*/

                final boolean[] flag = {true};
                //create the code popup when code get generated on firebase
                //final boolean finalFlag = flag[0];


                if(isNetworkAvailable()){

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(flag[0]){

                                flag[0] =false;
//                            startActivity(intent);
//                            finish();

                                showCodeDialog();



                            }


//                        startActivityForResult(intent,0);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(CreateActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
                        }
                    });

                }else
                    Toast.makeText(CreateActivity.this, "Check your Internet Connection", Toast.LENGTH_LONG).show();


            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateActivity.this,JoinActivity.class));
                finish();
            }
        });





    }

    private void showCodeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
        builder.setMessage("Share this code with others")
                .setTitle(spClass.getValue("CODE")).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(CreateActivity.this, MapsActivity.class);
                        intent.putExtra("CODE",spClass.getValue("CODE"));
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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
            ActivityCompat.requestPermissions(CreateActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(location!=null){
            lat = location.getLatitude();
            lng = location.getLongitude();

            Log.i(TAG, "Latitude: "+lat);
            Log.i(TAG, "Longtitude: "+lng);
//            Toast.makeText(this, "dfd"+lat, Toast.LENGTH_SHORT).show();

        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    boolean doubleBackToExitPressedOnce=false;
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }
}
