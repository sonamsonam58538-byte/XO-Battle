package com.example.xobattle;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {


    Button b1,b2,b3,b4,b5,b6,b7,b8,b9,btnReset;

    TextView tvTurn,tvScore;


    DatabaseReference roomRef;


    String roomCode;
    String myPlayer;

    String currentTurn="X";


    int xScore=0;
    int oScore=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);



        roomCode=getIntent().getStringExtra("roomCode");

        myPlayer=getIntent().getStringExtra("player");



        FirebaseDatabase database =
                FirebaseDatabase.getInstance(
                        "https://xo-battle-90324-default-rtdb.asia-southeast1.firebasedatabase.app/"
                );


        roomRef =
                database.getReference("rooms")
                        .child(roomCode);




        tvTurn=findViewById(R.id.tvTurn);
        tvScore=findViewById(R.id.tvScore);



        b1=findViewById(R.id.b1);
        b2=findViewById(R.id.b2);
        b3=findViewById(R.id.b3);

        b4=findViewById(R.id.b4);
        b5=findViewById(R.id.b5);
        b6=findViewById(R.id.b6);

        b7=findViewById(R.id.b7);
        b8=findViewById(R.id.b8);
        b9=findViewById(R.id.b9);


        btnReset=findViewById(R.id.btnReset);



        Button[] buttons={
                b1,b2,b3,
                b4,b5,b6,
                b7,b8,b9
        };


        for(Button b:buttons){

            b.setOnClickListener(v -> playMove((Button)v));

        }



        btnReset.setOnClickListener(v -> resetGame());



        listenBoard();

        listenTurn();



    }





    private void playMove(Button button){


        if(!myPlayer.equals(currentTurn)){

            Toast.makeText(
                    this,
                    "Opponent Turn",
                    Toast.LENGTH_SHORT
            ).show();

            return;

        }



        if(!button.getText().toString().equals("")){

            return;

        }



        String id =
                getResources()
                        .getResourceEntryName(button.getId());



        roomRef.child("board")
                .child(id)
                .setValue(myPlayer);



        currentTurn =
                currentTurn.equals("X")
                        ? "O"
                        : "X";



        roomRef.child("turn")
                .setValue(currentTurn);


    }





    private void listenBoard(){


        roomRef.child("board")
                .addValueEventListener(
                        new ValueEventListener() {


                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot) {


                                setButton(b1,snapshot.child("b1").getValue(String.class));
                                setButton(b2,snapshot.child("b2").getValue(String.class));
                                setButton(b3,snapshot.child("b3").getValue(String.class));

                                setButton(b4,snapshot.child("b4").getValue(String.class));
                                setButton(b5,snapshot.child("b5").getValue(String.class));
                                setButton(b6,snapshot.child("b6").getValue(String.class));

                                setButton(b7,snapshot.child("b7").getValue(String.class));
                                setButton(b8,snapshot.child("b8").getValue(String.class));
                                setButton(b9,snapshot.child("b9").getValue(String.class));


                                checkWinner();

                            }


                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error) {

                            }

                        });

    }





    private void listenTurn(){


        roomRef.child("turn")
                .addValueEventListener(
                        new ValueEventListener() {


                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot) {


                                if(snapshot.exists()){


                                    currentTurn =
                                            snapshot.getValue(String.class);



                                    if(currentTurn.equals(myPlayer)){

                                        tvTurn.setText("Your Turn");

                                    }
                                    else{

                                        tvTurn.setText("Opponent Turn");

                                    }


                                }


                            }


                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error) {


                            }


                        });


    }





    private void setButton(Button b,String value){

        if(value!=null){

            b.setText(value);

        }

    }






    private void checkWinner(){


        String[][] win={

                {"b1","b2","b3"},
                {"b4","b5","b6"},
                {"b7","b8","b9"},

                {"b1","b4","b7"},
                {"b2","b5","b8"},
                {"b3","b6","b9"},

                {"b1","b5","b9"},
                {"b3","b5","b7"}

        };



        Button[] buttons={
                b1,b2,b3,
                b4,b5,b6,
                b7,b8,b9
        };



        for(String[] line:win){


            String a=findText(line[0]);
            String b=findText(line[1]);
            String c=findText(line[2]);



            if(!a.equals("")
                    && a.equals(b)
                    && a.equals(c)){



                Toast.makeText(
                        this,
                        a+" Winner",
                        Toast.LENGTH_SHORT
                ).show();



                disableButtons();


            }


        }


    }





    private String findText(String id){


        int res =
                getResources()
                        .getIdentifier(
                                id,
                                "id",
                                getPackageName()
                        );


        return ((Button)findViewById(res))
                .getText()
                .toString();

    }





    private void resetGame(){


        roomRef.child("board").removeValue();


        for(String s:new String[]{
                "b1","b2","b3",
                "b4","b5","b6",
                "b7","b8","b9"}){


            roomRef.child("board")
                    .child(s)
                    .setValue("");

        }


        roomRef.child("turn")
                .setValue("X");


        enableButtons();


    }





    private void disableButtons(){

        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        b4.setEnabled(false);
        b5.setEnabled(false);
        b6.setEnabled(false);
        b7.setEnabled(false);
        b8.setEnabled(false);
        b9.setEnabled(false);

    }





    private void enableButtons(){

        Button[] b={
                b1,b2,b3,
                b4,b5,b6,
                b7,b8,b9
        };


        for(Button x:b){

            x.setEnabled(true);
            x.setText("");

        }


    }


}