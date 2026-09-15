package com.example.xobattle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class OnlineActivity extends AppCompatActivity {


    Button btnCreate, btnJoin;
    TextView tvRoomCode, tvStatus;
    EditText etJoinCode;

    DatabaseReference roomsRef;

    String roomCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);


        btnCreate = findViewById(R.id.btnCreate);
        btnJoin = findViewById(R.id.btnJoin);

        tvRoomCode = findViewById(R.id.tvRoomCode);
        tvStatus = findViewById(R.id.tvStatus);

        etJoinCode = findViewById(R.id.etJoinCode);



        FirebaseDatabase database =
                FirebaseDatabase.getInstance(
                        "https://xo-battle-90324-default-rtdb.asia-southeast1.firebasedatabase.app/"
                );


        roomsRef = database.getReference("rooms");



        // CREATE ROOM

        btnCreate.setOnClickListener(v -> {

            createRoom();

        });



        // JOIN ROOM

        btnJoin.setOnClickListener(v -> {


            String code =
                    etJoinCode.getText()
                            .toString()
                            .trim();


            if(code.isEmpty()){

                Toast.makeText(
                        this,
                        "Enter Room Code",
                        Toast.LENGTH_SHORT
                ).show();

                return;

            }



            DatabaseReference room =
                    roomsRef.child(code);



            room.addListenerForSingleValueEvent(
                    new ValueEventListener() {

                        @Override
                        public void onDataChange(
                                @NonNull DataSnapshot snapshot) {


                            if(snapshot.exists()){


                                room.child("player2")
                                        .setValue("O");


                                Intent intent =
                                        new Intent(
                                                OnlineActivity.this,
                                                GameActivity.class
                                        );


                                intent.putExtra(
                                        "roomCode",
                                        code
                                );


                                intent.putExtra(
                                        "player",
                                        "O"
                                );


                                startActivity(intent);



                            }
                            else {


                                Toast.makeText(
                                        OnlineActivity.this,
                                        "Room Not Found",
                                        Toast.LENGTH_SHORT
                                ).show();

                            }

                        }


                        @Override
                        public void onCancelled(
                                @NonNull DatabaseError error) {


                        }

                    });


        });


    }





    private void createRoom(){


        roomCode =
                String.valueOf(
                        new Random().nextInt(900000)+100000
                );



        DatabaseReference room =
                roomsRef.child(roomCode);



        HashMap<String,Object> data =
                new HashMap<>();


        data.put("player1","X");
        data.put("status","waiting");



        room.setValue(data)
                .addOnSuccessListener(unused -> {



                    tvRoomCode.setText(
                            "Room Code : "
                                    + roomCode
                    );


                    tvStatus.setText(
                            "Waiting for opponent..."
                    );


                    Toast.makeText(
                            this,
                            "Room Created",
                            Toast.LENGTH_SHORT
                    ).show();




                    room.child("player2")
                            .addValueEventListener(
                                    new ValueEventListener() {


                                        @Override
                                        public void onDataChange(
                                                @NonNull DataSnapshot snapshot) {


                                            if(snapshot.exists()){


                                                Intent intent =
                                                        new Intent(
                                                                OnlineActivity.this,
                                                                GameActivity.class
                                                        );


                                                intent.putExtra(
                                                        "roomCode",
                                                        roomCode
                                                );


                                                intent.putExtra(
                                                        "player",
                                                        "X"
                                                );


                                                startActivity(intent);


                                            }


                                        }


                                        @Override
                                        public void onCancelled(
                                                @NonNull DatabaseError error) {


                                        }

                                    });



                })

                .addOnFailureListener(e -> {


                    Toast.makeText(
                            this,
                            "Room Create Failed",
                            Toast.LENGTH_SHORT
                    ).show();


                });


    }


}