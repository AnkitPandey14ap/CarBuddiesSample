package ankit.applespace.carbuddies.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import ankit.applespace.carbuddies.JavaPackage.SpClass;
import ankit.applespace.carbuddies.R;


public class CodeActivity extends AppCompatActivity {

    TextView codeTV;
    Button okBtn;
    Button shareBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        codeTV = findViewById(R.id.code);
        okBtn = findViewById(R.id.okBtn);
        shareBtn = findViewById(R.id.shareBtn);


        SpClass spClass = new SpClass(this);

        codeTV.setText(spClass.getValue("CODE"));
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CodeActivity.this, MapsActivity.class));
                finish();

            }
        });
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //TO DO:

            }
        });



    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(CodeActivity.this, CreateActivity.class));
        finish();
    }
}
