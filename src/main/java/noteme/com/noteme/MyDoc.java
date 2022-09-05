package noteme.com.noteme;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyDoc extends AppCompatActivity {

    private static final String TAG = "ViewFile";
    private FirebaseDatabase database;
    private DatabaseReference mDatabase;
    private  FirebaseAuth firebaseAuth;
    private  FirebaseAuth.AuthStateListener authStateListener;
    private String userID;
    private TextView tvMyDoc;
    private Button btnHome;
    private ListView mListFile;
    private List<String> File;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_doc);
        tvMyDoc = (TextView) findViewById(R.id.tvMyDoc);

        mListFile = (ListView) findViewById(R.id.listFile);
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        userID = user.getUid();
        File = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("Users");


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                File.clear();

                String Judul = dataSnapshot.child(userID).child("Files").child("judul").getValue(String.class);
                String Isi = dataSnapshot.child(userID).child("Files").child("isi").getValue(String.class);

                File.add("Judul : " +Judul);
                File.add("Isi   : " +Isi);

                adapter = new ArrayAdapter<String>(MyDoc.this, android.R.layout.simple_list_item_1, File);
                mListFile.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

/*
        mDatabase.child("Files").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot fileShapshot : dataSnapshot.getChildren()) {
                    String value = fileShapshot.getValue(String.class);
                    File.add(value);
                    arrayAdapter.notifyDataSetChanged();
                }
                mListFile.setAdapter(arrayAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                File.remove(value);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        btnHome=(Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyDoc.this, Home.class);
                startActivity(i);
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {

           String Judul = dataSnapshot.child(userID).child("Files").child("judul").getValue(String.class);
           String Isi = dataSnapshot.child(userID).child("Files").child("isi").getValue(String.class);

            File.add(Judul);
            File.add(Isi);


        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, File);
        mListFile.setAdapter(arrayAdapter);
    }

}
