//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)


package de.prog3.tetrix.game;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import de.prog3.tetrix.R;
import de.prog3.tetrix.game.Class.Gamefield;
import de.prog3.tetrix.game.Class.MediaPlayerHandler;
import de.prog3.tetrix.game.Class.NextGamefield;
import de.prog3.tetrix.game.Class.SettingsHandler;
import de.prog3.tetrix.game.db.DatabaseHandler;


public class GameActivity extends AppCompatActivity {
    private Gamefield gamefield;
    private Handler handler = new Handler();
    private PausedDialog pausedDialog;
    private DatabaseHandler mydb;
    private NextGamefield nextField;
    private GameoverDialog gameoverDialog;
    private MediaPlayerHandler musicMediaPlayer;
    private MediaPlayerHandler effectMediaPlayer;
    private TextView score;
    private TextView highscore;
    private TextView level;

    public double speed = 1;
    public double anfangsSpeed = speed;
    public double normalSpeed = speed;
    public double speedFactor;
    public int boostedSpeed = 20;
    public int levelLine;
    private int levelUP;
    private long lastTouch = -1;
    private boolean stop;
    public boolean isDialog;



    @SuppressLint({"ClickableViewAccessibility", "DefaultLocale"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        this.levelUP = 0;
        speedFactor = 1;
        levelLine = 10;

        // Hide the status bar.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Classes
        nextField = new NextGamefield(this);
        gamefield = new Gamefield(this, nextField);
        mydb = new DatabaseHandler(this);
        pausedDialog = new PausedDialog(this, gamefield, this);
        gameoverDialog = new GameoverDialog(this, gamefield, mydb, this);


        //Buttons, Textviews & MediaPlayers
        ImageButton buttonL = findViewById(R.id.Button_Left);
        ImageButton buttonR = findViewById(R.id.Button_Right);
        ImageButton buttonD = findViewById(R.id.Button_Down);
        ImageButton buttonRot = findViewById(R.id.Button_Rotation);
        ImageButton pauseButton = findViewById(R.id.Button_Pause);
        highscore = findViewById(R.id.HighScore);
        score = findViewById(R.id.Score);
        level = findViewById(R.id.Level);
        highscore.setText(String.format("%06d", mydb.getHighScore()));
        musicMediaPlayer = new MediaPlayerHandler(this, R.raw.tetrix_soundtrack, "music");
        effectMediaPlayer = new MediaPlayerHandler(this, R.raw.tetrix_effect, "effect");
        musicMediaPlayer.play();

        //Layouts
        LinearLayout gameLayout = (LinearLayout) findViewById(R.id.game);
        gameLayout.addView(gamefield);
        LinearLayout nextFieldLayout = (LinearLayout) findViewById(R.id.NextField);
        nextFieldLayout.addView(nextField);

        //Logik Thread
        final Runnable nextFrameRunnable = new Runnable() {
            @Override
            public void run() {
                if (!stop) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (gamefield.nextFrame()) {
                                levelCheck();
                                score.setText(gamefield.getScore());
                                if (gamefield.getLineCleared()) {
                                    effectMediaPlayer.play();
                                    vibrate(150);
                                    gamefield.resetLineCleared();
                                }
                            } else {
                                stop = true;
                                statusReset();
                                endGame();
                            }
                        }
                    });
                }
                gamefield.postDelayed(this, 1000 / (long) speed);
            }
        };

        //Refresh Rate Thread
        final Runnable FPS = new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!stop) {
                            gamefield.invalidate();
                            nextField.invalidate();
                        }
                    }
                });
                gamefield.postDelayed(this, 1000 / 60);
            }
        };

        //Button Down
        buttonD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    vibrate(10);
                    long currentTouch = System.currentTimeMillis();
                    if (currentTouch - lastTouch <150) {
                    gamefield.moveInstantDown();
                    levelCheck();
                    } else {
                        gamefield.removeCallbacks(nextFrameRunnable);
                        speed = boostedSpeed;
                        levelCheck();
                        gamefield.post(nextFrameRunnable);
                        lastTouch = currentTouch;
                    }
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    gamefield.removeCallbacks(nextFrameRunnable);
                    speed = normalSpeed;
                    levelCheck();
                    gamefield.post(nextFrameRunnable);
                }
                return false;
            }
        });

        //Button Rotate
        buttonRot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate(10);
                if (!gamefield.isFinished) {
                    gamefield.rotate();
                }
            }
        });

        //Button Right
        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate(10);
                if (!gamefield.isFinished) {
                    gamefield.moveRight();
                }
            }
        });

        //Button Left
        buttonL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrate(10);
                if (!gamefield.isFinished) {
                    gamefield.moveLeft();
                }
            }
        });

        //Pause Button
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
                pausedDialog.show();
            }
        });


        gamefield.post(nextFrameRunnable);
        gamefield.post(FPS);
    }


    public void levelCheck() {
        int tmp = levelUP;
        levelUP = gamefield.getlineScore()/ levelLine;
        if (tmp < levelUP) {
            speed = speedFactor * levelUP+ anfangsSpeed;
            normalSpeed = speed;
            level.setText(String.valueOf(levelUP));

        }
    }
    private  void statusReset(){
        levelUP = 0;
        speed = 1;
        normalSpeed = 1;
    }
    @Override
    protected void onPause() {
        super.onPause();
        stop = true;
        musicMediaPlayer.pause();

    }

    @Override
    protected void onResume() {
        if (isDialog){
            onPause();
            return;
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        stop = false;
        musicMediaPlayer.resumeMusic();
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    //End Game popup
    //TODO MediaPlayer settings file.
    private void endGame() {
        musicMediaPlayer.pause();
        gameoverDialog.show();
    }

    @SuppressLint("DefaultLocale")
    public void restart() {
        highscore.setText(String.format("%06d", mydb.getHighScore()));
        level.setText("0");
        speed=anfangsSpeed;
        normalSpeed=anfangsSpeed;
        gamefield.reset();
        musicMediaPlayer.play();
        onResume();
    }

    public void resume() {

        onResume();
    }

    //Handle Vibration with SDK < 26 (Deprecated API) and SDK >= 26
    private void vibrate(int n) {

        if (SettingsHandler.isVibrationON()) {
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) Objects.requireNonNull(getSystemService(VIBRATOR_SERVICE))).vibrate(VibrationEffect.createOneShot(n, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //Deprecated API.
                ((Vibrator) Objects.requireNonNull(getSystemService(VIBRATOR_SERVICE))).vibrate(n);
            }
        }
    }


}


