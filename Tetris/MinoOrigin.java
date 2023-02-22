package Tetris;

public class MinoOrigin {
    protected int x;
    protected int y;
    protected boolean xOffset;
    protected boolean yOffset;

    public MinoOrigin (int x,int  y ,boolean xOffset,boolean yOffset){
        this.x = x;
        this.y = y;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean getXOffset(){
        return xOffset;
    }

    public boolean getYOffset(){
        return yOffset;
    }
}
