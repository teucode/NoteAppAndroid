package noteme.com.noteme;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NewFile extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseUser user;

    private EditText txtJudul, txtIsi;
    private String Judul, Isi;
    private ImageButton IBsave;
    private Button btnCancel;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_file);
        txtJudul = (EditText) findViewById(R.id.txtJudul);
        txtIsi = (EditText) findViewById(R.id.txtIsi);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(user.getUid());

        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(NewFile.this, Home.class);
                startActivity(i);
            }
        });

        IBsave = (ImageButton) findViewById(R.id.IBsave);
        IBsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Judul = txtJudul.getText().toString().trim();
                String Isi = txtIsi.getText().toString().trim();

                mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Files");

                SaveNote saveNote = new SaveNote(Judul, Isi);

                mDatabase.push().setValue(saveNote);

                NoteModel noteModel = new NoteModel();
                noteModel.setJudul(Judul);
                noteModel.setIsi(Isi);

                mDatabase.setValue(noteModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(NewFile.this, "Stored...", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(NewFile.this, "Error...", Toast.LENGTH_LONG).show();
                        }
                    }
                });



                Intent i = new Intent(NewFile.this, Home.class);
                startActivity(i);
            }
        });
    }


}