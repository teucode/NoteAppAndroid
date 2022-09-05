package noteme.com.noteme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewProject extends AppCompatActivity {
    public TextView tvScan, tvNewDoc, tvNewFile, tvClose;
    public ImageButton IBscan, IBfile, IBdoc, IBclose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_project);
        tvScan = (TextView) findViewById(R.id.tvScan);
        tvNewFile = (TextView) findViewById(R.id.tvNewFile);
        tvClose = (TextView) findViewById(R.id.tvClose);

        IBscan = (ImageButton) findViewById(R.id.IBscan);
        IBscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewProject.this, CameraActivity.class);
                startActivity(i);
            }
        });


        IBfile = (ImageButton) findViewById(R.id.IBfile);
        IBfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewProject.this, NewFile.class);
                startActivity(i);
            }
        });

        IBclose = (ImageButton) findViewById(R.id.IBclose);
        IBclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NewProject.this, Home.class);
                startActivity(i);
            }
        });


    }
}