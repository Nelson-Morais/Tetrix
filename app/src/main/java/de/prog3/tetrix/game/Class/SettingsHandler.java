//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix.game.Class;

import de.prog3.tetrix.MainActivity;

public class SettingsHandler {

    private static final String music = "music";
    private static final String effect = "effect";
    private static final String vibration = "vibration";


    public static void setMusicON() {
        MainActivity.sharedPref.edit().putBoolean(music, true).apply();
    }

    public static void setMusicOFF() {
        MainActivity.sharedPref.edit().putBoolean(music, false).apply();
    }

    public static void setEffectON() {
        MainActivity.sharedPref.edit().putBoolean(effect, true).apply();
    }

    public static void setEffectOFF() {
        MainActivity.sharedPref.edit().putBoolean(effect, false).apply();
    }

    public static void setVibrationON(){
        MainActivity.sharedPref.edit().putBoolean(vibration, true).apply();
    }

    public static void setVibrationOFF() {
        MainActivity.sharedPref.edit().putBoolean(vibration, false).apply();
    }

    public static boolean isSoundON() {
        return MainActivity.sharedPref.getBoolean(music, false);
    }

    public static boolean isEffectON(){
        return MainActivity.sharedPref.getBoolean(effect, false);
    }

    public static boolean isVibrationON(){
        return MainActivity.sharedPref.getBoolean(vibration, false);
    }
}
