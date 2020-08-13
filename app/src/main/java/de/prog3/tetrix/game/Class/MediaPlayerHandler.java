//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix.game.Class;

import android.app.Activity;
import android.media.MediaPlayer;


public class MediaPlayerHandler {

    private MediaPlayer mp;
    private String flag;
    private int uri;
    private Activity activity;


    public MediaPlayerHandler(Activity activity, int uri, String flag) {
        this.uri = uri;
        this.activity = activity;
        this.flag = flag;

    }

    public void play() {

        if (flag == "music") {
            if (mp == null) {
                mp = MediaPlayer.create(activity, uri);
            }
            if (SettingsHandler.isSoundON()) {
                mp.seekTo(0);
                mp.start();
                mp.setLooping(true);
            }
        }
        if(flag == "effect"){
            if(mp == null){
                mp = MediaPlayer.create(activity,uri);
            }
            if (SettingsHandler.isEffectON()){
                mp.seekTo(0);
                mp.start();

            }
        }
    }

    public void resumeMusic() {
        if (SettingsHandler.isSoundON()) {
            mp.start();
            mp.setLooping(true);
        }
    }

    public void stop() {
        if (mp.isPlaying()) {
            mp.stop();
        }
    }

    public void pause() {
        if (mp.isPlaying()) {
            mp.pause();
        }
    }
}

