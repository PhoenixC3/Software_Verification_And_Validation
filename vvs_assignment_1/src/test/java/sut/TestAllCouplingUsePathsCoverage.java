package sut;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// All-Coupling-Use-Paths Coverage for private method put

public class TestAllCouplingUsePathsCoverage {

    private TST<String> tst;

    @Before
    public void setUp() {
        tst = new TST<>();
    }

    // Def: key = null
    // Use: if (key == null)
    @Test(expected = IllegalArgumentException.class)
    public void testNullKey() {
        tst.put(null, "1");
    }

    // Def: key = ""
    // Use: if (key.length() == 0)
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyKey() {
        tst.put("", "1");
    }

    // Def1: key = "a"
    // Use1: key.charAt(d)
    // Def2: val = "1"
    // Use2: x.val = val
    // Def3: c = key.charAt(d)
    // Use3: x.c = c
    // Def4: x = null
    // Use4: x = if (x == null)
    @Test
    public void testInsertSingleKey() {
        tst.put("a", "1");
        assertEquals("1", tst.get("a"));
        assertEquals(1, tst.size());
    }

    // Def1: key = "d" ; key = "b"
    // Use1: key.charAt(d)
    // Def2: c = key.charAt(d)
    // Use2: if (c < x.c)
    // Def3: x = null
    // Use3: x.left = put(x.left, key, val, d)
    @Test
    public void testLeftBranchCreation() {
        tst.put("d", "1");
        tst.put("b", "2");
        assertEquals("2", tst.get("b"));
        assertEquals(2, tst.size());
    }

    // Def1: key = "d" ; key = "f"
    // Use1: key.charAt(d)
    // Def2: c = key.charAt(d)
    // Use2: if (c > x.c)
    // Def3: x = null
    // Use3: x.right = put(x.right, key, val, d)
    @Test
    public void testRightBranchCreation() {
        tst.put("d", "1");
        tst.put("f", "2");
        assertEquals("2", tst.get("f"));
        assertEquals(2, tst.size());
    }

    // Def1: key = "cat" ; key = "car"
    // Use1: key.charAt(d)
    // Def2: c = key.charAt(d)
    // Use2: if (c == x.c)
    // Def3: x = null
    // Use3: x.mid = put(x.mid, key, val, d+1)
    @Test
    public void testMidBranchCreation() {
        tst.put("cat", "1");
        tst.put("car", "2");
        assertEquals("1", tst.get("cat"));
        assertEquals("2", tst.get("car"));
        assertEquals(2, tst.size());
    }

    // Def1: key = "hat"
    // Use1: key.charAt(d)
    // Def2: val = "3" ; val = "5"
    // Use2: x.val = val
    // Def3: c = key.charAt(d)
    // Use3: if (c == x.c)
    // Def4: x = null
    // Use4: x.val = val
    @Test
    public void testOverwriteValue() {
        tst.put("hat", "3");
        assertEquals("3", tst.get("hat"));
        tst.put("hat", "5");
        assertEquals("5", tst.get("hat"));
        assertEquals(1, tst.size());
    }

    // Def1: key = "dog" ; key = "cat"
    // Use1: key.charAt(d)
    // Def2: c = key.charAt(d)
    // Use2: if (c < x.c)
    // Def3: x != null
    // Use3: x.left = put(x.left, key, val, d)
    @Test
    public void testInsertLeftOfMidTree() {
        tst.put("dog", "10");
        tst.put("cat", "20");
        assertEquals("10", tst.get("dog"));
        assertEquals("20", tst.get("cat"));
        assertEquals(2, tst.size());
    }

    // Def1: key = "dog" ; key = "cat"
    // Use1: key.charAt(d)
    // Def2: c = key.charAt(d)
    // Use2: if (c > x.c)
    // Def3: x != null
    // Use3: x.right = put(x.right, key, val, d)
    @Test
    public void testInsertRightOfMidTree() {
        tst.put("cat", "1");
        tst.put("dog", "2");
        assertEquals("1", tst.get("cat"));
        assertEquals("2", tst.get("dog"));
        assertEquals(2, tst.size());
    }

    // Def1: key = "car" ; key = "cart"
    // Use1: key.charAt(d)
    // Def2: c = key.charAt(d)
    // Use2: if (c == x.c)
    // Def3: x != null
    // Use3: x.mid = put(x.mid, key, val, d+1)
    @Test
    public void testDeepRecursionInMidBranch() {
        tst.put("car", "1");
        tst.put("cart", "2");
        assertEquals("1", tst.get("car"));
        assertEquals("2", tst.get("cart"));
        assertEquals(2, tst.size());
    }
}
