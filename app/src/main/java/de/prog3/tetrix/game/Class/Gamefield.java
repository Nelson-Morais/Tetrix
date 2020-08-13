//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix.game.Class;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.view.View;

import java.util.ArrayList;
import java.util.Random;

import de.prog3.tetrix.R;
import de.prog3.tetrix.game.Abstract.AbstractPiece;

import de.prog3.tetrix.game.pieces.LPieceLeft;
import de.prog3.tetrix.game.pieces.LPieceRight;
import de.prog3.tetrix.game.pieces.LongPiece;
import de.prog3.tetrix.game.pieces.OPiece;
import de.prog3.tetrix.game.pieces.OnePiece;
import de.prog3.tetrix.game.pieces.TPiece;
import de.prog3.tetrix.game.pieces.ZPieceLeft;
import de.prog3.tetrix.game.pieces.ZPieceRight;


public class Gamefield extends View {
    public static final int WIDTH = 10;
    public static final int HEIGHT = 21;
    public int score;
    private int bonusPunkte;
    private int lineScore;
    private ArrayList<AbstractPiece> list;
    private ArrayList<AbstractPiece> listNextPiece;
    private AbstractPiece nextPiece;
    private AbstractPiece OnePiece;
    Random generator;

    NextGamefield nextField;
    // Tetris Grid 10x21
    Block grid[][] = new Block[WIDTH][HEIGHT];
    ActivePiece activePiece;

    Bitmap gamefieldBackground;

    public boolean isFinished = false;
    private boolean lineCleared = false;


    public Gamefield(Context context, NextGamefield nextField) {
        super(context);
        generator = new Random(System.currentTimeMillis());
        this.nextField = nextField;
        gamefieldBackground = BitmapFactory.decodeResource(context.getResources(), R.drawable.gamefield);

        score = 0;
        lineScore= 0;
        bonusPunkte = 50;
        // Erezugung des Grids
        for (int i = 0; i < grid.length; i++) {
            for (int k = 0; k < grid[i].length; k++) {
                grid[i][k] = new Block();
            }
        }
        Bitmap prediktion = BitmapFactory.decodeResource(context.getResources(), R.drawable.square_prediction);
        Bitmap animation = BitmapFactory.decodeResource(context.getResources(), R.drawable.square_line);

        //Next Piece Bitmaps
        Bitmap zpieceright = BitmapFactory.decodeResource(context.getResources(),R.drawable.zpieceright);
        Bitmap zpieceleft = BitmapFactory.decodeResource(context.getResources(),R.drawable.zpieceleft);
        Bitmap longpiece = BitmapFactory.decodeResource(context.getResources(),R.drawable.longpiece);
        Bitmap tpiece = BitmapFactory.decodeResource(context.getResources(),R.drawable.tpiece);
        Bitmap lpiecerechts = BitmapFactory.decodeResource(context.getResources(),R.drawable.lpiecerechts);
        Bitmap lpiecelinks = BitmapFactory.decodeResource(context.getResources(),R.drawable.lpiecelinks);
        Bitmap opiece = BitmapFactory.decodeResource(context.getResources(),R.drawable.opiece);


        OnePiece = new OnePiece(animation,animation,animation); // dient als Lücken Füller bei einer Vollen Reihe
        //initialisierung der List
        list = new ArrayList<AbstractPiece>();
        list.add(new LPieceLeft(BitmapFactory.decodeResource(context.getResources(), R.drawable.syellow),prediktion,lpiecerechts));
        list.add(new LongPiece(BitmapFactory.decodeResource(context.getResources(), R.drawable.sblue),prediktion,longpiece));
        list.add(new LPieceRight(BitmapFactory.decodeResource(context.getResources(), R.drawable.scyan),prediktion,lpiecelinks));
        list.add(new OPiece(BitmapFactory.decodeResource(context.getResources(), R.drawable.sgreen),prediktion,opiece));
        list.add(new TPiece(BitmapFactory.decodeResource(context.getResources(), R.drawable.sorange),prediktion,tpiece));
        list.add(new ZPieceLeft(BitmapFactory.decodeResource(context.getResources(), R.drawable.sred),prediktion,zpieceright));
        list.add(new ZPieceRight(BitmapFactory.decodeResource(context.getResources(), R.drawable.spurple),prediktion,zpieceleft));

        activePiece = new ActivePiece(grid);
        initialStartPiece();
    }
    public String getScore() {
        return String.format("%06d", score);
    }
    public int getScoreInt() {
        return score;
    }
    public int getlineScore() {
        return lineScore;
    }

    public boolean getLineCleared(){
        return lineCleared;
    }
    public void resetLineCleared(){
        lineCleared = false;
    }

    public boolean nextFrame() {
        if (isFinished) return false;

        boolean hasMovedDown = activePiece.movePieceDown();
        if (!hasMovedDown) {
            int scoreCount = checkLine();
            if (1 < scoreCount) {
                scoreCount--;
                score = score + scoreCount * bonusPunkte;
            }
            getNewPiece();
            boolean addedSuccessfully = activePiece.addToGrid();
            if (!addedSuccessfully) {
                isFinished = true;
            }
        }
        if(!activePiece.canNextFrameDown()){
            fullLineAnimation();
        }
        return true;
    }

    public void createRandomNextPiece() { // das aus der liste gezogene Piece wird in nextPiece gespeichert
        int k = (int)(generator.nextDouble()*list.size());
        nextPiece = list.get(k);
        nextField.addPiece(list.get(k));
    }
    private void initialStartPiece(){
        createRandomNextPiece();
        activePiece.spawnPiece(nextPiece.getPiece());
        createRandomNextPiece();
        activePiece.addToGrid();
    }
    private void getNewPiece(){
        activePiece.spawnPiece(nextPiece.getPiece());
        createRandomNextPiece();
    }
    private void fullLineAnimation(){
        for (int k = HEIGHT - 1; k >= 0; k--) {
            if (numberInLine(k) == WIDTH) {
                setLine(k);
                lineCleared = true;
            }
        }
    }
    private int checkLine() { // prüft ob eine Line Voll und setzt die jeweiligeen werte
        int scoreCount = 0;
        for (int k = HEIGHT - 1; k >= 0; k--) {
            if (numberInLine(k) == WIDTH) {
                lineScore++;
                score = score + 100;
                removeGridLine(k);
                moveGridDown(k);
                scoreCount++;
                k++;
            }
        }

        return scoreCount;
    }
    private int numberInLine(int y) { // pruft auf den übergebenen y wert, ob ein reihe Voll ist
        int count =0;
        for (Block[] blocks : grid) {
            if (blocks[y].isActive()) {
                count++;
            }
        }
        return count;
    }
    private void setLine(int y) { // Diese Methode setzt eine Volle Line auf das Objekt OnePiece
        for (Block[] blocks : grid) {
            blocks[y].setPiece(OnePiece, false);
        }
    }

    private void moveGridDown(int y) { // bewegt alles an der stelle y um eins Runter
        y--;
        for (int k = y; k >= 0; k--) {
            for (Block[] blocks : grid) {
                if (blocks[k].isActive()) {
                    blocks[k + 1].setPiece(blocks[k].getPiece(), false);
                    blocks[k].clear();
                }
            }
        }
    }

    private void removeGridLine(int y) { // löscht die angegebnde y Line
        for (Block[] blocks : grid) {
            blocks[y].clear();
        }
    }

    //Steuerung des aktiven Pieces
    public void moveLeft() {
        activePiece.movePieceLeft();
    }
    public void moveRight() {
        activePiece.movePieceRight();
    }
    public void moveInstantDown() {
        activePiece.moveInstantDown();
        fullLineAnimation();
    }
    public void rotate() {
        activePiece.rotatePiece();
    }


    public void reset() {
        for (Block[] blocks : grid) {
            for (Block block : blocks) {
                block.clear();
            }
        }
        this.lineScore=0;
        this.score = 0;
        this.isFinished = false;
        nextField.clear();
        nextPiece = null;
        activePiece.reset();

        initialStartPiece();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int borderoffset = 4;
        int x = borderoffset;
        int blockSize;

        int width = getWidth() - borderoffset * 2;
        int height = getHeight() - borderoffset * 2;
        if ((width / WIDTH) * HEIGHT > height) {
            blockSize = height / HEIGHT;
            // Spielfeld ist breiter als hoch
        } else {
            blockSize = width / WIDTH;
            // Spielfeld ist höher als breit
        }
        x = (width - blockSize * WIDTH) / 2; // centrieren

        // draw gamefield background
        if (gamefieldBackground.getWidth() != blockSize * WIDTH + borderoffset * 2) {
            gamefieldBackground = Bitmap.createScaledBitmap(
                    gamefieldBackground,
                    blockSize * WIDTH + borderoffset * 2,
                    blockSize * HEIGHT + borderoffset * 2,
                    false
            );
        }
        canvas.drawBitmap(gamefieldBackground, x - borderoffset, 0, null);

        // draw blocks
        for (int i = 0; i < grid.length; i++) {
            for (int k = 0; k < grid[i].length; k++) {
                grid[i][k].draw(canvas, x + (i * blockSize), borderoffset + (k * blockSize), blockSize);

            }
        }
    }
}
