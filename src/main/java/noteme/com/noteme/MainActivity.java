package noteme.com.noteme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    RelativeLayout RelLay1;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            RelLay1.setVisibility(View.VISIBLE);
        }
    };

    private EditText txtEmail, txtPassword;
    private Button btnLogin, btnCA;
    private String Email, Password;
    private FirebaseAuth auth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelLay1 = (RelativeLayout) findViewById(R.id.RelLay1);
        handler.postDelayed(runnable, 4000);

        auth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loging in...");

        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        btnCA  = (Button) findViewById(R.id.btnCA);
        btnCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity.this, CreateAccount.class);
                startActivity(i2);
            }
        });


        btnLogin = (Button) findViewById(R.id.btnlogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidate()) {
                    UserLogin();
                    Intent i = new Intent(MainActivity.this, Home.class);
                    startActivity(i);
                }
            }
        });

    }

    private void UserLogin() {

        mProgress.show();
        Email = txtEmail.getText().toString().trim();
        Password = txtPassword.getText().toString().trim();
        auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    mProgress.dismiss();
                    checkEmailVerif();
                }
                else {
                    mProgress.dismiss();
                    Toast.makeText(MainActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    private void checkEmailVerif() {
        FirebaseUser firebaseUser = auth.getInstance().getCurrentUser();
        Boolean emailflag = firebaseUser.isEmailVerified();

        if(emailflag) {
            finish();
            startActivity(new Intent(MainActivity.this, Home.class));
        }
        else {
            Toast.makeText(this, "Verify your email...", Toast.LENGTH_SHORT).show();
            auth.signOut();
        }

    }

    private boolean isValidate() {
        boolean isValidate = true;

        Email = txtEmail.getText().toString().trim();
        Password = txtPassword.getText().toString().trim();
        if (Email.length() == 0) {
            txtEmail.setError("Required field");
            isValidate = false;
        }
        if (Password.length() == 0) {
            txtPassword.setError("Required field");
            isValidate = false;
        }
        return isValidate;
    }
}