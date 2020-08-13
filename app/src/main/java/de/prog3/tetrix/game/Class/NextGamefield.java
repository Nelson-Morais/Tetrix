//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix.game.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import de.prog3.tetrix.game.Abstract.AbstractPiece;

public class NextGamefield extends View {
    private AbstractPiece nextPiece;

    public NextGamefield(Context context) {
        super(context);
    }

    public void addPiece(AbstractPiece piece) {
        nextPiece = piece;
    }

    public void clear(){
        nextPiece =null;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (nextPiece.getNextPicture().getWidth() != getWidth()) { // falls der bitmap nicht die größe hat die es eigenlich haben sollte
            nextPiece.setNextPicture(Bitmap.createScaledBitmap(nextPiece.getNextPicture(), getWidth(),getHeight(),false));
        }
        if(nextPiece != null){
            canvas.drawBitmap(nextPiece.getNextPicture(), 0, 0, null);
        }

    }
}
