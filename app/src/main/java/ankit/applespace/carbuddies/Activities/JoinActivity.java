package ankit.applespace.carbuddies.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.raycoarana.codeinputview.CodeInputView;
import com.raycoarana.codeinputview.OnCodeCompleteListener;

import ankit.applespace.carbuddies.JavaPackage.SpClass;
import ankit.applespace.carbuddies.R;

public class JoinActivity extends AppCompatActivity {

    SpClass sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        sp=new SpClass(this);

        final CodeInputView codeInput = findViewById(R.id.codeInput);
        codeInput.addOnCompleteListener(new OnCodeCompleteListener() {
            @Override
            public void onCompleted(String code) {
                //check if submitted code is right or wrong

                if(isNetworkAvailable())
                    isCodeRight(code);
                else{
                    Toast.makeText(JoinActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(JoinActivity.this,JoinActivity.class));
                }



            }
        });


    }

    private void isCodeRight(final String code) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref=ref.child("Codes");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(code).exists()) {
//                    Toast.makeText(JoinActivity.this, "ok", Toast.LENGTH_SHORT).show();
                    sp.setValue("CODE", code);

                    Intent intent = new Intent(JoinActivity.this, MapsActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Toast.makeText(JoinActivity.this, "Invalid CODE", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(JoinActivity.this,JoinActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(JoinActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(JoinActivity.this,MainActivity.class));
        finish();
    }
}
