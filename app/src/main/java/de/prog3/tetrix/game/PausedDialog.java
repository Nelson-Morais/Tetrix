//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix.game;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import de.prog3.tetrix.R;
import de.prog3.tetrix.game.Class.Gamefield;
import de.prog3.tetrix.game.Class.SettingsHandler;

public class PausedDialog extends Dialog {

    private PausedDialog himself;
    private GameActivity gameActivity;
    private Gamefield gamefield;

    private Button restartButton, resumeButton;
    private Switch musicSwitch, effectSwitch, vibrationSwitch;


    PausedDialog(Context context, Gamefield gamefield, GameActivity gameActivity) {
        super(context);
        this.gameActivity = gameActivity;
        this.gamefield = gamefield;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paused);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        himself = this;


        himself.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        restartButton = findViewById(R.id.popup_restart);
        resumeButton = findViewById(R.id.popup_resume);
        musicSwitch = findViewById(R.id.switch_sound);
        effectSwitch = findViewById(R.id.switch_effect);
        vibrationSwitch = findViewById(R.id.switch_vibration);

        checkState();


        //Resume Button
        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSettings();
                himself.dismiss();
                gameActivity.resume();
            }
        });

        //Restart Button
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSettings();
                dismiss();
                gameActivity.restart();
            }
        });

    }
    @Override
    public void dismiss(){
        super.dismiss();
        gameActivity.isDialog =false;
    }
    @Override
    public void show(){
        super.show();
        gameActivity.isDialog =true;
    }

    private void checkSettings() {
        if (musicSwitch.isChecked()) {
            SettingsHandler.setMusicON();
        } else {
            SettingsHandler.setMusicOFF();
        }
        if (effectSwitch.isChecked()) {
            SettingsHandler.setEffectON();
        } else {
            SettingsHandler.setEffectOFF();
        }
        if (vibrationSwitch.isChecked()) {
            SettingsHandler.setVibrationON();
        } else {
            SettingsHandler.setVibrationOFF();
        }
    }

    private void checkState() {
        if (SettingsHandler.isSoundON()) {
            musicSwitch.setChecked(true);
        } else {
            musicSwitch.setChecked(false);
        }
        if (SettingsHandler.isEffectON()) {
            effectSwitch.setChecked(true);
        } else {
            effectSwitch.setChecked(false);
        }
        if (SettingsHandler.isVibrationON()) {
            vibrationSwitch.setChecked(true);
        } else {
            vibrationSwitch.setChecked(false);
        }
    }

}

