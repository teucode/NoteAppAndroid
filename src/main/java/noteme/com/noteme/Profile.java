package noteme.com.noteme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    private Button btnChange, btnDeact, btnLogout, btnSave;
    private EditText txtUname;
    private TextView txtEmail;
    private ImageView imgView;
    private Uri uriProfileImage;
    private String profileImageURL;
    DatabaseReference mDatabase;
    ProgressDialog dialog;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        btnLogout= findViewById(R.id.btnLogout);
        txtEmail= findViewById(R.id.txtShowEmail);
        txtUname= findViewById(R.id.txtShowUname);
        imgView=(ImageView) findViewById(R.id.ImageProfile);
        dialog=new ProgressDialog(this);

        loadUserInformation();

        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

        btnSave=(Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent i = new Intent (Profile.this, MainActivity.class);
                startActivity(i);
            }
        });

        btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Profile.this, ChangePass.class);
                startActivity(i);
            }
        });

        btnDeact=(Button) findViewById(R.id.btnDeact);
        btnDeact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null)
                {
                    dialog.setMessage("Deactivating...");
                    dialog.show();
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(),"Account Deactivated", Toast.LENGTH_LONG).show();
                                finish();
                                Intent i = new Intent(Profile.this, MainActivity.class);
                                startActivity(i);
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Account could not be deactivated", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }



    private void saveUserInformation() {
        final String displayName = txtUname.getText().toString().trim();

        if(displayName.isEmpty()) {
            txtUname.setError("Name Required");
            txtUname.requestFocus();

            return;
        }


        FirebaseUser user = mAuth.getCurrentUser();
        mDatabase=FirebaseDatabase.getInstance().getReference();
        mDatabase.child("Users").child(user.getUid()).child("name").setValue(displayName);


        if(user!=null && profileImageURL!=null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .setPhotoUri(Uri.parse(profileImageURL))
                    .build();
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(Profile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() !=null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                imgView.setImageBitmap(bitmap);

                uploadImageToFirebaseStorage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebaseStorage() {
        StorageReference profileImageRef =
                FirebaseStorage.getInstance().getReference("profilepics/"+System.currentTimeMillis()+".jpg");
        if(uriProfileImage!=null) {
            profileImageRef.putFile(uriProfileImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profileImageURL = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Profile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }


    private void showImageChooser() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i, "Select profile image"), CHOOSE_IMAGE);
    }

    private void loadUserInformation() {
        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null) {
            if (user.getPhotoUrl() != null) {
                Glide.with(this)
                        .load(user.getPhotoUrl().toString())
                        .into(imgView);
            }
            if (user.getDisplayName() != null) {
                txtUname.setText(user.getDisplayName());
            }
            if(user.getEmail() != null) {
                txtEmail.setText(user.getEmail());
            }
        }

    }

}
