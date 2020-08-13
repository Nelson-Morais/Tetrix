//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import de.prog3.tetrix.game.GameActivity;


public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button buttonStart = findViewById(R.id.buttonStart);

        // Hide the status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

       sharedPref = getSharedPreferences("gameSettings",MODE_PRIVATE);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGame();
            }
        });

        checkSettings();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Hide the status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Disable Transition effect
        overridePendingTransition(0, 0);
    }

    private void openGame(){
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    private void checkSettings(){
        SharedPreferences.Editor editor;
        if(!sharedPref.contains("initialized")){
            editor = sharedPref.edit();
            //Init Settings XML and Flag
            editor.putBoolean("initialized",true);
            //Settings
            editor.putBoolean("vibration",true);
            editor.putBoolean("music",true);
            editor.putBoolean("effect",true);
            editor.apply();
        }
    }
}
