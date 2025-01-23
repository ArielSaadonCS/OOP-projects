import java.io.Serializable;

/**
 * This class represents a location on the map, each cell in the internal array in 2D array
 * is an Index, represented by integers only
 */
public class Index2D implements Pixel2D, Serializable{
    private int _x, _y;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {_x=x;_y=y;}
    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}

    public Index2D(String pos) {
        // add you code here
        try {
            String[] a = pos.split(",");
            _x = Integer.parseInt(a[0]);
            _y = Integer.parseInt(a[1]);
        }
        catch(IllegalArgumentException e) {
            System.err.println("ERR: got wrong format string for Index2D init, got:"+pos+"  should be of format: x,y");
            throw(e);
        }
        ////////////////////
    }

    @Override
    public int getX() {
        return _x;
    }

    @Override
    public int getY() {
        return _y;
    }
    public double distance2D(Pixel2D t) {
        // add you code here
        if(t == null){
            throw new RuntimeException("can't calculate distance t is null");
        }
        double dx = this.getX() - t.getX();
        double dy = this.getY() - t.getY();
        double dis = (dx * dx + dy * dy);
        return Math.sqrt(dis);
        ////////////////////
    }
    @Override
    public String toString() {

        // add you code here
        ////////////////////
        return _x + "," + _y;
    }
    @Override
    public boolean equals(Object t) {
        // add you code here
        if (t == null || !(t instanceof Pixel2D)) {
            return false;
        }
        Pixel2D t2 = (Pixel2D) t;
        return ((_x == t2.getX()) && (_y == t2.getY()));
        ////////////////////
    }
}
