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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import ankit.applespace.carbuddies.JavaPackage.SpClass;
import ankit.applespace.carbuddies.Model.User;
import ankit.applespace.carbuddies.R;
import mehdi.sakout.fancybuttons.FancyButton;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FancyButton createBtn;
    private FancyButton joinBtn;
    private FancyButton continueBtn;
    private TextView usernameTV;
    private String TAG="Ankit";
    private ProgressBar progressBar;

    SpClass spClass;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Codes");

    LocationManager locationManager;
    private double lat;
    private double lng;
    private ShareActionProvider mShareActionProvider;
    private Random random;
    private String code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        spClass = new SpClass(this);

        /*
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTVNav = headerView.findViewById(R.id.usernameTV);
        usernameTVNav.setText(spClass.getValue("NAME"));*/


        usernameTV = findViewById(R.id.usernameTV);
        usernameTV.setText("Hello, "+spClass.getValue("NAME")+"!");

        //it will be used only one time to get the user's Name
        final String userName = spClass.getValue("NAME");
        if (userName.equals("null")) {
            startActivity(new Intent(MainActivity.this, NameActivity.class));
            finish();
        }


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        createBtn = findViewById(R.id.createBtn);
        joinBtn = findViewById(R.id.joinBtn);
        continueBtn = findViewById(R.id.continueBtn);


        final TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        //Log.d("Ankit", "Android ID: " + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    1);
//            Toast.makeText(this, "permission issue", Toast.LENGTH_SHORT).show();


            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1);

            return;
        }



        //Log.d("Ankit", "Device ID : " + tm.getDeviceId());
        spClass.setValue("ID", tm.getDeviceId());

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE},
                            1);
//                    Toast.makeText(CreateActivity.this, "Permission issue", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Show progress bar untill background work is done
                progressBar = findViewById(R.id.progressBar);
                setProgressBarVisible();

                random = new Random();
                code = String.format("%04d", random.nextInt(10000));




                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        while(dataSnapshot.child(code).exists()) {
                            code = String.format("%04d", random.nextInt(10000));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                        Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });










                getLocation();

                final boolean[] flag = {true};
                //create the code popup when code get generated on firebase
                //final boolean finalFlag = flag[0];
                if(isNetworkAvailable()){

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(flag[0]){
                                flag[0] =false;
                                spClass.setValue("CODE", code);
                                myRef.child(spClass.getValue("CODE")).child(spClass.getValue("ID")).setValue(new User(spClass.getValue("NAME"),lat,lng,spClass.getValue("ID")));
                                myRef.child(spClass.getValue("CODE")).child(spClass.getValue("ID")).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        setProgressBarGone();

                                    }

                                    @Override
                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                                        setProgressBarGone();
                                    }

                                    @Override
                                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                                        setProgressBarGone();
                                    }

                                    @Override
                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        setProgressBarGone();
                                    }
                                });

                                showCodeDialog();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(MainActivity.this, "Network issue", Toast.LENGTH_SHORT).show();
                            setProgressBarGone();
                        }
                    });

                }else{
                    setProgressBarGone();
                    Toast.makeText(MainActivity.this, "Check your Internet Connection", Toast.LENGTH_LONG).show();
                }

            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,JoinActivity.class));
                finish();
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
                finish();
            }
        });

    }

    private void setProgressBarGone() {
        progressBar.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void setProgressBarVisible() {
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    private void showCodeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Share this code with others")
                .setTitle(spClass.getValue("CODE")).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("CODE",spClass.getValue("CODE"));
                        startActivity(intent);
                        finish();

                    }
                })
                .setNegativeButton("Share", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        try {

                            createBtn.setVisibility(View.INVISIBLE);
                            joinBtn.setVisibility(View.INVISIBLE);
                            continueBtn.setVisibility(View.VISIBLE);


                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("text/plain");
                            intent.putExtra(Intent.EXTRA_SUBJECT, "Car Buddies");

                            String code = spClass.getValue("CODE");
                            intent.putExtra(Intent.EXTRA_TEXT, code);
                            intent.putExtra("CODE",code);

                            String shareBody="Hey, I want to share my live location with you! Please install the CAR BUDDIES app so that we can track each other's location with best route between us \nAndroid: \n";
                            shareBody= shareBody + "https://play.google.com/store/apps/details?id=ankit.applespace.carbuddies \n\n";

                            shareBody =shareBody+"If already installed \n";
                            shareBody=shareBody+"http://www.ankit.applespace.carbuddies/login\n\n";

                            shareBody=shareBody+"Start app, go to JOIN GROUP and enter the code \""+spClass.getValue("CODE")+"\"";

                            intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(intent, "choose one"));
                        } catch(Exception e) {
                            Log.i(TAG, "onClick: "+"somthing went wrong");
                        }


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
            ActivityCompat.requestPermissions(MainActivity.this,
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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

//            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.shareButton);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
//        String shareBody = "\nLet me recommend you this application\n\n";
//        shareBody = shareBody+"http://www.my.app.com/launch"+"https://play.google.com/store/apps/details?id=ankit.applespace.carbuddies \n\n";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Car Buddies");

//        shareBody =shareBody+ "http://www.ankit.applespace.carbuddies/login";

        String shareBody = "Now don't need to bother if you lost your friend's vehicle while driving just see their exact location in CAR BUDDIES app, if they are nearby or not \n\nThe best app to share your real-time location with friends while driving, where all your friends/family can see each other's location at the same time \n\nInstall the Android app \n";
        shareBody= shareBody + "https://play.google.com/store/apps/details?id=ankit.applespace.carbuddies \n\n";

/*
        shareBody=shareBody+"if Android app is already installed \n";
        shareBody=shareBody+"http://www.ankit.applespace.carbuddies/login";
*/

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);

        //then set the sharingIntent
        mShareActionProvider.setShareIntent(sharingIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Car Buddies");

                String shareBody = "Now don't need to bother if you lost your friend's vehicle while driving just see in CAR BUDDIES app their exact location, if they are near or not \n\n\nThe best app to share your real-time location with friends while driving, where all of your friends can see each others location at the same time \n\nInstall the Android app \n";
                shareBody= shareBody + "https://play.google.com/store/apps/details?id=ankit.applespace.carbuddies \n\n";

                i.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                Toast.makeText(this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "Car Buddies");

                String shareBody = "Now don't need to bother if you lost your friend's vehicle on the road while you all are going somewhere by different vehicles, just see their exact location in CAR BUDDIES app, if they are near by or not \n\nThe best app to share your real-time location with friends while driving, where all your friends/family can see each others location at the same time \n\nInstall the Android app \n";
                shareBody= shareBody + "https://play.google.com/store/apps/details?id=ankit.applespace.carbuddies \n\n";


                i.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch(Exception e) {
                Toast.makeText(this, "Somthing went wrong", Toast.LENGTH_SHORT).show();;
            }

        } else if (id == R.id.nav_send) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"3applespace@gmail.com"});
//        i.putExtra(Intent.EXTRA_EMAIL  , arrayList);
            i.putExtra(Intent.EXTRA_SUBJECT, "Car Buddies Feedback");
//            i.putExtra(Intent.EXTRA_TEXT   , feedbackEditText.getText().toString());
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
