import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Index2DTest {
    private Pixel2D _p1 = new Index2D(1, 2);
    private Pixel2D _p2 = new Index2D(4, 4);

    @Test
    void distanceTest() {
        double d = _p1.distance2D(_p2);
        double eps = 0.0001;
        assertEquals(3.6055, d, eps);
    }

    @Test
    void toStringTest() {
        String p = _p1.toString();
        Index2D p2 = new Index2D(p);
        assertEquals(_p1, p2);
        String s = _p2.toString();
        assertEquals(s, "4,4");
    }

    @Test
    void equals() {
        Pixel2D p11 = new Index2D(_p1);
        assertEquals(_p1, p11);
        Pixel2D ps1 = new Index2D(5,5);
        Pixel2D ps2 = new Index2D(5,5);
        assertTrue(ps1.equals(ps2));
        assertFalse(_p1.equals(ps1));
    }
}