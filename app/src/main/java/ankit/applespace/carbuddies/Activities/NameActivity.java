package ankit.applespace.carbuddies.Activities;

import android.Manifest;
import android.app.Activity;

import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ankit.applespace.carbuddies.JavaPackage.SpClass;
import ankit.applespace.carbuddies.Model.Name;
import ankit.applespace.carbuddies.Model.User;
import ankit.applespace.carbuddies.R;
import at.markushi.ui.CircleButton;

public class NameActivity extends Activity {

    EditText userName;
    CircleButton next;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");
    private SpClass spClass;
    private ProgressBar progressBar;

    TelephonyManager tm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        spClass = new SpClass(this);
        userName = findViewById(R.id.userName);
        next = findViewById(R.id.next);
        progressBar = findViewById(R.id.progressBar2);


//get the device ID and save on sharedprefrence
        tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(NameActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE},
                    1);

            return;


        }

    }

    void saveUserNameFB(final String name) {


        final boolean[] flag = {true};
        if (isNetworkAvailable()) {


//            myRef.setValue(name+" "+spClass.getValue("ID"));
            myRef.child(name+" "+spClass.getValue("ID")).setValue(new Name(name, spClass.getValue("ID")));
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    spClass.setValue("NAME", name);
                    setProgressBarGone();
                    startActivity(new Intent(NameActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(NameActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            setProgressBarGone();
            Toast.makeText(NameActivity.this, "Check your Internet Connection", Toast.LENGTH_LONG).show();
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        assert tm != null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(this, "Permission Issue", Toast.LENGTH_SHORT).show();
            return;
        }
        spClass.setValue("ID", tm.getDeviceId());

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=userName.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    userName.setError("Enter your name");
                    Toast.makeText(NameActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
                    userName.requestFocus();
                } else {
//                    progressBar.setVisibility(View.VISIBLE);
                    setProgressBarVisible();
                    saveUserNameFB(name);
                }


            }
        });
    }



}
