//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix.game.Class;

import android.graphics.Bitmap;
import android.graphics.Canvas;


import de.prog3.tetrix.game.Abstract.AbstractPiece;


public class Block {
    protected AbstractPiece piece;
    protected boolean isPrediction;

    public Block()
    {
        this.piece = null;
    }

    public AbstractPiece getPiece() {
        return piece;
    }

    public boolean isActive() { // gibt an was ein aktiver Piec ist
        return piece != null & !isPrediction ;
    }
    public void setPiece(AbstractPiece piece,boolean p) {
        this.piece = piece;
        this.isPrediction= p;
    }
    public void draw(Canvas canvas, int x , int y, int size){
        if(piece != null){
            if (piece.getImage().getWidth() != size) { // falls der bitmap nicht die größe hat die es eigenlich haben sollte
                piece.setImage(Bitmap.createScaledBitmap(piece.getImage(), size,size,false));
            }
            if (piece.getImagePre().getWidth() != size) {
                piece.setImagePre(Bitmap.createScaledBitmap(piece.getImagePre(), size,size,false));
            }
            if (isPrediction){
                canvas.drawBitmap(piece.getImagePre(), x, y, null);
            }else {
                canvas.drawBitmap(piece.getImage(), x, y, null);
            }
        }
    }
    public void clear(){
        piece = null;
        this.isPrediction = false;
    };
}
