// for representing coordinates of a vertex
//package A5;

public class Coordinate {
    private int X;     // x coordinate in GUI
    private int Y;     // y coordinate in GUI
    private int ind;   // index of vertex displayed in GUI
    public Coordinate(){}
    
    public Coordinate(int x, int y, int Index) {
        X   = x;
        Y   = y;
        ind = Index;
    }
    
    public int getX(){return X;}
    public int getY(){return Y;}
    public int getIndex(){return ind;}
    
    public void setX(int x){ X = x; }
    public void setY(int y){ Y = y; }
    public void setIndex(int Index){ ind = Index; }
}