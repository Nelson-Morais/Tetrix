//Projektarbeit Prog3: Tetris
//von Nelson Morais (879551) & Marcel Sauer (886022) geschrieben
package de.prog3.tetrix.game.pieces;

import android.graphics.Bitmap;

import de.prog3.tetrix.game.Abstract.AbstractPiece;

public class OnePiece extends AbstractPiece {

    public OnePiece(Bitmap image, Bitmap imagePre, Bitmap nextPicture) {
            super(1, image,imagePre,nextPicture);
            this.blocksBase[0][0]=true;
            copyInblockBase();
    }
}
