package noteme.com.noteme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CreateAccount extends AppCompatActivity {

    public EditText txtEmail, txtPasswordCA, txtName;
    public Button btnCA;
    public String Name,Email,Password;
    public ProgressDialog mDialog;
    public FirebaseAuth mAuth;
    public DatabaseReference mdatabase;
    private ProgressDialog mProgress;
    private String AES = "AES";
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_account);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPasswordCA = (EditText) findViewById(R.id.txtPasswordCA);
        txtName = (EditText) findViewById(R.id.txtName);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Signing up...");
        btnCA = (Button) findViewById(R.id.btnCA);

        btnCA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isValidate()) {
                    //AddData();
                    SignUp();
                    Intent i = new Intent(CreateAccount.this, MainActivity.class);
                    startActivity(i);
                }

            }
        });

        databaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
    }

    public void AddData() {
        String Name = txtName.getText().toString().trim();
        String Email = txtEmail.getText().toString().trim();
        String Password = txtPasswordCA.getText().toString().trim();

        SaveData saveData = new SaveData(Name,Email,Password);

        databaseReference.push().setValue(saveData);

        Toast.makeText(getApplication(),"Account Created Successfully", Toast.LENGTH_LONG).show();
    }



    private boolean isValidate() {
        boolean isValidate = true;

        Name = txtName.getText().toString().trim();
        Email = txtEmail.getText().toString().trim();
        Password = txtPasswordCA.getText().toString().trim();
        if(Name.length()==0) {
            txtName.setError("Enter Name");
            isValidate=false;
        }
        if(Email.length()==0) {
            txtEmail.setError("Enter Email");
            isValidate=false;
        } else if (!Utility.isValidEmail(Email)) {
            txtEmail.setError("Please enter valid email ID");
            isValidate=false;
        }
        if(Password.length()==0) {
            txtPasswordCA.setError("Enter Pssword");
            isValidate=false;
        } else if (Password.length()<8) {
            txtPasswordCA.setError("Password must contain min 8 character");
            isValidate=false;
        }
        return isValidate;

    }

    private static class Utility {
        public static boolean isValidEmail(String txtEmail)
        {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            CharSequence inputStr = txtEmail;
            Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
            return pattern.matcher(inputStr).matches();
        }
    }

    private void SignUp() {
        final String Name = txtName.getText().toString().trim();
        final String Email = txtEmail.getText().toString().trim();
        final String Password = txtPasswordCA.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            mProgress.show();
                if(task.isSuccessful()) {
                    final FirebaseUser user = task.getResult().getUser();
                    if(user != null) {
                        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    mProgress.dismiss();
                                    //finish();
                                    try{
                                        String Passwordenc = encrypt(txtPasswordCA.getText().toString());
                                        //Password = Passwordenc;
                                    }
                                    catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                                    databaseReference = databaseReference.child(user.getUid());

                                    SaveData saveData = new SaveData(Name,Email,Password);

                                    databaseReference.push().setValue(saveData);

                                    UserModel userModel = new UserModel();
                                    userModel.setName(Name);
                                    userModel.setEmail(Email);
                                    userModel.setPassword(Password);


                                    databaseReference.setValue(userModel);
                                    sendEmailVerification();
                                    //Toast.makeText(getApplication(),"Account Created Successfully", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(CreateAccount.this,"Successfully registered, check your Email for verification...",Toast.LENGTH_SHORT).show();
                        mAuth.signOut();
                        finish();
                        startActivity(new Intent(CreateAccount.this, MainActivity.class));
                    } else {
                        Toast.makeText(CreateAccount.this, "Can't send verification email...", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private String encrypt(String password) throws Exception{

        SecretKeySpec key = generateKey(password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(password.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }
/*
    public  void computeMD5Hash(String pass)
    {
        try{
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(pass.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for (int i = 0; i<messageDigest.length; i++)
            {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length()<2)
                    h="0"+h;
                MD5Hash.append(h);
            }
            result.setText(MD5Hash);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
    }
*/
    private void OnAuth(FirebaseUser user) {
        createAnewUser(user.getUid());
    }

    private void createAnewUser(String uid) {
        User user = BuildNewuser();
        mdatabase.child(uid).setValue(user);
    }


    private User BuildNewuser(){
        return new User(
                getDisplayName(),
                getUserEmail()
        );
    }

    public String getDisplayName() {
        return Name;
    }

    public String getUserEmail() {
        return Email;
    }

}