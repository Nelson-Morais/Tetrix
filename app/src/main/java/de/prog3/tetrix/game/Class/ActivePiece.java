//Projektarbeit Prog3: Tetris
//Autor: Nelson Morais (879551) & Marcel Sauer (886022)
package de.prog3.tetrix.game.Class;


import de.prog3.tetrix.game.Abstract.AbstractPiece;


public class ActivePiece {
    private int x;
    private int y;

    private int yPre;
    private int xAusPunkt;

    protected AbstractPiece piece;

    // referenziert auf dem Grid wo es sich befindet
    private Block[][] grid;

    public ActivePiece(Block[][] grid) {
        this.grid = grid;
        this.x = (grid.length/2)-1;
        this.xAusPunkt = x;
        y = 0;
        yPre = 0;
    }
    public void spawnPiece(AbstractPiece piece){
        resetP();
        this.piece = piece;
        boolean check = true;
        this.x = (grid.length/2)-(int)piece.getSizeD2()/2;

        for (int k = 0; k < piece.getSizeD2();k++) {
            for (int i = 0; i < piece.getSizeD2(); i++) {
                check = check && !piece.getBlocks()[i][k];
            }
            if (check) {
                y--;
            } else {
                break;
            }
        }

        check = true;
        for (int k = piece.getSizeD2()-1; k >=0;k--) {
            for (int i = 0; i < piece.getSizeD2(); i++) {
                if (x+i >= 0 && y+k >=0 ) {
                    if (piece.getBlocks()[i][k] && grid[i+x][k+y].isActive()){
                        y--;
                        i=0;
                        k=piece.getSizeD2()-1;
                    }
                }
            }
        }
    }
    private void resetP(){
        y=0;
        x= xAusPunkt;
    }
    private void clear(){
        piece=null;
    }


    private boolean moveLeft() {
        x--;
        for (int i = 0; i <piece.getBlocks().length;i++){
            for (int k = 0; k <piece.getBlocks()[0].length;k++){
                if(piece.getBlocks()[i][k]){
                    if(i+x<0){
                        x++;
                        return false;
                    }

                    if(grid[i+x][k+y].isActive()){
                        x++;
                        return false;
                    }
                }
            }
        }
        return true;
    }
    private boolean moveRight() {
        x++;
        for (int i = 0; i <piece.getBlocks().length;i++){
            for (int k = 0; k <piece.getBlocks()[i].length;k++){
                if(piece.getBlocks()[i][k]){
                    if(i+x >= grid.length){
                        x--;
                        return false;
                    }

                    if(grid[i+x][k+y].isActive()){
                        x--;
                        return false;
                    }
                }
            }
        }
        return true;
    }


    private boolean moveDown() {
        y++;
        for (int i = 0; i <piece.getBlocks().length;i++){
                for (int k = 0; k <piece.getBlocks()[i].length;k++){
                    if(piece.getBlocks()[i][k]) {
                        // Unterkante des Spielfelds erreicht?
                        if (k+y >= grid[0].length) {
                            y--;
                            return false;
                        }
                        // kolision mit einem anderen block
                        if(grid[i+x][k+y].isActive()){
                            y--;
                            return false;
                        }
                    }
                }
        }
        return true;
    }
    public boolean canNextFrameDown(){ // dient als überprufung ob der nächste tick der aktive Piece down gehen kann
        if(y< 0){
            return true;
        }
        removeFromGrid();
        boolean tmp = moveDown();
        if(tmp){
            y--;
        }
        addToGrid();
        return tmp;
    }
    public void movePieceLeft() { //Steuerung nach Links
        removeFromGrid();
        moveLeft();
        addToGrid();
    }
    public void movePieceRight() {//Steuerung nach Rechts
        removeFromGrid();
        moveRight();
        addToGrid();
    }


    public boolean addToGrid() {
        return addToGrid(true);

    }
    public boolean addToGrid(boolean renderPreview) { // fügt den aktiven Piece zum grid hinzu mit dem Prediktion(renderPreview)
        boolean end = true;
        for (int i = 0; i <piece.getBlocks().length;i++){
            for (int k = 0; k <piece.getBlocks()[i].length;k++){
                if(piece.getBlocks()[i][k]) {
                    if (x+i >= 0 && x+i < grid.length
                            && y+k >=0 && y+k < grid[0].length) {
                        Block blockBelow = grid[x + i][y + k];
                        if (blockBelow.isActive()) {
                            end =false;
                        }
                    }else {end=false;}
                }
            }
        }
        if(end && renderPreview){
           instantDownPre(); //setzt die Preview zum grid dazu
        }
        updateGrid(this.piece,this.x ,this.y,false); // fügt den Piece zm grid dazu
        return end;
    }

    public void removeFromGrid() { // löscht den Preview und den aktiven Piece com grid
        updateGrid(null,this.x ,this.yPre,false);
        updateGrid(null,this.x,this.y,false);
    }
    private void instantDownPre(){ // setzten des Preview des aktiven Pieces
        int tmp = y;
        instantDown();
        updateGrid(this.piece,x ,y,true);
        yPre = y;
        y = tmp;
    }

    private void instantDown(){ // setzt denaktiven Piece soweit down Piece dieser kollediert
        for (int i = y;i <grid[0].length;i++){
            if (!moveDown()){
                break;
            }
        }
    }
    public void moveInstantDown(){ //Steuerung setzt den aktiven Piece sofort nach Unten
        removeFromGrid();
        instantDown();
        addToGrid();
    }
    private void updateGrid(AbstractPiece piece, int x, int y, boolean pre){ //updatet den jeweiligen Piece an der gesetzten position im grid, dabei wir doch angegeben ob es sich um eine Preview handelt
        for (int i = 0; i <this.piece.getBlocks().length;i++){
            for (int k = 0; k <this.piece.getBlocks()[i].length;k++){
                if (this.piece.getBlocks()[i][k]) {
                    if (x+i >= 0 && x+i < grid.length
                            && y+k >=0 && y+k < grid[0].length) {
                        grid[x + i][y + k].setPiece(piece,pre);
                    }
                }
            }
        }
    }

    public void reset(){
        resetP();
        clear();
    }
    public boolean movePieceDown() {//Steuerung setzt den Piece down wenn er kann
        removeFromGrid();
        boolean tmp = moveDown();
        addToGrid();
        return tmp;
    }


    private boolean canRotate(boolean[][] pre){ // prüft ob der gesetzte Array im grid passt wenn nicht wird false zurückgegegben
        for (int i = 0; i <piece.getBlocks().length;i++){
            for (int k = 0; k <piece.getBlocks()[i].length;k++){
                if(pre[i][k]) {
                    // Unterkante des Spielfelds erreicht?
                    if (x+i >= 0 && x+i < grid.length
                            && y+k >=0 && y+k < grid[0].length) {
                        Block blockBelow = grid[x+i][y+k];
                        if(blockBelow.isActive()){
                            return false;
                        }
                    }else {return false;}
                }
            }
        }
        return true;
    }
    
    //Quelle: https://stackoverflow.com/questions/2799755/rotate-array-clockwise
    public void rotatePiece(){ //Steuerung um zu Rotieren
        removeFromGrid();
        int count = 0;
        do {
            count++;
            for (int i = 0; i <piece.getBlocks().length;i++){
                for (int k = 0; k <piece.getBlocks()[i].length;k++){
                    piece.getBlockRot()[k][piece.getBlocks().length-1-i]=piece.getBlocks()[i][k]; // setzt den Rotation Array vom Piece
                }
            }
        }while (!canRotate(piece.getBlockRot())&&count<5); // wenn dieser nicht pass und nicht unter 4 ist wird wiederholt

        if(count<4){ // wenn der counter über 3 ist hat sich an dem zu stand nicht geändert da ein Piece 4 zustande beim Rotieren hat
            this.piece.copyRotInblock();
        }

        addToGrid();
    }
}
