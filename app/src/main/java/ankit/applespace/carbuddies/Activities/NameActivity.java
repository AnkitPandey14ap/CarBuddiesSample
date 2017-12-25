package ankit.applespace.carbuddies.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import ankit.applespace.carbuddies.R;
import at.markushi.ui.CircleButton;

public class NameActivity extends Activity {

    EditText userName;
    CircleButton next;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);

        userName = findViewById(R.id.userName);
        next = findViewById(R.id.next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name=userName.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    userName.setError("Enter your name");
                    Toast.makeText(NameActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
                    userName.requestFocus();
                } else {
                    Intent intent = new Intent(NameActivity.this, MainActivity.class);
                    intent.putExtra("USER", name);
                    sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("NAME", name);
                    editor.commit();



                    startActivity(intent);
                    finish();
                }


            }
        });
    }

    @Override
    public void onBackPressed() {
        /*super.onBackPressed();*/
    }
}
