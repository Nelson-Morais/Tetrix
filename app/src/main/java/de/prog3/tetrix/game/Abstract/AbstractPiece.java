//Projektarbeit Prog3: Tetris
//von Nelson Morais (879551) & Marcel Sauer (886022) geschrieben
package de.prog3.tetrix.game.Abstract;

import android.graphics.Bitmap;

public abstract class AbstractPiece {
    protected boolean blocksBase[][];
    protected boolean blocks[][];
    protected Bitmap image;
    protected Bitmap nextPicture;
    protected Bitmap imagePre;
    protected boolean blockRot[][];
    protected int sizeD2;

    public AbstractPiece(int sizeD2,Bitmap image,Bitmap imagePre, Bitmap nextPicture){
        blocks = new boolean[sizeD2][sizeD2];
        blockRot = new boolean[sizeD2][sizeD2];
        blocksBase = new boolean[sizeD2][sizeD2];
        this.sizeD2 =sizeD2;
        this.image = image;
        this.imagePre=imagePre;
        this.nextPicture = nextPicture;
    }

    public boolean[][] getBlocks() {
        return blocks;
    }
    public Bitmap getImagePre(){return imagePre;}

    public void setImagePre(Bitmap imagePre) {
        this.imagePre = imagePre;
    }

    public boolean[][] getBlockRot() {
        return blockRot;
    }

    public Bitmap getImage() {
        return image;
    }

    public Bitmap getNextPicture() {
        return nextPicture;
    }

    public void setNextPicture(Bitmap nextPicture) {
        this.nextPicture = nextPicture;
    }

    public void setImage(Bitmap image) {this.image=image;}

    public int getSizeD2() {
        return sizeD2;
    }
    private void resetRot() {
        copyInblockBase();
    }
    protected void copyInblockBase() {
        for (int i = 0; i < blocksBase.length;i++){
            System.arraycopy(blocksBase[i], 0, blocks[i], 0, blocksBase[i].length);
        }
    }
    public void copyRotInblock() {
        for (int i = 0; i < blockRot.length;i++){
            System.arraycopy(blockRot[i], 0, blocks[i], 0, blockRot[i].length);
        }
    }
    public AbstractPiece getPiece(){
        resetRot();
        return this;
    }
}
