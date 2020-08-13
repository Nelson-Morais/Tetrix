//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix.game.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static String DB_NAME = "Highscore.db";
    private static String TABLE_NAME = "Highscore_t";
    private static String COL_0 = "SpielID";
    private static String COL_1 = "Nick";
    private static String COL_2 = "Score";



    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( "
                + COL_0 + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + COL_1 + " TEXT, "
                + COL_2 + " INT )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String nick, String score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, nick);
        contentValues.put(COL_2, score);

        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }


    public int getHighScore() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select max(Score) from highscore_t",null);
        result.moveToFirst();
        int highscore = result.getInt(0);
        return highscore;
    }
    public String getName(int n){
        String name;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select " + COL_1 +" from "+ TABLE_NAME + " Order by "+ COL_2 +" DESC LIMIT 1 OFFSET " + n,null);
        result.moveToFirst();

        if (result.getCount()!=0) {
            name = result.getString(0);
        }else {
            name="---";
        }
        return name;
    }

    public String getScore(int n){
        String score;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select " + COL_2 +" from "+ TABLE_NAME + " Order by "+ COL_2 +" DESC LIMIT 1 OFFSET " + n,null);
        result.moveToFirst();

        if (result.getCount()!=0) {
            score = result.getString(0);
        }else {
            score="0";
        }
        return score;
    }

}



