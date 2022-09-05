package noteme.com.noteme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Home extends AppCompatActivity {

    private ImageButton IBadd;
    private ImageButton IBMyFile, IBProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        IBadd = (ImageButton) findViewById(R.id.IBadd);
        IBadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Home.this, NewProject.class);
                startActivity(i);
            }
        });

        IBMyFile = (ImageButton) findViewById(R.id.IBMyFile);
        IBMyFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(Home.this, MyDoc.class);
                startActivity(i2);
            }
        });

        IBProfile=(ImageButton)findViewById(R.id.IBProfile);
        IBProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(Home.this, Profile.class);
                startActivity(i3);
            }
        });
    }

}
