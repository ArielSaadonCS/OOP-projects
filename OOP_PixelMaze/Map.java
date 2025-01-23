import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class represents a 2D map as a "screen" or a raster matrix or maze over integers.
 * The map demonstrates a two-dimensional array and has methods (algorithms) that color
 * the map according to their purpose and they will be detailed below.
 * @author boaz.benmoshe
 */
public class Map implements Map2D, Serializable {
    private int[][] _map; // a 2D array that represent all the pixels in the map and there values
    private boolean _cyclicFlag = true;
    private static final int OBS = -2; //default value of a pixel that represent an obstacle(for shortestPath algorithms)
    private static final int UV = -1; //default value of a pixel that represent an unvisited pixel(for shortestPath algorithms)
    private static final int VISITED = Color.BLUE.getRGB();
    /**
     * Constructs a w*h 2D raster map with an init value v.
     * @param w the width of the map
     * @param h the height of the map
     * @param v the value(color) of a pixel
     */
    public Map(int w, int h, int v) {
        init(w, h, v);
    }

    /**
     * Constructs a square map (size*size).
     * @param size the width/height of the map
     */
    public Map(int size) {
        this(size, size, 0);
    }

    /**
     * Constructs a map from a given 2D array.
     * @param data set the pixels with value of the 2D array cells
     */
    public Map(int[][] data) {
        init(data);
    }

    /**
     * Initializes the map with height, width and value (color)
     * @param w the width of the underlying 2D array.
     * @param h the height of the underlying 2D array.
     * @param v the init value of all the entries in the 2D array.
     */
    @Override
    public void init(int w, int h, int v) {
        // add you code here
        _map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                _map[i][j] = v;
            }
        }
    }

    /**
     * Initializes the map with height, width and value (color)
     * all data is copied from a 2D array
     * @param arr a 2D int array.
     */
    @Override
    public void init(int[][] arr) {
        // add you code here
        int w = arr.length;
        int h = arr[0].length;
        _map = new int[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                _map[i][j] = arr[i][j];
            }
        }
    }

    /**
     * Initializes the map with height, width and value (color)
     * all data is copied from a Pixel array
     * @param ps a given pixel array that set values at the map
     * @param val the value in every cell, that represents the color in every pixel
     */
    public void init(Pixel2D[] ps, int val){
        for (int i = 0; i < ps.length; i++) {
            setPixel(ps[i], val);
        }
    }
    @Override
    public int[][] getMap() {
       int[][] ans = new int[_map.length][_map[0].length];
        for (int i = 0; i < ans.length; i++) {
            for (int j = 0; j < ans[0].length; j++) {
                ans[i][j] = _map[i][j];
            }
         }
        return ans;
    }

    @Override
    public int getWidth() {
        return _map.length;
    }

    @Override
    public int getHeight() {
        return _map[0].length;
    }

    @Override
    public int getPixel(int x, int y) {
        return _map[x][y];
    }

    @Override
    public int getPixel(Pixel2D p) {
        return this.getPixel(p.getX(), p.getY());
    }

    @Override
    public void setPixel(int x, int y, int v) {
        _map[x][y] = v;
    }

    @Override
    public void setPixel(Pixel2D p, int v) {
        setPixel(p.getX(), p.getY(), v);
    }


    @Override
    /**
     * Fills this map with the new color (new_v) starting from p.
     * https://en.wikipedia.org/wiki/
     * This algorithm get a point(the entry point) and with a helper function checks
     * all its neighbors if they are suitable paints them with its own color, after that one
     * of the painted neighbors becomes xy and so on until all the pixels are painted or do not match
     * @param xy the entry point un the map (no need obsColor because all different values from p1 are obsColor)
     * @param new_v the value(color) that fills all relevant pixels
     */
    public int fill(Pixel2D xy, int new_v) {
        int ans = 0;
        int old_v = getPixel(xy);
        ArrayList<Pixel2D> filler = new ArrayList<>();
        filler.add(xy);
        while (!filler.isEmpty()) {
            xy = filler.getFirst();
            filler.removeFirst();
            if (isInside(xy)) {
                int v = getPixel(xy);
                if (v == old_v) {
                    ans++;
                    checkAndFill(filler, xy, old_v, new_v);
                }
            }
        }
        return ans;
    }

    /**
     * Computes the distance of the shortest path (minimal number of consecutive neighbors) from p1 to p2.
     * Notes: the distance is using computing the shortest path and returns its length-1, as the distance fro  a point
     * to itself is 0, while the path contains a single point.
     * @param p1 starting point
     * @param p2 end point
     * @param obsColor the value that represents a barrier (it cannot be painted or passed through)
     * @return the number of steps of the shortest path between p1 and p2
     */
    public int shortestPathDist(Pixel2D p1, Pixel2D p2, int obsColor) {
        int ans = -1;
        // add you code here
        Pixel2D[] dist = shortestPath(p1, p2, obsColor);
        if (dist != null){
            return dist.length-1;
        }
        return ans;
    }

    @Override
    /**
     * Compute the shortest possible path between p1 and p2, this path does NOT contain the obsColor.
     * A path is an ordered set of pixels where each consecutive pixels in the path are neighbors in this map.
     * In case there is no valid path between p1 and p2 should return null;
     * This method with the help of a helper function(allDistance) defines a barrier (obsColor) checks if p1(entry point)
     *  is eligible and from p1 goes to all eligible neighbors (up,down, left, right) and gives them the value of p1 +1
     * then goes to one of the nearest neighbors and does the same thing until it reaches p2, and then with the help of
     * another helper function (nextTo) fill the shortest path between p2 and p1 with p1 value.
     * @param p1 entry point
     * @param p2 end point
     * @param obsColor an obstacle in the map
     * @return an array of the pixels(coordinates) of the shortest path between p1 and p2
     */
    public Pixel2D[] shortestPath(Pixel2D p1, Pixel2D p2, int obsColor) {
        Pixel2D[] ans = null;  // the result.
        if(getPixel(p2) == obsColor){
            return null;
        }
        if (p1.equals(p2)){
            ans = new Index2D[1];
            ans[0] = p1;
        }
        Map2D map = allDistance(p1,obsColor);
        ArrayList<Pixel2D> road = nextTo(map, p2, p1);
        for (int i = 0; i < road.size(); i++) {
            Pixel2D pixel = road.get(i);
            setPixel(pixel, VISITED);
        }
         ans = road.toArray(new Pixel2D[0]);
        return ans;
    }

    /**
     * The method receives an array of pixels and checks for an efficient
     * way to visit all the points
     * @param points an array with a set of points.
     * @param obsColor the color which is addressed as an obstacle.
     */
    @Override
    public Pixel2D[] shortestPath(Pixel2D[] points, int obsColor) {
        Pixel2D[] ans = null;
        // add you code here
        ArrayList<Pixel2D> unvisited = new ArrayList<>();
        ArrayList<Pixel2D> visited = new ArrayList<>();
        unvisited.addAll(Arrays.asList(points));
        visited.addFirst(unvisited.getFirst());
        while (!unvisited.isEmpty()) {
            Pixel2D current = unvisited.get(0);
            Pixel2D next = minDistance(unvisited, current, obsColor);
            if (next == null) {
                break;
            }
            visited.add(next);
            unvisited.removeFirst();
            unvisited.remove(next);
            unvisited.add(0, next);
        }
        ans = visited.toArray(new Pixel2D[0]);
        for (int i = 0; i < ans.length - 1; i++) {
            shortestPath(ans[i], ans[i + 1], obsColor);
        }
        ////////////////////
        return ans;
    }

    @Override
    public boolean isInside(Pixel2D p) {
        return isInside(p.getX(), p.getY());
    }

    @Override
    public boolean isCyclic() {
        return _cyclicFlag;
    }

    @Override
    public void setCyclic(boolean cy) {
        _cyclicFlag = cy;
    }

    private boolean isInside(int x, int y) {
        return x >= 0 && y >= 0 && x < this.getWidth() && y < this.getHeight();
    }

    /**
     * This function calculates all possible distances from the selected point
     * by placing the value 0 in the selected pixel and each step in any direction
     * is the value of the selected pixel plus 1
     * @param start the source (starting) point
     * @param obsColor the color representing obstacles
     */
    @Override
    public Map2D allDistance(Pixel2D start, int obsColor) {
        Map2D ans = null;
        // add you code here
        ans = new Map(this.getMap());
        if(this.getPixel(start) != obsColor){
            ans.setPixel(start,0);
            allDistance(ans,start,obsColor);
        }
        ////////////////////
        return ans;
    }

    private void allDistance(Map2D path, Pixel2D p1, int obsColor){
        int[][] copy = getMap();
        copy = tmpCopy(copy,obsColor);
         path.init(copy);
        ArrayList<Pixel2D> filler = new ArrayList<>();
        path.setPixel(p1,0);
        filler.add(p1);
        while (!filler.isEmpty()){
            Pixel2D step = filler.getFirst();
            filler.removeFirst();
            int v = path.getPixel(step);
            if (isInside(step)) {
                if (v != OBS) {
                    int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
                    for (int i = 0; i < dirs.length; i++) {
                        int x = step.getX() + dirs[i][0];
                        int y = step.getY() + dirs[i][1];
                        if (!isInside(x,y) && isCyclic()) {
                            Pixel2D cyclicPixel = cyclic(x, y);
                            x = cyclicPixel.getX();
                            y = cyclicPixel.getY();
                        }
                    Pixel2D p = new Index2D(x, y);
                    if (isInside(p) && path.getPixel(p) == UV) {//צריך בדיקה
                        path.setPixel(p, v + 1);
                        filler.add(p);
                        }
                    }
                }
            }
        }
    }

    /**
     * This function checks the areas divided on the map (islands), checks
     * each area by selecting an entry pixel and checking the suitability
     * of all surrounding it until it stops (running the fill method on each island)
     * @param obsColor the pixel value to be an obstacle
     */
    @Override
    public int numberOfConnectedComponents(int obsColor) {
        int ans = -1;
        // add you code here
        int count = 0;
        Map tmp = new Map(getMap());
        tmp.setCyclic(isCyclic());
        tmp._map = tmp.tmpCopy(getMap(),obsColor);
        int unvisited = UV;
        int visited = 1;
        for (int i = 0; i < tmp.getWidth(); i++) {
            for (int j = 0; j < tmp.getHeight(); j++) {
                if(tmp._map[i][j] == unvisited){
                    tmp.fill(new Index2D(i,j),visited);
                    count = count + 1;
                }
            }
        }
        ans = count;
        ////////////////////
        return ans;
    }

    @Override
    public boolean equals(Object ob) {
        boolean ans = false;
        // add you code here
        if (ob == null || !(ob instanceof Map2D) || isCyclic() != ((Map2D) ob).isCyclic()){
            return false;
        }
        Map2D map = (Map2D) ob;
        for (int i = 0; i < getWidth(); i++) {
            for (int j = 0; j < getHeight() ; j++) {
                if(map.getMap()[i][j] != _map[i][j]){
                    return false;
                }
            }
        }

        ////////////////////
        return true;
    }
    /**
     * Fill the chosen pixel(the entry point xy) with the new color(new_v) and check the four closest
     * pixels(neighbors) if they are inside the map and with xy old color(old_v) fill them to
     * and add them to pixels(ArrayList)
     * @param pixels 2d array represent the filled pixels in the map
     * @param xy the entry point
     * @param old_v the entry point old color
     * @param new_v the new color to fill all the possible pixels
     */
    public void checkAndFill(ArrayList<Pixel2D> pixels, Pixel2D xy, int old_v, int new_v) {
        if (getPixel(xy) != old_v || getPixel(xy) == new_v) {
            return;
        }
        setPixel(xy, new_v);
        int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int i = 0; i < dirs.length; i++) {
            int x = xy.getX() + dirs[i][0];
            int y = xy.getY() + dirs[i][1];
            if (isCyclic()){
                Pixel2D cyclicPixel = cyclic(x,y);
                x = cyclicPixel.getX();
                y = cyclicPixel.getY();}
            if (isInside(new Index2D(x, y)) && getPixel(x, y) == old_v) {
                setPixel(xy, new_v);
                pixels.add(new Index2D(x, y));}
        }
    }

    /**
     * When a pixel goes out of the bounds (if _cycligFlag is true)
     * of the map (the 2D array), the pixel to the left of (0,i) is (getWidth()-1,i).
     * the pixel to the right of (getWidth()-1,i) is (0,i).
     * the pixel above (j,getHeight()-1) is (j,0).
     * the pixel below (j,0) is (j,getHeight()-1).
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return new pixel in its updated location
     */
    public Pixel2D cyclic(int x, int y){
        if(x < 0){
            x = getWidth()-1;
        }else if(x >= getWidth()){
            x = 0;
        }
        if(y < 0){
            y =  getHeight()-1;
        }
        else if(y >= getHeight()){
            y = 0;
        }
        return new Index2D(x,y);
    }

    /**
     * this function is a helper function to shortestPath, after getting the
     * map set with all the values from all distance this function find the
     * shortest way from the end to the beginning
     * @param map map set by allDistance
     * @param p2 the end point(destination of shortestPath)
     * @param p1 the start point
     */
    public ArrayList<Pixel2D> nextTo(Map2D map, Pixel2D p2, Pixel2D p1) {
        ArrayList<Pixel2D> road = new ArrayList<>();
        road.add(p2);
        while (!p2.equals(p1)) {
            int[][] dirs = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int i = 0; i < dirs.length; i++) {
                int x = p2.getX() + dirs[i][0];
                int y = p2.getY() + dirs[i][1];
                if (!isInside(x,y) && isCyclic()) {
                    Pixel2D cyclicPixel = cyclic(x, y);
                    x = cyclicPixel.getX();
                    y = cyclicPixel.getY();
                }
                Pixel2D p = new Index2D(x, y);
                if (isInside(p) && map.getPixel(p) == map.getPixel(p2) -1) {
                    road.add(p);
                    p2 = new Index2D(p);
                    break;
                }
            }
        }
        return road;
    }

    /**
     * This function is a helper function for shortestPath (TSP) given the points
     * that need to be visited, the function calculates the closest
     * points and thus the route is created
     * @param unvisited list of pixels that not visited yet
     * @param current the current pixel for checking the minimum distance
     * @param obsColor the pixel value to be an obstacle
     */
    public Pixel2D minDistance(ArrayList<Pixel2D> unvisited, Pixel2D current, int obsColor) {
        Map tmp = new Map(getMap());
        int distance = Integer.MAX_VALUE;
        Pixel2D closest = null;
        for (int i = 1; i < unvisited.size(); i++) {
            Pixel2D p = unvisited.get(i);
            int dist = tmp.shortestPathDist(current, p, obsColor);
            if (dist < distance) {
                distance = dist;
                closest = p;
            }
        }
        return closest;
    }

    /**
     * A helper function allDistance and number Of ConnectedComponents in order
     * not to avoid bugs in the original map _map creates a copy on which the
     * calculations are performed
     * @param copy a 2D array that represents the values in the copied map
     * @param obsColor the pixel value to be an obstacle
     */
    public int[][] tmpCopy(int[][] copy, int obsColor){
        for (int i = 0; i < _map.length ; i++) {
            for (int j = 0; j < _map[0].length ; j++) {
                if(_map[i][j] == obsColor)
                    copy[i][j] = OBS;
                else {
                    copy[i][j] = UV;
                }
            }
        }
        return copy;
    }

}
