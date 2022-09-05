package noteme.com.noteme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePass extends AppCompatActivity{

    private EditText txtNewPass;
    private Button btnChange;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);

        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        txtNewPass=(EditText)findViewById(R.id.txtNewPass);
        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    dialog.setMessage("Changing Password...");
                    dialog.show();
                    user.updatePassword(txtNewPass.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_LONG).show();
                                        auth.signOut();
                                        finish();
                                        Intent i = new Intent(ChangePass.this, Home.class);
                                        startActivity(i);
                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Password could not be changed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }

                Intent i = new Intent(ChangePass.this, Home.class);
                startActivity(i);
            }
        });
    }

}
