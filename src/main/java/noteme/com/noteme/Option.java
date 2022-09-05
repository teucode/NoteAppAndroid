package noteme.com.noteme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Option extends AppCompatActivity {
    public Button btnOKOption;
    public TextView tvOption, tvMove, tvRename, tvAdd, tvDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvOption = (TextView) findViewById(R.id.tvOption);
        tvMove = (TextView) findViewById(R.id.tvMove);
        tvRename = (TextView) findViewById(R.id.tvRename);
        tvAdd = (TextView) findViewById(R.id.tvAdd);
        tvDelete = (TextView) findViewById(R.id.tvDelete);
        btnOKOption = (Button) findViewById(R.id.btnOKOption);
        btnOKOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Option.this, Home.class);
                startActivity(i);
            }
        });
    }
}

