//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix.game;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import android.widget.TextView;
import android.widget.Toast;

import de.prog3.tetrix.MainActivity;
import de.prog3.tetrix.R;
import de.prog3.tetrix.game.Class.Gamefield;
import de.prog3.tetrix.game.db.DatabaseHandler;


public class GameoverDialog extends Dialog {

    private Gamefield gamefield;
    private GameoverDialog himself;
    private GameActivity gameactivity;
    private DatabaseHandler mydb;
    private Button submitButton;
    private TextView nicknameInput, firstplacename, firstplacescore,secondplacename, secondplacescore,thirdplacename,thirdplacescore, yourscore;
    private String score;
    private TextView finalscore;

    public GameoverDialog(Context context, Gamefield gamefield, DatabaseHandler mydb, GameActivity gameActivity) {
        super(context);
        this.gameactivity = gameActivity;
        this.gamefield = gamefield;
        this.mydb = mydb;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        himself =this;

        //Buttons
        submitButton = findViewById(R.id.popup_submit);
        Button shareButton = findViewById(R.id.popup_share);
        Button restartButton = findViewById(R.id.popup_restart);
        Button backButton = findViewById(R.id.popup_back);
        nicknameInput = findViewById(R.id.nicknameInput);
        firstplacename = findViewById(R.id.firstplacename);
        firstplacescore = findViewById(R.id.firstplacescore);
        secondplacename = findViewById(R.id.secondplacename);
        secondplacescore = findViewById(R.id.secondplacescore);
        thirdplacename = findViewById(R.id.thirdplacename);
        thirdplacescore = findViewById(R.id.thirdplacescore);
        yourscore = findViewById(R.id.yourScore);
        finalscore = findViewById(R.id.finalScore);


        //SHARE BUTTON
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "My Score on Tetrix is: " + score);
                gameactivity.startActivity(Intent.createChooser(shareIntent, "Share using"));
            }
        });

        //BACK BUTTON
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i=new Intent(getContext(), MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                himself.dismiss();
                gameactivity.startActivity(i);

            }
        });

        //SUBMIT BUTTON
        submitButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                submitButton.setEnabled(false);
                nicknameInput.setEnabled(false);
                insetToDb();
                getTop3();
                nicknameInput.setText(null);
            }
        });


        //RESTART BUTTON
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                gameactivity.restart();
                submitButton.setEnabled(true);
                nicknameInput.setText(null);
                nicknameInput.setEnabled(true);
                nicknameInput.clearFocus();

            }
        });


    }

    private void insetToDb(){
        boolean isInserted =  mydb.insertData(nicknameInput.getText().toString(),gamefield.getScore());
        if(isInserted){
            Toast.makeText(getContext(), "Score saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Couldn't save score", Toast.LENGTH_SHORT).show();
        }
    }

    private void getTop3(){
        firstplacescore.setText(mydb.getScore(0));
        firstplacename.setText(mydb.getName(0));
        secondplacescore.setText(mydb.getScore(1));
        secondplacename.setText(mydb.getName(1));
        thirdplacescore.setText(mydb.getScore(2));
        thirdplacename.setText(mydb.getName(2));
    }

    @Override
    public void show(){
        super.show();
        score = String.valueOf(gamefield.getScoreInt());
        if(gamefield.getScoreInt()>mydb.getHighScore()){
            yourscore.setText("NEW HIGHSCORE:");
        }else{
            yourscore.setText("YOUR SCORE:");
        }
        finalscore.setText(score);
        getTop3();
        gameactivity.isDialog = true;
    }

    @Override
    public void dismiss(){
        super.dismiss();
        gameactivity.isDialog = false;
    }

    //Hide Keyboard on touch outside its scope.
    //Source: https://stackoverflow.com/a/54308582/1375582 by sumit sonawane
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) gameactivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
