package Tetris;

import Tetris.KickTable.KickTableBuilder;

public class Mino {
    protected KickTable kickTable = (new KickTableBuilder()).build();
    protected MinoColor color;
    protected BooleanDataGrid shape;
    protected MinoOrigin origin;

    public Mino(BooleanDataGrid shape, MinoOrigin origin){
        this.shape = shape;
        this.origin = origin;
    }

    public Mino(BooleanDataGrid shape, MinoOrigin origin, KickTable kickTable){
        this.shape = shape;
        this.origin = origin;
        // this.kickTable = kickTable;
    }

    public Mino(BooleanDataGrid shape, MinoOrigin origin, KickTable kickTable, MinoColor coor){
        this.shape = shape;
        this.origin = origin;
        // this.kickTable = kickTable;
        // this.color = color;
    }

    public Mino() {
    }

    static public Mino I(){
        // TODO
        return new Mino();
    }
    
    static public Mino O(){
        // TODO
        return new Mino();
    }
    
    static public Mino S(){
        // TODO
        return new Mino();
    }
    
    static public Mino Z(){
        // TODO
        return new Mino();
    }
    
    static public Mino J(){
        // TODO
        return new Mino();
    }
    
    static public Mino L(){
        // TODO
        return new Mino();
    }
    
    static public Mino T(){
        // TODO
        return new Mino();
    }
    
    public void setColor(MinoColor c){
        this.color = c;
    }
}
