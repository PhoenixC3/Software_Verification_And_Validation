package sut;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

// All-Coupling-Use-Paths Coverage for private method put

public class AllCouplingUsePathsCoverageTest {

    private TST<String> tst;

    @BeforeEach
    public void setUp() {
        tst = new TST<>();
    }

    // Covers: x == null and d == key.length() - 1
    @Test
    public void testInsertSingleKey() {
        tst.put("a", "1");
        assertEquals("1", tst.get("a"));
        assertEquals(1, tst.size());
    }

    // Covers: c < x.c (go left branch)
    @Test
    public void testLeftBranchCreation() {
        tst.put("d", "1");
        tst.put("b", "2");
        assertEquals("2", tst.get("b"));
        assertEquals(2, tst.size());
    }

    // Covers: c > x.c (go right branch)
    @Test
    public void testRightBranchCreation() {
        tst.put("d", "1");
        tst.put("f", "2");
        assertEquals("2", tst.get("f"));
        assertEquals(2, tst.size());
    }

    // Covers: x == null, c == x.c, and d < key.length() - 1
    @Test
    public void testMidBranchCreation() {
        tst.put("cat", "1");
        tst.put("car", "2");
        assertEquals("1", tst.get("cat"));
        assertEquals("2", tst.get("car"));
        assertEquals(2, tst.size());
    }

    // Covers: x != null and d == key.length() - 1
    @Test
    public void testOverwriteValue() {
        tst.put("hat", "3");
        assertEquals("3", tst.get("hat"));
        tst.put("hat", "5");
        assertEquals("5", tst.get("hat"));
        assertEquals(1, tst.size());
    }

    // Covers: Left-branch condition (c < x.c), with non-null x
    @Test
    public void testInsertLeftOfMidTree() {
        tst.put("dog", "10");
        tst.put("cat", "20");
        assertEquals("10", tst.get("dog"));
        assertEquals("20", tst.get("cat"));
        assertEquals(2, tst.size());
    }

    // Covers: Right-branch condition (c > x.c), with non-null x
    @Test
    public void testInsertRightOfMidTree() {
        tst.put("cat", "1");
        tst.put("dog", "2");
        assertEquals("1", tst.get("cat"));
        assertEquals("2", tst.get("dog"));
        assertEquals(2, tst.size());
    }

    // Covers: Mid branch multiple times (c == x.c and d < key.length() - 1)
    @Test
    public void testDeepRecursionInMidBranch() {
        tst.put("car", "1");
        tst.put("cart", "2");
        assertEquals("1", tst.get("car"));
        assertEquals("2", tst.get("cart"));
        assertEquals(2, tst.size());
    }
}
