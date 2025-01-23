import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.awt.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
/**
This is a very basic Testing class for Map - please note that this JUnit
 contains only a very limited testing method and should be added many other
 methods for testing all the functionality of Map2D - both in correctness and in runtime.
*/
 class MapTest {
    /**
     * _m_3_3 =
     * 0,1,0
     * 1,0,1
     * 0,1,0
     *
     * _m0 =
     * 1,1,1,1,1
     * 1,0,1,0,1
     * 1,0,0,0,1
     * 1,0,1,0,1
     * 1,1,1,1,1
     * 1,0,1,0,1
     *
     * 1, 1, 1, 1, 1
     * 1,-1, 1,-1, 1
     * 1,-1,-1,-1, 1
     * 1,-1, 1,-1, 1
     * 1, 1, 1, 1, 1
     * 1,-1, 1,-1, 1
     *
     * m2[3][2] = 0, m2[1][2] = 10, |sp|=11 (isCiclic = false;}
     * =============
     * 7, 8, 9, 1, 7
     * 6,-1,10,-1, 6
     * 5,-1,-1,-1, 5
     * 4,-1, 0,-1, 4
     * 3, 2, 1, 2, 3
     * 4,-1, 2,-1, 4
     *
     * m[3][2] = 0, m2[1][2] = 5, |sp|=5 (isCiclic = true;}
     * 5, 4, 3, 4, 5
     * 6,-1, 4,-1, 6
     * 5,-1,-1,-1, 5
     * 4,-1, 0,-1, 4
     * 3, 2, 1, 2, 3
     * 4,-1, 2,-1, 4
     */
    public static final int OBS = 1;
    private int[][] _map = {{1,1,1,1,1}, {1,0,1,0,1}, {1,0,0,0,1},  {1,0,1,0,1},  {1,1,1,1,1}, {1,0,1,0,1}};
    private int[][] _map_3_3 = {{0,1,0}, {1,0,1}, {0,1,0}};
    private int[][] _map_8_8 = {{0,0,0,0,1,0,0,0}, {0,0,0,0,1,0,0,0}, {0,0,0,0,1,0,0,0},  {0,0,0,0,1,0,0,0},{0,0,0,0,1,0,0,0},{0,0,0,0,1,0,0,0},{0,0,0,0,1,0,0,0},{0,0,0,0,1,0,0,0}};
    private int[][] _map400 = new int[400][400];
    private int[][] _blocked = {{1,1,1,1},{1,1,1,1},{1,1,1,1},{1,1,1,1}};
    private Map2D _m0, _m1, _m2, _m3, _m3_3, _bigMap, _map_8,_blockedMap ;
    @BeforeEach
    public void setup() {
        _m0 = new Map(_map);
        _m1 = new Map(_map); _m1.setCyclic(true);
        _m2 = new Map(_map); _m2.setCyclic(false);
        _m3 = new Map(_map);
        _m3_3 = new Map(_map_3_3);
        _map_8 = new Map(_map_8_8); _map_8.setCyclic(false);
        _bigMap = new Map(_map400);
        _blockedMap = new Map(_blocked);
    }
    @Test
    @Timeout(value = 1, unit = SECONDS)
    void init() {
        int[][] bigarr = new int [500][500];
        _m1.init(bigarr);
        assertEquals(bigarr.length, _m1.getWidth());
        assertEquals(bigarr[0].length, _m1.getHeight());
        Pixel2D p1 = new Index2D(3,2);
        _m1.fill(p1,1);
    }

    @Test
    void testEquals() {
        assertEquals(_m0,_m1);
        assertEquals(_m0,_m3);
        assertNotEquals(_m1,_m2);
        _m3.setPixel(2,2,17);
        assertNotEquals(_m0,_m3);
    }
    @Test
    void getMap() {
        int[][] m0 = _m0.getMap();
        _m1.init(m0);
        assertEquals(_m0,_m1);
    }

    @Test
    void testFill0() { //regular
        Pixel2D p1 = new Index2D(0,0);
        int f0 = _m0.fill(p1,2);
        assertEquals(f0,21);
    }
    @Test
    void testFill1() {
        Pixel2D p1 = new Index2D(0,1);
        _m0.setPixel(p1,0);
        int f0 = _m0.fill(p1,2);
        assertEquals(f0,9);
        _m0.setCyclic(false);
        int f2 = _m0.fill(p1,3);
        assertEquals(f2,8);
    }
    @Test //blocked map
    void testFill2(){
        Pixel2D p1 = new Index2D(0,1);
       int ans = _blockedMap.fill(p1,OBS);
        assertEquals(1,ans);
    }
    @Test //time
    @Timeout(value = 1, unit = SECONDS)
    void testFill3(){
        Pixel2D p1 = new Index2D(5,7);
        Ex3.setSize(4);
        Ex3.setCyclic(1);
        Ex3.setPattern(2);
        Ex3._map.fill(p1, Color.pink.getRGB());
    }
    @Test //gui
    void testFill4(){
        Pixel2D p1 = new Index2D(5,7);
        Ex3.setSize(2);
        Ex3.setCyclic(2);
        Ex3.setPattern(2);
        Ex3._map.fill(p1, Color.pink.getRGB());
        Ex3.drawMap(Ex3._map);
    }
    @Test
    void testAllDistance() {
        Pixel2D p1 = new Index2D(3,2);
        Pixel2D p2 = new Index2D(1,0);
        Map2D m00 = _m0.allDistance(p1, 0);
        assertEquals(6, m00.getPixel(p2));
    }

    @Test//regular and big map
    void testShortestPath0() {
        Pixel2D p1 = new Index2D(3,2);
        Pixel2D p2 = new Index2D(1,2);
        Pixel2D[] path = _m0.shortestPath(p1, p2, 0);
        assertEquals(5, path.length);
        path = _m2.shortestPath(p1, p2, 0);
        assertEquals(11, path.length);

      _bigMap.init(400,400,-1);
         p1 = new Index2D(0,0);
         p2 = new Index2D(350,350);
        _bigMap.fill(p1,1);
        path = _bigMap.shortestPath(p1,p2,-3);
        assertEquals(101,path.length);
    }
    @Test // 0 steps
    void  testShortestPath2(){
        Pixel2D p1 = new Index2D(3,3);
        Pixel2D p2 = new Index2D(3,3);
        Pixel2D[] path = _m0.shortestPath(p1,p2,-1);
        assertEquals(1,path.length);
        assertEquals(3,path[0].getX());
        assertEquals(3,path[0].getY());
    }
    @Test //null
    void testShortestPath3(){
        Pixel2D p1 = new Index2D(2,2);
        Pixel2D p2 = new Index2D(4,4);
      Pixel2D[] path = _map_8.shortestPath(p1,p2,OBS);
        assertNull(path);
    }
    @Test //time
    @Timeout(value = 1, unit = SECONDS)
    void testShortestPath4() {
        Pixel2D p1 = new Index2D(2,2);
        Pixel2D p2 = new Index2D(390,390);
        _bigMap.shortestPath(p1,p2,OBS);
    }
    @Test // blocked
    void testShortestPath5(){
        Pixel2D p1 = new Index2D(0,0);
        Pixel2D p2 = new Index2D(3,3);
        Pixel2D[] path = _blockedMap.shortestPath(p1,p2,OBS);
        assertNull(path);
    }
    @Test //gui
    void testShortestPath6(){
        Pixel2D p1 = new Index2D(1,5);
        Pixel2D p2 = new Index2D(4,8);
        Ex3.setSize(2);
        Ex3.setCyclic(1);
        Ex3.setPattern(2);
        Ex3._map.shortestPath(p1,p2,Ex3.OBS);
        Ex3.drawMap(Ex3._map);
    }

    @Test // regular
    void testShortestPathTSP0(){
        Pixel2D p1 = new Index2D(1,3);
        Pixel2D p2 = new Index2D(3,6);
        Pixel2D p3 = new Index2D(3,3);
        Pixel2D p4 = new Index2D(0,3);
        Pixel2D p5 = new Index2D(2,2);
        Pixel2D[] path = {p1,p2,p3,p4,p5};
        Pixel2D[] road = _map_8.shortestPath(path,OBS);
       Pixel2D[] check = {p1,p4,p3,p5,p2};
        assertArrayEquals(check,road);
    }
    @Test // time
    @Timeout(value = 1, unit = SECONDS)
    void testShortestPathTSP1(){
        Pixel2D p1 = new Index2D(1,0);
        Pixel2D p2 = new Index2D(15,6);
        Pixel2D p3 = new Index2D(40,3);
        Pixel2D p4 = new Index2D(50,15);
        Pixel2D p5 = new Index2D(60,70);
        Pixel2D[] path = {p1,p2,p3,p4,p5};
        Ex3.setSize(4);
        Ex3.setCyclic(1);
        Ex3.setPattern(2);
        Ex3._map.shortestPath(path,Ex3.OBS);
    }

    @Test // gui
    void testShortestPathTSP2(){
        Pixel2D p1 = new Index2D(1,0);
        Pixel2D p2 = new Index2D(15,6);
        Pixel2D p3 = new Index2D(25,3);
        Pixel2D p4 = new Index2D(17,15);
        Pixel2D p5 = new Index2D(39,30);
        Pixel2D[] path = {p1,p2,p3,p4,p5};
        Ex3.setSize(3);
        Ex3.setCyclic(1);
        Ex3.setPattern(2);
        Ex3._map.shortestPath(path,Ex3.OBS);
        Ex3.drawMap(Ex3._map);
    }
    @Test // cyclic
    void testShortestPathTSP3(){
        Pixel2D p1 = new Index2D(1,0);
        Pixel2D p2 = new Index2D(3,6);
        Pixel2D p3 = new Index2D(2,3);
        Pixel2D p4 = new Index2D(17,15);
        Pixel2D[] path = {p1,p2,p3,p4};
        Ex3.setSize(2);
        Ex3.setCyclic(1);
        Ex3.setPattern(2);
        Ex3._map.shortestPath(path,Ex3.OBS);
        assertNotNull(Ex3._map);
    }
    @Test// not cyclic
    void testNumberOfConnectedComponents0(){
        Ex3.setSize(2);
        Ex3.setPattern(3);
        Ex3.setCyclic(2);
       int ans = Ex3._map.numberOfConnectedComponents(Ex3.OBS);
       assertEquals(2,ans);
    }
    @Test //cyclic
    void testNumberOfConnectedComponents1(){
        Ex3.setSize(3);
        Ex3.setPattern(3);
        Ex3.setCyclic(1);
        int ans = Ex3._map.numberOfConnectedComponents(Ex3.OBS);
        assertEquals(1,ans);
    }
}